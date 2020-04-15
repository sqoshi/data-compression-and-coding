import contextlib
import math
import os
import sys


class Puller:
    def __init__(self, inp):
        self.input = inp
        self.actualByte = 0
        self.rest = 0

    def read(self):
        if self.actualByte == -1:
            return -1
        if self.rest == 0:
            temp = self.input.read(1)
            if len(temp) == 0:
                self.actualByte = -1
                return -1
            self.actualByte = temp[0]
            self.rest = 8
        assert self.rest > 0
        self.rest -= 1
        return (self.actualByte >> self.rest) & 1

    def readNext(self):
        result = self.read()
        if result != -1:
            return result
        else:
            raise EOFError()

    def close(self):
        self.input.close()
        self.actualByte = -1
        self.rest = 0


class Pusher:

    def __init__(self, out):
        self.output = out
        self.actualByte = 0
        self.bitsInserted = 0

    def write(self, b):
        if b not in (0, 1):
            raise ValueError()
        self.actualByte = (self.actualByte << 1) | b
        self.bitsInserted += 1
        if self.bitsInserted == 8:
            towrite = bytes((self.actualByte,))
            self.output.write(towrite)
            self.actualByte = 0
            self.bitsInserted = 0

    def close(self):
        while self.bitsInserted != 0:
            self.write(0)
        self.output.close()


class AbstractTable:

    def getSymbolLim(self):
        raise NotImplementedError()

    def get(self, symbol):
        raise NotImplementedError()

    def set(self, symbol, freq):
        raise NotImplementedError()

    def increment(self, symbol):
        raise NotImplementedError()

    def getTotal(self):
        raise NotImplementedError()

    def getLow(self, symbol):
        raise NotImplementedError()

    def getHigh(self, symbol):
        raise NotImplementedError()


class FlatFrequencyTable(AbstractTable):

    def __init__(self, value):
        if value < 1:
            raise ValueError()
        self.symbolsQuantity = value

    def getSymbolLim(self):
        return self.symbolsQuantity

    def get(self, symbol):
        self.inspectSymbol(symbol)
        return 1

    def getTotal(self):
        return self.symbolsQuantity

    def getLow(self, symbol):
        self.inspectSymbol(symbol)
        return symbol

    def getHigh(self, symbol):
        self.inspectSymbol(symbol)
        return symbol + 1

    def inspectSymbol(self, symbol):
        if 0 <= symbol < self.symbolsQuantity:
            return
        else:
            raise ValueError()

    def set(self, symbol, freq):
        raise NotImplementedError()

    def increment(self, symbol):
        raise NotImplementedError()


class FrequencyTable(AbstractTable):
    def __init__(self, frequencies):
        if isinstance(frequencies, AbstractTable):
            symbolLim = frequencies.getSymbolLim()
            self.frequencies = [frequencies.get(i) for i in range(symbolLim)]
        else:
            self.frequencies = list(frequencies)

        if len(self.frequencies) < 1:
            raise ValueError()
        for freq in self.frequencies:
            if freq < 0:
                raise ValueError()
        self.total = sum(self.frequencies)
        self.cumulative = None

    def getSymbolLim(self):
        return len(self.frequencies)

    def get(self, symbol):
        self.inspectSymbol(symbol)
        return self.frequencies[symbol]

    def set(self, symbol, freq):
        self.inspectSymbol(symbol)
        if freq < 0:
            raise ValueError()
        temp = self.total - self.frequencies[symbol]
        assert temp >= 0
        self.total = temp + freq
        self.frequencies[symbol] = freq
        self.cumulative = None

    def increment(self, symbol):
        self.inspectSymbol(symbol)
        self.total += 1
        self.frequencies[symbol] += 1
        self.cumulative = None

    def getTotal(self):
        return self.total

    def getLow(self, symbol):
        self.inspectSymbol(symbol)
        if self.cumulative is None:
            self.checkCumulative()
        return self.cumulative[symbol]

    def getHigh(self, symbol):
        self.inspectSymbol(symbol)
        if self.cumulative is None:
            self.checkCumulative()
        return self.cumulative[symbol + 1]

    def checkCumulative(self):
        result = [0]
        s = 0
        for freq in self.frequencies:
            s += freq
            result.append(s)
        assert s == self.total
        self.cumulative = result

    def inspectSymbol(self, symbol):
        if 0 <= symbol < len(self.frequencies):
            return
        else:
            raise ValueError()


