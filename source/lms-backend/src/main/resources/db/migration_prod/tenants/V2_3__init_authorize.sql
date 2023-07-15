INSERT INTO role (id, description, title)
VALUES (1, 'Đây là admin', 'Admin');
INSERT INTO role (id, description, title)
VALUES (2, 'Đây là employee', 'Nhân viên');
INSERT INTO role (id, description, title)
VALUES (3, 'Học sinh', 'Học sinh');
INSERT INTO role (id, description, title)
VALUES (4, 'Giáo viên', 'Giáo viên');

---------------------------------------------- ROLE-ID 1 : ADMIN

-- ID 1: VIEW ALL COURSES
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 1, 0, -1, 0, 0, 0, 0);

-- ID 2: CREATE COURSE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 2, 0, 0, 0, 0, 0, 0);

-- ID 3: VIEW COURSE DETAIL
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 3, 0, -1, 0, 0, 0, -1);

-- ID 4: UPDATE COURSE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 4, 0, -1, 0, 0, 0, -1);

-- ID 5: DELETE COURSE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 5, 0, -1, 0, 0, 0, -1);


-- ID 6: VIEW LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 6, 0, -1, 0, 0, 0, -1);

-- ID 7: CREATE LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 7, 0, -1, 0, 0, 0, 0);

-- ID 8: UPDATE LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 8, 0, -1, 0, 0, 0, -1);

-- ID 9: DELETE LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 9, 0, -1, 0, 0, 0, -1);


-- TODO: check duplicate
-- INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
--                                  is_limit_by_manager, is_limit_by_learn)
-- VALUES (1, 1, 1, 0, -1, 0, 1);
--
-- INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
--                                  is_limit_by_manager, is_limit_by_learn)
-- VALUES (1, 2, 1, 1, -1, 0, -1);

-- ID 40: VIEW LIST TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 40, 0, -1, 0, 0, 0, -1);

-- ID 41: VIEW TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 41, 0, -1, 0, 0, 0, -1);

-- ID 42: CREATE TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 42, 0, -1, 0, 0, 0, 0);

-- ID 43: UPDATE TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 43, 0, -1, 0, 0, 0, -1);

-- ID 44: DELETE TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 44, 0, -1, 0, 0, 0, 1);

-- ID 50: VIEW LIST CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (1, 50, -1, -1, -1, -1, -1);

-- ID 51: VIEW CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (1, 51, -1, -1, -1, -1, -1);

-- ID 60: VIEW LIST QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 60, 0, -1, 0, 0, -1, 0);

-- ID 61: VIEW QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 61, 0, -1, 0, 0, 1, -1);

-- ID 62: CREATE QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 62, 0, -1, 0, 0, 0, 0);

-- ID 63: UPDATE QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 63, 0, -1, 0, 0, 0, -1);

-- ID 61: DELETE QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 65, 0, -1, 0, 0, 0, -1);

-- ID 66: VIEW QUIZ RESULT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 66, 0, -1, 0, 0, 0, -1);

-- ID 68: VIEW QUIZ RESULT MANAGEMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 68, 0, 1, 0, 0, 0, -1);

-- ID 100: VIEW CLASS SCHEDULER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 100, 0, -1, 0, 0, 0, 0);

-- ID 101: UPDATE CLASS SCHEDULER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 101, 0, -1, 0, 0, 0, 0);

-- ID 102: ADD CLASS SESSION
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 102, 0, -1, 0, 0, 0, -1);

-- ID 103: UPDATE CLASS SESSION
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 103, 0, -1, 0, 0, 0, -1);

-- ID 104: DELETE CLASS SESSION
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 104, 0, -1, 0, 0, 0, -1);

-- ID 121: CREATE ANNOUNCEMENT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 121, 0, -1, 0, 0, 0, 0);

-- ID 123: VIEW LIST ANNOUNCEMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 123, 0, -1, 0, 0, 0, 1);


-- ID 130: VIEW LIST STUDENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 130, 0, -1, 0, 0, 0, 0);

-- ID 131: ADD STUDENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 131, 0, -1, 0, 0, 0, 0);

-- ID 140: ADD TEACHER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 140, 0, -1, 0, 0, 0, 0);

-- ID 141: ADD TEACHER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (1, 141, 0, -1, 0, 0, 0, 0);



INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (2, 1, 1, 1, -1, 0, 1);
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (2, 2, 1, -1, -1, 0, -1);

