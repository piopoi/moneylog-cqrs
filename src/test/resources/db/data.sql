set FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE member;
TRUNCATE TABLE category;
set FOREIGN_KEY_CHECKS = 1;


INSERT INTO category (name)
VALUES ("식비"),
       ("교통비"),
       ("주거비"),
       ("쇼핑"),
       ("투자 및 저축"),
       ("고정비"),
       ("기타")
;
