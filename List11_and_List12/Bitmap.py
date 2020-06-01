from Pixel import *


def check_cond(x, ret_x, wid):
    """
    Checks if given values are in 0,256 dist
    :param x:
    :param ret_x:
    :param wid:
    :return:
    """
    if x < 0:
        ret_x = 0
    elif x >= wid:
        ret_x = wid - 1
    return ret_x


class Bitmap:
    """
    Represents the bitmap of tga format image
    """

    def __init__(self, bitmap, wid, hi):
        self.wid = wid
        self.hi = hi
        result = []
        level = []
        for i in range(wid * hi):
            level.append(Pixel(blue=bitmap[i * 3], green=bitmap[i * 3 + 1], red=bitmap[i * 3 + 2]))
            if wid == len(level):
                result.insert(0, level)
                level = []
        self.bitmap = result

    def __getitem__(self, pos):
        x, y = pos
        ret_x, ret_y = x, y
        ret_x = check_cond(x, ret_x, self.wid)
        ret_y = check_cond(y, ret_y, self.hi)
        return self.bitmap[ret_y][ret_x]


def parse(bitmap, wid, hi):
    """
    Parsing map to list of pixels
    :param bitmap:
    :param wid:
    :param hi:
    :return:
    """
    result = []
    level = []
    for i in range(wid * hi):
        level.append(Pixel(blue=bitmap[i * 3], green=bitmap[i * 3 + 1], red=bitmap[i * 3 + 2]))
        if wid == len(level):
            result.insert(0, level)
            level = []
    return result
