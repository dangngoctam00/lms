insert into grade_tag(title, scope, scope_id, is_primitive, has_graded, updated_at, graded_at, is_public)
values ( 'Kiểm tra 15p lần 1', 'CLASS', '1', true, true, now(), now(), true),
       ( 'Kiểm tra 15p lần 2', 'CLASS', '1', true, true, now(), now(), true),
       ( 'Kiểm tra 15p lần 3', 'CLASS', '1', true, true, now(), now(), true),
       ( 'Kiểm tra 1 tiết lần 1', 'CLASS', '1', true, true, now(), now(), true);

insert into grade_tag_student(tag_id, student_id, grade)
VALUES (3, 5, 7),
       (3, 6, 4),
       (3, 7, 6.25),
       (3, 8, 7.8),
       (3, 9, 9.2),
       (4, 5, 8),
       (4, 6, 10),
       (4, 7, 5.25),
       (4, 8, 8.8),
       (4, 9, 1.2),
       (5, 5, 10),
       (5, 6, 3),
       (5, 7, 4.25),
       (5, 8, 8.8),
       (5, 9, 2.2),
       (6, 5, 3),
       (6, 6, 8),
       (6, 7, 0.15),
       (6, 8, 4.8),
       (6, 9, 0.2);