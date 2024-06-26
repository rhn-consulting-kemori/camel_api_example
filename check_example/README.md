# check_example
## Build & Run
mvn clean spring-boot:run

## Build Container Image
mvn package
podman build -t <your image tag> .

## Run COntainer
podman run -d --name <container name> -p 8182:8182 <your image tag>

## Curl
curl http://localhost:8182/camel/mock/card-check/A-001/3540000100010001