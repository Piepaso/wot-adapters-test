#!/bin/zsh

cleanup() {
  kill 0
  exit 0
}

trap cleanup SIGINT

mosquitto -p 61890 -v > /tmp/mosquitto61890.log 2>&1 &

sleep 0.5

cd ./test-wot-digital-adapter
./gradlew run
