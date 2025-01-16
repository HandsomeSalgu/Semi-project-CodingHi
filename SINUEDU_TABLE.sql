----------테이블 삭제---------

DROP TABLE USERS CASCADE CONSTRAINT;	
DROP TABLE CATEGORY CASCADE CONSTRAINT;	
DROP TABLE QNA CASCADE CONSTRAINT;	
DROP TABLE REPLY CASCADE CONSTRAINT;	
DROP TABLE LECTURE CASCADE CONSTRAINT;	
DROP TABLE CHAPTER CASCADE CONSTRAINT;	
DROP TABLE VIEW_CHAPTER CASCADE CONSTRAINT;	
DROP TABLE BOOKMARK_LECTURE CASCADE CONSTRAINT;	
DROP TABLE IMAGE CASCADE CONSTRAINT;


----------시퀀스 삭제---------
DROP sequence USERS_SEQ;
DROP sequence CATEGORY_SEQ;
DROP sequence QNA_SEQ;
DROP sequence reply_SEQ;
DROP sequence LECTURE_SEQ;
DROP sequence chapter_SEQ;
DROP sequence IMAGE_SEQ;





----------유저 테이블---------
CREATE TABLE USERS (
    user_no       NUMBER,
    user_id       VARCHAR2(50)   NOT NULL UNIQUE,
    user_pw       VARCHAR2(100)  NOT NULL,
    user_name     VARCHAR2(50)   NOT NULL,
    user_nick     VARCHAR2(50)   NOT NULL,
    phone	        VARCHAR(13)     NOT NULL,
    brith_date    DATE               NOT NULL,
    join_date      DATE           DEFAULT SYSDATE NOT NULL,
    hint          VARCHAR2(200)  NOT NULL,
    hint_answer   VARCHAR2(200)  NOT NULL,
    admin         VARCHAR2(1)    DEFAULT 'N' CHECK (admin IN ('Y', 'N')),
    status        VARCHAR2(1)    DEFAULT 'N' CHECK (status IN ('Y', 'N'))
);

COMMENT ON COLUMN USERS.user_no       IS '사용자 번호';
COMMENT ON COLUMN USERS.user_id       IS '아이디';
COMMENT ON COLUMN USERS.user_pw       IS '비밀번호';
COMMENT ON COLUMN USERS.user_name     IS '이름';
COMMENT ON COLUMN USERS.user_nick     IS '닉네임';
COMMENT ON COLUMN USERS.joindate      IS '가입일자';
COMMENT ON COLUMN USERS.hint          IS '비밀번호 힌트';
COMMENT ON COLUMN USERS.hint_answer   IS '비밀번호 답변';
COMMENT ON COLUMN USERS.admin         IS '''Y''면 관리자, ''N''이면 일반사용자';
COMMENT ON COLUMN USERS.status        IS '''N''면 현재 사용, ''Y''면 비사용자';

ALTER TABLE USERS ADD CONSTRAINT PK_USER PRIMARY KEY (
	user_no
);

CREATE SEQUENCE USERS_SEQ;

INSERT INTO USERS VALUES(USERS_SEQ.NEXTVAL, 'admin', 'admin', '권태혁', 'salgu', '010-1111-2222' , '20200101' ,sysdate, '내가 태어난 지역의 이름은?', '대전', 'Y', 'Y');
INSERT INTO USERS VALUES(USERS_SEQ.NEXTVAL, 'wajangchang', 'wajangchang', '이창', '와장창', '010-2222-3333', '20200101' ,sysdate, '내가 태어난 지역의 이름은?', '평택', 'N', 'Y');
INSERT INTO USERS VALUES(USERS_SEQ.NEXTVAL, 'sedketchup', 'sedketchupg', '조민주', '슬픈캐찹', '010-3333-4444' , '20200101' ,sysdate, '내가 태어난 지역의 이름은?', '안양', 'N', 'Y');



----------카테고리 테이블---------

CREATE TABLE category (
	cg_no	number,
	cg_name	varchar2(50) NOT NULL
);

COMMENT ON COLUMN category.cg_no    is '카테고리 번호';
COMMENT ON COLUMN category.cg_name  is '카테고리 이름';

ALTER TABLE category ADD CONSTRAINT PK_CATEGORY PRIMARY KEY (
	cg_no
);

CREATE SEQUENCE category_SEQ;

