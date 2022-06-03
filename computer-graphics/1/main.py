from matplotlib import pyplot as plt, patches


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return "(" + str(self.x) + " ; " + str(self.y) + ")"


class RNode:
    def __init__(self, left_ind, interval_points):
        self.left_ind = left_ind
        self.right_ind = left_ind + len(interval_points)
        self.left_son = None
        self.right_son = None
        data = []
        for p in interval_points:
            for y in p[1]:
                data.append(Point(p[0], y))
        data = sorted(data, key=lambda d: d.y)
        self.interval_points = build_tb_tree(None, data)

    def __repr__(self):
        return "{" + str(self.left_ind) + " " + str(self.right_ind) + " l:" + str(self.left_son) + \
               " r:" + str(self.right_son) + str(self.interval_points) + "}"


class TBNode:
    def __init__(self, point):
        self.point = point
        self.left = None
        self.right = None
        self.right_thread = None


def read_points(filename):
    points = []
    with open(filename) as f:
        input_lines = f.readlines()
        for line in input_lines:
            x, y = line.split()
            points.append(Point(float(x), float(y)))
        return points


def read_region(filename):
    with open(filename) as f:
        line1 = f.readline()
        x1, x2 = line1.split()
        x = [float(x1), float(x2)]
        line2 = f.readline()
        y1, y2 = line2.split()
        y = [float(y1), float(y2)]
        return x, y


def generate_intervals(points):
    intervals = {}
    for p in points:
        if p.x not in intervals:
            intervals[p.x] = [p.y]
        else:
            intervals[p.x].append(p.y)
    return intervals


def build_tb_tree(parent, points):
    mid_ind = len(points) // 2
    root = TBNode(points[mid_ind])
    if mid_ind > 0:
        root.left = build_tb_tree(root, points[:mid_ind])
        # print(points)
        if len(points) > 2:
            root.right = build_tb_tree(parent, points[mid_ind + 1:])
    else:
        root.right_thread = parent
    return root


def build_region_tree(left_ind, points):
    mid_ind = len(points) // 2
    root = RNode(left_ind, points)

    if mid_ind > 0:
        # print(points[:mid_ind+1])
        # print(points[mid_ind+1:])
        root.left_son = build_region_tree(left_ind, points[:mid_ind])
        root.right_son = build_region_tree(left_ind + mid_ind, points[mid_ind:])

    return root


def get_intervals_pointers(intervals, x_region):
    x_pointers = []
    # print(intervals)
    flag = False
    for i in range(len(intervals)):
        if intervals[i][0] >= x_region[0] and not flag:
            # print(i)
            x_pointers.append(i)
            flag = True

        if intervals[i][0] > x_region[1]:
            x_pointers.append(i)
            break
    # print(x_pointers)
    while len(x_pointers) < 2:
        x_pointers.append(len(intervals))
    return x_pointers


def y_search(root: TBNode, y_region):
    cur_node = root
    res = []
    while cur_node.left is not None:
        cur_node = cur_node.left

    while cur_node.point.y < y_region[0]:
        if cur_node.right is not None:
            cur_node = cur_node.right
        elif cur_node.right_thread is not None:
            cur_node = cur_node.right_thread
        else:
            return []

    while cur_node.point.x <= y_region[1]:
        res.append(cur_node.point)
        if cur_node.right is not None:
            cur_node = cur_node.right
        elif cur_node.right_thread is not None:
            cur_node = cur_node.right_thread
        else:
            break

    return res


def region_search(root: RNode, x_region, y_region):
    if root.left_ind >= x_region[1] or root.right_ind <= x_region[0]:
        return []

    if root.right_ind - root.left_ind < 4:
        return y_search(root.interval_points, y_region)
    res = []
    left_reg = region_search(root.left_son, x_region, y_region)
    if left_reg:
        res += left_reg
    right_reg = region_search(root.right_son, x_region, y_region)
    if right_reg:
        res += right_reg
    return res


def init():
    points = read_points("points.txt")
    x_region, y_region = read_region("region.txt")
    points = sorted(points, key=lambda p: p.x)

    fig, ax = plt.subplots(2)
    ax[0].set_xlim([0, 9])
    ax[0].set_ylim([0, 9])
    ax[1].set_xlim([0, 9])
    ax[1].set_ylim([0, 9])

    rect = patches.Rectangle((x_region[0], y_region[0]), x_region[1] - x_region[0], y_region[1] - y_region[0], linewidth=1, edgecolor='b',
                             facecolor='none')
    ax[0].add_patch(rect)

    for point in points:
        circle = patches.Circle((point.x, point.y), radius=0.051, color='b')
        ax[0].add_patch(circle)

    rect2 = patches.Rectangle((x_region[0], y_region[0]), x_region[1] - x_region[0], y_region[1] - y_region[0], linewidth=1, edgecolor='b',
                              facecolor='none')

    ax[1].add_patch(rect2)
    intervals = generate_intervals(points)
    data = [[x, y] for x, y in intervals.items()]
    tree = build_region_tree(0, data)
    result = region_search(tree, get_intervals_pointers(data, x_region), y_region)

    for point in result:
        circle = patches.Circle((point.x, point.y), radius=0.051, color='b')
        ax[1].add_patch(circle)

    plt.show()


init()
