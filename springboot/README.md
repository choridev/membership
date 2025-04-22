# 📦 Membership API Server

Spring Boot 기반의 Membership API 서버입니다.
사용자는 멤버십을 등록하고, 조회하고, 포인트를 추가하거나 삭제할 수 있습니다.
TDD 프로그래밍 학습을 위해 개발했습니다.
[망나니개발자님의 ATDD-Membership](https://github.com/MangKyu/ATDD-Membership)을 참고하여 작성했습니다.

---

## 📁 주요 기능

- 멤버십 등록
- 멤버십 전체/단일 조회
- 멤버십 삭제
- 멤버십 포인트 추가

---

## 📌 기술 스택

- Java 17
- Spring Boot 3.4.4
- Spring Web
- Spring Validation
- Lombok

---

## 📌 기타

- 유효성 검사는 `javax.validation` 어노테이션으로 처리됩니다.
- 모든 응답은 RESTful한 상태 코드와 함께 반환됩니다.