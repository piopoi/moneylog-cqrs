# Moneylog-CQRS

[머니로그(moneylog)](https://github.com/piopoi/moneylog) 서비스를
[CQRS Pattern](https://en.wikipedia.org/wiki/Command_Query_Responsibility_Segregation)으로 
Refactoring하는 프로젝트.

<br>

# 사용 기술 & 도구
- Java 21
- Spring Boot 3.1.5
- Spring Security 6.1.5
- **MySQL 8.0.34 -> Command**
- **MongoDB 7.0.11 -> Query**
- **Apache Kafka, ZooKeeper**
- Kafka-ui
- Redis -> Cache
- Docker, Docker Compose
- Swagger
- Jira

<br>

# 서비스 세팅

## Database, Middleware 설치

### Used Docker Images
- mysql:8.0.34
- mongo:7.0.11
- redis:latest
- confluentinc/cp-kafka:latest
- confluentinc/cp-zookeeper:latest
- provectuslabs/kafka-ui:latest

### Docker Compose 실행
```
cd docker
docker-compose -p moneylog-cqrs up -d
```

### kafka 제대로 실행되었는지 확인
```
cd docker/kafka-ui
docker-compose up -d
```

[http://localhost:8089](http://localhost:8089)에 접속하여 
