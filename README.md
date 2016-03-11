# Nesty
Http RESTful Api implemention on Netty async io

## Lastest version
0.1.0

changeslog 
0.1.0 

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

	// start httpserver directly
	AsyncHttpServerProvider.create("127.0.0.1", 8080)
						.scanHttpController("org.nesty.example.httpserver.handler")
						.start();

	// would not to reach here ......
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

## Performance

java -server -Xmx4G -Xms4G -Xmn1536M -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:PermSize=256m -XX:MaxPermSize=256m -XX:+DisableExplicitGC

* Conccurent : 512 http connections 
* Qps : 40,000+
* Latency : < 5ms

detail : https://github.com/gugemichael/nesty/wiki/Performance-Detail