INSERT INTO CATEGORY VALUES(category_SEQ.nextval, 'JAVA');
INSERT INTO CATEGORY VALUES(category_SEQ.nextval, 'ORACLE');
INSERT INTO CATEGORY VALUES(category_SEQ.nextval, 'JDBC');
INSERT INTO CATEGORY VALUES(category_SEQ.nextval, 'HTML');





----------QNA 테이블---------

CREATE TABLE QNA(
	qna_no	number,
	qna_title	varchar2(200)	NOT NULL,
	qna_detail	clob	NOT NULL,
    	writer	number	NOT NULL,
    	create_date	date	DEFAULT SYSDATE NOT NULL,
	update_date	date	DEFAULT SYSDATE NOT NULL,
	views	number	    DEFAULT 0       NOT NULL,
	cg_no	number	    NOT NULL,
	notice	varchar2(1)	DEFAULT 'N' CHECK (notice IN ('Y', 'N'))	
);


ALTER TABLE QNA ADD CONSTRAINT PK_QNA PRIMARY KEY (
	qna_no
);

ALTER TABLE QNA ADD CONSTRAINT FK_category_TO_QNA_1 FOREIGN KEY (
	cg_no
)
REFERENCES category (
	cg_no
);

COMMENT ON COLUMN QNA.qna_no       is '게시글 번호';
COMMENT ON COLUMN QNA.qna_title    is '제목';
COMMENT ON COLUMN QNA.qna_detail   is '내용';
COMMENT ON COLUMN QNA.writer       is '글쓴이';
COMMENT ON COLUMN QNA.create_date  is '생성날짜';
COMMENT ON COLUMN QNA.update_date  is '수정 날짜';
COMMENT ON COLUMN QNA.views        is '해당 게시물에 접속하면 조회수가 1씩 증가(글쓴이 제외)';
COMMENT ON COLUMN QNA.cg_no        is '카테고리 번호';
COMMENT ON COLUMN QNA.notice       is '"Y" 로 입력시 질문글이 아닌 공지글로 등록할 수 있는 관리자(강사) 전용 옵션';

CREATE SEQUENCE QNA_SEQ;

INSERT INTO QNA VALUES(QNA_SEQ.nextval, '이거 뭔지 모르겠어요', '이 함수가 뭔지 모르는데 봐주실 수 있을까요?', 3,sysdate, sysdate, default, 1, 'N' );
INSERT INTO QNA VALUES(QNA_SEQ.nextval, '진짜 뭔지 모르겠는데', '한 번만 봐줄 수 있을까요?', 3,sysdate, sysdate, default, 2, 'N' );
INSERT INTO QNA VALUES(QNA_SEQ.nextval, '난 오늘도 뭘 먹어야되는질 모르겠네요', '햄버거 먹을까', 2,sysdate, sysdate, default, 3, 'N' );
INSERT INTO QNA VALUES(QNA_SEQ.nextval, '너무 배고파요', '밥좀 먹고 싶네', 2,sysdate, sysdate, default, 4, 'N' );



----------댓글 테이블---------
CREATE TABLE REPLY (
	rep_no	number,
	rep_comment	varchar2(1500)	NOT NULL,
	user_no	number	NOT NULL,
	qna_no	number	NOT NULL
);

ALTER TABLE REPLY ADD CONSTRAINT PK_REPLY PRIMARY KEY (
	rep_no
);

ALTER TABLE REPLY ADD CONSTRAINT FK_USER_TO_REPLY_1 FOREIGN KEY (
	user_no
)
REFERENCES USERS (
	user_no
);

ALTER TABLE REPLY ADD CONSTRAINT FK_QNA_TO_REPLY_1 FOREIGN KEY (
	qna_no
)
REFERENCES QNA (
	qna_no
);

COMMENT ON COLUMN REPLY.rep_no          is '댓글 번호';
COMMENT ON COLUMN REPLY.rep_comment     is '댓글 내용';
COMMENT ON COLUMN REPLY.user_no         is '사용자 번호';
COMMENT ON COLUMN REPLY.qna_no          is '게시글 번호';

create sequence reply_SEQ;

insert into reply values(reply_seq.nextval, '진짜 뭐라는 건지 1도 모르겠네 ㅋㅋ', 1, 1);
insert into reply values(reply_seq.nextval, '저도 모르겠어요...', 3, 2 );
insert into reply values(reply_seq.nextval, '진짜 뭐라는 건지 아무거나 쳐먹어', 2, 3);

