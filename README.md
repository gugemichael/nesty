# Nesty
Http RESTful Api implemention on Netty async io

## Lastest version
0.2.0

changeslog 
0.2.0
	* support http long connection(Connection: keep-alive)
	* support root path (uri path is /) stats and counter information
0.1.0 
	* original snapshot version

## Features

* Http HTTP/1.1 protocol support 

GET | POST | UPDATE | DELETE
--- | --- | --- | ---

* Http Restful serialized (usually as json) in string body (With Gson)
* Http Short Connection on async mode by default (With Netty 4.2)
* Http request mapping variable support

Annotation | From 
--- | --- 
@Header | http header 
@RequestParam | http url query string or http body key value pairs 
@PathVariabl | http uri path vairable with {path} 
@Body | http body 

* Http request mapping method params type support

Class Type | Default value (require = false is set) | Description
--- | --- | --- 
int,short,long | 0 | primitive
float,double | 0.0d | primitive
String | null | string value
Enum | null | enum class type
Class | null | from http body serializer parsed

## TODO
* Long connection support (require explict Connection: Keep-Alive header set)
* Spring or Mybatis intergrated


## Usage

* Simplest http server

```java

public static void main(String[] args) {

    // 1. create httpserver
    HttpServerRouteProvider server = AsyncHttpServerProvider.create("127.0.0.1", 8080);

    // 2. choose http params. this is unnecessary
    server.useOptions(new HttpServerOptions().setMaxConnections(4096)
                                            .setHandlerTimeout(10000)
                                            .setIoThreads(8)
                                            .setHandlerThreads(256));

    server.scanHttpController("com.nesty.test.neptune")
            .scanHttpController("com.nesty.test.billing")
            .scanHttpController("org.nesty.example.httpserver.handler");

    // 3. start http server
    if (!server.start())
        System.err.println("HttpServer run failed");

    try {
        // join and wait here
        server.join();
        server.shutdown();
    } catch (InterruptedException ignored) {
    }

    // would not to reach here as usual ......
}
```

* Controlloer

```java
@Controller
@RequestMapping("/projects")
public class ServiceController {

	@RequestMapping(value = "/{projectId}", method = RequestMethod.GET)
	public ServiceResponse getProjectById(@PathVariable("projectId") Integer projectId) {
		System.out.println("getProjectById() projectId " + projectId);
		return new ServiceResponse();
	}
}

```

More examples. Please visit https://github.com/gugemichael/nesty/wiki/More-Examples

## Threads Model

* Netty Bootstrap(io threads) + ThreadPool(logic threads)

![screenshot](http://img1.tbcdn.cn/L1/461/1/40ef4fb553fb9b565ddf79989a6f17877dcb3de7)

## Performance

java -server -Xmx4G -Xms4G -Xmn1536M -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:PermSize=256m -XX:MaxPermSize=256m -XX:+DisableExplicitGC

Http short connection
* Conccurent : 512 http connections 
* Qps : 40,000+
* Latency : < 10ms

Http long connection (Connection: keep-alive)
* Conccurent : 512 http connections 
* Qps : 80,000 ~ 100,000
* Latency : < 50ms

detail : https://github.com/gugemichael/nesty/wiki/Performance-Detail

