## D208

  </br>
  </br>
  </br>

## 🙇🏻팀명 : 덜지니어스
  
  </br>
  </br>
  </br>
    
  
## 👥 프로젝트 한 줄 설명
- 어디서든 즐길 수 있는 두뇌 심리 멀티플레이 게임 서비스
  </br>
  </br>
  </br>
  
  
## 🧑🏻‍🦯 프로젝트 목적
  - 국내에서 꾸준히 인기를 얻고 있는 두뇌 전략 게임을 어플리케이션으로 친구들과 함께 즐기고 싶어!!
 
 
  </br>
  </br>
 
## 🛠 기술 스택
`공통` - OAuth 2.0

`Front` - React, TypeScript, React-Query, tailwind, npm, Recoil, vite

`Back` - Java, Springboot, Spring Security, Swagger, JUnit, Gradle, Python

`DB` - MySQL

`채팅` - Socket.io, Redis

`Deploy` - AWS EC2, RDS, S3, Nginx, Jenkins, Docker

`Cooperation` - Figma, Jira, GitLab, Mattermost, Notion 
 

  </br>
  </br>
 
## 🔌 아키텍처
<img src="./images/architecture.png" height="400">
맹맹마블의 서비스 아키텍쳐 구성도입니다.

네이버 소셜 로그인으로 로그인 기능을 구현하였습니다.

유저에게 게임 커뮤니티 기능을 제공하는 user service,
게임 로직을 처리하는 game server,
유저의 게임 플레이 데이터를 분석하여 플레이 스타일을 제공할 analysis server,
총 3개의 서버로 나누었고,
이 서버들의 앞단에 api gateway 서버를 두어 적절한 서비스에 라우팅 하게 하였습니다.

game server는 유저들에게 실시간성 보장과, 데이터 전달을 위해 stomp 프로토콜을 이용한 socket 통신으로 구현할 것입니다.

또한 유저에게 발급 된 token을 저장하는 session redis와,
실시간으로 변화하는 게임 데이터를 저장할 cache redis
총 두 개의 redis 서버를 두었습니다.

그리고 게임 플레이 데이터와, 유저별 게임 플레이 스타일을 분석한 정보는 RDBMS에 저장 됩니다.


  </br>
  </br>
  
## 🔌 맹맹마블 기능
#### 네이버 소셜 로그인 지원
 
 
#### 로비에서 대기방 입장 및 생성
 
 
#### 여러가지 캐릭터 구매
 
 
#### 채팅이 가능한 대기방
 
 
#### 랜덤으로 정해지는 순서
 
 
#### 신나는 게임 화면

  </br>
  </br>
  





## 👩‍👩‍👧‍👧 팀원
SSAFY 9기
| 김상근 | 김진영 | 서현덕 | 시민주 | 심은진 | 이일호 |
| ------ | ------ |----|-----|----|-----|
| <a href="https://github.com/ksg2388"><img src="https://avatars.githubusercontent.com/u/45422827?v=4" alt="ksg2388" width="100" height="100"></a> | <a href="https://github.com/jinyoungMango"><img src="https://avatars.githubusercontent.com/u/123930169?v=4" alt="jinyoungMango" width="100" height="100"></a> |<a href="https://github.com/hd9775"><img src="https://avatars.githubusercontent.com/u/12166357?v=4" alt="hd9775" width="100" height="100"></a>|<a href="https://github.com/tlalswn23"><img src="https://avatars.githubusercontent.com/u/35682216?v=4" alt="tlalswn23" width="100" height="100"></a>|<a href="https://github.com/unzinzanda"><img src="https://avatars.githubusercontent.com/u/93910197?v=4" alt="unzinzanda" width="100" height="100"></a>|<a href="https://github.com/LEEILHO"><img src="https://avatars.githubusercontent.com/u/33304873?v=4" alt="LEEILHO" width="100" height="100"></a>|
| FrontEnd | Android | BackEnd | BackEnd | FrontEnd | BackEnd |
