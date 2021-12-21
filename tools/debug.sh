#!/bin/bash
cd `dirname $0`/..
java -ea -Xms512m -Xmx32g -classpath build/production/algs4:build/classes/java/main "$@"
