# 코딩 교육 사이트 SINUEDU - 권태혁
<img width="500" height="500" alt="우리의사랑스러운신우쌤" src="https://github.com/user-attachments/assets/a63666ca-ce36-41c0-b998-6ac25ee498e4" />


## 배포 :earth_asia:
- 배포 링크 : <link>http://sinuedu.online/</link>
- TEST ID : salgu
- TEST PWD : salgu
<br>

## 수행 기간 :calendar:
- 2024.12.08~2025.01.14
<br>

## 팀 구성원 :family:
- 팀장 : 권태혁
- 팀원 : 이상윤, 조민주, 이창, 문성종
<br>

## 개발 배경 :bulb:
- 코딩 교육 수요 증가로 인해 학습 자료의 강의 플랫폼의 필요성이 높아지고 있어 접근성과 효율성을 갖춘 교육 플랫폼의 필요성을 느꼈습니다.
- 초보자도 쉽게 접근할 수 있으며 사용자 친화적인 벤치마킹 사이트의 장점을 조합하여 독보적인 교육 사이트를 만들어보면 어떨까 생각했습니다.
- 그래서 코딩 학습이 필요한 사람들이 언제 어디서든 쉽게 사용할 수 있도록 국내 맞춤형 코딩 교육 사이트를 개발하였습니다.
<br>

## 프로젝트 주요 목표 :rocket:
1. 접근성과 효율성을 갖춘 온라인 코딩 교육 플랫폼
2. 사용자 친화적인 인터페이스
3. 회원간 질문을 통한 커뮤니티 협업 학습 환경 조성
4. 관리자의 원할할 회원 및 게시물 관리
<br>

## 사용기술 및 개발 환경 :computer:
- Backend: Java, Spring Boot, MyBatis
- Frontend: JavaScript, HTML/CSS, jQuery
- Database: MySQL, Oracle
- Infrastructure: AWS EC2, Linux(Ubuntu), Cloudflare(CDN)
- Tools & DevOps: GitHub
<br>

## 프로젝트 주요 기능 :heavy_exclamation_mark:
1. <b>회원 관리 기능</b>
    * 로그인/로그아웃, 회원가입, 아이디/비밀번호 찾기, 회원 정보 수정
<br/>

2. <b>강의 게시판 기능</b>
    * 카테고리 및 강의 목록, 강의 게시글 관리
<br/>

3. <b>질문 게시판 기능</b>
    * 질문 게시글 목록, 질문 작성/수정
<br/>

4. <b>관리자 기능</b>
    * 회원 관리, 강의 게시글 추가/수정
<br/>

## 프로젝트 내 담당 역할 및 구현 파트 :bangbang:
### 1. 강의 게시판

### 강의 목록 페이지 **(ID/PWD : salgu**) 로 접속 후 확인

**주소 : http://sinuedu.online/lecture/list**

<img width="1386" height="941" alt="image" src="https://github.com/user-attachments/assets/478b490c-26ea-4f74-9f2f-239f4bb8fa04" />


**주요 구현 화면**

- 강의 목록 페이지에는 좌측 사이드바와 우측 강의 목록 영역으로 구성되어 있습니다.
- 각 강의 카드는 강의 제목, 카테고리, 북마크 표시, 챕터 수, 진도율, 별점 등의 정보를 포함하고 있습니다.
- 사이드바의 ‘강의 목록’ 아래에는 BOOKMARK, JAVA, ORACLE, JDBC, HTML-CSS 등 카테고리별 구분 메뉴가 있습니다.

**주요 구현 기능** 