-- ID 142: UPDATE TEACHER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (2, 142, 0, -1, 0, 0, 0, 0);

-- ID 142: REMOVE TEACHER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (2, 143, 0, -1, 0, 0, 0, 0);

-- ID 142: VIEW_CLASS_ATTENDANCE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (2, 220, 0, -1, 0, 0, 0, 0);


-- ID 142: VIEW_SESSION_ATTENDANCE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (2, 221, 0, -1, 0, 0, 0, 0);


-- ID 142: UPDATE_SESSION_ATTENDANCE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (2, 222, 0, -1, 0, 0, 0, 0);

---------------------------------------------- ROLE-ID 3 : STUDENT

-- ID 6: VIEW LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 6, 0, -1, 0, 0, 0, -1);

-- ID 50: VIEW LIST CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (3, 50, 1, 1, 1, 1, 1);

-- ID 51: VIEW CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (3, 51, 1, 1, 1, 1, 1);

-- ID 60: VIEW LIST QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 60, 0, -1, 0, 0, 1, 0);

-- ID 61: VIEW QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 61, 0, -1, 0, 0, 1, -1);

-- ID 65: DO QUIZ
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 65, 0, 0, 0, 0, -1, 0);

-- ID 66: VIEW QUIZ RESULT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 66, 0, -1, 0, 0, 1, 1);

-- ID 70: VIEW LIST POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 70, 0, -1, 0, 0, 1, 0);

-- ID 71: VIEW POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 71, 0, -1, 0, 0, 1, 0);

-- ID 72: CREATE POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 72, 0, -1, 0, 0, 1, 0);

-- ID 73: UPDATE POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 73, 0, 0, 0, 0, 0, 0);

-- ID 74: DELETE POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 74, 0, -1, 0, 0, 1, 1);

-- ID 75: VIEW LIST COMMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 75, 0, -1, 0, 0, 1, 0);

-- ID 76: COMMENT POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 76, 0, -1, 0, 0, 1, 0);

-- ID 77: INTERACT POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 77, 0, -1, 0, 0, 1, 0);

-- ID 78: UPDATE COMMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 78, 0, 0, 0, 0, 0, 0);

-- ID 79: DELETE COMMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 79, 0, -1, 0, 0, 1, 1);

-- ID 80: VIEW LIST UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 80, 0, 0, 0, 0, 1, 0);

-- ID 81: VIEW UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 81, 0, 0, 0, 0, 1, 0);

-- ID 90: VIEW LIST VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 90, 0, 0, 0, 0, 1, -1);

-- ID 91: VIEW VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 91, 0, 1, 0, 0, 1, -1);

-- ID 100: VIEW CLASS SCHEDULER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 100, 0, 0, 0, 0, 1, 0);

-- ID 123: VIEW ANNOUNCEMENT (ONLY APPLIED TO CLASS)
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 123, 0, 1, 0, 0, 1, 0);

-- ID 130: VIEW STUDENT LIST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 130, 0, -1, 0, 0, 1, 0);

-- ID 140: VIEW TEACHER LIST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 140, 0, -1, 0, 0, 1, 0);

INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (3, 163, 0, 0, 0, 0, 1, 0);


---------------------------------------------- ROLE-ID 4 : TEACHER

-- ID 1: VIEW ALL COURSES
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 1, 0, 1, 0, 0, 0, 0);

-- ID 3: VIEW COURSE DETAIL
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 3, 0, 1, 0, 0, 0, -1);

-- ID 6: VIEW LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 6, 0, 1, 0, 0, 0, -1);

-- ID 7: CREATE LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 7, 0, 1, 0, 0, 0, 0);

-- ID 8: UPDATE LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 8, 0, 1, 0, 0, 0, -1);

-- ID 9: DELETE LEARNING CONTENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 9, 0, 1, 0, 0, 0, 1);

-- ID 40: VIEW LIST TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 40, 0, 1, 0, 0, 0, -1);

-- ID 41: VIEW TEST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 41, 0, 1, 0, 0, 0, -1);

-- ID 50: VIEW LIST CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (4, 50, 1, 1, 1, 1, 1);

-- ID 51: VIEW CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (4, 51, 1, 1, 1, 1, 1);

-- ID 53: CREATE CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (4, 52, 1, 0, 0, 0, 0);

