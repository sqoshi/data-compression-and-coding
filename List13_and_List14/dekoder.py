import sys
from array import *

from Hamming import *

if __name__ == '__main__':
    if len(sys.argv) < 3:
        sys.stderr.write('Not enough arguments, example use: \n \t python3 dekoder.py in out')
        sys.exit()
    file_in, file_out = sys.argv[1], sys.argv[2]
    input_data = open(file_in, "rb").read()
    output_data = open(file_out, "wb")
    bin_arr = array('B')
    error_counter = 0
    bytes_builder = ""
    for byte in input_data:
        y = np.array([int(bit) for bit in list(bin(byte)[2:])])
        while len(y) < 8:
            y = np.insert(y, 0, 0)
        error = np.mod(H.dot(y.reshape(-1, 1)), 2)
        if np.count_nonzero(error) != 0:
            error_pos = int(''.join([str(x[0]) for x in error]), 2) - 1
            if error_pos < 4:
                y[error_pos] = swap(y[error_pos])
            else:
                error_counter += 1
        bytes_builder = bytes_builder + ''.join([str(el) for el in y[:4]])
        if len(bytes_builder) == 8:
            bin_arr.append(int(bytes_builder, 2))
            bytes_builder = ''
    bin_arr.tofile(output_data)
    print('Double errors: ', error_counter)
