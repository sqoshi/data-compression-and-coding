import subprocess
import sys

# author Piotr Popis
import time


def get_millis(seconds):
    """
    seconds to millis converter
    :param seconds:
    :return:
    """
    return seconds * 10 ** 3


def get_current_time():
    """
    :return: current time
    """
    return get_millis(float(time.time()))


def test():
    """
    Procedure of executions to get decoded file and results. -- test
    :return:
    """
    start = get_current_time()
    # test_file = '/home/piotr/Documents/data-compression-and-coding/Tests/tadeusz'
    if len(sys.argv) < 2:
        sys.stderr.write('python3 test.py input')
        sys.exit()
    test_file = sys.argv[1]
    subprocess.call(
        ['python3', 'koder.py', test_file, 'encoded.txt'])
    subprocess.call(['python3', 'szum.py', '0.01', 'encoded.txt', 'noised.txt'])
    subprocess.call(['python3', 'dekoder.py', 'noised.txt', 'decoded.txt'])
    subprocess.call(
        ['python3', 'sprawdz.py', test_file, 'decoded.txt'])
    subprocess.call(['rm', 'encoded.txt', 'noised.txt'])
    end = get_current_time()
    print('Execution time:', '%.3f' % ((end - start) / 10 ** 3), 's')


test()
