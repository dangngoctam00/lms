INSERT INTO class (name, code, avatar, started_at, ended_at, status, type, course_id,
                   created_at, updated_at)
VALUES ('LH1', 'LH1', null, '2022-04-12', '2022-06-12', 'ONGOING',
        'OFFLINE',
        1, '2022-04-10 00:00:00', '2022-04-10 00:00:00');

INSERT INTO "class_teacher" (class_id, teacher_id, role)
VALUES (1, 2, 'TEACHER');
INSERT INTO "class_teacher" (class_id, teacher_id, role)
VALUES (1, 3, 'TEACHER_ASSISTANT');

INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 5);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 6);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 7);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 8);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 9);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 10);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 11);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 12);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 13);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 14);
INSERT INTO "class_student" (class_id, student_id)
VALUES (1, 15);


INSERT INTO grade_tag (title, scope, scope_id)
VALUES ('Điểm danh', 'CLASS', 1);

INSERT INTO class_learning_content (id)
VALUES (1);

INSERT INTO grade_tag (id, title, scope, scope_id)
VALUES (default, 'TAG 1', 'COURSE', 1);

INSERT INTO chapter_course (id, title, course_content_id, sort_index)
VALUES (default, 'Chương 1 Khóa 1', 1, 1);

INSERT INTO chapter_activity_course (activity_id, activity_type, chapter_id, sort_index)
VALUES (1, 'QUIZ', 1, 1);

INSERT INTO quiz_course (id, title, tag_id, exam_id, description, course_id)
VALUES (default, 'Bài kiểm tra ngữ pháp 1', 2, 2, 'Kiểm tra sau buổi học đầu tiên', 1);

INSERT INTO chapter_class (id, title, chapter_course_id, learning_content_id, sort_index)
VALUES (default, 'Chương 1 Khóa 1', 1, 1, 1);

INSERT INTO chapter_activity_class (activity_id, activity_type, chapter_id, sort_index)
VALUES (1, 'QUIZ', 1, 1);

INSERT INTO quiz_class (id, title, description, tag_id, exam_id, quiz_course_id, class_id, state, create_at, created_by,
                        updated_at, updated_by)
VALUES (default, 'Bài kiểm tra ngữ pháp 1', 'Kiểm tra sau buổi học đầu tiên', 2, 2, 1, 1, 'PUBLIC',
        '2022-04-30 18:59:21.195513', 'lmslms1', '2022-04-30 18:59:21.195513', 'lmslms1');

INSERT INTO quiz_config (id, started_at, valid_before, have_time_limit, time_limit,
                         max_attempt, view_previous_sessions, view_previous_sessions_time, pass_score,
                         create_at, created_by, updated_at, updated_by)
VALUES (1, '2022-07-30 00:00:00', '2022-10-07 00:00:00', false, null, 1, true, '2022-04-30 00:00:00',
        0, '2022-04-30 19:18:43.889984', 'lmslms1', '2022-04-30 19:18:43.889984', 'lmslms1');

