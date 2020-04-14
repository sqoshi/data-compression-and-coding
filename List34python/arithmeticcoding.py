import contextlib
import math
import os
import sys


class Puller:
    def __init__(self, inp):
        self.input = inp
        self.actualByte = 0
        self.nbRest = 0

    def pull(self):
        if self.actualByte == -1:
            return -1
        if self.nbRest == 0:
            byte = self.input.pull(1)
            if len(byte) == 0:
                self.actualByte = -1
                return -1
            self.actualByte = byte[0]
            self.nbRest = 8
        assert self.nbRest > 0
        self.nbRest -= 1
        return (self.actualByte >> self.nbRest) & 1

    def pullNext(self):
        result = self.pull()
        if result != -1:
            return result
        else:
            raise EOFError()

    def close(self):
        self.input.close()
        self.actualByte = -1
        self.nbRest = 0


class Pusher:
    def __init__(self, out):
        self.output = out
        self.actualByte = 0
        self.nbInserted = 0

    def push(self, b):
        if b not in (0, 1):
            raise ValueError("zly znak")
        self.actualByte = (self.actualByte << 1) | b
        self.nbInserted += 1
        if self.nbInserted == 8:
            towrite = bytes((self.actualByte,))
            self.output.push(towrite)
            self.actualByte = 0
            self.nbInserted = 0

    def close(self):
        while self.nbInserted != 0:
            self.push(0)
        self.output.close()


class FrequencyTable:

    def symbolLimes(self):
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


class FlatFrequencyTable(FrequencyTable):

    def __init__(self, value):
        self.numSymbols = value

    def symbolLimes(self):
        return self.numSymbols

    def get(self, symbol):
        self.inspectSymbol(symbol)
        return 1

    def getTotal(self):
        return self.numSymbols

    def getLow(self, symbol):
        self.inspectSymbol(symbol)
        return symbol

    def getHigh(self, symbol):
        self.inspectSymbol(symbol)
        return symbol + 1

    def inspectSymbol(self, symbol):
        if 0 <= symbol < self.numSymbols:
            return
        else:
            raise ValueError("symbol poza zasiegiem")

    def set(self, symbol, freq):
        raise NotImplementedError()

    def increment(self, symbol):
        raise NotImplementedError()


class SimpleFrequencyTable(FrequencyTable):

    def __init__(self, frequencies):
        if isinstance(frequencies, FrequencyTable):
            numSymbol = frequencies.symbolLimes()
            self.frequencies = [frequencies.get(i) for i in range(numSymbol)]
        else:
            self.frequencies = list(frequencies)

        if len(self.frequencies) < 1:
            raise ValueError("nie wystarczajaca ilosc symbol < 1")
        for x in self.frequencies:
            if x < 0:
                raise ValueError("czestosc jest ujemna - niemozliwe..")

        self.total = sum(self.frequencies)
        self.cumulative = None

    def symbolLimes(self):
        return len(self.frequencies)

    def get(self, symbol):
        self.inspectSymbol(symbol)
        return self.frequencies[symbol]

    def set(self, symbol, frequency):
        self.inspectSymbol(symbol)
        if frequency < 0:
            raise ValueError("czestosc ujemna - niemozliwe..")
        temp = self.total - self.frequencies[symbol]
        assert temp >= 0
        self.total = temp + frequency
        self.frequencies[symbol] = frequency
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
            self._init_cumulative()
        return self.cumulative[symbol]

    def getHigh(self, symbol):
        self.inspectSymbol(symbol)
        if self.cumulative is None:
            self._init_cumulative()
        return self.cumulative[symbol + 1]

    def _init_cumulative(self):
        c = [0]
        res = 0
        for x in self.frequencies:
            res += x
            c.append(res)
        assert res == self.total
        self.cumulative = c

    def inspectSymbol(self, symbol):
        if 0 <= symbol < len(self.frequencies):
            return
        else:
            raise ValueError("symbol poza zasiegiem")


