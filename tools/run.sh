#!/bin/bash
cd `dirname $0`/..
java -Xms512m -Xmx8g -classpath build/classes/java/main "$@"
