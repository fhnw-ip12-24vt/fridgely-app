#!/bin/bash
cd "$1"
pkill java
export DISPLAY=:0
export XAUTHORITY=/home/ip12/.Xauthority
java -XX:+UseZGC -Xmx1G -jar "$2".jar
exit 0
