create table BUDGET (
    BUD_ID number(19,0) not null,
    BUD_MONTHLY_AMOUNT number(19,2) not null,
    BUD_NAME varchar2(255) not null,
    BUD_VERSION number(10,0),
    primary key (BUD_ID)
);


create sequence BUDGET_SEQ start with 10000;
    