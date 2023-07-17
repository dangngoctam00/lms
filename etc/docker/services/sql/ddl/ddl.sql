CREATE TYPE "question_type" AS ENUM (
  'multichoice',
  'writing',
  'fill_in_blank',
  'fill_in_blank_with_choices',
  'submit_file'
);

CREATE TYPE "role_lecturer" AS ENUM (
  'teacher',
  'teaching_assistant'
);

CREATE TABLE "user"
(
    "id"         SERIAL PRIMARY KEY,
    "first_name" varchar,
    "last_name"  varchar,
    "email"      varchar,
    "password"   varchar,
    "phone"      varchar,
    "avatar"     varchar,
    "created_at" timestamp
);

CREATE TABLE "permission"
(
    "id"                    SERIAL PRIMARY KEY,
    "title"                 varchar,
    "code"                  varchar,
    "description"           varchar,
    "has_limit_by_branch"   boolean,
    "has_limit_by_teaching" boolean,
    "has_limit_by_dean"     boolean,
    "has_limit_by_manager"  boolean,
    "has_limit_by_learn"    boolean,
    "permission_group_id"   int
);

CREATE TABLE "permission_group"
(
    "id"                         SERIAL PRIMARY KEY,
    "title"                      varchar,
    "parent_permission_group_id" int
);

CREATE TABLE "role"
(
    "id"          SERIAL PRIMARY KEY,
    "description" varchar,
    "title"       varchar
);

CREATE TABLE "user_has_permission"
(
    "user_id"              int,
    "permission"           int,
    "is_limit_by_branch"   boolean,
    "is_limit_by_teaching" boolean,
    "is_limit_by_dean"     boolean,
    "is_limit_by_manager"  boolean,
    "is_limit_by_learn"    boolean,
    "valid_from"           timestamp,
    "expires_at"           timestamp,
    PRIMARY KEY ("user_id", "permission")
);

CREATE TABLE "role_has_permission"
(
    "role_id"              int,
    "permission_id"        int,
    "is_limit_by_branch"   boolean,
    "is_limit_by_teaching" boolean,
    "is_limit_by_dean"     boolean,
    "is_limit_by_manager"  boolean,
    "is_limit_by_learn"    boolean,
    PRIMARY KEY ("role_id", "permission_id")
);

CREATE TABLE "user_has_role"
(
    "user_id"    int,
    "role_id"    int,
    "valid_from" timestamp,
    "expires_at" timestamp,
    PRIMARY KEY ("user_id", "role_id")
);

CREATE TABLE "group"
(
    "id"    SERIAL PRIMARY KEY,
    "name"  varchar,
    "admin" int NOT NULL
);

CREATE TABLE "user_join_group"
(
    "user_id"               int,
    "group_id"              int,
    "number_unseen_message" int,
    PRIMARY KEY ("user_id", "group_id")
);

CREATE TABLE "message"
(
    "id"         SERIAL PRIMARY KEY,
    "content"    varchar,
    "is_file"    boolean,
    "send_by"    int NOT NULL,
    "group_id"   int NOT NULL,
    "created_at" timestamp
);

CREATE TABLE "staff"
(
    "id" int PRIMARY KEY NOT NULL
);

CREATE TABLE "student"
(
    "id" int PRIMARY KEY NOT NULL
);

CREATE TABLE "quiz"
(
    "id"               int PRIMARY KEY,
    "test_id"          int NOT NULL,
    "published_time"   timestamp,
    "due_at"           timestamp,
    "submission_limit" int,
    "tag"              int NOT NULL
);

CREATE TABLE "student_has_quiz_result"
(
    "student_id" int NOT NULL,
    "quiz_id"    int NOT NULL,
    "grade"      int
);

CREATE TABLE "question"
(
    "id"         SERIAL PRIMARY KEY,
    "test_id"    int NOT NULL,
    "point"      int,
    "type"       question_type,
    "attachment" varchar,
    "note"       varchar
);

CREATE TABLE "option_for_multichoice"
(
    "id"          SERIAL,
    "question_id" int NOT NULL,
    "content"     varchar,
    "is_right"    boolean,
    "audio"       varchar,
    "image"       varchar,
    PRIMARY KEY ("id", "question_id")
);

CREATE TABLE "blank_none_option"
(
    "id"              SERIAL PRIMARY KEY,
    "question_id"     int NOT NULL,
    "expected_answer" varchar
);

CREATE TABLE "blank_has_option"
(
    "id"          SERIAL PRIMARY KEY,
    "question_id" int NOT NULL
);

CREATE TABLE "option_for_fill_in_blank"
(
    "id"       SERIAL PRIMARY KEY,
    "blank_id" int NOT NULL,
    "content"  varchar,
    "is_right" varchar
);

CREATE TABLE "answer"
(
    "id"            SERIAL PRIMARY KEY,
    "question_id"   int NOT NULL,
    "submission_id" int NOT NULL,
    "value"         varchar(256),
    "attachment"    varchar,
    "grade"         double precision
);

CREATE TABLE "test"
(
    "id"          SERIAL PRIMARY KEY,
    "title"       varchar,
    "description" varchar,
    "create_at"   timestamp,
    "created_by"  int,
    "updated_at"  timestamp,
    "updated_by"  int
);

CREATE TABLE "test_has_question"
(
    "id" SERIAL PRIMARY KEY
);

CREATE TABLE "submission"
(
    "id"         SERIAL PRIMARY KEY,
    "quiz_id"    int NOT NULL,
    "student_id" int NOT NULL,
    "duration"   bigint,
    "grade"      double precision,
    "created_at" timestamp
);

CREATE TABLE "tag"
(
    "id"    SERIAL PRIMARY KEY,
    "title" varchar
);

CREATE TABLE "grade_formular"
(
    "id"         SERIAL PRIMARY KEY,
    "content"    varchar,
    "result_tag" int NOT NULL
);

