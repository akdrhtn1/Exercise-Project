CREATE TABLE MEMBER(
    ID VARCHAR2(20) CONSTRAINT USER_PK PRIMARY KEY,
    PASSWORD VARCHAR2(20) NOT NULL,
    NAME VARCHAR2(20) NOT NULL,
    BIRTH DATE DEFAULT SYSDATE,
    GENDER NUMBER NOT NULL,
    PHONE VARCHAR2(20) NOT NULL,
    EMAIL VARCHAR2(20) NOT NULL,
    ADDRESS VARCHAR2(20) NOT NULL,
    POINT VARCHAR2(20) NOT NULL,
    CARD_NUMBER VARCHAR2(20) NOT NULL,
    CARD_NUMBER NUMBER(20) DEFAULT 0
);

CREATE TABLE PRODUCT (
    PRODUCT_ID VARCHAR2(20) NOT NULL CONSTRAINT PRODUCT_PK PRIMARY KEY 
    , PRODUCT_NAME VARCHAR2(20) NOT NULL 
    , PRICE NUMBER NOT NULL 
    , PRODUCT_SIZE NUMBER NOT NULL 
    , STOCK NUMBER NOT NULL 
    , CATEGORY VARCHAR2(20) NOT NULL
    , PRODUCT_GENDER VARCHAR2(20) NOT NULL
);


CREATE TABLE PRODUCT_REVIEWS (
    ID NUMBER CONSTRAINT REVIEWS_PK PRIMARY KEY,
    PRO_ID VARCHAR2(20) CONSTRAINT PRODUCT_FK
    REFERENCES PRODUCT(PRODUCT_ID) ON DELETE CASCADE,
    CONTENT VARCHAR2(100),
    WRITER VARCHAR2(20) CONSTRAINT PRODUCT_FK2
    REFERENCES MEMBER(ID) ON DELETE CASCADE
    ,W_DATE DATE DEFAULT SYSDATE
);


CREATE TABLE MANAGER (
    ID VARCHAR2(20) NOT NULL CONSTRAINT MANAGER_PK PRIMARY KEY 
    , PASSWORD VARCHAR2(20) NOT NULL 
    , NAME VARCHAR2(20) NOT NULL 
    , PHONE VARCHAR(20) NOT NULL
    , MONEY NUMBER DEFAULT 0
);

-- 카드 테이블 추가
CREATE TABLE CARD(
    ID VARCHAR2(20) CONSTRAINT CARD_PK PRIMARY KEY,
    MEMBER_ID VARCHAR2(20) CONSTRAINT CARD_FK
    REFERENCES MEMBER(ID) ON DELETE CASCADE,
    BALANCE VARCHAR2(20) NOT NULL
);

-- 장바구니 테이블 추가 
CREATE TABLE CART(
    SB_ID NUMBER CONSTRAINT SB_PK PRIMARY KEY,
    MEMBER_ID VARCHAR2(20) CONSTRAINT CART_FK
    REFERENCES MEMBER(ID) ON DELETE CASCADE,
    PRODUCT_ID VARCHAR2(20) CONSTRAINT CART_FK2
    REFERENCES PRODUCT(PRODUCT_ID) ON DELETE CASCADE,
    PRODUCT_NAME VARCHAR2(100)
);


CREATE TABLE BUY (
    ID NUMBER CONSTRAINT BUY_PK PRIMARY KEY
    , PRODUCT_ID VARCHAR2(20) CONSTRAINT BUY_FK REFERENCES PRODUCT(PRODUCT_ID) ON DELETE CASCADE
    , MEMBER_ID VARCHAR2(20) CONSTRAINT BUY_FK2 REFERENCES MEMBER(ID) ON DELETE CASCADE
    , B_DATE DATE DEFAULT SYSDATE,
    COUNT NUMBER NOT NULL 
);



---------시퀀스
--1) 구매
CREATE SEQUENCE  "SHOPPING"."SEQ_BUY"  
MINVALUE 1 MAXVALUE 9999999999999999999999999999 
INCREMENT BY 1 START WITH 9 NOCACHE  NOORDER  NOCYCLE ;

--2) 장바구니
 CREATE SEQUENCE  "SHOPPING"."SEQ_CART"  
 MINVALUE 1 MAXVALUE 9999999999999999999999999999 
 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;

--3)리뷰
CREATE SEQUENCE  "SHOPPING"."SEQ_REVIEW"  
MINVALUE 1 MAXVALUE 9999999999999999999999999999 
INCREMENT BY 1 START WITH 90 NOCACHE  NOORDER  NOCYCLE ;