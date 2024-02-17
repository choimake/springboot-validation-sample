# springboot-validate-sample
SpringBootでRestAPIのバリデーションを実装したサンプル

RequestのUnitTestとIntegrationTestの実装方法の検証目的

※ただし、独自アノテーションの実装は試していない

## How to Use
起動し、以下のcurlが通れば成功
```
curl -v -X POST -H "Content-Type: application/json" -d '{"id": "123456789012", "name": "fugafuga", "age": 20}'   POST http://localhost:8080/echo
```

出力例
```
Note: Unnecessary use of -X or --request, POST is already inferred.
* Could not resolve host: POST
* Closing connection 0
curl: (6) Could not resolve host: POST
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#1)
> POST /echo HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.1.2
> Accept: */*
> Content-Type: application/json
> Content-Length: 53
>
< HTTP/1.1 200
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Sat, 17 Feb 2024 00:12:26 GMT
<
* Connection #1 to host localhost left intact
{"id":"123456789012","name":"fugafuga","age":20,"description":null}%

```