CREATE TABLE "grade"
(
    "student_id" int NOT NULL,
    "tag_id"     int NOT NULL,
    "grade"      double precision
);
-- TODO: need add branch when creating course
CREATE TABLE "learning_subject"
(
    "id"         SERIAL PRIMARY KEY,
    "name"       varchar,
    "code"       varchar,
    "price"      int,
    "background" varchar,
    "created_at" timestamp,
    "updated_at" timestamp
);

CREATE TABLE "student_join_learning_subject"
(
    "student_id"          int,
    "learning_subject_id" int,
    "jpa"                 double precision,
    PRIMARY KEY ("student_id", "learning_subject_id")
);

CREATE TABLE "class_has_student"
(
    "student_id" int,
    "class_id"   int,
    PRIMARY KEY ("student_id", "class_id")
);

CREATE TABLE "course"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "interactive_course"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "self_paced_course"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "program"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "courses_programs"
(
    "course_id"  int,
    "program_id" int,
    "order"      int,
    PRIMARY KEY ("course_id", "program_id")
);

CREATE TABLE "semester"
(
    "id"                    SERIAL PRIMARY KEY,
    "interactive_course_id" int NOT NULL,
    "semester_leader_id"    int NOT NULL,
    "start_at"              timestamp,
    "end_at"                timestamp,
    "branch_id"             timestamp
);

CREATE TABLE "class"
(
    "id"          SERIAL PRIMARY KEY,
    "semester_id" int
);

CREATE TABLE "class_session"
(
    "id"          SERIAL PRIMARY KEY,
    "class_id"    int NOT NULL,
    "room_id"     int NOT NULL,
    "start_at"    timestamp,
    "end_at"      timestamp,
    "description" varchar
);

CREATE TABLE "lecturer_teach_class"
(
    "lecturer_id" int,
    "class_id"    int,
    PRIMARY KEY ("lecturer_id", "class_id")
);

CREATE TABLE "lecturer_teach_class_session"
(
    "lecturer_id"         int,
    "class_session_id"    int,
    "substitute_lecturer" int,
    "role"                role_lecturer,
    PRIMARY KEY ("lecturer_id", "class_session_id", "substitute_lecturer")
);

CREATE TABLE "room"
(
    "id" SERIAL PRIMARY KEY
);

CREATE TABLE "meeting_room"
(
    "id"   int PRIMARY KEY NOT NULL,
    "link" varchar         NOT NULL
);

CREATE TABLE "real_room"
(
    "id"       int PRIMARY KEY NOT NULL,
    "capacity" int             NOT NULL,
    "branh_id" int             NOT NULL
);

CREATE TABLE "branch"
(
    "id"         SERIAL PRIMARY KEY,
    "name"       varchar,
    "address"    varchar,
    "manager_id" int NOT NULL
);

CREATE TABLE "branch_has_user"
(
    "user_id"   int,
    "branch_id" int,
    PRIMARY KEY ("user_id", "branch_id")
);

CREATE TABLE "rating"
(
    "id"                  SERIAL,
    "learning_subject_id" int NOT NULL,
    "number_stars"        int,
    "content"             varchar,
    PRIMARY KEY ("id", "learning_subject_id")
);

CREATE TABLE "certificate"
(
    "id"                  SERIAL PRIMARY KEY,
    "created_by"          int NOT NULL,
    "template_id"         int NOT NULL,
    "obtained_by"         int NOT NULL,
    "learning_subject_id" int NOT NULL,
    "name"                varchar,
    "obtained_at"         timestamp
);

CREATE TABLE "staff_grant_certificate"
(
    "staff_id"       int,
    "certificate_id" int,
    PRIMARY KEY ("staff_id", "certificate_id")
);

CREATE TABLE "certificate_template"
(
    "id"         SERIAL PRIMARY KEY,
    "background" varchar
);

CREATE TABLE "learning_unit"
(
    "id"            SERIAL PRIMARY KEY,
    "title"         varchar,
    "teaching_unit" varchar,
    "module_id"     int NOT NULL,
    "created_at"    timestamp,
    "updated_at"    timestamp,
    "created_by"    int NOT NULL
);

CREATE TABLE "module"
(
    "id" int PRIMARY KEY NOT NULL
);

CREATE TABLE "voting"
(
    "id"         int PRIMARY KEY NOT NULL,
    "attachment" varchar,
    "title"      varchar
);

CREATE TABLE "voting_option"
(
    "id"         SERIAL PRIMARY KEY,
    "voting_id"  int NOT NULL,
    "created_by" int NOT NULL,
    "content"    varchar,
    "attachment" varchar
);

CREATE TABLE "user_select_voting_option"
(
    "user_id"          int NOT NULL,
    "voting_option_id" int NOT NULL,
    PRIMARY KEY ("user_id", "voting_option_id")
);

CREATE TABLE "assignment"
(
    "id"             int PRIMARY KEY NOT NULL,
    "due_at"         timestamp,
    "published_time" timestamp,
    "description"    varchar,
    "tag"            int             NOT NULL,
    "attachment"     varchar
);

CREATE TABLE "student_submit_assignment"
(
    "student_id"    int NOT NULL,
    "assignment_id" int NOT NULL,
    "submited_at"   timestamp,
    "submission"    varchar,
    "state"         varchar,
    "grade"         double precision,
    PRIMARY KEY ("student_id", "assignment_id")
);

CREATE TABLE "learning_resources"
(
    "id"   int PRIMARY KEY NOT NULL,
    "path" varchar
);

CREATE TABLE "feedback"
(
    "id"          SERIAL PRIMARY KEY,
    "content"     varchar,
    "class_id"    int NOT NULL,
    "lecturer_id" int,
    "created_at"  timestamp
);

CREATE TABLE "post"
(
    "id"                  SERIAL PRIMARY KEY,
    "title"               varchar,
    "content"             varchar,
    "attachment"          varchar,
    "is_pinned"           boolean,
    "created_by"          int NOT NULL,
    "belongs_to_class"    int,
    "belongs_to_semester" int,
    "created_at"          timestamp,
    "updated_at"          timestamp
);

