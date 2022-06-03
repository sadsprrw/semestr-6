
class Point:

    def __init__(self, x, y):
        self.x = x
        self.y = y

    def compare(self, other):
        if self.y == other.y:
            if self.x == other.x:
                return 0
            elif self.x > other.x:
                return 1
            else:
                return -1
        elif self.y > other.y:
            return 1
        else:
            return -1

    def __repr__(self):
        return '(' + str(self.x) + ', ' + str(self.y) + ')'

    def __lt__(self, other):
        if self.y == other.y:
            return self.x < other.x
        else:
            return self.y < other.y
