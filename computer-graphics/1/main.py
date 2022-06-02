from matplotlib import pyplot as plt, patches

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

class Interval:
    def __init__(self, start, end):
        self.start = start
        self.end = end

class Node:
    def __init__(self, interval, left_son, right_son):
        self.interval = interval
        self.left_son = left_son
        self.right_son = right_son

        self.interval_nodes = []

def read_points(filename):
    points = []
    

points = read_points("points.txt")
region = read_points("region.txt")