CREATE TABLE "post_has_likes"
(
    "user_id"  int NOT NULL,
    "post_id"  int NOT NULL,
    "liked_at" timestamp,
    PRIMARY KEY ("user_id", "post_id")
);

CREATE TABLE "comment"
(
    "id"         SERIAL PRIMARY KEY,
    "post_id"    int NOT NULL,
    "content"    varchar,
    "attachment" varchar,
    "created_by" int NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp
);

CREATE TABLE "comment_has_likes"
(
    "user_id"    int NOT NULL,
    "comment_id" int NOT NULL,
    "liked_at"   timestamp,
    PRIMARY KEY ("user_id", "comment_id")
);

CREATE TABLE "notification"
(
    "id"         SERIAL PRIMARY KEY,
    "sender_id"  int NOT NULL,
    "title"      varchar,
    "content"    varchar,
    "attachment" varchar
);

CREATE TABLE "user_receive_notification"
(
    "notification_id" int,
    "user_id"         int,
    PRIMARY KEY ("notification_id", "user_id")
);

CREATE TABLE "student_join_class_session"
(
    "student_id"       int,
    "class_session_id" int,
    "join"             varchar,
    "note"             varchar,
    "late"             boolean,
    PRIMARY KEY ("student_id", "class_session_id")
);

CREATE TABLE "manager_manage_staff"
(
    "manager_id" int,
    "staff_id"   int,
    PRIMARY KEY ("manager_id", "staff_id")
);

CREATE TABLE "manager_manage_learning_subject"
(
    "manager_id"          int,
    "learning_subject_id" int,
    PRIMARY KEY ("manager_id", "learning_subject_id")
);

CREATE TABLE "course_use_learning_unit"
(
    "course_id"        int,
    "learning_unit_id" int,
    PRIMARY KEY ("course_id", "learning_unit_id")
);

CREATE TABLE "semester_use_learning_unit"
(
    "semester_id"      int,
    "learning_unit_id" int,
    PRIMARY KEY ("semester_id", "learning_unit_id")
);

ALTER TABLE "permission"
    ADD FOREIGN KEY ("permission_group_id") REFERENCES "permission_group" ("id");

ALTER TABLE "permission_group"
    ADD FOREIGN KEY ("parent_permission_group_id") REFERENCES "permission_group" ("id");

ALTER TABLE "user_has_permission"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_has_permission"
    ADD FOREIGN KEY ("permission") REFERENCES "permission" ("id");

ALTER TABLE "role_has_permission"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id");

ALTER TABLE "role_has_permission"
    ADD FOREIGN KEY ("permission_id") REFERENCES "permission" ("id");

ALTER TABLE "user_has_role"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_has_role"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id");

ALTER TABLE "group"
    ADD FOREIGN KEY ("admin") REFERENCES "user" ("id");

ALTER TABLE "user_join_group"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_join_group"
    ADD FOREIGN KEY ("group_id") REFERENCES "group" ("id");

ALTER TABLE "message"
    ADD FOREIGN KEY ("send_by") REFERENCES "user" ("id");

ALTER TABLE "message"
    ADD FOREIGN KEY ("group_id") REFERENCES "group" ("id");

ALTER TABLE "staff"
    ADD FOREIGN KEY ("id") REFERENCES "user" ("id");

ALTER TABLE "student"
    ADD FOREIGN KEY ("id") REFERENCES "user" ("id");

ALTER TABLE "quiz"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "quiz"
    ADD FOREIGN KEY ("test_id") REFERENCES "test" ("id");

ALTER TABLE "quiz"
    ADD FOREIGN KEY ("tag") REFERENCES "tag" ("id");

ALTER TABLE "student_has_quiz_result"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_has_quiz_result"
    ADD FOREIGN KEY ("quiz_id") REFERENCES "quiz" ("id");

ALTER TABLE "question"
    ADD FOREIGN KEY ("test_id") REFERENCES "test" ("id");

ALTER TABLE "option_for_multichoice"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "blank_none_option"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "blank_has_option"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "option_for_fill_in_blank"
    ADD FOREIGN KEY ("blank_id") REFERENCES "blank_has_option" ("id");

ALTER TABLE "answer"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "answer"
    ADD FOREIGN KEY ("submission_id") REFERENCES "submission" ("id");

ALTER TABLE "submission"
    ADD FOREIGN KEY ("quiz_id") REFERENCES "quiz" ("id");

ALTER TABLE "submission"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "grade_formular"
    ADD FOREIGN KEY ("result_tag") REFERENCES "tag" ("id");

ALTER TABLE "grade"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "grade"
    ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("id");

ALTER TABLE "student_join_learning_subject"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_join_learning_subject"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "class_has_student"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "class_has_student"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "course"
    ADD FOREIGN KEY ("id") REFERENCES "learning_subject" ("id");

ALTER TABLE "interactive_course"
    ADD FOREIGN KEY ("id") REFERENCES "course" ("id");

ALTER TABLE "self_paced_course"
    ADD FOREIGN KEY ("id") REFERENCES "course" ("id");

ALTER TABLE "program"
    ADD FOREIGN KEY ("id") REFERENCES "learning_subject" ("id");

ALTER TABLE "courses_programs"
    ADD FOREIGN KEY ("course_id") REFERENCES "course" ("id");

ALTER TABLE "courses_programs"
    ADD FOREIGN KEY ("program_id") REFERENCES "program" ("id");

ALTER TABLE "semester"
    ADD FOREIGN KEY ("interactive_course_id") REFERENCES "interactive_course" ("id");

ALTER TABLE "semester"
    ADD FOREIGN KEY ("semester_leader_id") REFERENCES "staff" ("id");

ALTER TABLE "class"
    ADD FOREIGN KEY ("semester_id") REFERENCES "semester" ("id");

ALTER TABLE "class_session"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "class_session"
    ADD FOREIGN KEY ("room_id") REFERENCES "room" ("id");

