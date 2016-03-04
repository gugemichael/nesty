# Nesty
Http RESTful Api implemention on Netty async io

## Features

* Http HTTP/1.1 protocol. support GET/POST/UPDATE/DELETE
* Http Restful serialized and formatted(json) in string or Json body (With Gson)
* Http short connection on async mode by default (With Netty 4.2)

## Usage

* simplest http server

```java

public static void main(String[] args) {

	// start httpserver directly
	AsyncHttpServerProvider.create("127.0.0.1", 8080)
						.scanHttpProvider("org.nesty.example.httpserver.handler")
						.start();

	// would not to reach here ......
}
```

* Standerd http server

```java

public static void main(String[] args) {

	// 1. create httpserver
	HttpServer server = AsyncHttpServerProvider.create("127.0.0.1", 8080);

	// 2. choose http params
	server.setMaxConnections(128);
	server.setHandlerTimeout(10000);
	server.setIoThreads(4);
	server.setHandlerThreads(128);
	server.scanHttpProvider("org.nesty.example.httpserver.handler");

	// 3. start server and block for servicing
	if (!server.start()) {
		System.err.println("HttpServer run failed");
	}

	// would not to reach here ......
}
```

* Bussiness Controlloer

```java

public class ServiceController {

	@Path("/a")
	@Method(HttpMethod.GET)
	public ServiceResponse service() {

		// do somethings like DB operation or 
		// rpc call or other http request !!

		return new ServiceResponse();
	}
}

```


## Threads Model

* Netty Bootstrap(io threads) + ThreadPool(logic threads)

## Performance
