import time


def go():
    with open("input.txt") as f:
        lines = f.readlines()
        return lines


def test():
    start = time.process_time_ns()
    lines = go()
    elapsed = time.process_time_ns() - start
    print(f"{len(lines)} in {((elapsed / 1000))} us")


test()
