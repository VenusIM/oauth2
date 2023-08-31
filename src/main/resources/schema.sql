DROP TABLE IF EXISTS MEMBER;

CREATE TABLE MEMBER
(
    pk              bigint       NOT NULL AUTO_INCREMENT,
    userId          bigint       NOT NULL,
    userName        VARCHAR(255) NOT NULL,
    email           VARCHAR(255),
    providerType    VARCHAR(10),
    profileImageUrl VARCHAR(255),
    createAt        TIMESTAMP DEFAULT NOW(),

    PRIMARY KEY (pk)

)