from point import Point


class Parabola:

    is_focus = 0
    is_vertex = 1

    def __init__(self, point):
        if isinstance(point, Point):
            self.point = point
            self.type = self.is_focus
        else:
            self.point = None
            self.type = self.is_vertex
        self.parent = None
        self.left_child = None
        self.right_child = None
        self.edge = None
        self.event = None

    def __repr__(self):
        return f"Focus at {format(self.point)}" if self.type == self.is_focus \
            else f"Vertex/Edge begining at {format(self.edge.start)}"

    def set_left_child(self, point):
        self.left_child = point
        self.left_child.parent = self

    def set_right_child(self, point):
        self.right_child = point
        self.right_child.parent = self

    @staticmethod
    def get_left_child(p):
        if p is None:
            return None
        child = p.left_child
        while child.type == Parabola.is_vertex:
            child = child.right_child
        return child

    @staticmethod
    def get_right_child(p):
        if p is None:
            return None
        child = p.right_child
        while child.type == Parabola.is_vertex:
            child = child.left_child
        return child

    @staticmethod
    def get_left_parent(p):
        parent = p.parent
        if parent is None:
            return None
        last = p
        while parent.left_child == last:
            if parent.parent is None:
                return None
            last = parent
            parent = parent.parent

        return parent

    @staticmethod
    def get_right_parent(p):
        parent = p.parent
        if parent is None:
            return None
        last = p
        while parent.right_child == last:
            if parent.parent is None:
                return None
            last = parent
            parent = parent.parent

        return parent

    @staticmethod
    def get_left(p):
        return Parabola.get_left_child(Parabola.get_left_parent(p))

    @staticmethod
    def get_right(p):
        return Parabola.get_right_child(Parabola.get_right_parent(p))