class CheckedFrequencyTable(AbstractTable):

    def __init__(self, table):
        self.frequencyTable = table

    def getSymbolLim(self):
        result = self.frequencyTable.getSymbolLim()
        if result <= 0:
            raise AssertionError()
        return result

    def get(self, symbol):
        result = self.frequencyTable.get(symbol)
        if not self.validateSymbol(symbol):
            raise AssertionError()
        if result < 0:
            raise AssertionError()
        return result

    def getTotal(self):
        result = self.frequencyTable.getTotal()
        if result < 0:
            raise AssertionError()
        return result

    def getLow(self, symbol):
        if self.validateSymbol(symbol):
            low = self.frequencyTable.getLow(symbol)
            high = self.frequencyTable.getHigh(symbol)
            if not (0 <= low <= high <= self.frequencyTable.getTotal()):
                raise AssertionError()
            return low
        else:
            self.frequencyTable.getLow(symbol)
            raise AssertionError()

    def getHigh(self, symbol):
        if self.validateSymbol(symbol):
            low = self.frequencyTable.getLow(symbol)
            high = self.frequencyTable.getHigh(symbol)
            if not (0 <= low <= high <= self.frequencyTable.getTotal()):
                raise AssertionError()
            return high
        else:
            self.frequencyTable.getHigh(symbol)
            raise AssertionError()

    def set(self, symbol, frequency):
        self.frequencyTable.set(symbol, frequency)
        if not self.validateSymbol(symbol) or frequency < 0:
            raise AssertionError()

    def increment(self, symbol):
        self.frequencyTable.increment(symbol)
        if not self.validateSymbol(symbol):
            raise AssertionError()

    def validateSymbol(self, symbol):
        return 0 <= symbol < self.getSymbolLim()


class Base:
    def __init__(self, value):
        if value < 1:
            raise ValueError()
        self.stateBits = value
        self.segment = 1 << self.stateBits
        self.segmentHalf = self.segment >> 1
        self.segmentQuarter = self.segmentHalf >> 1
        self.minScope = self.segmentQuarter + 2
        self.maxTotal = self.minScope
        self.sMask = self.segment - 1
        self.low = 0
        self.high = self.sMask

    def update(self, frequencies, symbol):
        low = self.low
        high = self.high
        if low >= high or (low & self.sMask) != low or (high & self.sMask) != high:
            raise AssertionError()
        scope = high - low + 1
        if not (self.minScope <= scope <= self.segment):
            raise AssertionError()
        total = frequencies.getTotal()
        symbolLowLim = frequencies.getLow(symbol)
        symbolHighLim = frequencies.getHigh(symbol)
        if symbolLowLim == symbolHighLim:
            raise ValueError()
        if total > self.maxTotal:
            raise ValueError()
        newSymbolLowLim = low + symbolLowLim * scope // total
        newSymbolHighLim = low + symbolHighLim * scope // total - 1
        self.low = newSymbolLowLim
        self.high = newSymbolHighLim
        while ((self.low ^ self.high) & self.segmentHalf) == 0:
            self.shift()
            self.low = ((self.low << 1) & self.sMask)
            self.high = ((self.high << 1) & self.sMask) | 1
        while (self.low & ~self.high & self.segmentQuarter) != 0:
            self.underflow()
            self.low = (self.low << 1) ^ self.segmentHalf
            self.high = ((self.high ^ self.segmentHalf) << 1) | self.segmentHalf | 1

    def shift(self):
        raise NotImplementedError()

    def underflow(self):
        raise NotImplementedError()


class Encoder(Base):
    def __init__(self, value, outBit):
        super(Encoder, self).__init__(value)
        self.output = outBit
        self.numUnderflow = 0

    def write(self, frequencies, symbol):
        if not isinstance(frequencies, CheckedFrequencyTable):
            frequencies = CheckedFrequencyTable(frequencies)
        self.update(frequencies, symbol)

    def finish(self):
        self.output.write(1)

    def shift(self):
        bit = self.low >> (self.stateBits - 1)
        self.output.write(bit)

        for _ in range(self.numUnderflow):
            self.output.write(bit ^ 1)
        self.numUnderflow = 0

    def underflow(self):
        self.numUnderflow += 1


