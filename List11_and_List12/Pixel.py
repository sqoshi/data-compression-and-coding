class Pixel:
    """
    Abstract representation of pixel image, based on 3 components RGB
    """
    def __init__(self, red, green, blue):
        """
        Instantnate object pixel
        :param red:
        :param green:
        :param blue:
        """
        self.red = red
        self.green = green
        self.blue = blue

    def __add__(self, pixel2):
        """
        Adding values of components
        :param pixel2:
        :return:
        """
        return Pixel(self.red + pixel2.red, self.green + pixel2.green, self.blue + pixel2.blue)

    def __sub__(self, pixel2):
        """
        Substraction of  values in components
        :param pixel2:
        :return:
        """
        return Pixel(self.red - pixel2.red, self.green - pixel2.green, self.blue - pixel2.blue)

    def __mul__(self, k):
        """
        Mult values of components
        :param k:
        :return:
        """
        return Pixel(self.red * k, self.green * k, self.blue * k)

    def __div__(self, k):
        """
        Divides values of components
        :param k:
        :return:
        """
        return Pixel(self.red / k, self.green / k, self.blue / k)

    def __floordiv__(self, k):
        """
        FLoor impl values of components
        :param k:
        :return:
        """
        return Pixel(self.red // k, self.green // k, self.blue // k)

    def __mod__(self, k):
        """
        mod div values of components
        :param k:
        :return:
        """
        return Pixel(self.red % k, self.green % k, self.blue % k)

    def quant(self, step):
        """
        Quantify values of components
        :param step:
        :return:
        """
        return Pixel(int(self.red // step * step), int(self.green // step * step), int(self.blue // step * step))