ALTER TABLE "lecturer_teach_class"
    ADD FOREIGN KEY ("lecturer_id") REFERENCES "staff" ("id");

ALTER TABLE "lecturer_teach_class"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "lecturer_teach_class_session"
    ADD FOREIGN KEY ("lecturer_id") REFERENCES "staff" ("id");

ALTER TABLE "lecturer_teach_class_session"
    ADD FOREIGN KEY ("class_session_id") REFERENCES "class_session" ("id");

ALTER TABLE "lecturer_teach_class_session"
    ADD FOREIGN KEY ("substitute_lecturer") REFERENCES "staff" ("id");

ALTER TABLE "meeting_room"
    ADD FOREIGN KEY ("id") REFERENCES "room" ("id");

ALTER TABLE "real_room"
    ADD FOREIGN KEY ("id") REFERENCES "room" ("id");

ALTER TABLE "real_room"
    ADD FOREIGN KEY ("branh_id") REFERENCES "branch" ("id");

ALTER TABLE "branch"
    ADD FOREIGN KEY ("manager_id") REFERENCES "user" ("id");

ALTER TABLE "branch_has_user"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "branch_has_user"
    ADD FOREIGN KEY ("branch_id") REFERENCES "branch" ("id");

ALTER TABLE "rating"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("created_by") REFERENCES "staff" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("template_id") REFERENCES "certificate_template" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("obtained_by") REFERENCES "student" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "staff_grant_certificate"
    ADD FOREIGN KEY ("staff_id") REFERENCES "staff" ("id");

ALTER TABLE "staff_grant_certificate"
    ADD FOREIGN KEY ("certificate_id") REFERENCES "certificate" ("id");

ALTER TABLE "learning_unit"
    ADD FOREIGN KEY ("module_id") REFERENCES "module" ("id");

ALTER TABLE "learning_unit"
    ADD FOREIGN KEY ("created_by") REFERENCES "staff" ("id");

ALTER TABLE "module"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "voting"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "voting_option"
    ADD FOREIGN KEY ("voting_id") REFERENCES "voting" ("id");

ALTER TABLE "voting_option"
    ADD FOREIGN KEY ("created_by") REFERENCES "user" ("id");

ALTER TABLE "user_select_voting_option"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_select_voting_option"
    ADD FOREIGN KEY ("voting_option_id") REFERENCES "voting_option" ("id");

ALTER TABLE "assignment"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "assignment"
    ADD FOREIGN KEY ("tag") REFERENCES "tag" ("id");

ALTER TABLE "student_submit_assignment"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_submit_assignment"
    ADD FOREIGN KEY ("assignment_id") REFERENCES "assignment" ("id");

ALTER TABLE "learning_resources"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "feedback"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");
CREATE TYPE "question_type" AS ENUM (
  'multichoice',
  'writing',
  'fill_in_blank',
  'fill_in_blank_with_choices',
  'submit_file'
);

CREATE TYPE "role_lecturer" AS ENUM (
  'teacher',
  'teaching_assistant'
);

CREATE TABLE "user"
(
    "id"         SERIAL PRIMARY KEY,
    "first_name" varchar,
    "last_name"  varchar,
    "email"      varchar,
    "password"   varchar,
    "phone"      varchar,
    "avatar"     varchar,
    "created_at" timestamp
);

CREATE TABLE "permission"
(
    "id"                    SERIAL PRIMARY KEY,
    "title"                 varchar,
    "code"                  varchar,
    "description"           varchar,
    "has_limit_by_branch"   boolean,
    "has_limit_by_teaching" boolean,
    "has_limit_by_dean"     boolean,
    "has_limit_by_manager"  boolean,
    "has_limit_by_learn"    boolean,
    "permission_group_id"   int
);

CREATE TABLE "permission_group"
(
    "id"                         SERIAL PRIMARY KEY,
    "title"                      varchar,
    "parent_permission_group_id" int
);

CREATE TABLE "role"
(
    "id"          SERIAL PRIMARY KEY,
    "description" varchar,
    "title"       varchar
);

CREATE TABLE "user_has_permission"
(
    "user_id"              int,
    "permission"           int,
    "is_limit_by_branch"   boolean,
    "is_limit_by_teaching" boolean,
    "is_limit_by_dean"     boolean,
    "is_limit_by_manager"  boolean,
    "is_limit_by_learn"    boolean,
    "valid_from"           timestamp,
    "expires_at"           timestamp,
    PRIMARY KEY ("user_id", "permission")
);

CREATE TABLE "role_has_permission"
(
    "role_id"              int,
    "permission_id"        int,
    "is_limit_by_branch"   boolean,
    "is_limit_by_teaching" boolean,
    "is_limit_by_dean"     boolean,
    "is_limit_by_manager"  boolean,
    "is_limit_by_learn"    boolean,
    PRIMARY KEY ("role_id", "permission_id")
);

CREATE TABLE "user_has_role"
(
    "user_id"    int,
    "role_id"    int,
    "valid_from" timestamp,
    "expires_at" timestamp,
    PRIMARY KEY ("user_id", "role_id")
);

CREATE TABLE "group"
(
    "id"    SERIAL PRIMARY KEY,
    "name"  varchar,
    "admin" int NOT NULL
);

CREATE TABLE "user_join_group"
(
    "user_id"               int,
    "group_id"              int,
    "number_unseen_message" int,
    PRIMARY KEY ("user_id", "group_id")
);

CREATE TABLE "message"
(
    "id"         SERIAL PRIMARY KEY,
    "content"    varchar,
    "is_file"    boolean,
    "send_by"    int NOT NULL,
    "group_id"   int NOT NULL,
    "created_at" timestamp
);

CREATE TABLE "staff"
(
    "id" int PRIMARY KEY NOT NULL
);

CREATE TABLE "student"
(
    "id" int PRIMARY KEY NOT NULL
);

