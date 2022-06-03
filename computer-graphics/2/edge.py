from point import Point


class Edge:

    def __init__(self, first, left, right):
        self.start = first
        self.site_left = left
        self.site_right = right
        self.direction = Point(right.y - left.y, -(right.x - left.x))
        self.end = None
        self.neighbour = None
        self.slope = (right.x - left.x)/(left.y - right.y)
        self.yint = (left.y + right.y)/2 - self.slope * (right.x + left.x)/2

    def __repr__(self):
        return self.start + ', ' + self.end