----------강의 테이블---------
CREATE TABLE LECTURE (
	lec_no	number,
	lec_title	varchar(50)	NOT NULL,
	lec_subtitle	varchar2(200)	NOT NULL,
	lec_desc	clob	NOT NULL,
	cg_no	number	NOT NULL,
	lec_img	varchar(50)	DEFAULT 'sample.png' NULL
);

ALTER TABLE LECTURE ADD CONSTRAINT PK_LECTURE PRIMARY KEY (
	lec_no
);

ALTER TABLE LECTURE ADD CONSTRAINT FK_category_TO_LECTURE_1 FOREIGN KEY (
	cg_no
)
REFERENCES category (
	cg_no
);

COMMENT ON COLUMN LECTURE.lec_no        is '강의 번호';
COMMENT ON COLUMN LECTURE.lec_title     is '강의명';
COMMENT ON COLUMN LECTURE.lec_subtitle  is '강의 부제목';
COMMENT ON COLUMN LECTURE.lec_desc      is '강의 설명';
COMMENT ON COLUMN LECTURE.cg_no         is '카테고리 번호';
COMMENT ON COLUMN LECTURE.lec_img       is '표지 이미지 없으면 샘플이미지로 대체';

CREATE SEQUENCE LECTURE_SEQ;

insert into lecture values(LECTURE_SEQ.nextval, '프로그래밍 기초', '객체 지향 프로그래밍', 'Java는 객체 지향 프로그래밍(OOP) 언어로, 전 세계적으로 널리 사용 되는 강력하고 안정적인 언어입니다. 특히 크로스 플랫폼 애플리케이션을 개발하는 데 탁월한 특징을 가지고 있습니다.',
                           1, default);
insert into lecture values(LECTURE_SEQ.nextval, '변수', '데이터를 저장하는 메모리 공간', '변수는 특정 타입의 값을 저장할 수 있는 메모리 공간입니다. 변수를 선언할 때, 변수의 자료형과 이름을 지정합니다. 변수의 값은 프로그램 실행 중에 바뀔 수 있습니다.',
                           1, default);
insert into lecture values(LECTURE_SEQ.nextval, '연산자', '변수나 값에 대해 특정 연산을 수행하는 기호','자바에서 연산자는 프로그램 내에서 데이터를 처리하고 조건을 평가하는 데 필수적인 역할을 합니다.',
                           1, default);
insert into lecture values(LECTURE_SEQ.nextval, '제어문', '프로그램의 흐름을 제어하는 문장', '프로그램의 흐름을 유동적으로 제어하는 핵심 요소로, 조건에 따라 다양한 실행 경로를 선택하고 반복문으로 반복적인 작업을 효율적으로 처리할 수 있게 도와줍니다.',
                           1, default);
insert into lecture values(LECTURE_SEQ.nextval, '배열', '데이터를 하나의 변수로 묶어서 저장하는 자료구조', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           1, default);

insert into lecture values(LECTURE_SEQ.nextval, '네트워크', '여러 대의 컴퓨터를 통신 회선으로 연결한 것', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           2, default);
insert into lecture values(LECTURE_SEQ.nextval, 'Oracle Database', '데이터를 하나의 변수로 묶어서 저장하는 자료구조', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           2, default);
insert into lecture values(LECTURE_SEQ.nextval, '개발환경구축', '데이터를 하나의 변수로 묶어서 저장하는 자료구조', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           2, default);
insert into lecture values(LECTURE_SEQ.nextval, 'DQL', '데이터를 하나의 변수로 묶어서 저장하는 자료구조', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           2, default);
insert into lecture values(LECTURE_SEQ.nextval, 'DML', '데이터를 하나의 변수로 묶어서 저장하는 자료구조', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           2, default);
insert into lecture values(LECTURE_SEQ.nextval, 'ORACLE', '데이터를 하나의 변수로 묶어서 저장하는 자료구조', '배열을 사용하면 여러 개의 값을 하나의 변수 이름으로 관리할 수 있으며, 각 값은 **인덱스(index)**를 통해 접근할 수 있습니다.',
                           2, default);



----------챕터 테이블---------
CREATE TABLE chapter (
	chpa_no	number,
	chap_title	varchar2(200)	            NOT NULL,
    chap_detail	clob	NOT NULL,
	create_date	date	DEFAULT sysdate     NOT NULL,
	update_date	date	DEFAULT sysdate     NOT NULL,
	views	    number	DEFAULT 0           NOT NULL,
	lec_no	    number	NOT NULL,
	chap_rate	number	NULL
);