CREATE TABLE "quiz"
(
    "id"               int PRIMARY KEY,
    "test_id"          int NOT NULL,
    "published_time"   timestamp,
    "due_at"           timestamp,
    "submission_limit" int,
    "tag"              int NOT NULL
);

CREATE TABLE "student_has_quiz_result"
(
    "student_id" int NOT NULL,
    "quiz_id"    int NOT NULL,
    "grade"      int
);

CREATE TABLE "question"
(
    "id"         SERIAL PRIMARY KEY,
    "test_id"    int NOT NULL,
    "point"      int,
    "type"       question_type,
    "attachment" varchar,
    "note"       varchar
);

CREATE TABLE "option_for_multichoice"
(
    "id"          SERIAL,
    "question_id" int NOT NULL,
    "content"     varchar,
    "is_right"    boolean,
    "audio"       varchar,
    "image"       varchar,
    PRIMARY KEY ("id", "question_id")
);

CREATE TABLE "blank_none_option"
(
    "id"              SERIAL PRIMARY KEY,
    "question_id"     int NOT NULL,
    "expected_answer" varchar
);

CREATE TABLE "blank_has_option"
(
    "id"          SERIAL PRIMARY KEY,
    "question_id" int NOT NULL
);

CREATE TABLE "option_for_fill_in_blank"
(
    "id"       SERIAL PRIMARY KEY,
    "blank_id" int NOT NULL,
    "content"  varchar,
    "is_right" varchar
);

CREATE TABLE "answer"
(
    "id"            SERIAL PRIMARY KEY,
    "question_id"   int NOT NULL,
    "submission_id" int NOT NULL,
    "value"         varchar(256),
    "attachment"    varchar,
    "grade"         double precision
);

CREATE TABLE "test"
(
    "id"          SERIAL PRIMARY KEY,
    "title"       varchar,
    "description" varchar,
    "create_at"   timestamp,
    "created_by"  int,
    "updated_at"  timestamp,
    "updated_by"  int
);

CREATE TABLE "test_has_question"
(
    "id" SERIAL PRIMARY KEY
);

CREATE TABLE "submission"
(
    "id"         SERIAL PRIMARY KEY,
    "quiz_id"    int NOT NULL,
    "student_id" int NOT NULL,
    "duration"   bigint,
    "grade"      double precision,
    "created_at" timestamp
);

CREATE TABLE "tag"
(
    "id"    SERIAL PRIMARY KEY,
    "title" varchar
);

CREATE TABLE "grade_formular"
(
    "id"         SERIAL PRIMARY KEY,
    "content"    varchar,
    "result_tag" int NOT NULL
);

CREATE TABLE "grade"
(
    "student_id" int NOT NULL,
    "tag_id"     int NOT NULL,
    "grade"      double precision
);

CREATE TABLE "learning_subject"
(
    "id"         SERIAL PRIMARY KEY,
    "name"       varchar,
    "code"       varchar,
    "price"      int,
    "background" varchar,
    "created_at" timestamp,
    "updated_at" timestamp
);

CREATE TABLE "student_join_learning_subject"
(
    "student_id"          int,
    "learning_subject_id" int,
    "jpa"                 double precision,
    PRIMARY KEY ("student_id", "learning_subject_id")
);

CREATE TABLE "class_has_student"
(
    "student_id" int,
    "class_id"   int,
    PRIMARY KEY ("student_id", "class_id")
);

CREATE TABLE "course"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "interactive_course"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "self_paced_course"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "program"
(
    "id" int PRIMARY KEY
);

CREATE TABLE "courses_programs"
(
    "course_id"  int,
    "program_id" int,
    "order"      int,
    PRIMARY KEY ("course_id", "program_id")
);

CREATE TABLE "semester"
(
    "id"                    SERIAL PRIMARY KEY,
    "interactive_course_id" int NOT NULL,
    "semester_leader_id"    int NOT NULL,
    "start_at"              timestamp,
    "end_at"                timestamp,
    "branch_id"             timestamp
);

CREATE TABLE "class"
(
    "id"          SERIAL PRIMARY KEY,
    "semester_id" int
);

CREATE TABLE "class_session"
(
    "id"          SERIAL PRIMARY KEY,
    "class_id"    int NOT NULL,
    "room_id"     int NOT NULL,
    "start_at"    timestamp,
    "end_at"      timestamp,
    "description" varchar
);

CREATE TABLE "lecturer_teach_class"
(
    "lecturer_id" int,
    "class_id"    int,
    PRIMARY KEY ("lecturer_id", "class_id")
);

CREATE TABLE "lecturer_teach_class_session"
(
    "lecturer_id"         int,
    "class_session_id"    int,
    "substitute_lecturer" int,
    "role"                role_lecturer,
    PRIMARY KEY ("lecturer_id", "class_session_id", "substitute_lecturer")
);

CREATE TABLE "room"
(
    "id" SERIAL PRIMARY KEY
);

CREATE TABLE "meeting_room"
(
    "id"   int PRIMARY KEY NOT NULL,
    "link" varchar         NOT NULL
);

CREATE TABLE "real_room"
(
    "id"       int PRIMARY KEY NOT NULL,
    "capacity" int             NOT NULL,
    "branh_id" int             NOT NULL
);

CREATE TABLE "branch"
(
    "id"         SERIAL PRIMARY KEY,
    "address"    varchar,
    "manager_id" int NOT NULL
);

CREATE TABLE "branch_has_user"
(
    "user_id"   int,
    "branch_id" int,
    PRIMARY KEY ("user_id", "branch_id")
);

CREATE TABLE "rating"
(
    "id"                  SERIAL,
    "learning_subject_id" int NOT NULL,
    "number_stars"        int,
    "content"             varchar,
    PRIMARY KEY ("id", "learning_subject_id")
);

CREATE TABLE "certificate"
(
    "id"                  SERIAL PRIMARY KEY,
    "created_by"          int NOT NULL,
    "template_id"         int NOT NULL,
    "obtained_by"         int NOT NULL,
    "learning_subject_id" int NOT NULL,
    "name"                varchar,
    "obtained_at"         timestamp
);

