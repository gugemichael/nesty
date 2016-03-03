# Nesty
Http RESTful Api implemention on Netty async io

## 1. Features :

* Http HTTP/1.1 protocol. support GET/POST/UPDATE/DELETE
* Http Restful serialized and formatted(json) in string or Json body (With Gson)
* Http short connection on async mode by default (With Netty 4.2)

## 2. Network Model :

* Netty Bootstrap(io threads) + ThreadPool(logic threads)

## 3. Performance :
