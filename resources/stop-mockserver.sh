#!/usr/bin/env bash
port=$1

echo "Stopping mockserver..."

response=$(curl -X PUT http://localhost:${port}/stop -w "%{http_code}" -s -o /dev/null)

if [[ ${response} == 200 ]]; then
  echo "Stopped mockserver"
else
  echo "Failed to stop mockserver (status $response)"
fi
