import os

def infer_path(path):
    if not os.path.exists(os.path.realpath(path)):
        path = './'+path[len('../../'):]
    return os.path.realpath(path)
