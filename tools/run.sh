#!/bin/bash
cd `dirname $0`/..
java -Xms512m -Xmx8g -classpath build/production/algs4:build/classes/java/main "$@"
