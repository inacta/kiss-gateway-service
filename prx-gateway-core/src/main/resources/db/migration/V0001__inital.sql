
    drop table if exists TAG cascade;

    drop table if exists VOUCHERS cascade;

    create table TAG (
       ID varchar(255) not null,
        CREATED timestamp not null,
        LAST_UPDATE timestamp not null,
        COUNTER int4,
        SECRET_KEY varchar(255),
        CHIP_UUID varchar(255),
        primary key (ID)
    );

    create table VOUCHERS (
       ID varchar(255) not null,
        CREATED timestamp not null,
        LAST_UPDATE timestamp not null,
        ACTIVE boolean,
        EMAIL varchar(255),
        EMAIL_SENT boolean,
        ORDER_NUMBER int4,
        PRICE varchar(255),
        VOUCHER varchar(255) UNIQUE,
        primary key (ID)
    );
create index IDX_CHIP_UUID on TAG (CHIP_UUID);
create index IDX_VOUCHER on VOUCHERS (VOUCHER);
