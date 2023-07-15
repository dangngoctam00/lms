INSERT INTO "tenant" (tenant_id, schema, username, password, firstname, lastname, email, phone, domain)
VALUES ('tenant1', 'tenant1', 'tenant1', '$2a$10$dTw4if3uFlj4ewuDLm6Puyy4OOE.Y/yZqJajdBLHg1T69PHGKAWa', 'Quân',
        'Trần Ngọc Nhật', 'abcxyz@gmail.com', '123450', 'tenant1');

INSERT INTO "tenant_customize" (tenant_id) values ('tenant1');