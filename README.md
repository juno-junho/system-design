## URL Shortener

### 요구사항
- 단축 url을 생성하는 기능 구현
- 단축 URL에는 숫자 0-9, 영문 대소문자만 사용 가능
- 단축된 URL을 시스템에서 삭제나 갱신은 하지 못한다

#### 내가 추가한 요구사항
- 조회수를 기록하고 조회수를 기준으로 인기 순으로 정렬하여 보여준다
- 조회수가 하루에 1000회 이상이면 인기 URL로 표기하고, 캐싱해 결과를 보관한다
  - 캐싱 시간은 하루 단위로 설정한다

### 개략적 추정
- 쓰기 연산 : 매일 1억개의 단축 URL 생성 -> 초당 1160개의 쓰기 연산
- 읽기 연산 : 읽기 연산과 쓰기 연산 비율 10 : 1 -> 초당 11600개의 읽기 연산
- 단축 서비스 10년간 운영한다 가정하면 3650억개 레코드 보관해야 한다
- 축약 전 URL 평균 길이는 100
- 10년동안 필요한 저장 용량은 3650억 * 100Byte = 36.5TB

### endpoint
- POST /data/shorten
  - Request
    - URL : 단축할 URL
  - Response
    - URL : 단축된 URL
- GET /shortenUrl
  - Response
    - URL : 원본 URL


### UI 
- long url 입력 -> shorten url로 변환
- shorten url get 요청 -> 유효한 url ? 원본 url로 redirect : error message

### 문제점
- 현시점 Base-62 변환하는 과정에 있어서 auto-increment 되는 Id값을 사용.
- 3650억개의 레코드를 커버링하지만, 문제점은 1씩 증가하기에 다음 쓸 수 있는 단축 URL이 무엇인지 쉽게 알아 낼 수 있어 보안상의 문제 발생

### 해결
- 보안상의 문제점을 해결하기위해서 해결할 수 있는 방법은 다음과 같다.
1. id값을 uuid로 생성 -> base 62로 인코딩하기 힘들다. 숫자로만 구성되었으면 좋겠다. & 너무 길다
2. random number 생성 -> 중복이 발생할 수 있다. hash를 사용하는 방식의 문제점을 해결하지 못한다.
3. snowflake 생성기법 사용 -> 추후 scale out시 분산 환경에서 사용하기에 적합하다. 또한 timestamp 기반으로 정렬도 가능하다
=> snowflake 생성기법을 사용하여 id값을 생성하고 base 62로 인코딩하여 shorten URL을 생성하자!


snowflake id 생성 기법 적용 코드 출처 : https://ramka-devstory.tistory.com/2

