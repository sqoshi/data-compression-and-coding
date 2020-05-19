import math
import sys
from collections import defaultdict
from functools import reduce
from itertools import chain


def ecd2(a, b):
    return sum(pow((x - y), 2) for x, y in zip(a, b))


def parse(bitmap, width, height):
    return [(bitmap[i * 3], bitmap[i * 3 + 1], bitmap[i * 3 + 2]) for i in range(width * height)]


def get_amount(bitmap, codes):
    result = []
    for p in bitmap:
        diff_list = [ecd2(p, x) for x in codes]
        result.append(codes[diff_list.index(min(diff_list))])
    return result


def get_vector(team):
    result = [0.0, 0.0, 0.0]
    for vec in team:
        for i, x in enumerate(vec):
            result[i] += x / len(team)
    return result


def get_new_vector(c, e):
    return [x * (1.0 + e) for x in c]


def get_malformation(c, data, size):
    return reduce(lambda s, d: s + d / size, (ecd2(c, vec) for vec in data), 0.0)


def get_malformation_all(list, data, size):
    return reduce(lambda s, d: s + d / size, (ecd2(c_i, data[i]) for i, c_i in enumerate(list)), 0.0)


def get_bytes(bitmap):
    return bytes(list(chain.from_iterable(bitmap)))


def get_mse(old, new):
    return (1 / len(old)) * sum([pow(ecd2(old[i], new[i]), 2) for i in range(len(old))])


def get_snr(x, mserr):
    return ((1 / len(x)) * sum(sum(pow(xij, 2) for xij in xi) for xi in x)) / mserr


def get_codes_all(data, size):
    eps = 1e-5
    codes = []
    result = []
    c = get_vector(data)
    codes.append(c)
    distance = get_malformation(c, data, len(data))
    while len(codes) < size:
        codes, distance = divide(data, codes, distance, eps)
    for b, g, r in codes:
        result.append((math.floor(b), math.floor(g), math.floor(r)))
    return result


def update(codes, environment_vectors, environment_vectors_indexes, neighbour):
    for code in range(len(codes)):
        env = environment_vectors.get(code) or []
        if len(env) > 0:
            new = get_vector(env)
            codes[code] = new
            for i in environment_vectors_indexes[code]:
                neighbour[i] = new


def build_env(data, codes, neighbour, environment_vectors, environment_vectors_indexes):
    for i, vec in enumerate(data):
        min_distance, closest_c_index = None, None
        for code, c in enumerate(codes):
            d = ecd2(vec, c)
            if min_distance is None or d < min_distance:
                min_distance = d
                neighbour[i] = c
                closest_c_index = code
        environment_vectors[closest_c_index].append(vec)
        environment_vectors_indexes[closest_c_index].append(i)


def build_new_codes(codes, eps):
    cnew = []
    for c in codes:
        c1 = get_new_vector(c, eps)
        c2 = get_new_vector(c, -eps)
        cnew.extend((c1, c2))
    return cnew


def divide(data, codes, initial, eps):
    distance, step = 0, 0
    err = eps + 1
    codes = build_new_codes(codes, eps)
    while err > eps:
        neighbour = [None] * len(data)
        environment_vectors = defaultdict(list)
        environment_vectors_indexes = defaultdict(list)
        build_env(data, codes, neighbour, environment_vectors, environment_vectors_indexes)
        update(codes, environment_vectors, environment_vectors_indexes, neighbour)
        if distance > 0:
            previous_distance = distance
        else:
            previous_distance = initial
        distance = get_malformation_all(neighbour, data, len(data))
        err = (previous_distance - distance) / previous_distance
        step += 1
    return codes, distance


def print_results(mse, snr):
    print("mean square error:", mse)
    print("signal to noise ratio:", snr)


def main():
    with open(sys.argv[1], "rb") as f:
        tga = f.read()
    header, footer = tga[:18], tga[-26:]
    width, height = tga[13] * 256 + tga[12], tga[15] * 256 + tga[14]
    image_bitmap = parse(tga[18:-26], width, height)
    codes = get_codes_all(image_bitmap, pow(2, int(sys.argv[3])))
    current_bitmap = get_amount(image_bitmap, codes)
    pack = get_bytes(current_bitmap)
    m = get_mse(image_bitmap, current_bitmap)
    s = get_snr(image_bitmap, m)
    print_results(m, s)
    with open(sys.argv[2], "wb") as f:
        f.write(header + pack + footer)


main()
