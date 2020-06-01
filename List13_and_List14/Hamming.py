import subprocess

import numpy as np


def opposite(val):
    return '1' if val == '0' else '0'


def swap(val):
    return 1 if val == 0 else 0


H = np.array([[0, 1, 1, 1, 1, 0, 0, 0],
              [1, 0, 1, 1, 0, 1, 0, 0],
              [1, 1, 0, 1, 0, 0, 1, 0],
              [1, 1, 1, 0, 0, 0, 0, 1]])
G = np.array([[swap(x) for x in row] for row in H])
test_msg4 = np.array([1, 0, 1, 1])

