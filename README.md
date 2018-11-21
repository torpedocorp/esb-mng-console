# esb-mng-console

esb-mng-console은 camel 기반의 ESB 어플리케이션 운영을 위한 관리콘솔 웹 어플리케이션입니다.
<!--more-->

esb-mng-console을 이용하여 camel기반 ESB 어플리케이션과 bizframe-mas를 효과적으로 운영할 수 있습니다.

## feature

 - bizframe-mas 어플리케이션 컨테이너 상태 모니터링
 - bizframe-mas Application 시작/중지
 - bizframe-mas config 조회
 - camel 어플리케이션 context, route 트리 조회
 - camel 어플리케이션 라우트 상태 조회 및 제어
 - camel exchange 정보 수집
 - camel exchange 별 Trace 정보 모니터링
 - (원격)어플리케이션 상태 및 전송 현황 실시간 조회
 - (원격)어플리케이션 실시간 통합 토폴로지 제공
 - TODAY 전송 현황 실시간 조회
 - hawtio JMX 웹 클라이언트를 사용하여 상세 모니터링
 - camel exchange 통계 정보 제공
  
## Building from the source

 1. jdk 8 이상 설치 
 2. apache ant 설치
 3. esb-mng-console 프로젝트 저장소에서 소스를 clone 혹은 다운로드 (https://github.com/torpedocorp/esb-mng-console)
 4. 커맨드 라인상에서 ant build 수행

## Architecture
  - spring 4.0
  - hibernate 5.0
  - derby
