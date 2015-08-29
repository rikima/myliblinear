#!/bin/sh
cur=$(dirname $0)

jar=$cur/target/myliblinear-1.0-SNAPSHOT.jar

java -cp $jar de.bwaldvogel.liblinear.Predict $*