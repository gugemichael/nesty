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

* Bussiness Controlloer. Just like SpringMVC or JettyJersey

```java
@Controller
@RequestMapping("/projects")
public class ServiceController {

	// [POST] http://host:port/projects/1
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ServiceResponse createProject(@Body ProjectModel project) {
		System.out.println("createProject() projectName " + project.getProjectName());
		return new ServiceResponse();
	}

	// [GET] http://host:port/projects
	@RequestMapping("/")
	public ServiceResponse getAllProjects() {
		System.out.println("getAllProjects()");
		return new ServiceResponse();
	}

	// [GET] http://host:port/projects/1
	@RequestMapping("/{projectId}")
	public ServiceResponse getProjectById(@PathVariable("projectId") Integer projectId) {
		System.out.println("getProjectById() projectId " + projectId);
		return new ServiceResponse();
	}

	// [GET] http://host:port/projects/name/my_project1?owner=nesty
	@RequestMapping("/name/{projectName}")
	public ServiceResponse getProjectByName(@PathVariable("projectName") String projectName,
												@RequestParam(value = "owner", required = false) String owner) {
		System.out.println("getProjectByNam() projectName " + projectName + ", owner " + owner);
		return new ServiceResponse();
	}

	// [UPDATE] http://host:port/projects/1
	@RequestMapping(value = "/{projectId}", method = RequestMethod.UPDATE)
	public ServiceResponse updateProjectNameById(@PathVariable("projectId") Integer projectId,
													@Body ProjectModel project) {
		System.out.println("updateProjectNameById projectId " + projectId + ". projectName " + project.getProjectName());
		return new ServiceResponse();
	}
}

```


## Threads Model

* Netty Bootstrap(io threads) + ThreadPool(logic threads)

## Performance
