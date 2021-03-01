# Spring Project Template

This is a project template with all libraries required in order to create a REST based aplication

## Getting Started 

There are some configurat you need to add to local environment before runnin g the application


- Add `artifactory_user` & `artifactory_password` as environment variable environmet variable
- Run SQL scripts to load

database, you can get a copy from administrator


### Prerequisites


- Required sofware
    - Postgresql
	

### Running Spring Boot Application


From a terminal:

```

$ ./gradlew bootRun -- args='-- spring.profiles.active=dev'
```

#### Running using Docker
You need to install docker in your local machine

```
$ ./gradlew clean build
$ docker build -t myapp/spring-example .
$ docker run -d -p 8080:8082 myapp/spring-example --spring.profiles.active=dev --DS_DB_URL=jdbc:postgresql://host.docker.internal:5432/example --server.port=8082
```

In docker run we are mapping from 8082 to 8080 port so after running `docker run` you should be able to access to:

http://localhost:8080/example-demo/app/greeting

and thre mock response will be shown

```
{
   "greeting": "Hi, from secured endpoint"
}
```

## Running the tests

There are two kind of tests you may want to run


### Unit Testing and Jacoco Coverage


```
$ ./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

### Integration testing


In order to run  this tests you need to run the spring application before,and then type in a terminal


```

$ ./gradlew integrationTest

```

## Authors


* **Victor de la Cruz**  * Initial work* - [vcgdev](https://github.com/VCGDEV)
* **Lucy Sanchez**  * Update docs- [Lucy](https://github.com/lucysriver)