CREATE TABLE "staff_grant_certificate"
(
    "staff_id"       int,
    "certificate_id" int,
    PRIMARY KEY ("staff_id", "certificate_id")
);

CREATE TABLE "certificate_template"
(
    "id"         SERIAL PRIMARY KEY,
    "background" varchar
);

CREATE TABLE "learning_unit"
(
    "id"            SERIAL PRIMARY KEY,
    "title"         varchar,
    "teaching_unit" varchar,
    "module_id"     int NOT NULL,
    "created_at"    timestamp,
    "updated_at"    timestamp,
    "created_by"    int NOT NULL
);

CREATE TABLE "module"
(
    "id" int PRIMARY KEY NOT NULL
);

CREATE TABLE "voting"
(
    "id"         int PRIMARY KEY NOT NULL,
    "attachment" varchar,
    "title"      varchar
);

CREATE TABLE "voting_option"
(
    "id"         SERIAL PRIMARY KEY,
    "voting_id"  int NOT NULL,
    "created_by" int NOT NULL,
    "content"    varchar,
    "attachment" varchar
);

CREATE TABLE "user_select_voting_option"
(
    "user_id"          int NOT NULL,
    "voting_option_id" int NOT NULL,
    PRIMARY KEY ("user_id", "voting_option_id")
);

CREATE TABLE "assignment"
(
    "id"             int PRIMARY KEY NOT NULL,
    "due_at"         timestamp,
    "published_time" timestamp,
    "description"    varchar,
    "tag"            int             NOT NULL,
    "attachment"     varchar
);

CREATE TABLE "student_submit_assignment"
(
    "student_id"    int NOT NULL,
    "assignment_id" int NOT NULL,
    "submited_at"   timestamp,
    "submission"    varchar,
    "state"         varchar,
    "grade"         double precision,
    PRIMARY KEY ("student_id", "assignment_id")
);

CREATE TABLE "learning_resources"
(
    "id"   int PRIMARY KEY NOT NULL,
    "path" varchar
);

CREATE TABLE "feedback"
(
    "id"          SERIAL PRIMARY KEY,
    "content"     varchar,
    "class_id"    int NOT NULL,
    "lecturer_id" int,
    "created_at"  timestamp
);

CREATE TABLE "post"
(
    "id"                  SERIAL PRIMARY KEY,
    "title"               varchar,
    "content"             varchar,
    "attachment"          varchar,
    "is_pinned"           boolean,
    "created_by"          int NOT NULL,
    "belongs_to_class"    int,
    "belongs_to_semester" int,
    "created_at"          timestamp,
    "updated_at"          timestamp
);

CREATE TABLE "post_has_likes"
(
    "user_id"  int NOT NULL,
    "post_id"  int NOT NULL,
    "liked_at" timestamp,
    PRIMARY KEY ("user_id", "post_id")
);

CREATE TABLE "comment"
(
    "id"         SERIAL PRIMARY KEY,
    "post_id"    int NOT NULL,
    "content"    varchar,
    "attachment" varchar,
    "created_by" int NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp
);

CREATE TABLE "comment_has_likes"
(
    "user_id"    int NOT NULL,
    "comment_id" int NOT NULL,
    "liked_at"   timestamp,
    PRIMARY KEY ("user_id", "comment_id")
);

CREATE TABLE "notification"
(
    "id"         SERIAL PRIMARY KEY,
    "sender_id"  int NOT NULL,
    "title"      varchar,
    "content"    varchar,
    "attachment" varchar
);

CREATE TABLE "user_receive_notification"
(
    "notification_id" int,
    "user_id"         int,
    PRIMARY KEY ("notification_id", "user_id")
);

CREATE TABLE "student_join_class_session"
(
    "student_id"       int,
    "class_session_id" int,
    "join"             varchar,
    "note"             varchar,
    "late"             boolean,
    PRIMARY KEY ("student_id", "class_session_id")
);

CREATE TABLE "manager_manage_staff"
(
    "manager_id" int,
    "staff_id"   int,
    PRIMARY KEY ("manager_id", "staff_id")
);

CREATE TABLE "manager_manage_learning_subject"
(
    "manager_id"          int,
    "learning_subject_id" int,
    PRIMARY KEY ("manager_id", "learning_subject_id")
);

CREATE TABLE "course_use_learning_unit"
(
    "course_id"        int,
    "learning_unit_id" int,
    PRIMARY KEY ("course_id", "learning_unit_id")
);

CREATE TABLE "semester_use_learning_unit"
(
    "semester_id"      int,
    "learning_unit_id" int,
    PRIMARY KEY ("semester_id", "learning_unit_id")
);

ALTER TABLE "permission"
    ADD FOREIGN KEY ("permission_group_id") REFERENCES "permission_group" ("id");

ALTER TABLE "permission_group"
    ADD FOREIGN KEY ("parent_permission_group_id") REFERENCES "permission_group" ("id");

ALTER TABLE "user_has_permission"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_has_permission"
    ADD FOREIGN KEY ("permission") REFERENCES "permission" ("id");

ALTER TABLE "role_has_permission"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id");

ALTER TABLE "role_has_permission"
    ADD FOREIGN KEY ("permission_id") REFERENCES "permission" ("id");

ALTER TABLE "user_has_role"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_has_role"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id");

ALTER TABLE "group"
    ADD FOREIGN KEY ("admin") REFERENCES "user" ("id");

ALTER TABLE "user_join_group"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_join_group"
    ADD FOREIGN KEY ("group_id") REFERENCES "group" ("id");

ALTER TABLE "message"
    ADD FOREIGN KEY ("send_by") REFERENCES "user" ("id");

ALTER TABLE "message"
    ADD FOREIGN KEY ("group_id") REFERENCES "group" ("id");

ALTER TABLE "staff"
    ADD FOREIGN KEY ("id") REFERENCES "user" ("id");

