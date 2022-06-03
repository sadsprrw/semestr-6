from queue import PriorityQueue
from event import Event
from edge import Edge
from point import Point
from parabola import Parabola
from math import sqrt
from random import random
from matplotlib import pyplot as plt, patches
import numpy as np


class Voronoy:

    def __init__(self, sites):
        self.sites = sites
        self.edges = []
        self.events = PriorityQueue(0)
        self.root = None
        self.curr_y = 0

        self.generate_voronoi()

    def generate_voronoi(self):
        for p in self.sites:
            self.events.put(Event(p, Event.site_event))

        count = 0
        while not self.events.empty():

            e = self.events.get()
            print(e)
            self.curr_y = e.point.y
            count += 1
            if e.type == Event.site_event:
                self.handle_site(e.point)
            else:
                self.handle_circle(e)

        self.curr_y = 200

        self.end_edges(self.root)
        for e in self.edges:
            if e.neighbour is not None:
                e.start = e.neighbour.end
                e.neighbour = None

    def end_edges(self, parabola):
        if parabola.type == Parabola.is_focus:
            parabola = None
            return

        x = self.get_edge_x(parabola)
        parabola.edge.end = Point(x, parabola.edge.slope * x + parabola.edge.yint)

        self.edges.append(parabola.edge)

        self.end_edges(parabola.left_child)
        self.end_edges(parabola.right_child)

        parabola = None

    def handle_site(self, point):
        if self.root is None:
            self.root = Parabola(point)
            return

        parabola = self.get_parabola_by_x(point.x)
        if parabola.event is not None:
            parabola.event.point.x = -1000
            parabola.event.point.y = -1000
            e = self.events.get()
            print(e)
            print(parabola.event)
            print(parabola.event == e)
            parabola.event = None

        start = Point(point.x, self.get_y(parabola.point, point.x))
        el = Edge(start, parabola.point, point)
        er = Edge(start, point, parabola.point)
        el.neighbour = er
        er.neighbour = el
        parabola.edge = el
        parabola.type = Parabola.is_vertex

        p0 = Parabola(parabola.point)
        p1 = Parabola(point)
        p2 = Parabola(parabola.point)

        parabola.set_left_child(p0)
        parabola.set_right_child(Parabola(0))
        parabola.right_child.edge = er
        parabola.right_child.set_left_child(p1)
        parabola.right_child.set_right_child(p2)

        self.check_circle_event(p0)
        self.check_circle_event(p2)

    def handle_circle(self, event):
        p1 = event.arc
        xl = Parabola.get_left_parent(p1)
        xr = Parabola.get_right_parent(p1)

        p0 = Parabola.get_left_child(xl)
        p2 = Parabola.get_right_child(xr)

        if p0 is not None:
            if p0.event is not None:
                print(p0.event)
                p0.event.point.x = -1000
                p0.event.point.y = -1000
                e = self.events.get()
                print(e)
                p0.event = None

        if p2 is not None:
            if p2.event is not None:
                print(p2.event)
                p2.event.point.x = -1000
                p2.event.point.y = -1000
                e = self.events.get()
                print(e)
                p2.event = None

        p = Point(event.point.x, self.get_y(p1.point, event.point.x))

        xl.edge.end = p
        xr.edge.end = p
        self.edges.append(xl.edge)
        self.edges.append(xr.edge)

        higher = Parabola(0)
        current_par = p1

        while current_par != self.root:

            current_par = current_par.parent
            if current_par == xl:
                higher = xl
            if current_par == xr:
                higher = xr

        higher.edge = Edge(p, p0.point, p2.point)

        g_parent = p1.parent.parent
        if p1.parent.left_child == p1:
            if g_parent.left_child == p1.parent:
                g_parent.set_left_child(p1.parent.right_child)
            if g_parent.right_child == p1.parent:
                g_parent.set_right_child(p1.parent.right_child)
        else:
            if g_parent.left_child == p1.parent:
                g_parent.set_left_child(p1.parent.left_child)
            if g_parent.right_child == p1.parent:
                g_parent.set_right_child(p1.parent.left_child)

        op = p1.point
        p1.parent = None
        p1 = None

        self.check_circle_event(p0)
        self.check_circle_event(p2)

    def check_circle_event(self, p):
        lp = Parabola.get_left_parent(p)
        rp = Parabola.get_right_parent(p)

        if lp is None or rp is None:
            return

        a = Parabola.get_left_child(lp)
        c = Parabola.get_right_child(rp)

        if a is None or c is None or a.point == c.point:
            return

        if self.ccw(a.point, p.point, c.point) != 1:
            return

        start = self.get_edge_intersection(lp.edge, rp.edge)

        if start is None:
            return

        dx = p.point.x - start.x
        dy = p.point.y - start.y
        d = sqrt(dx*dx + dy*dy)
        if start.y + d < self.curr_y:
            return

        ep = Point(start.x, start.y + d)
        e = Event(ep, Event.circle_event)
        e.arc = p
        p.event = e
        self.events.put(e)

    def ccw(self, a, b, c):
        area = (b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)
        if area < 0:
            return -1
        elif area > 0:
            return 1
        else:
            return 0

    def get_edge_intersection(self, a, b):
        if b.slope == a.slope and b.yint != a.yint:
            return None

        x = (b.yint - a.yint)/(a.slope - b.slope)
        y = a.slope*x + a.yint
        return Point(x, y)

    def get_edge_x(self, parabola):
        left = Parabola.get_left_child(parabola)
        right = Parabola.get_right_child(parabola)

        p = left.point
        r = right.point

        dp = 2 * (p.y - self.curr_y)
        a1 = 1 / dp
        b1 = -2 * p.x / dp
        c1 = (p.x * p.x + p.y * p.y - self.curr_y * self.curr_y)/dp

        dp2 = 2 * (r.y - self.curr_y)
        a2 = 1 / dp2
        b2 = -2 * r.x / dp2
        c2 = (r.x * r.x + r.y * r.y - self.curr_y * self.curr_y) / dp2

        a = a1 - a2
        b = b1 - b2
        c = c1 - c2

        disc = b * b - 4 * a * c
        x1 = (-b + sqrt(disc)) / (2 * a)
        x2 = (-b - sqrt(disc)) / (2 * a)

        ry = 0
        if p.y > r.y:
            ry = max(x1, x2)
        else:
            ry = min(x1, x2)

        return ry

    def get_parabola_by_x(self, x):
        parabola = self.root
        x1 = 0
        while parabola.type == Parabola.is_vertex:
            x1 = self.get_edge_x(parabola)
            if x1 > x:
                parabola = parabola.left_child
            else:
                parabola = parabola.right_child
        return parabola

    def get_y(self, p, x):
        dp = 2 * (p.y - self.curr_y)
        a1 = 1 / dp
        b1 = -2 * p.x / dp
        c1 = (p.x * p.x + p.y * p.y - self.curr_y * self.curr_y)/dp

        return a1 * x * x + b1 * x + c1


def init():
    N = 7
    points = []

    for i in range(N):
        x = random()*100
        y = random()*100
        points.append(Point(x, y))

    diagram = Voronoy(points)

    fig, ax = plt.subplots()
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)

    for p in points:
        circle = patches.Circle((p.x, p.y), radius=0.5, color='b')
        ax.add_patch(circle)

    for e in diagram.edges:
        x = [e.start.x, e.end.x]
        y = [e.start.y, e.end.y]

        plt.plot(x, y)

    plt.show()


init()
