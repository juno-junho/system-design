## URL Shortener

### 요구사항
- 단축 url을 생성하는 기능 구현
- 단축 URL에는 숫자 0-9, 영문 대소문자만 사용 가능
- 단축된 URL을 시스템에서 삭제나 갱신은 하지 못한다

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
