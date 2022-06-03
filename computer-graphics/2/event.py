from point import Point


class Event:

    site_event = 0
    circle_event = 1

    def __init__(self, point, p_type):
        self.point = point
        self.type = p_type
        self.arc = None

    def compare(self, other):
        return self.point.compare(other.point)

    def __lt__(self, other):
        return self.point < other.point

    def __repr__(self):

        return " " + self.point.__repr__() + ' ' + str(self.type)