ALTER TABLE "student"
    ADD FOREIGN KEY ("id") REFERENCES "user" ("id");

ALTER TABLE "quiz"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "quiz"
    ADD FOREIGN KEY ("test_id") REFERENCES "test" ("id");

ALTER TABLE "quiz"
    ADD FOREIGN KEY ("tag") REFERENCES "tag" ("id");

ALTER TABLE "student_has_quiz_result"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_has_quiz_result"
    ADD FOREIGN KEY ("quiz_id") REFERENCES "quiz" ("id");

ALTER TABLE "question"
    ADD FOREIGN KEY ("test_id") REFERENCES "test" ("id");

ALTER TABLE "option_for_multichoice"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "blank_none_option"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "blank_has_option"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "option_for_fill_in_blank"
    ADD FOREIGN KEY ("blank_id") REFERENCES "blank_has_option" ("id");

ALTER TABLE "answer"
    ADD FOREIGN KEY ("question_id") REFERENCES "question" ("id");

ALTER TABLE "answer"
    ADD FOREIGN KEY ("submission_id") REFERENCES "submission" ("id");

ALTER TABLE "submission"
    ADD FOREIGN KEY ("quiz_id") REFERENCES "quiz" ("id");

ALTER TABLE "submission"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "grade_formular"
    ADD FOREIGN KEY ("result_tag") REFERENCES "tag" ("id");

ALTER TABLE "grade"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "grade"
    ADD FOREIGN KEY ("tag_id") REFERENCES "tag" ("id");

ALTER TABLE "student_join_learning_subject"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_join_learning_subject"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "class_has_student"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "class_has_student"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "course"
    ADD FOREIGN KEY ("id") REFERENCES "learning_subject" ("id");

ALTER TABLE "interactive_course"
    ADD FOREIGN KEY ("id") REFERENCES "course" ("id");

ALTER TABLE "self_paced_course"
    ADD FOREIGN KEY ("id") REFERENCES "course" ("id");

ALTER TABLE "program"
    ADD FOREIGN KEY ("id") REFERENCES "learning_subject" ("id");

ALTER TABLE "courses_programs"
    ADD FOREIGN KEY ("course_id") REFERENCES "course" ("id");

ALTER TABLE "courses_programs"
    ADD FOREIGN KEY ("program_id") REFERENCES "program" ("id");

ALTER TABLE "semester"
    ADD FOREIGN KEY ("interactive_course_id") REFERENCES "interactive_course" ("id");

ALTER TABLE "semester"
    ADD FOREIGN KEY ("semester_leader_id") REFERENCES "staff" ("id");

ALTER TABLE "class"
    ADD FOREIGN KEY ("semester_id") REFERENCES "semester" ("id");

ALTER TABLE "class_session"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "class_session"
    ADD FOREIGN KEY ("room_id") REFERENCES "room" ("id");

ALTER TABLE "lecturer_teach_class"
    ADD FOREIGN KEY ("lecturer_id") REFERENCES "staff" ("id");

ALTER TABLE "lecturer_teach_class"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "lecturer_teach_class_session"
    ADD FOREIGN KEY ("lecturer_id") REFERENCES "staff" ("id");

ALTER TABLE "lecturer_teach_class_session"
    ADD FOREIGN KEY ("class_session_id") REFERENCES "class_session" ("id");

ALTER TABLE "lecturer_teach_class_session"
    ADD FOREIGN KEY ("substitute_lecturer") REFERENCES "staff" ("id");

ALTER TABLE "meeting_room"
    ADD FOREIGN KEY ("id") REFERENCES "room" ("id");

ALTER TABLE "real_room"
    ADD FOREIGN KEY ("id") REFERENCES "room" ("id");

ALTER TABLE "real_room"
    ADD FOREIGN KEY ("branh_id") REFERENCES "branch" ("id");

ALTER TABLE "branch"
    ADD FOREIGN KEY ("manager_id") REFERENCES "user" ("id");

ALTER TABLE "branch_has_user"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "branch_has_user"
    ADD FOREIGN KEY ("branch_id") REFERENCES "branch" ("id");

ALTER TABLE "rating"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("created_by") REFERENCES "staff" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("template_id") REFERENCES "certificate_template" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("obtained_by") REFERENCES "student" ("id");

ALTER TABLE "certificate"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "staff_grant_certificate"
    ADD FOREIGN KEY ("staff_id") REFERENCES "staff" ("id");

ALTER TABLE "staff_grant_certificate"
    ADD FOREIGN KEY ("certificate_id") REFERENCES "certificate" ("id");

ALTER TABLE "learning_unit"
    ADD FOREIGN KEY ("module_id") REFERENCES "module" ("id");

ALTER TABLE "learning_unit"
    ADD FOREIGN KEY ("created_by") REFERENCES "staff" ("id");

ALTER TABLE "module"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "voting"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "voting_option"
    ADD FOREIGN KEY ("voting_id") REFERENCES "voting" ("id");

ALTER TABLE "voting_option"
    ADD FOREIGN KEY ("created_by") REFERENCES "user" ("id");

ALTER TABLE "user_select_voting_option"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "user_select_voting_option"
    ADD FOREIGN KEY ("voting_option_id") REFERENCES "voting_option" ("id");

ALTER TABLE "assignment"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "assignment"
    ADD FOREIGN KEY ("tag") REFERENCES "tag" ("id");

ALTER TABLE "student_submit_assignment"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_submit_assignment"
    ADD FOREIGN KEY ("assignment_id") REFERENCES "assignment" ("id");

ALTER TABLE "learning_resources"
    ADD FOREIGN KEY ("id") REFERENCES "learning_unit" ("id");

ALTER TABLE "feedback"
    ADD FOREIGN KEY ("class_id") REFERENCES "class" ("id");

ALTER TABLE "feedback"
    ADD FOREIGN KEY ("lecturer_id") REFERENCES "staff" ("id");

