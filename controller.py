#!/usr/bin/env python
# -*- coding: utf-8 -*-

import Queue
import random
import subprocess
import threading
import thread
import time
import os
import sys

from uiautomator import device as d

# import uiautomator2 as u2

# d = u2.connect_usb()

stop = False


def read_logcat():
    global stop

    # cmd = 'adb logcat | grep --color=always -E "%s" ' % filters
    cmd = 'adb logcat'
    # You'll need to add any command line arguments here.
    process = subprocess.Popen([cmd], stdout=subprocess.PIPE, shell=True)

    while True:
        line = process.stdout.readline()
        if line != '':
            # print line
            if "keyboard hide" in line.rstrip():
                stop = True
                break
        else:
            break


def get_event_count():
    if len(sys.argv) > 1:
        return int(sys.argv[1])
    else:
        return 100


def random_click(start_x, end_x, start_y, end_y):
    global stop
    start = 0
    event_count = get_event_count()
    while start < event_count:
        time.sleep(0.05)
        x = start_x + random.random() * (end_x - start_x)
        y = start_y + random.random() * (end_y - start_y)
        d.click(x, y)

        start += 1

        print start, x, y

        if stop:
            break


def run():
    screen_width = d.info['displayWidth']
    screen_height = d.info['displayHeight']
    keyboard_width = d.info['displayWidth']

    toolbar_height = screen_width * 0.11944444444444445
    keyboard_height = screen_width * 0.6444444444444445
    ime_panel_height = toolbar_height + keyboard_height
    cmd = 'adb shell am start im.weshine.testcontroller/im.weshine.testcontroller.MainActivity'
    os.system(cmd)

    time.sleep(4)

    start_y = int(d(resourceId="im.weshine.testcontroller:id/tv_info").info['text'])
    end_y = start_y + ime_panel_height

    thread.start_new_thread(read_logcat, ())

    random_click(0, screen_width, start_y, end_y)


if __name__ == '__main__':
    run()
