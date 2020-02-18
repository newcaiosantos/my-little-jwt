#!/bin/bash
echo "my-little-jwt deploy begin..."
docker-compose down
git pull origin master
./build.sh
docker-compose up --build -d
echo "my-little-jwt deploy end."