class Decoder(Base):
    def __init__(self, value, inBit):
        super(Decoder, self).__init__(value)
        self.input = inBit
        self.code = 0
        for _ in range(self.stateBits):
            self.code = self.code << 1 | self.read_code_bit()

    def read(self, frequencies):
        if not isinstance(frequencies, CheckedFrequencyTable):
            frequencies = CheckedFrequencyTable(frequencies)
        total = frequencies.getTotal()
        if total > self.maxTotal:
            raise ValueError()
        scope = self.high - self.low + 1
        offset = self.code - self.low
        value = ((offset + 1) * total - 1) // scope
        assert value * scope // total <= offset
        assert 0 <= value < total
        begin = 0
        end = frequencies.getSymbolLim()
        while end - begin > 1:
            m = (begin + end) >> 1
            if frequencies.getLow(m) > value:
                end = m
            else:
                begin = m
        assert begin + 1 == end
        symbol = begin
        assert frequencies.getLow(symbol) * scope // total <= offset < frequencies.getHigh(symbol) * scope // total
        self.update(frequencies, symbol)
        if not (self.low <= self.code <= self.high):
            raise AssertionError()
        return symbol

    def shift(self):
        self.code = ((self.code << 1) & self.sMask) | self.read_code_bit()

    def underflow(self):
        self.code = (self.code & self.segmentHalf) | ((self.code << 1) & (self.sMask >> 1)) | self.read_code_bit()

    def read_code_bit(self):
        temp = self.input.read()
        if temp == -1:
            temp = 0
        return temp


def encode(args):
    inputFile, outputFile = args
    with open(inputFile, "rb") as inp, \
            contextlib.closing(Pusher(open(outputFile, "wb"))) as outBit:
        initialFrequencies = FlatFrequencyTable(257)
        frequencies = FrequencyTable(initialFrequencies)
        enc = Encoder(32, outBit)
        while True:
            symbol = inp.read(1)
            if len(symbol) == 0:
                break
            enc.write(frequencies, symbol[0])
            frequencies.increment(symbol[0])
        enc.write(frequencies, 256)
        enc.finish()


def decode(args):
    inputFile, outputFile = args
    with open(inputFile, "rb") as inp, open(outputFile, "wb") as out:
        inBit = Puller(inp)
        initialFrequencies = FlatFrequencyTable(257)
        frequencies = FrequencyTable(initialFrequencies)
        dec = Decoder(32, inBit)
        while True:
            symbol = dec.read(frequencies)
            if symbol == 256:
                break
            out.write(bytes((symbol,)))
            frequencies.increment(symbol)


def findAlphabet(filepath):
    alphabet = {}
    size = 0.0
    with open(filepath, 'rb') as f:
        while True:
            c = f.read(1)
            size += 1
            if not c:
                break
            key = ord(c)
            if key in alphabet.keys():
                alphabet[key] = alphabet.get(key) + 1
            else:
                alphabet[key] = 1
    return alphabet, size


def computeEntropy(filepath):
    alphabet, size = findAlphabet(filepath)
    H = 0.0
    for x, y in alphabet.items():
        Pi = y / size
        H += Pi * (-math.log2(Pi))
    return H, len(alphabet)


def getInformation(filepath):
    coded = "code"
    sizeUncompressed = os.stat(filepath).st_size
    sizeCompressed = os.stat(coded).st_size
    uncompressed, uncompressedSymbolsQuantity = computeEntropy(filepath)
    compressed, compressedSymbolsQuantity = computeEntropy(coded)
    print("Entropy of uncompressed file: ", uncompressed)
    print("Entropy of compressed file: ", compressed)
    print("-------------------------------------------")
    print("Size of uncompressed file: ", sizeUncompressed)
    print("Size of compressed file: ", sizeCompressed)
    print("-------------------------------------------")
    print("Compression rate = ", sizeUncompressed / sizeCompressed)
    print("-------------------------------------------")
    print("Average codeword length of compressed: ", sizeCompressed / compressedSymbolsQuantity)


def main(filepath):
    if len(sys.argv) < 1:
        filepath = "/home/piotr/Documents/data-compression-and-coding/" \
                   "Tests/pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt "
    encode([filepath, "code"])
    decode(["code", "decode"])
    getInformation(filepath)


main(sys.argv[1])
