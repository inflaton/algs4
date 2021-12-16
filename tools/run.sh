#!/bin/bash
cd `dirname $0`/..
java -Xms512m -Xmx64g -classpath build/classes/java/main "$@"
