-- 작업 데이터베이스 변경
USE market_db;

-- 테이블 정보 보기
DESC book;

-- 1. 모든 도서의 이름과 가격을 검색하시오
SELECT bookname, price
FROM book;

-- 2. 모든 도서의 도서번호,  도서이름, 출판사, 가격을 검색하시오
SELECT bookid, bookname, publisher, price
FROM book;

SELECT *
FROM book;

-- 3. 도서 테이블에 있는 모든 출판사를 검색하시오
SELECT DISTINCT publisher
FROM book;

-- 4. 가격이 20,000원 미만인 도서를 검색하시오
SELECT *
FROM book
WHERE price < 20000;

-- 5. 가격이 10,000원 이상 20,000 이하인 도서를 검색하시오
SELECT *
FROM book
WHERE price >= 10000 AND price <= 20000;

SELECT *
FROM book
WHERE price BETWEEN 10000 AND 20000;

-- 6. 출판사가 ‘굿스포츠’ 혹은 ‘대한미디어’인 도서를 검색하시오
SELECT *
FROM book
WHERE publisher = '굿스포츠' OR publisher = '대한미디어';

SELECT *
FROM book
WHERE publisher IN ('굿스포츠', '대한미디어');

-- 7. ‘축구의 역사’를 출간한 출판사를 검색하시오.
SELECT publisher
FROM book
WHERE bookname = '축구의 역사';

-- 8. 도서이름에 ‘축구’가 포함된 출판사를 검색하시오
SELECT publisher
FROM book
WHERE bookname LIKE '%축구%';

-- 9. 축구에 관한 도서 중 가격이 20,000원 이상인 도서를 검색하시오
SELECT *
FROM book
WHERE bookname LIKE '%축구%' AND price >= 20000;

-- 10. 도서를 이름순으로 검색하시오
SELECT *
FROM book
ORDER BY bookname ASC;

-- 11. 도서를 가격순으로 검색하고, 가격이 같으면 이름순으로 검색하시오.
SELECT *
FROM book
ORDER BY price ASC, bookname ASC;

-- 12. 도서를 가격의 내림차순으로 검색하시오. 만약 가격이 같다면 출판사의 오름차순으로 검색한다.
SELECT *
FROM book
ORDER BY price DESC, bookname ASC;