-- ID 53: UPDATE CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn)
VALUES (4, 53, 0, 1, 0, 0, 0);

-- ID 60: VIEW LIST QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 60, 0, 1, 0, 0, 1, 0);

-- ID 61: VIEW QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 61, 0, 1, 0, 0, 1, -1);

-- ID 62: CREATE QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 62, 0, 1, 0, 0, 0, 0);

-- ID 63: UPDATE QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 63, 0, 1, 0, 0, 0, 1);

-- ID 64: DELETE QUIZ CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 65, 0, 1, 0, 0, 0, 1);

-- ID 66: VIEW QUIZ RESULT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 66, 0, 1, 0, 0, 0, -1);

-- ID 66: GRADE QUIZ
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 67, 0, 1, 0, 0, 0, -1);

-- ID 68: VIEW QUIZ RESULT MANAGEMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 68, 0, 1, 0, 0, 0, -1);

-- ID 70: VIEW LIST POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 70, 0, 1, 0, 0, -1, 0);

-- ID 71: VIEW POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 71, 0, 1, 0, 0, -1, 0);

-- ID 72: CREATE POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 72, 0, 1, 0, 0, -1, 0);

-- ID 73: UPDATE POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 73, 0, 0, 0, 0, 0, 0);

-- ID 74: DELETE POST : Teacher can delete all post in class taught by him
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 74, 0, 1, 0, 0, -1, -1);

-- ID 75: VIEW LIST COMMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 75, 0, 1, 0, 0, -1, 0);

-- ID 76: COMMENT POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 76, 0, 1, 0, 0, -1, 0);

-- ID 77: INTERACT POST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 77, 0, 1, 0, 0, -1, 0);

-- ID 78: UPDATE COMMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 78, 0, 0, 0, 0, 0, 0);

-- ID 79: DELETE COMMENT
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 79, 0, 1, 0, 0, -1, -1);

-- ID 80: VIEW LIST UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 80, 0, 1, 0, 0, 1, 0);

-- ID 81: VIEW UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 81, 0, 1, 0, 0, 1, -1);

-- ID 82: CREATE UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 82, 0, 1, 0, 0, 0, 0);

-- ID 83: UPDATE UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 83, 0, 1, 0, 0, 0, 1);

-- ID 84: DELETE UNIT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 84, 0, 1, 0, 0, 0, 1);

-- ID 90: VIEW LIST VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 90, 0, 1, 0, 0, 1, 0);

-- ID 91: VIEW VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 91, 0, 1, 0, 0, 1, -1);

-- ID 92: CREATE VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 92, 0, 1, 0, 0, 0, 0);

-- ID 93: UPDATE VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 93, 0, 1, 0, 0, 0, 1);

-- ID 94: DELETE VOTING
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 94, 0, 1, 0, 0, 0, 1);

-- ID 100: VIEW CLASS SCHEDULER
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 100, 0, 1, 0, 0, 0, 0);

-- ID 102: ADD CLASS SESSION
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 102, 0, 1, 0, 0, 0, 0);

-- ID 103: UPDATE CLASS SESSION
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 103, 0, 1, 0, 0, 0, 1);

-- ID 104: DELETE CLASS SESSION
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 104, 0, 1, 0, 0, 0, 1);

-- ID 121: CREATE ANNOUNCEMENT CLASS
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 121, 0, 1, 0, 0, 0, 0);

-- ID 123: VIEW ANNOUNCEMENT (ONLY APPLIED TO CLASS)
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 123, 0, 1, 0, 0, 1, 0);


-- ID 130: VIEW STUDENT LIST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 130, 0, 1, 0, 0, -1, 0);

-- ID 140: VIEW TEACHER LIST
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 140, 0, 1, 0, 0, -1, 0);

-- ID 220: VIEW_CLASS_ATTENDANCE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 220, 0, -1, 0, 0, 0, 0);


-- ID 221: VIEW_SESSION_ATTENDANCE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 221, 0, -1, 0, 0, 0, 0);


-- ID 222: UPDATE_SESSION_ATTENDANCE
INSERT INTO role_has_permission (role_id, permission_id, is_limit_by_branch, is_limit_by_teaching, is_limit_by_dean,
                                 is_limit_by_manager, is_limit_by_learn, is_limit_by_owner)
VALUES (4, 222, 0, -1, 0, 0, 0, 0);

-- -------------------------------------------