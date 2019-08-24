#!/usr/bin/env bash

script_path=`dirname $0`
cd ${script_path}

sh start-mockserver.sh 1080

echo "Finished startup"
