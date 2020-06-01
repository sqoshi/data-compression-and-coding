import sys
from array import *

if __name__ == '__main__':
    if len(sys.argv) < 3:
        sys.stderr.write('Not enough arguments, example use: \n \t python3 sprawdz.py in1 in2')
        sys.exit()
    file_in1, file_in2 = sys.argv[1], sys.argv[2]
    input_data1 = open(file_in1, "rb")
    input_data2 = open(file_in2, "rb")
    bin_arr = array('B')
    counter = 0
    for b1, b2 in zip(input_data1.read(), input_data2.read()):
        if bin(b1)[2:4] != bin(b2)[2:4]:
            counter += 1
        if bin(b1)[4:] != bin(b2)[4:]:
            counter += 1
    print(counter)