class CheckedFrequencyTable(FrequencyTable):
    def __init__(self, tab):
        self.frequencyTable = tab

    def symbolLimes(self):
        result = self.frequencyTable.symbolLimes()
        if result <= 0:
            raise AssertionError("symbol limit =<0")
        return result

    def get(self, symbol):
        result = self.frequencyTable.get(symbol)
        if not self.checkRange(symbol):
            raise AssertionError("val err sb")
        if result < 0:
            raise AssertionError("ujemna czestosc symbolu")
        return result

    def getTotal(self):
        result = self.frequencyTable.getTotal()
        if result < 0:
            raise AssertionError("total ujemna czestosc")
        return result

    def getLow(self, symbol):
        if self.checkRange(symbol):
            low = self.frequencyTable.getLow(symbol)
            high = self.frequencyTable.getHigh(symbol)
            if not (0 <= low <= high <= self.frequencyTable.getTotal()):
                raise AssertionError("symb low cum freq poza zasiegiem")
            return low
        else:
            self.frequencyTable.getLow(symbol)
            raise AssertionError("val err sb")

    def getHigh(self, symbol):
        if self.checkRange(symbol):
            low = self.frequencyTable.getLow(symbol)
            high = self.frequencyTable.getHigh(symbol)
            if not (0 <= low <= high <= self.frequencyTable.getTotal()):
                raise AssertionError("poza zasiegiem sym high cum freq ")
            return high
        else:
            self.frequencyTable.getHigh(symbol)
            raise AssertionError("val err sb")

    def set(self, symbol, freq):
        self.frequencyTable.set(symbol, freq)
        if not self.checkRange(symbol) or freq < 0:
            raise AssertionError("val err sb")

    def increment(self, symbol):
        self.frequencyTable.increment(symbol)
        if not self.checkRange(symbol):
            raise AssertionError("val err sb")

    def checkRange(self, symbol):
        return 0 <= symbol < self.symbolLimes()


class Base:
    def __init__(self, numBits):
        if numBits < 1:
            raise ValueError("nb musi byc wieksze niz 1!")
        self.numStateBits = numBits
        self.segment = 1 << self.numStateBits
        self.segmentHalf = self.segment >> 1
        self.segmentQuarter = self.segmentHalf >> 1
        self.minRange = self.segmentQuarter + 2
        self.maxTotal = self.minRange
        self.stateMask = self.segment - 1
        self.low = 0
        self.high = self.stateMask

    def update(self, frequencies, symbol):
        low = self.low
        high = self.high
        if low >= high or (low & self.stateMask) != low or (high & self.stateMask) != high:
            raise AssertionError("lims poza zasiegiem")
        scope = high - low + 1
        if not (self.minRange <= scope <= self.segment):
            raise AssertionError("wyznaczony scope poza zasiegiem")
        total = frequencies.getTotal()
        symbolLowLim = frequencies.getLow(symbol)
        symbolHighLim = frequencies.getHigh(symbol)
        if symbolLowLim == symbolHighLim:
            raise ValueError("symbol ma czestosc rowna 0 ?")
        if total > self.maxTotal:
            raise ValueError("total jest za duzy")

        newSymbolLowLim = low + symbolLowLim * scope // total
        newSymbolHighLim = low + symbolHighLim * scope // total - 1
        self.low = newSymbolLowLim
        self.high = newSymbolHighLim

        while ((self.low ^ self.high) & self.segmentHalf) == 0:
            self.shift()
            self.low = ((self.low << 1) & self.stateMask)
            self.high = ((self.high << 1) & self.stateMask) | 1

        while (self.low & ~self.high & self.segmentQuarter) != 0:
            self.underflow()
            self.low = (self.low << 1) ^ self.segmentHalf
            self.high = ((self.high ^ self.segmentHalf) << 1) | self.segmentHalf | 1

    def shift(self):
        raise NotImplementedError()

    def underflow(self):
        raise NotImplementedError()


