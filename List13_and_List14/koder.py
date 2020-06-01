import sys
from array import *

from Hamming import *


def bitstring_to_bytes(s):
    return int(s, 2).to_bytes(len(s) // 8, byteorder='big')


if __name__ == '__main__':
    if len(sys.argv) < 3:
        sys.stderr.write('Not enough arguments, example use: \n \t python3 koder.py in out')
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
