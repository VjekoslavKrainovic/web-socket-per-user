`docker build -t websocket-backend .`

`docker run -p 8080:8080 -e PORT=8080 websocket-backend`

`http://localhost:8080/test/{authorization-header-value}`