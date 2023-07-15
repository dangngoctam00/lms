CREATE TABLE "tenant_customize"
(
    "tenant_id" varchar(50) primary key,
    "name"      varchar(50)  default 'BKLMS',
    "logo"      varchar(500) default 'https://cdn.haitrieu.com/wp-content/uploads/2021/09/Logo-DH-Bach-Khoa-HCMUT.png',
    "banner"    varchar(500) default 'https://i.pinimg.com/736x/56/86/03/568603cbd1860c67bf8f6776cbe7f885.jpg'
);

ALTER TABLE "tenant_customize"
    ADD FOREIGN KEY ("tenant_id") REFERENCES "tenant" ("tenant_id");