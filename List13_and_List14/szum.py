import random
import sys
from array import *

from Hamming import *

if __name__ == '__main__':
    if len(sys.argv) < 4:
        sys.stderr.write('Not enough arguments, example use: \n \t python3 szum.py p in out')
        sys.exit()
    p, file_in, file_out = float(sys.argv[1]), sys.argv[2], sys.argv[3]
    input_data = open(file_in, "rb")
    output_data = open(file_out, "wb")
    bin_arr = array('B')
    changes = 0
    all = 0
    for x in input_data.read():
        que = []
        for y in list(bin(x)[2:]):
            all += 1
            if p >= random.uniform(0, 1):
                changes += 1
                y = opposite(y)
            que.append(y)
        bin_arr.append(int(''.join(que), 2))
    bin_arr.tofile(output_data)
    #print(input_data.__sizeof__(), output_data.__sizeof__(), all, changes)
