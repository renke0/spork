#!/usr/bin/env bash
port=$1

echo "stopping mockserver..."

response=$(curl -X PUT http://localhost:${port}/stop -w "%{http_code}" -s -o /dev/null)

if [[ ${response} == 200 ]]; then
  echo "stopped mockserver"
else
  echo "failed to stop mockserver (status $response)"
fi
