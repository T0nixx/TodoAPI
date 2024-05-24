# Todo API

## 개요

간단한 Todo API입니다.

- signUp api를 통해 회원 가입 후, signIn api를 통해 토큰을 발급받습니다. authorization header에 해당
  토큰을 명시하여 다른 api들을 사용할 수 있습니다.
- Todo 생성, 수정, 삭제, 전체 목록 조회, 특정 Todo 조회, 상태 변경
- Comment 생성, 수정, 삭제

## Use-Case Diagram

<p align="center"><img src="assets/todo-uml.png" alt="todo-uml.png"></p>

## Entity Relationship Diagram

<p align="center"><img src="assets/ERD.png" alt="ERD.png"></p>

## API specification

<p align="center"><img src="assets/API-spec.png" alt="API-spec.png"></p>

http://localhost:8080/swagger-ui/index.html#

## Environment

- JDK: temurin 21.0.3
- Kotlin: 1.9.23
- Spring Boot: 3.2.5
- IDE: IntelliJ IDEA 2024.1
- jjwt: 0.12.5
- DB: postgres