ALTER TABLE chapter ADD CONSTRAINT PK_CHAPTER PRIMARY KEY (
	chpa_no
);

ALTER TABLE chapter ADD CONSTRAINT FK_LECTURE_TO_chapter_1 FOREIGN KEY (
	lec_no
)
REFERENCES LECTURE (
	lec_no
);

COMMENT ON COLUMN chapter.chpa_no      is '챕터 번호';
COMMENT ON COLUMN chapter.chap_title   is '챕터명';
COMMENT ON COLUMN chapter.chap_detail  is '챕터 내용';
COMMENT ON COLUMN chapter.create_date  is '생성 날짜';
COMMENT ON COLUMN chapter.update_date  is '수정 날짜';
COMMENT ON COLUMN chapter.views        is '해당 챕터에 접속하면 조회수가 1씩 증가(글쓴이 제외)';
COMMENT ON COLUMN chapter.lec_no       is '강의 번호';
COMMENT ON COLUMN chapter.chap_rate    is '평균평점';

create sequence chapter_SEQ;

insert into chapter values (chapter_seq.nextval, '객체 지향 프로그래밍이란?', '객체 지향 프로그랭이란...', sysdate, sysdate, default, 1, default);

----------챕터 조회 여부(진도율)---------
CREATE TABLE view_chapter (
	user_no	number	NOT NULL,
	chap_no	number	NOT NULL,
	rate	number	NULL
);

ALTER TABLE view_chapter ADD CONSTRAINT PK_VIEW_CHAPTER PRIMARY KEY (
	user_no,
	chap_no
);

ALTER TABLE view_chapter ADD CONSTRAINT FK_USER_TO_view_chapter_1 FOREIGN KEY (
	user_no
)
REFERENCES USERS (
	user_no
);

ALTER TABLE view_chapter ADD CONSTRAINT FK_chapter_TO_view_chapter_1 FOREIGN KEY (
	chap_no
)
REFERENCES chapter (
	chpa_no
);

COMMENT ON COLUMN view_chapter.user_no      is '사용자 번호';
COMMENT ON COLUMN view_chapter.chap_no   is '챕터 번호';
COMMENT ON COLUMN view_chapter.rate  is '평점';

insert into view_chapter values(3,1, 5);

-----------------------------------------------------------------------------------------------------
CREATE TABLE BOOKMARK_LECTURE (
	user_no	number	NOT NULL,
	lec_no	number	NOT NULL
);

ALTER TABLE BOOKMARK_LECTURE ADD CONSTRAINT PK_BOOKMARK_LECTURE PRIMARY KEY (
	user_no,
	lec_no
);

ALTER TABLE BOOKMARK_LECTURE ADD CONSTRAINT FK_USER_TO_BOOKMARK_LECTURE_1 FOREIGN KEY (
	user_no
)
REFERENCES USERS (
	user_no
);

ALTER TABLE BOOKMARK_LECTURE ADD CONSTRAINT FK_LECTURE_TO_BOOKMARK_LECTURE_1 FOREIGN KEY (
	lec_no
)
REFERENCES LECTURE (
	lec_no
);

COMMENT ON COLUMN BOOKMARK_LECTURE.user_no      is '사용자 번호';
COMMENT ON COLUMN BOOKMARK_LECTURE.lec_no      is '강의 번호';

insert into BOOKMARK_LECTURE values (3, 1);

-----------------------------------------------------------------------------------------------------
CREATE TABLE image (
	img_no	number	NOT NULL,
	img_path	varchar2(100)	NOT NULL,
	img_name	varchar2(50)	NOT NULL,			
	img_rename	varchar2(50)	NOT NULL,
	board_type	varchar2(50)	NOT NULL,
	board_id		number		NOT NULL
);

ALTER TABLE image ADD CONSTRAINT PK_IMAGE PRIMARY KEY (
	img_no
);

COMMENT ON COLUMN image.img_no      is '이미지 번호';
COMMENT ON COLUMN image.img_path      is '이미지가 저장될 경로';
COMMENT ON COLUMN image.img_name      is '이미지 이름';
COMMENT ON COLUMN image.img_rename      is '이름이 같은 경우 재설정을 위한 컬럼';
COMMENT ON COLUMN image.board_type      is '("qna", "chapter", "lecture")';
COMMENT ON COLUMN image.board_id      is '해당 글 또는 이미지 사용처의 주식별자 번호';

CREATE SEQUENCE image_SEQ;


-----------------------------------------------------------------------------------------------------

























