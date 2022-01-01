#!/bin/bash
cd `dirname $0`/..
java -Xms512m -Xmx8g -classpath "build\production\assignments;build\classes\java\main" %*
