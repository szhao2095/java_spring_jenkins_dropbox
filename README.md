# java_spring_jenkins_docker (dont mind the typo)
Java Springboot, Jenkins + Docker pipeline

### TODO:
- [ ] Frontend related Typescript etc..
- [ ] Integration tests
- [ ] Unit test for controller ?
- [ ] iaas ?

Folder organization
````
src/main/java/com/example/UserAuth
│
├── controller
│   ├── UserApiController.java
│   └── UserController.java // login, registration
│
├── model
│   └── User.java
│
├── exception
│   └── UsernameAlreadyExistsException.java
│
├── repository
│   └── UserRepository.java
│
├── service
│   ├── UserService.java
│
├── security
│   ├── CustomAuthenticationFailureHandler.java
│   ├── SecurityConfig.java
│   ├── ThymeleafConfig.java
│   └── WebSecurityConfig.java
│
└── UserAuthApplication.java

src/test/java/com/example/UserAuth
│
├── controller
│   └── UserControllerTests.java
│
├── repository
│   └── UserRepositoryTests.java
│
├── service
│   └── UserServiceTests.java
│
````


```mermaid
Pipeline;
    SCM(Git)-->Jenkins (Docker image);
    Jenkins-->Agent (Docker image, same Docker using alpine:socat)
    Agent-->Build, Test (using application-test.properties)
    Agent-->Build docker image (with prod configured in ENV), Upload to Dockerhub
```