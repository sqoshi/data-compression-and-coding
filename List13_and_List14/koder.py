import sys
from array import *

import numpy as np


def swap(val):
    return 1 if val == 0 else 0


H = np.array([[0, 1, 1, 1, 1, 0, 0, 0],
              [1, 0, 1, 1, 0, 1, 0, 0],
              [1, 1, 0, 1, 0, 0, 1, 0],
              [1, 1, 1, 0, 0, 0, 0, 1]])
G = np.array([[swap(x) for x in row] for row in H])
test_msg4 = np.array([1, 0, 1, 1])


def swap(val):
    return 1 if val == 0 else 0


class Buffer:
    def __init__(self, output_file, max_size=8):
        self.max_size = max_size
        self.bytes = list()
        self.filename = output_file

    def write(self, data):
        self.bytes.append(data)
        if len(self.bytes) >= 8:
            print('writing')


def bitstring_to_bytes(s):
    return int(s, 2).to_bytes(len(s) // 8, byteorder='big')


if __name__ == '__main__':
    if len(sys.argv) < 3:
        sys.stderr.write('Not enough arguments, example use: \n \t python3 koder in out')
        sys.exit()
    file_in, file_out = sys.argv[1], sys.argv[2]
    input_data = open(file_in, "rb").read()

    output_data = open(file_out, "wb")
    bin_arr = array('B')
    for byte in input_data:
        binary_string = list('{0:08b}'.format(byte))
        left, right = np.array([int(x) for x in binary_string[:int(len(binary_string) / 2)]]), np.array(
            [int(x) for x in binary_string[int(len(binary_string) / 2):]])
        bin_arr.append(int(''.join([str(b) for b in np.mod(left.dot(G), 2)]), 2))
        bin_arr.append(int(''.join([str(b) for b in np.mod(right.dot(G), 2)]), 2))
    bin_arr.tofile(output_data)
    print(output_data.__sizeof__())
    print(input_data.__sizeof__())
