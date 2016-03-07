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
						.scanHttpController("org.nesty.example.httpserver.handler")
						.start();

	// would not to reach here ......
}
```

* Standerd http server

```java

public static void main(String[] args) {

	// 1. create httpserver
	HttpServer server = AsyncHttpServerProvider.create("127.0.0.1", 8080);

	// 2. choose server params
	server.setMaxConnections(1024);
	server.setHandlerTimeout(10000);
	server.setIoThreads(4);
	server.setHandlerThreads(256);
	server.scanHttpController("org.nesty.example.httpserver.handler");

	// 3. start server and block for servicing
	if (!server.start()) {
		System.err.println("HttpServer run failed");
	}

	// would not to reach here ......
}
```

* Bussiness Controlloer (just like SpringMVC, JettyJersey)

```java
@Controller
public class ServiceController {

	@RequestMapping(value = "/service/get")
	public ServiceResponse serviceGet() {
		System.out.println("ServiceController api serviceGet() called");
		return new ServiceResponse();
	}

	@RequestMapping(value = "/service/post", method = HttpMethod.POST)
	public ServiceResponse servicePost() {
		System.out.println("ServiceController api servicePost() called");
		return new ServiceResponse();
	}
}

```


## Threads Model

* Netty Bootstrap(io threads) + ThreadPool(logic threads)

## Performance
