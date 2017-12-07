-- create Business Table
CREATE TABLE Business (
    BID VARCHAR (50),
    City VARCHAR (30),
    State VARCHAR (5),
    BName VARCHAR (100),
    Stars NUMBER,
    Address VARCHAR (150),
    Review_cnt VARCHAR(10)
);

-- create Business MainCategory Table
CREATE TABLE MainCategory (
    BID VARCHAR (50),
    MainCG VARCHAR (50)
);

-- create Business SubCategory Table
CREATE TABLE SubCategory (
    BID VARCHAR (50),
    SubCG VARCHAR (50)
);

-- create Attribute Table
CREATE TABLE Attrs (
    BID VARCHAR (50),
    Attr VARCHAR (50)
);

-- create Users Table
CREATE TABLE Users (
    UserId VARCHAR (50),
    Uname VARCHAR (50)
);

-- create Review Table
CREATE TABLE Review (
    UVOTES VARCHAR (50),
    UserId VARCHAR (50),
    RID VARCHAR (50),
    BID VARCHAR (50),
    Stars NUMBER,
    RDATE DATE,
    RText CLOB
);

-- create Checkin Table
CREATE TABLE Checkin (
    BID VARCHAR (50),
    Cin NUMBER
);

-- create Hours Table
CREATE TABLE Hours (
    BID VARCHAR (50),
    DAYS VARCHAR (10),
    FROM_TIME VARCHAR (20),
    TO_TIME VARCHAR (20)
);

CREATE INDEX BID ON BUSINESS(BID);
CREATE INDEX ATTR ON ATTRS(ATTR);
CREATE INDEX CIN ON CHECKIN(CIN);
CREATE INDEX DAYS ON HOURS(DAYS);
CREATE INDEX RID ON REVIEW(RID);
CREATE INDEX SUBCG ON SUBCATEGORY(SUBCG);
CREATE INDEX USERID ON USERS(USERID);