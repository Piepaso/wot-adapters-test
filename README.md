In the current configuration, the two tests work together:
- the digital test exposes a thing from a demo physical asset;
- the physical test connects to the thing to build another DT;

test-digital.sh also starts the necessary MQTT server (requires Mosquitto).
