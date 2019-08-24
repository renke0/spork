#!/usr/bin/env bash
port=$1
jar_url="http://search.maven.org/remotecontent?filepath=org/mock-server/mockserver-netty/5.6.1/mockserver-netty-5.6.1-jar-with-dependencies.jar"
file_path="./tmp/mockserver"
jar_name="mockserver.jar"
full_path="$file_path/$jar_name"

if [[ ! -f "$full_path" ]]; then
  mkdir -p ${file_path}
  echo "downloading mockserver..."
  curl ${jar_url} --output ${full_path}
fi

echo "starting mockserver..."
java -jar ${full_path} -serverPort ${port} &

while [[ "$(curl -s -o /dev/null -w "%{http_code}" -X PUT http://localhost:${port}/status)" != "200" ]];
  do sleep 2;
done

echo "Started mockserver on localhost:$port"
