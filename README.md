# 콘서트 예약 서비스

## Getting Started
### Prerequisites
#### Running Docker Containers
`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```

```
docker-compose -f ./docker-compose-redis.yml up -d
```

---
## 동시성 제어 기법(Lock) 비교 분석 및 구현
### [동시성 제어 기법(Lock) 비교 분석 및 구현](https://github.com/han-chunsik/hhplus-service/wiki/%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%A0%9C%EC%96%B4-%EA%B8%B0%EB%B2%95(Lock)-%EB%B9%84%EA%B5%90-%EB%B6%84%EC%84%9D-%EB%B0%8F-%EA%B5%AC%ED%98%84)

## Week-01: 요구사항 분석 및 설계
### [1. Milestone](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%EB%A7%88%EC%9D%BC%EC%8A%A4%ED%86%A4)
### [2. 요구사항 정의 및 설계](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EC%9D%98-%EB%B0%8F-%EC%84%A4%EA%B3%84)
- [기능/비기능 요구사항 정의](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EC%9D%98-%EB%B0%8F-%EC%84%A4%EA%B3%84#-%EA%B8%B0%EB%8A%A5%EB%B9%84%EA%B8%B0%EB%8A%A5-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD)
- [FlowChart](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EC%9D%98-%EB%B0%8F-%EC%84%A4%EA%B3%84#-flowchart)
- [Sequencediagram](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EC%9D%98-%EB%B0%8F-%EC%84%A4%EA%B3%84#-sequencediagram)
- [Statediagram](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD-%EC%A0%95%EC%9D%98-%EB%B0%8F-%EC%84%A4%EA%B3%84#-statediagram)

### [3. ERD](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-DB-%EC%84%A4%EA%B3%84)

### [4. 패키지 구조](https://github.com/han-chunsik/hhplus-service/wiki/%EC%84%A4%EA%B3%84-%7C-%ED%8C%A8%ED%82%A4%EC%A7%80-%EA%B5%AC%EC%A1%B0)