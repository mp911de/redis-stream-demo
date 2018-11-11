# Redis Stream Demo Project
 
This repository contains a demo for Redis Streams using Java.

## Modules

* [`simple-streams`](simple-streams/) Simple Stream usage with Java.
* [`redis-stream-frontend`](redis-stream-frontend/) Frontend for Feature Poll.
* [`redis-stream-demo`](redis-stream-demo/) Demo application bundling [`redis-stream-frontend`](redis-stream-frontend/).

## Building

* [`simple-streams`](simple-streams/) Use bundled Maven wrapper. Change directory to [`simple-streams`](simple-streams/) and run `$ ./mvn package`.
* [`redis-stream-frontend`](redis-stream-frontend/) Built through `redis-stream-demo`. Make sure to have NodeJS and `ng` installed (`$ npm i -g ng`).
* [`redis-stream-demo`](redis-stream-demo/) Use bundled Maven wrapper. Change directory to [`redis-stream-demo`](redis-stream-demo/) and run `$ ./mvn package`.

## License

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 
