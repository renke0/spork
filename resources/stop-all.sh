#!/usr/bin/env bash

script_path=`dirname $0`
cd ${script_path}

sh stop-mockserver.sh 1080

echo "All services stopped"