- 사이드바의 카테고리(BOOKMARK, JAVA, ORACLE, JDBC, HTML-CSS) 중 하나를 클릭하면,
자바스크립트를 통해 location.href = "/lecture/category/" + categoryName 형태로 HTTP 요청을 보냅니다. 이 요청은 DispatcherServlet을 거쳐 매핑된 Controller로 전달되고,
Controller에서는 @PathVariable로 categoryName 값을 받습니다.
- Controller에서는 categoryName이 "BOOKMARK"인지 여부를 if문으로 구분하여,
즐겨찾기 목록일 경우와 일반 카테고리 강의 목록일 경우 각각 다른 Mapper를 호출하도록 구현했습니다. 이후 조회된 데이터를 Model에 담아 ViewResolver를 통해 View로 전달하고,
선택된 카테고리에 맞는 강의 목록을 화면에 렌더링합니다.
- 각 강의 카드에는 북마크 아이콘이 배치되어 있습니다.
th:if 문을 사용해 북마크 여부에 따라 노란색(등록) 또는 흰색(미등록) 으로 표시되도록 했습니다.
북마크 아이콘 클릭 시 AJAX 비동기 통신을 이용해 DB의 북마크 상태를 UPDATE하고,
결과에 따라 ‘등록되었습니다’ 또는 ‘해제되었습니다’라는 alert 창을 띄운 후 페이지를 새로고침합니다.
- 강의 썸네일 이미지는 DB에 저장된 이미지 URL을 불러와 출력했습니다.
강의 테이블에는 이미지 주소 컬럼이 따로 없기 때문에,
이미지를 관리하는 별도의 테이블에 강의 번호(FK) 컬럼을 추가해 관계를 맺었습니다.
해당 강의 번호에 맞는 이미지가 존재하면 해당 URL을,
없을 경우에는 미리 설정해둔 기본 샘플 이미지를 표시하도록 구현했습니다.

---

### 강의 카드 목록 페이지

**주소 :  http://sinuedu.online/lecture/2**

<img width="1269" height="874" alt="image" src="https://github.com/user-attachments/assets/a76e267b-e205-4303-9d81-e8dde460be59" />


**주요 구현 화면**

- 강의 상세 페이지에는 강의 제목, 부제목, 간단한 설명, 챕터 수, 진행률, 평점이 표시됩니다.
- 하단에는 해당 강의의 챕터 목록이 순서대로 나열되어 있습니다.
- 각 강의와 챕터에는 수정 및 삭제 버튼이 배치되어 있습니다.

**주요 구현 기능** 

- 강의의 챕터 수는 DB에서 SELECT 문으로 조회한 데이터를 ArrayList<Lecture>에 담고,
list.size() 메서드를 이용해 총 챕터 개수를 계산했습니다.
- 진행률(Percent) 은 VIEW_CHAPTER라는 별도의 테이블을 만들어 관리했습니다.
사용자가 로그인(Session 저장)한 상태에서 챕터를 클릭하면,
VIEW_CHAPTER에 유저 번호(user_no) 와 챕터 번호(chapter_no) 가 함께 저장됩니다.
이후 SELECT COUNT(*) 문을 이용해 로그인한 사용자의 해당 강의 시청 수를 구하고,
총 챕터 수(capCount)로 나눈 뒤 ((userProgressRate / capCount) * 100) 계산식을 통해 진도율(%) 을 계산했습니다.
- 평점(Rating) 은 VIEW_CHAPTER 테이블에 사용자가 챕터별로 남긴 평점을 저장해두고,
각 챕터 테이블에 해당 평점의 평균을 업데이트하도록 구성했습니다.
강의 전체 평점은 각 챕터의 총 평점 합을 평점을 받은 챕터 수로 나누어 계산했습니다.
- 수정 / 삭제 기능은 Thymeleaf의 th:if 문을 이용해 관리자 계정만 버튼이 보이도록 설정했습니다.
수정 버튼을 클릭하면 수정 페이지로 이동하고,삭제 버튼을 누를 경우 데이터가 바로 삭제되지 않고,
해당 행의 status 컬럼 값을 'Y' → 'N' 으로 변경하는 UPDATE 문을 사용하여 
비활성화 처리했습니다.

---

### 강의 수정 페이지 **(ID/PWD : salgu**) 로 접속 후 확인

 **주소 : http://sinuedu.online/manager/lectureUpdate/1**

<img width="1174" height="819" alt="image" src="https://github.com/user-attachments/assets/499f3e50-b13e-4752-b41f-f164295f59ba" />


**주요 구현 화면**

- 카테고리 선택, 썸네일 선택, 제목, 부제목, 강의 설명을 입력할 수 있는 입력창과 선택창이 있습니다.
- 하단에는 수정 및 취소 버튼이 있습니다.

**주요 구현 기능** 

