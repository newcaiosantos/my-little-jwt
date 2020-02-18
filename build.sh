#!/bin/bash
echo "my-little-jwt bulid begin..."
docker run -v ~/.m2:/root/.m2 -v "$PWD":/usr/src -w /usr/src maven:3.6.1-jdk-8-slim mvn package -DskipTests
echo "my-little-jwt bulid end."