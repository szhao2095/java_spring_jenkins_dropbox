# java_spring_jenkins_docker (dont mind the typo)
Java Springboot, Jenkins + Docker pipeline

### TODO:
- Unit test for controller
- Integration tests
- Jenkins pipeline
- Docker setup related
- iaas ?
- Frontend related Typescript etc..
- Postgres

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