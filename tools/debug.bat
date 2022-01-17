@echo off
cd `dirname $0`/..
java -ea -Xms512m -Xmx32g -classpath "build\production\assignments;build\classes\java\main" %*
