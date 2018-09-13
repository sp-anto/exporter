#!/bin/bash
ltrace -x 'getopt*' -L -F work/ltrace.conf -A 100 -s 100 $1 /dev/null 2>&1