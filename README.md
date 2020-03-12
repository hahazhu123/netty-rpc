# netty-rpc
RPC framework based on netty

There are several moudules in project:

* rpc-api: generally service api means interface.
* rpc-service: service api implement.
* rpc-common: some necessary components both for `rpc-client` and `rpc-server`.
* rpc-client: contain a netty client, send rpc message to server.
* rpc-server: contain a netty server, receive rpc message and provided service.