ALTER TABLE "post"
    ADD FOREIGN KEY ("created_by") REFERENCES "user" ("id");

ALTER TABLE "post"
    ADD FOREIGN KEY ("belongs_to_class") REFERENCES "class" ("id");

ALTER TABLE "post"
    ADD FOREIGN KEY ("belongs_to_semester") REFERENCES "semester" ("id");

ALTER TABLE "post_has_likes"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "post_has_likes"
    ADD FOREIGN KEY ("post_id") REFERENCES "post" ("id");

ALTER TABLE "comment"
    ADD FOREIGN KEY ("post_id") REFERENCES "post" ("id");

ALTER TABLE "comment"
    ADD FOREIGN KEY ("created_by") REFERENCES "user" ("id");

ALTER TABLE "comment_has_likes"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "comment_has_likes"
    ADD FOREIGN KEY ("comment_id") REFERENCES "comment" ("id");

ALTER TABLE "notification"
    ADD FOREIGN KEY ("sender_id") REFERENCES "user" ("id");

ALTER TABLE "user_receive_notification"
    ADD FOREIGN KEY ("notification_id") REFERENCES "notification" ("id");

ALTER TABLE "user_receive_notification"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "student_join_class_session"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_join_class_session"
    ADD FOREIGN KEY ("class_session_id") REFERENCES "class_session" ("id");

ALTER TABLE "manager_manage_staff"
    ADD FOREIGN KEY ("manager_id") REFERENCES "staff" ("id");

ALTER TABLE "manager_manage_staff"
    ADD FOREIGN KEY ("staff_id") REFERENCES "staff" ("id");

ALTER TABLE "manager_manage_learning_subject"
    ADD FOREIGN KEY ("manager_id") REFERENCES "staff" ("id");

ALTER TABLE "manager_manage_learning_subject"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "voting_option"
    ADD FOREIGN KEY ("voting_id") REFERENCES "real_room" ("id");

ALTER TABLE "course_use_learning_unit"
    ADD FOREIGN KEY ("course_id") REFERENCES "course" ("id");

ALTER TABLE "course_use_learning_unit"
    ADD FOREIGN KEY ("learning_unit_id") REFERENCES "learning_unit" ("id");

ALTER TABLE "semester_use_learning_unit"
    ADD FOREIGN KEY ("semester_id") REFERENCES "semester" ("id");

ALTER TABLE "semester_use_learning_unit"
    ADD FOREIGN KEY ("learning_unit_id") REFERENCES "learning_unit" ("id");

ALTER TABLE "feedback"
    ADD FOREIGN KEY ("lecturer_id") REFERENCES "staff" ("id");

ALTER TABLE "post"
    ADD FOREIGN KEY ("created_by") REFERENCES "user" ("id");

ALTER TABLE "post"
    ADD FOREIGN KEY ("belongs_to_class") REFERENCES "class" ("id");

ALTER TABLE "post"
    ADD FOREIGN KEY ("belongs_to_semester") REFERENCES "semester" ("id");

ALTER TABLE "post_has_likes"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "post_has_likes"
    ADD FOREIGN KEY ("post_id") REFERENCES "post" ("id");

ALTER TABLE "comment"
    ADD FOREIGN KEY ("post_id") REFERENCES "post" ("id");

ALTER TABLE "comment"
    ADD FOREIGN KEY ("created_by") REFERENCES "user" ("id");

ALTER TABLE "comment_has_likes"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "comment_has_likes"
    ADD FOREIGN KEY ("comment_id") REFERENCES "comment" ("id");

ALTER TABLE "notification"
    ADD FOREIGN KEY ("sender_id") REFERENCES "user" ("id");

ALTER TABLE "user_receive_notification"
    ADD FOREIGN KEY ("notification_id") REFERENCES "notification" ("id");

ALTER TABLE "user_receive_notification"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "student_join_class_session"
    ADD FOREIGN KEY ("student_id") REFERENCES "student" ("id");

ALTER TABLE "student_join_class_session"
    ADD FOREIGN KEY ("class_session_id") REFERENCES "class_session" ("id");

ALTER TABLE "manager_manage_staff"
    ADD FOREIGN KEY ("manager_id") REFERENCES "staff" ("id");

ALTER TABLE "manager_manage_staff"
    ADD FOREIGN KEY ("staff_id") REFERENCES "staff" ("id");

ALTER TABLE "manager_manage_learning_subject"
    ADD FOREIGN KEY ("manager_id") REFERENCES "staff" ("id");

ALTER TABLE "manager_manage_learning_subject"
    ADD FOREIGN KEY ("learning_subject_id") REFERENCES "learning_subject" ("id");

ALTER TABLE "voting_option"
    ADD FOREIGN KEY ("voting_id") REFERENCES "real_room" ("id");

ALTER TABLE "course_use_learning_unit"
    ADD FOREIGN KEY ("course_id") REFERENCES "course" ("id");

ALTER TABLE "course_use_learning_unit"
    ADD FOREIGN KEY ("learning_unit_id") REFERENCES "learning_unit" ("id");

ALTER TABLE "semester_use_learning_unit"
    ADD FOREIGN KEY ("semester_id") REFERENCES "semester" ("id");

ALTER TABLE "semester_use_learning_unit"
    ADD FOREIGN KEY ("learning_unit_id") REFERENCES "learning_unit" ("id");
CREATE TABLE quiz
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title         VARCHAR(255),
    tag           VARCHAR(255),
    total_grade   DOUBLE PRECISION,
    acepted_grade DOUBLE PRECISION,
    grade_method  VARCHAR(255),
    time          INTEGER,
    max_attempt   INTEGER,
    penalty       INTEGER,
    sort_index    INTEGER,
    chapter_id    BIGINT,
    CONSTRAINT pk_quiz PRIMARY KEY (id)
);

ALTER TABLE quiz
    ADD CONSTRAINT FK_QUIZ_ON_CHAPTER FOREIGN KEY (chapter_id) REFERENCES chapter (id);