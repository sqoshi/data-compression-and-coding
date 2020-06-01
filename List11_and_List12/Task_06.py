# author Piotr Popis
import sys

from Bitmap import *
from Pixel import *


def save_output(header, footer, res1, b, res2, quantified):
    """
    Produces main output of encoding
    :param header:
    :param footer:
    :param res1:
    :param b:
    :param res2:
    :param quantified:
    :return:
    """
    with open("low_out.tga", "wb") as f:
        f.write(header + res1 + footer)
    with open("low_out_enc", "wb") as f:
        f.write(header + b + footer)
    with open("high_out.tga", "wb") as f:
        f.write(header + res2 + footer)
    with open("high_out_enc.tga", "wb") as f:
        f.write(header + quantified + footer)


def read_tga(file):
    """
    read tga image data
    :param file:
    :return:
    """
    with open(file, "rb") as f:
        tga = f.read()
    return tga, tga[:18], tga[len(tga) - 26:], tga[13] * 256 + tga[12], tga[15] * 256 + tga[14]


def compute_mse(original, new):
    """
    compute mean square error
    :param original:
    :param new:
    :return:
    """
    return (1 / len(original)) * sum([(a - b) ** 2 for a, b in zip(original, new)])


def compute_snr(x, mserr):
    """
    compute snare noise ratio
    :param x:
    :param mserr:
    :return:
    """
    return ((1 / len(x)) * sum([i ** 2 for i in x])) / mserr


def compute_errors(old, new):
    """
    Computate mse and snr as i n formal
    :param original:
    :param new:
    :return:
    """
    in_arr = []
    for pixel in old:
        in_arr += [pixel.blue, pixel.green, pixel.red]
    original_red, original_green, original_blue = [pixel.red for pixel in old], [pixel.green for pixel in
                                                                                 old], [pixel.blue for pixel
                                                                                        in old]
    out_arr = []
    for pixel in new:
        out_arr += [pixel.blue, pixel.green, pixel.red]
    new_red, new_green, new_blue = [pixel.red for pixel in new], [pixel.green for pixel in new], [pixel.blue for pixel
                                                                                                  in new]
    mserr = compute_mse(in_arr, out_arr)
    return mserr, compute_mse(original_red, new_red), compute_mse(original_green, new_green), \
           compute_mse(original_blue, new_blue), compute_snr(in_arr, mserr)


def encode(bitmap, k):
    """
    Encodes image
    :param bitmap:
    :param k:
    :return:
    """
    res1 = [percolate(bitmap, x, y) for y in reversed(range(bitmap.hi)) for x in range(bitmap.wid)]
    res2 = [percolate(bitmap, x, y, True) for y in reversed(range(bitmap.hi)) for x in range(bitmap.wid)]
    low = diff_code(res1)
    bytes_list = convert_to_list(low)
    bytes_list = [2 * x if x > 0 else abs(x) * 2 + 1 for x in bytes_list]
    bitstring = "".join([encode_num(x) for x in bytes_list])
    if len(bitstring) % 8 != 0:
        bitstring += "0" * (8 - (len(bitstring) % 8))
    quan = quantify(res2, k)
    quantified_bytes = bytes(convert_to_list(quan))
    bitmap = [bitmap[x, y] for y in reversed(range(bitmap.hi)) for x in range(bitmap.wid)]

    print_card(bitmap, res1, quan)
    return res1, bytes(int(bitstring[i: i + 8], 2) for i in range(0, len(bitstring), 8)), res2, quantified_bytes


