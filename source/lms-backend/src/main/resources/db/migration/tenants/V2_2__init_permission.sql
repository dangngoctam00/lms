-- =============== course =============================
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (1, 'Xem danh sách khóa học', 'VIEW_ALL_COURSE',
        'Người dùng được cấp quyền này có thể xem danh sách các khóa học đang có trong hệ thống của bạn', false, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (2, 'Tạo khóa học', 'CREATE_COURSE',
        'Người dùng được cấp quyền này có quyền tạo khóa học trong hệ thống của bạn', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (3, 'Xem chi tiết khóa học', 'VIEW_DETAIL_COURSE',
        'Người dùng được cấp quyền này có quyền xem chi tiết các khoá học của bạn', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (4, 'Chỉnh sửa khóa học', 'UPDATE_COURSE',
        'Người dùng được cấp quyền này có thể chỉnh sửa khóa học trong hệ thống của bạn', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (5, 'Xóa khóa học', 'DELETE_COURSE',
        'Người dùng được cấp quyền này có thể xóa khóa học trong hệ thống của bạn', true, true,
        true, true, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (6, 'Xem nội dung khóa học', 'VIEW_LEARNING_CONTENT',
        'Người dùng được cấp quyền này có quyền xem các hoạt động học tập của khóa học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (7, 'Tạo nội dung khóa học', 'CREATE_LEARNING_CONTENT',
        'Người dùng được cấp quyền này có quyền tạo các hoạt động học tập của khóa học', false, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (8, 'Cập nhật nội dung khóa học', 'UPDATE_LEARNING_CONTENT',
        'Người dùng được cấp quyền này có quyền cập nhật các hoạt động học tập của khóa học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (9, 'Xóa hoạt động học tập', 'DELETE_LEARNING_CONTENT',
        'Người dùng được cấp quyền này có quyền xóa các hoạt động học tập của khóa học', false, true,
        false, false, false, true);


-- =============== staff =============================
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (21, 'Xem danh các nhân viên', 'VIEW_ALL_STAFF',
        'Người dùng được cấp quyền này có thể xem danh sách các nhân viên đang có trong hệ thống của bạn', true, true,
        true, true, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (22, 'Tạo nhân viên', 'CREATE_STAFF',
        'Người dùng được cấp quyền này có thể tạo nhân viên trong hệ thống của bạn', true, true,
        true, true, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (23, 'Xem chi tiết thông tin nhân viên', 'VIEW_DETAIL_STAFF',
        'Người dùng được cấp quyền này có thể xem chi tiết thông tin những nhân viên của bạn', true, true,
        true, true, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (24, 'Cập nhật thông tin của nhân viên', 'UPDATE_STAFF',
        'Người dùng được cấp quyền này có thể cập nhật thông tin nhân viên của trung tâm', true, true,
        true, true, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (25, 'Xóa nhân viên', 'DELETE_STAFF',
        'Người dùng được cấp quyền này có thể xóa nhân viên trong hệ thống của bạn', true, true,
        true, true, true, false);


-- TEST
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (40, 'Xem danh sách đề kiểm tra trong khóa học', 'VIEW_LIST_TEST',
        'Người dùng được cấp quyền này có thể xem danh sách các đề kiểm tra ở khóa học', false, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (41, 'Xem chi tiết đề kiểm tra trong khóa học', 'VIEW_TEST',
        'Người dùng được cấp quyền này có thể xem chi tiết bài kiểm tra trong khóa học', false, true,
        false, false, true, true);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (42, 'Tạo đề kiểm tra trong khóa học', 'CREATE_TEST',
        'Người dùng được cấp quyền này có thể tạo mới đề kiểm tra ở khóa học', false, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (43, 'Xem chi tiết đề kiểm tra trong khóa học', 'UPDATE_TEST',
        'Người dùng được cấp quyền này có thể cập nhật chi tiết đề kiểm tra trong khóa học', false, true,
        false, false, false, true);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (44, 'Xem chi tiết đề kiểm tra trong khóa học', 'DELETE_TEST',
        'Người dùng được cấp quyền này có thể xóa chi tiết đề kiểm tra trong khóa học', false, true,
        false, false, false, true);

-- CLASS
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (50, 'Xem danh sách lớp học', 'VIEW_LIST_CLASS',
        'Người dùng được cấp quyền này có thể xem danh sách các lớp học ở trung tâm', true, true,
        true, true, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (51, 'Xem thông tin trong lớp học', 'VIEW_CLASS',
        'Người dùng được cấp quyền này có thể xem thông tin trong một lớp học', true, true,
        true, true, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (52, 'Tạo mới lớp học', 'CREATE_CLASS',
        'Người dùng được cấp quyền này có quyền tạo mới lớp học', true, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (53, 'Cập nhật thông tin lớp học', 'UPDATE_CLASS',
        'Người dùng được cấp quyền này có cập nhật thông tin của một lớp học', false, true,
        false, false, false, false);

-- QUIZ CLASS

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (60, 'Xem danh sách quiz trong lớp học', 'VIEW_LIST_QUIZ_CLASS',
        'Người dùng được cấp quyền này có thể xem danh sách quiz trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (61, 'Xem quiz trong lớp học', 'VIEW_QUIZ_CLASS',
        'Người dùng được cấp quyền này có thể xem quiz trong một lớp học', false, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (62, 'Tạo quiz trong lớp học', 'CREATE_QUIZ_CLASS',
        'Người dùng được cấp quyền này có thể tạo mới quiz trong một lớp học', true, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (63, 'Cập nhật quiz trong lớp học', 'UPDATE_QUIZ_CLASS',
        'Người dùng được cấp quyền này có thể cập nhật quiz trong một lớp học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (64, 'Xóa quiz trong lớp học', 'DELETE_QUIZ_CLASS',
        'Người dùng được cấp quyền này có thể xóa quiz trong một lớp học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (65, 'Làm quiz', 'DO_QUIZ',
        'Người dùng được cấp quyền này có thể làm bài quiz trong một lớp học', false, false,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (66, 'Làm quiz', 'VIEW_QUIZ_RESULT',
        'Người dùng được cấp quyền này có thể xem kết quả làm bài quiz trong một lớp học', false, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (67, 'Chấm điểm quiz', 'GRADE_QUIZ',
        'Người dùng được cấp quyền này có thể chấm điểm quiz cho học sinh trong một lớp học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (68, 'Xem thống kê bài quiz', 'VIEW_QUIZ_RESULT_MANAGEMENT',
        'Người dùng được cấp quyền này có thể xem thống kê điểm của học sinh trong một bài quiz trong lớp học', false,
        true,
        false, false, false, true);


-- UNIT CLASS

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (80, 'Xem danh sách bài học trong lớp học', 'VIEW_LIST_UNIT_CLASS',
        'Người dùng được cấp quyền này có thể xem danh sách bài học trong một lớp học', true, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (81, 'Xem bài học trong lớp học', 'VIEW_UNIT_CLASS',
        'Người dùng được cấp quyền này có thể xem một bài học trong một lớp học', true, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (82, 'Tạo bài học trong lớp học', 'CREATE_UNIT_CLASS',
        'Người dùng được cấp quyền này có thể tạo mới bài học trong một lớp học', true, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (83, 'Cập nhật quiz trong lớp học', 'UPDATE_UNIT_CLASS',
        'Người dùng được cấp quyền này có thể cập nhật bài học trong một lớp học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (84, 'Xóa bài học trong lớp học', 'DELETE_UNIT_CLASS',
        'Người dùng được cấp quyền này có thể xóa một bài học trong một lớp học', false, true,
        false, false, false, true);

-- VOTING

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (90, 'Xem danh sách bình chọn trong lớp học', 'VIEW_LIST_VOTING',
        'Người dùng được cấp quyền này có thể xem danh sách bình chọn trong một lớp học', true, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (91, 'Xem bình chọn trong lớp học', 'VIEW_VOTING',
        'Người dùng được cấp quyền này có thể xem một bình chọn trong một lớp học', true, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (92, 'Tạo bình chọn trong lớp học', 'CREATE_VOTING',
        'Người dùng được cấp quyền này có thể tạo mới bình chọn trong một lớp học', true, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (93, 'Cập nhật bình chọn trong lớp học', 'UPDATE_VOTING',
        'Người dùng được cấp quyền này có thể cập nhật bình chọn trong một lớp học', false, true,
        false, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (94, 'Xóa bình chọn trong lớp học', 'DELETE_VOTING',
        'Người dùng được cấp quyền này có thể xóa một bình chọn trong một lớp học', false, true,
        false, false, false, true);


-- POST
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (70, 'Xem danh sách bài đăng trong lớp học', 'VIEW_LIST_POST',
        'Người dùng được cấp quyền này có thể xem các bài đăng trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (71, 'Xem chi tiết bài đăng trong lớp học', 'VIEW_POST',
        'Người dùng được cấp quyền này có thể xem chi tiết các bài đăng trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (72, 'Tạo bài đăng trong lớp học', 'CREATE_POST',
        'Người dùng được cấp quyền này có thể tạo đăng trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (73, 'Cập nhật bài đăng trong lớp học', 'UPDATE_POST',
        'Người dùng được cấp quyền này có thể cập nhật bài đăng trong một lớp học', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (74, 'Xóa bài đăng trong lớp học', 'DELETE_POST',
        'Người dùng được cấp quyền này có thể xóa bài đăng trong một lớp học', false, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (75, 'Xem bình luận bài đăng trong lớp học', 'VIEW_LIST_COMMENT',
        'Người dùng được cấp quyền này có thể xem bình luận bài đăng trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (76, 'Bình luận bài đăng trong lớp học', 'COMMENT_POST',
        'Người dùng được cấp quyền này có bình luận bài đăng trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (77, 'Tương tác bài đăng trong lớp học', 'INTERACT_POST',
        'Người dùng được cấp quyền này có thể tương tác với bài đăng trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (78, 'Cập nhật bình luận bài đăng trong lớp học', 'UPDATE_COMMENT',
        'Người dùng được cấp quyền này có thể cập nhật bình luận bài đăng trong một lớp học', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (79, 'Xóa bình luận bài đăng trong lớp học', 'DELETE_COMMENT',
        'Người dùng được cấp quyền này có thể xóa bình luận bài đăng trong một lớp học', false, true,
        false, false, true, true);

-- CLASS SCHEDULER

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (100, 'Xem thời khóa biểu', 'VIEW_CLASS_SCHEDULER',
        'Người dùng được cấp quyền này có thể xem thời khóa biểu một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (101, 'Cập nhật thời khóa biểu trong lớp học', 'UPDATE_CLASS_SCHEDULER',
        'Người dùng được cấp quyền này có thể cập nhật thời khóa biểu trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (102, 'Thêm buổi học trong lớp học', 'CREATE_CLASS_SESSION',
        'Người dùng được cấp quyền này có thể thêm buổi học trong một lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (103, 'Cập nhật thông buổi học trong lớp học', 'UPDATE_CLASS_SESSION',
        'Người dùng được cấp quyền này có thể cập nhật thông tin buổi học trong một lớp học', false, true,
        false, false, true, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (104, 'Xóa buổi học trong lớp học', 'DELETE_CLASS_SESSION',
        'Người dùng được cấp quyền này có thể xóa buổi học trong một lớp học', false, true,
        false, false, true, true);

-- ANNOUNCEMENT

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (120, 'Gửi thông báo ở cấp trung tâm', 'CREATE_ANNOUNCEMENT_CENTRAL',
        'Người dùng được cấp quyền này có thể gửi thông báo ở cấp trung tâm', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (121, 'Gửi thông báo ở cấp lớp học', 'CREATE_ANNOUNCEMENT_CLASS',
        'Người dùng được cấp quyền này có thể gửi thông báo ở cấp lớp học', false, true,
        false, false, true, false);

-- insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
--                         has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
-- values (122, 'Thêm buổi học trong lớp học', 'CREATE_CLASS_SESSION',
--         'Người dùng được cấp quyền này có thể thêm buổi học trong một lớp học', false, true,
--         false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (123, 'Xem danh sách thông báo', 'VIEW_LIST_ANNOUNCEMENT',
        'Người dùng được cấp quyền này có quyền xem danh sách thông báo', false, true,
        false, false, true, true);


insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (130, 'Xem danh sách học sinh trong lớp học', 'VIEW_LIST_STUDENT_IN_CLASS',
        'Người dùng được cấp quyền này có quyền xem danh sách học sinh trong lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (131, 'Thêm học sinh vào lớp học', 'ADD_STUDENT_TO_CLASS',
        'Người dùng được cấp quyền này có thể học sinh vào lớp học', false, true,
        false, false, false, false);


insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (140, 'Xem danh sách giáo viên trong lớp học', 'VIEW_LIST_TEACHER_IN_CLASS',
        'Người dùng được cấp quyền này có quyền xem danh sách giáo viên trong lớp học', false, true,
        false, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (141, 'Thêm giáo viên vào lớp học', 'ADD_TEACHER',
        'Người dùng được cấp quyền này có quyền thêm giáo viên vào lớp học', false, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (142, 'Cập nhật vai trò giáo viên trong một lớp học', 'UPDATE_TEACHER',
        'Người dùng được cấp quyền này có quyền cập nhật vai trò của giáo viên trong lớp học', false, true,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (143, 'Thêm giáo viên vào lớp học', 'DELETE_TEACHER',
        'Người dùng được cấp quyền này có quyền xóa giáo viên khỏi lớp học', false, true,
        false, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (144, 'Cập nhật thông tin của học sinh', 'UPDATE_STUDENT',
        'Người dùng được cấp quyền này có quyền cập nhật thông tin của học sinh', true, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (145, 'Xóa học viên ra khỏi hệ thống', 'DELETE_STUDENT',
        'Người dùng được cấp quyền này có xóa học viên ra khỏi hệ thống', true, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (146, 'Xem thông tin của người dùng', 'VIEW_USER_INFO',
        'Người được cấp quyền này có thể xem thông của user khác trong hệ thống', true, true,
        true, true, true, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (147, 'Xóa lớp học', 'DELETE_CLASS',
        'Người được cấp quyền này có thể xóa lớp học', true, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (148, 'Xem chi tiết học viên', 'VIEW_DETAIL_STUDENT',
        'Xem thông tin chi tiết của học viên', true, true,
        true, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (149, 'Cấp tài khoản cho nhân viên', 'ALLOCATE_ACCOUNT_STAFF',
        'Tạo tài khoản để nhân viên có thể đăng nhập vào hệ thống', true, false,
        false, true, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (150, 'Xóa tài khoản của nhân viên', 'ALLOCATE_ACCOUNT_STAFF',
        'Thu hồi tài khoản của nhân viên, họ sẽ không đăng nhập vào hệ thống được nữa', true, false,
        false, true, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (151, 'Thêm vai trò mới', 'CREATE_ROLE',
        'Tạo một vai trò mới trong hệ thống', false, false,
        false, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (152, 'Cập nhật vai trò', 'UPDATE_ROLE',
        'Chỉnh sử quyền hạn của một vai trò trong hệ thống', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (153, 'Xem danh sách vai trò', 'VIEW_LIST_ROLE',
        'Xem được danh sách các vai trò đang có trong hệ thống', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (154, 'Xem chi tiết một vai trò', 'VIEW_DETAIL_ROLE',
        'Xem được chi tiết một vai trò đang có trong hệ thống', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (155, 'Xóa một vai trò', 'DELETE_ROLE',
        'Xóa một vai trò đang có trong hệ thống', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (156, 'Thêm học viên mới', 'ADD_STUDENT',
        'Thêm học viên mới vào hệ thống, cấp tài khoản cho học viên', true, false,
        true, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (157, 'Xem danh sách học sinh của trung tâm', 'VIEW_LIST_STUDENT',
        'Người dùng được cấp quyền này có quyền xem danh sách học sinh của trung tâm', true, false,
        true, false, true, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (158, 'Xem danh sách cột điểm trong lớp', 'VIEW_LIST_GRADE_TAG_IN_CLASS',
        'Người dùng được cấp quyền này có quyền thấy các cột điểm của lớp', false, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (159, 'Thêm cột điểm cho lớp', 'CREATE_GRADE_TAG_IN_CLASS',
        'Người dùng được cấp quyền này có quyền tạo thêm cột điểm cho lớp', false, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (160, 'Chỉnh sửa cột điểm', 'UPDATE_GRADE_TAG',
        'Người dùng được cấp quyền này có quyền chỉnh sửa cột điểm của lớp học', false, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (161, 'Xóa cột điểm', 'DELETE_GRADE_TAG',
        'Người dùng được cấp quyền này có quyền xóa cột điểm của lớp học', false, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (162, 'Xem chi tiết cột điểm', 'VIEW_DETAIL_GRADE_TAG',
        'Người dùng được cấp quyền này có quyền xem chi tiết một cột điểm', false, true,
        true, false, false, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (163, 'Xem điểm trong lớp học', 'VIEW_GRADE_RESULT_IN_CLASS',
        'Người dùng được cấp quyền này có quyền xem kết quả của các cột điểm trong lớp học', false, true,
        true, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (164, 'Cấp quyền cho một user khác', 'GRANT_PERMISSION',
        'Người dùng được cấp quyền này có thể cấp quyền cho một tài khoản khác', true, true,
        true, true, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (165, 'Tính điểm của các cột điểm', 'CALCULATE_GRADE_TAG',
        'Người dùng được cấp quyền này có thể tính điểm của các cột điểm', true, true,
        true, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (166, 'Xóa học viên ra khỏi lớp', 'REMOVE_STUDENT_FROM_CLASS',
        'Người dùng được cấp quyền này có thể xóa học viên ra khỏi lớp', true, true,
        true, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (200, 'Xem tài liệu tham khảo của trung tâm', 'VIEW_TEXTBOOK_CENTRAL',
        'Người dùng được cấp quyền này có thể xem các tài liệu tham khảo của trung tâm', true, true,
        true, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (201, 'Thêm tài liệu tham khảo cho trung tâm', 'ADD_TEXTBOOK_CENTRAL',
        'Người dùng được cấp quyền này có thể thêm các tài liệu tham khảo của trung tâm', false, false,
        false, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (202, 'Cập nhật tài liệu tham khảo cho trung tâm', 'UPDATE_TEXTBOOK_CENTRAL',
        'Người dùng được cấp quyền này có thể cập nhật tài liệu tham khảo của trung tâm', true, true,
        true, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (203, 'Xóa tài liệu tham khảo của trung tâm', 'DELETE_TEXTBOOK_CENTRAL',
        'Người dùng được cấp quyền này có thể xóa tài liệu tham khảo của trung tâm', true, true,
        true, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (204, 'Xem tài liệu tham khảo của khóa học', 'VIEW_TEXTBOOK_COURSE',
        'Người dùng được cấp quyền này có thể xem tài liệu của khóa học', true, true,
        true, false, false, true);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (205, 'Thêm tài liệu tham khảo vào khóa học', 'ADD_TEXTBOOK_COURSE',
        'Người dùng được cấp quyền này có thể thêm tài liệu tham khảo vào khóa học', true, true,
        true, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (206, 'Xóa tài liệu tham khảo khỏi khóa học', 'REMOVE_TEXTBOOK_COURSE',
        'Người dùng được cấp quyền này có thể xóa tài liệu tham khảo khỏi khóa học', true, true,
        true, false, false, false);

insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (207, 'Xem tài liệu tham khảo của lớp học', 'VIEW_TEXTBOOK_CLASS',
        'Người dùng được cấp quyền này có thể xem tài liệu tham khảo của lớp học', true, true,
        true, false, false, false);


insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (220, 'Xem kết quả điểm danh của lớp học', 'VIEW_CLASS_ATTENDANCE',
        'Người dùng được cấp quyền này có thể xem kết quả điểm danh của lớp học', false, true,
        false, false, true, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (221, 'Xem kết quả điểm danh của buổi học', 'VIEW_SESSION_ATTENDANCE',
        'Người dùng được cấp quyền này có thể xem kết quả điểm danh của một buổi học', false, true,
        false, false, true, false);
insert into permission (id, title, code, description, has_limit_by_branch, has_limit_by_teaching, has_limit_by_dean,
                        has_limit_by_manager, has_limit_by_learn, has_limit_by_owner)
values (222, 'Xem kết quả điểm danh của lớp học', 'UPDATE_SESSION_ATTENDANCE',
        'Người dùng được cấp quyền này có thể cập nhật kết quả điểm danh của một buổi học', false, true,
        false, false, true, false);