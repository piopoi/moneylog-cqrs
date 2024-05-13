# MoneyLog
개인 재무 관리 Server Application.

머니로그(moneylog)는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션으로,
사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움을 주는 것이 목표이다.

<br>

# 사용 기술 & 도구
- Spring Boot 3.1.5
- Spring Security 6.1.5
- MySQL 8.0.34
- Swagger
- Jira

<br>

# TODO

## Infrastructure
- [x] Swagger 적용

## 사용자
- [x] 사용자 엔티티 구현.
- [x] 사용자 생성 API 구현.
  - 필수 정보: 이메일, 비밀번호.
- [x] 사용자 로그인 API 구현.
  - Spring Security, JWT를 이용한 인증/인가 구현.

## 예산
- [x] 카테고리 엔티티 구현
  - 정보: 카테고리명, 평균 예산비율
- [x] 평균 예산비율 업데이트 기능 구현 
  - [x] batch로 1일 1회 update하는 Batch 기능 구현.
  - [x] 소수점 이하는 버림.
- [x] 모든 카테고리 조회 API 구현.
- [x] 예산 설정 API 구현.
  - 월 별 예산을 설정한다.
  - 카테고리 필수.
  - [x] 예산 설정 요청 시 사용자가 특정 카테고리에 예산을 할당하지 않은 경우, 서버에서 예산액을 0원으로 하여 자동 생성한다.
- [x] 예산 추천 API 구현.
  - 사용자가 총액을 입력하면, 카테고리 별 추천 예산 액수를 반환.
  - 자동 생성할 예산 비율: 기존 유저들이 설정한 비율의 평균 비율.
  - 10% 이하의 카테고리들은 모두 묶어 `기타`로 제공.
      - (ex) 8% 문화, 7% 레져 -> 15% 기타

## 지출 기록

- [x] 지출 엔티티 구현.
  - 필수 정보: `지출일시`, `지출액`, `카테고리`, `메모`, `합계제외`
- [x] 지출 생성 API 구현
- [x] 지출 수정 API 구현
- [x] 지출 읽기(상세) API 구현
- [x] 지출 읽기(목록) API 구현
  - 필터링: `지출일시`, `지출액`, `카테고리`
  - 조회된 모든 지출의 `지출 합계`, `카테고리 별 지출 합계`를 같이 반환.
- [x] 지출 삭제 API 구현

## 오늘의 지출

- [ ] 오늘의 지출 추천 API 구현
  - [ ] 월별 예산을 만족하기 위해 오늘 지출 가능한 금액을 `총액`과 `카테고리 별 금액`으로 반환.
    - (ex) 11월 9일 지출 가능 금액 총 30,000원, 식비 15,000 … 으로 페이지에 노출 예정.
  - [ ] 남은 기간 동안 잔여 예산을 균등 분할하여 추천한다.
    - 앞선 일자에서 하루에 지출 가능한 금액을 10만원 초과했다 하더라도,
    - 오늘 예산이 1만원 줄어드는 것이 아니라 남은 기간 동안 분배해서 부담한다. (10일 남았다면 1만원 씩 분할)
  - [ ] 예산 초과 시 관리자가 설정한 `최소 금액`을 반환한다.
    - `0원` 또는 `음수` 의 예산을 추천하지 않는다.
  - [ ] 사용자의 상황에 맞는 `멘트`를 제공한다.
    - 잘 아끼고 있을 때, 적당히 사용 중 일 때, 기준을 넘었을때, 예산을 초과하였을 때 등 유저의 상황에 맞는 메세지를 같이 노출한다.
    - (ex) “절약을 잘 실천하고 계세요! 오늘도 절약 도전!” 등
  - [ ] 추천 예산 액수는 백원 단위 반올림하여 반환한다.
    - (ex) 15333원 -> 15300원 반환.

- [ ] 오늘의 지출 안내 API 구현
  - [ ] 오늘 지출한 `총액`을 제공한다.
  - [ ] 오늘 지출한 `카테고리 별 금액`을 제공한다.
  - [ ] 월별 예산 기준 `카테고리 별` 통계 제공
    - [ ] `오늘 적정지출액` : 일자 기준 오늘 기준 사용했으면 적절했을 금액
    - [ ] `오늘 실제지출액` : 일자 기준 오늘 기준 사용한 금액
    - [ ] `위험도` : 카테고리 별 `실제지출액/적정지출액 * 100`
      - ex) 오늘 사용하면 적당한 금액 10,000원/ 사용한 금액 20,000원 이면 200%

## 지출 통계
- [ ] 지출 통계 API 구현
  - [ ] `지난 달` 대비 `총액` 증감률
    - 오늘이 10일차 라면, 지난달 10일차 까지의 데이터를 대상으로 비교
    - (ex) 지난달 대비 150% 지출
  - [ ] `지난 달` 대비 `카테고리 별` 증감률
    - 오늘이 10일차 라면, 지난달 10일차 까지의 데이터를 대상으로 비교
    - (ex) `식비` 지난달 대비 150% 지출
  - [ ] `지난 요일` 대비 소비율
    - 오늘이 `월요일` 이라면 지난 `월요일` 에 소비한 모든 기록 대비 소비율
    - (ex) `월요일` 평소 대비 80%
  - [ ] `다른 유저` 대비 소비율
    - 오늘 기준 다른 `유저` 가 예산 대비 사용한 평균 비율 대비 나의 소비율
    - 오늘 기준 다른 유저가 소비한 지출이 평균 50%(ex. 예산 100만원 중 50만원 소비중) 이고 나는 60% 이면 120%.
    - (ex) `다른 사용자` 대비 120%

<br>

# 요구사항
[요구사항 바로가기](docs/요구사항.md)
