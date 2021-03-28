#!/bin/bash
mydir="$(dirname "$BASH_SOURCE")"
cd $mydir
java -cp ./bin:./lib/commons-math3-3.6.1/commons-math3-3.6.1.jar SimplexSolver