
    create table ACCOUNT (
        ACT_ID number(19,0) not null,
        ACT_NAME varchar2(255),
        ACT_VERSION number(10,0),
        ACT_PARENT_FK number(19,0),
        primary key (ACT_ID)
    );

    create table BUDGET (
        BUD_ID number(19,0) not null,
        BUD_MONTHLY_AMOUNT number(19,2) not null,
        BUD_NAME varchar2(255 char) not null,
        BUD_VERSION number(10,0),
        primary key (BUD_ID)
    );
    
    create table TRANSACTION (
        TRX_ID number(19,0) not null,
        TRX_AMOUNT number(19,2),
        TRX_BOOKINGTEXT varchar2(1000),
        TRX_RECIEVER varchar2(1000),
        TRX_SENDER varchar2(1000),
        TRX_VALUTA date,
        TRX_VERSION number(10,0),
        TRX_ACT_ID number(19,0),
        primary key (TRX_ID)
    );

    alter table ACCOUNT 
        add constraint act_parent_act_fk
        foreign key (ACT_PARENT_FK) 
        references ACCOUNT;
        

    alter table TRANSACTION 
        add constraint trx_act_fk
        foreign key (TRX_ACT_ID) 
        references ACCOUNT;

        
  create index act_parent_fk_i on account (act_parent_fk)
  
        
  create index trx_act_fk_i on transaction (TRX_ACT_ID)
  


    create sequence ACCOUNT_SEQ start with 10000;

    create sequence 	TRANSACTION_SEQ start with 10000;
    

    create sequence BUDGET_SEQ start with 10000;    