class Encoder(Base):
    def __init__(self, numBits, OutBit):
        super(Encoder, self).__init__(numBits)
        self.output = OutBit
        self.numUnderFlow = 0

    def write(self, frequencies, symbol):
        if not isinstance(frequencies, CheckedFrequencyTable):
            frequencies = CheckedFrequencyTable(frequencies)
        self.update(frequencies, symbol)

    def finish(self):
        self.output.push(1)

    def shift(self):
        bit = self.low >> (self.numStateBits - 1)
        self.output.push(bit)
        for _ in range(self.numUnderFlow):
            self.output.push(bit ^ 1)
        self.numUnderFlow = 0

    def underflow(self):
        self.numUnderFlow += 1


def encode(args):
    if len(args) < 2:
        sys.exit("[in,out]")
    inputFile, outputFile = args
    with open(inputFile, "rb") as inp, \
            contextlib.closing(Pusher(open(outputFile, "wb"))) as outBit:
        initialFrequencies = FlatFrequencyTable(257)
        frequencies = SimpleFrequencyTable(initialFrequencies)
        enc = Encoder(32, outBit)
        while True:
            symbol = inp.read(1)
            if len(symbol) == 0:
                break
            enc.write(frequencies, symbol[0])
            frequencies.increment(symbol[0])
        enc.write(frequencies, 256)
        enc.finish()


class Decoder(Base):
    def __init__(self, numBits, bitIn):
        super(Decoder, self).__init__(numBits)
        self.input = bitIn
        self.code = 0
        for _ in range(self.numStateBits):
            self.code = self.code << 1 | self.readCodedBit()

    def read(self, frequencies):
        if not isinstance(frequencies, CheckedFrequencyTable):
            frequencies = CheckedFrequencyTable(frequencies)
        total = frequencies.getTotal()
        if total > self.maxTotal:
            raise ValueError("symbol total wieksza niz mmax")
        scope = self.high - self.low + 1
        offset = self.code - self.low
        value = ((offset + 1) * total - 1) // scope
        assert value * scope // total <= offset
        assert 0 <= value < total

        start = 0
        end = frequencies.symbolLimes()
        while end - start > 1:
            middle = (start + end) >> 1
            if frequencies.getLow(middle) > value:
                end = middle
            else:
                start = middle
        assert start + 1 == end

        symbol = start
        assert frequencies.getLow(symbol) * scope // total <= offset < frequencies.getHigh(symbol) * scope // total
        self.update(frequencies, symbol)
        if not (self.low <= self.code <= self.high):
            raise AssertionError("kod poza zasiegiem")
        return symbol

    def shift(self):
        self.code = ((self.code << 1) & self.stateMask) | self.readCodedBit()

    def underflow(self):
        self.code = (self.code & self.segmentHalf) | ((self.code << 1) & (self.stateMask >> 1)) | self.readCodedBit()

    def readCodedBit(self):
        temp = self.input.pull()
        if temp == -1:
            temp = 0
        return temp


def decode(args):
    if len(args) < 2:
        sys.exit("[in,out]")
    inputFile, outputFile = args
    with open(inputFile, "rb") as x, open(outputFile, "wb") as y:
        BitIn = Puller(x)
        initialFrequencies = FlatFrequencyTable(257)
        frequencies = SimpleFrequencyTable(initialFrequencies)
        dec = Decoder(32, BitIn)
        while True:
            symbol = dec.read(frequencies)
            if symbol == 256:
                break
            y.write(bytes((symbol,)))
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


def main():
    filepath = "/home/piotr/Documents/data-compression-and-coding/Tests/pan-tadeusz-czyli-ostatni-zajazd-na-litwie.txt"
    encode([filepath, "code"])
    decode(["code", "decode"])
    getInformation(filepath)


main()
