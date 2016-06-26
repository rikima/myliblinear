#!/bin/sh
cur=$(dirname $0)
pushd $cur

jar=$cur/target/scala-2.11/myliblinear_2.11-1.0.jar
for j in $(ls ./lib/*.jar) ; do
    jar=$jar:$j
done
echo $jar

java -cp $jar de.bwaldvogel.liblinear.Train $*

popd