def decode(low_data):
    """
    Decodes image
    :param low_data:
    :return:
    """
    hex_str = low_data.hex()
    bits = "".join(["{0:08b}".format(int(hex_str[x: x + 2], base=16)) for x in range(0, len(hex_str), 2)])
    codes = decode_code(bits)
    differences = [x // 2 if x % 2 == 0 else -(x // 2) for x in codes]
    bitmap = [Pixel(int(differences[i + 2]), int(differences[i + 1]), int(differences[i])) for i in
              range(0, len(differences), 3)]
    bitmap = diff_decode(bitmap)
    bitmap = convert_to_list(bitmap)
    return bytes(bitmap)


def encode_num(number):
    """
    Encodes number
    :param number:
    :return:
    """
    return '0' * (len(bin(number)[2:]) - 1) + bin(number)[2:]


def decode_code(code):
    """
    Decodes code
    :param code:
    :return:
    """
    res = []
    counter = 0
    i = 0
    while i < len(code):
        if code[i] == "0":
            counter += 1
            i += 1
        else:
            res.append(int(code[i: i + counter + 1], base=2))
            i += counter + 1
            counter = 0
    return res


def setup_component(color):
    """
    Controls the range of color component
    :param color:
    :return:
    """
    if color < 0:
        color = 0
    elif color > 255:
        color = 255
    return color


def setup_pixel(pixel):
    """
    Controls all components of given pixel
    :param pixel:
    :return:
    """
    pixel.red = setup_component(pixel.red)
    pixel.green = setup_component(pixel.green)
    pixel.blue = setup_component(pixel.blue)


def percolate(bitmap, x, y, high=False):
    """
    Filters upper,lower
    :param bitmap:
    :param x:
    :param y:
    :param high:
    :return:
    """
    w1 = [[1, 1, 1],
          [1, 1, 1],
          [1, 1, 1]]
    w2 = [[0, -1, 0],
          [-1, 5, -1],
          [0, -1, 0]]
    weights = w2 if high else w1
    pixel_org = Pixel(0, 0, 0)
    for i in range(-1, 2):
        for j in range(-1, 2):
            pixel_org += bitmap[x + i, y + j] * weights[i + 1][j + 1]
    weights_sum = sum([sum(level) for level in weights])
    if weights_sum <= 0:
        weights_sum = 1
    pixel_org = pixel_org // weights_sum
    setup_pixel(pixel_org)
    return pixel_org


def convert_to_list(bitmap):
    """
    Converts a bitmap to list
    :param bitmap:
    :return:
    """
    data = []
    for pixel in bitmap:
        data += [pixel.blue, pixel.green, pixel.red]
    return data


def convert_to_bytes(bitmap):
    """
    Convert bitmap to bytes
    :param bitmap:
    :return:
    """
    data = []
    for pixel in bitmap:
        data += [pixel.blue, pixel.green, pixel.red]
    return bytes(data)


def diff_code(bitmap):
    """
    Performs differntail encoding
    :param bitmap:
    :return:
    """
    item = bitmap[0]
    result = [item]
    for pixel in bitmap[1:]:
        item = pixel - item
        result.append(item)
        item = pixel
    return result


def diff_decode(differences):
    """
    Performs differntail decoding
    :param differences:
    :return:
    """
    item = differences[0]
    result = [item]
    for x in differences[1:]:
        item = item + x
        result.append(item)
    return result


def quantify(bitmap, k):
    """
    Quantizies a bitmap [1,...7]
    :param bitmap:
    :param k:
    :return:
    """
    return [pixel.quant(256 // (2 ** k)) for pixel in bitmap]


def print_card(bitmap, res1, quantified):
    """
    Output info printer
    :param bitmap:
    :param res1:
    :param quantified:
    :return:
    """
    print('=' * 150)
    rows = [['LOW  ||'] + list(compute_errors(bitmap, res1)), ['HIGH ||'] + list(compute_errors(bitmap, quantified))]
    print('{0:<10} {1:<20} {2:<20} {3:<20} {4:<20} {5:<20}'.format('.    ||', 'mse', 'mse red', 'mse green', 'mse blue',
                                                                   'snr'))
    for args in rows:
        print('{0:<10} {1:<20} {2:<20} {3:<20} {4:<20} {5:<20}'.format(*args))
    print('=' * 150)


def main():
    # example use  python3 Task_06.py example0.tga -e 4
    tga, header, footer, wid, hi = read_tga(sys.argv[1])
    if sys.argv[2] == '-e':
        bitmap = Bitmap(tga[18: len(tga) - 26], wid, hi)
        k = int(sys.argv[3])
        res1, b, res2, quantified = encode(bitmap, k)
        res1, res2 = bytes(convert_to_list(res1)), bytes(convert_to_list(res2))

        save_output(header, footer, res1, b, res2, quantified)
    elif sys.argv[2] == "-d":
        data = tga[18:-26]
        bitmap = decode(data)
        with open("out_dec_low.tga", "wb") as f:
            f.write(header + bitmap + footer)


main()
