# generic-client
# generic-client
# generic-client

docker run -e FAB_NETWORKCONFIG='network-localhost-config.json' -e PORT='80' --net="host" --name clientapp -d htran1/fabric-client-api:latest