- 카테고리 선택 칸은 select / option 문으로 구성되어 있습니다.
Controller에서 모든 카테고리 데이터를 List 형태로 조회해 ViewResolver를 통해 View로 전달하고,
View 단에서는 Thymeleaf의 th:each 문을 사용하여 list 안의 카테고리 정보를 반복 출력합니다.
따라서 DB의 Category 테이블 행이 늘어나면 View의 선택지도 자동으로 늘어납니다.
- 썸네일 선택 기능은 초기에는 로컬에 이미지를 저장하는 방식으로 구현했지만,
이후 유지보수 과정에서 Cloudflare R2 Object Storage 버킷을 사용하여
이미지 파일을 관리하도록 변경했습니다.
form 태그 안에 input type="file"을 사용해 업로드를 받고,
DispatcherServlet이 요청을 수신한 뒤 HandlerMapping을 통해 Controller의 해당 메서드를 찾아
@RequestParam으로 파일을 전달받습니다.
- 썸네일 처리 로직은 다음과 같습니다.
    - 파일이 비어 있을 경우 (file.isEmpty() == true)
        - 기본 썸네일로 대체합니다.
        - 기존 썸네일이 존재하면 삭제 후 기본 이미지로 교체하고,
        존재하지 않으면 그대로 유지합니다.
    - 파일이 존재할 경우 (file.isEmpty() == false)
        - 새로 업로드된 썸네일로 대체합니다.
        - 기존 썸네일이 있으면 UPDATE 문으로 수정하고,
        없을 경우 INSERT 문으로 새로운 정보를 추가합니다.
    
    썸네일 존재 여부는 Mapper를 통해 SELECT COUNT(*) 쿼리를 사용해 확인하며,
    결과값이 1이면 존재, 0이면 미존재로 구분합니다.
    
- Cloudflare R2 접근 설정을 위해
build.gradle에 해당 버킷의 의존성(dependency) 을 추가하고,
application.properties에 버킷 이름, 퍼블릭 주소, accessKey, secretKey 등을 등록하여
애플리케이션이 R2 버킷에 접근할 수 있도록 설정했습니다.
- 이후 R2Service 클래스를 생성해 @Service 애너테이션을 부여하여
Controller와 Repository 사이에서 실제 로직을 담당하도록 했습니다.
    - uploadFile() 메서드는 업로드된 파일의 퍼블릭 URL을 반환
    - deleteFile() 메서드는 논리값(boolean) 을 반환하여
    성공 시 성공 메시지, 실패 시 실패 메시지를 출력하도록 구성했습니다.

---

## 프로젝트 참여 소감 :smile:
이번 SINUEDU 프로젝트에서 조장이자 PL로서 다양한 경험을 할 수 있었습니다. 이 과정을 통해 팀원들과의 소통이 얼마나 중요한지를 깊이 깨달았습니다. 단순히 제가 맡은 기능에만 집중하기보다 팀원들과 함께 프로젝트를 만들어가며 어려움이 있을 때마다 서로 소통하고 부족한 부분을 채워 나갔습니다. <br><br> 첫 프로젝트로서 제가 담당한 기능을 직접 코딩하고 프로젝트에 적용해 보면서 많은 것을 배웠습니다. 예상치 못한 버그가 자주 발생하여 여러 차례 테스트를 거치면서 문제를 해결했고, 이를 통해 실력이 한층 더 성장한 것 같습니다.<br>
<br>특히 제가 맡은 부분은 강의 목록 리스트, 질문게시판 페이징, 게시물 상세 기능 구현이었습니다. 이 과정에서 Ajax를 활용해 비동기 방식으로 데이터를 가져오는 기능을 적용하였고, 강의 목록별 카테고리 선택 시 해당 강의의 챕터 수, 로그인한 계정의 진도율, 강의 총 평점까지 함께 불러오는 기능을 개발했습니다. <br><br>하지만 이때 DB 정규화에는 신경을 썼지만, 비정규화를 고려하지 못해 수많은 JOIN이 발생했고, 그 결과 로딩 속도 지연이라는 아쉬움이 있었습니다. <br> 이를 통해 단순히 기능 구현에 그치지 않고 데이터베이스 설계 단계에서 성능까지 고려해야 한다는 점을 배우게 되었으며, 다음 프로젝트에서는 이러한 부분을 보완하고 싶습니다.
<br>

## 요구사항 정의서
<link>https://docs.google.com/spreadsheets/d/10CiUG18fGrM2qKxKslMeXjslcGnF8abdkhVaAl-C9Gc/edit?gid=0#gid=0</link>
