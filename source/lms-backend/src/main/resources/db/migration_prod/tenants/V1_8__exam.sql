CREATE TYPE "question_type" AS ENUM (
    'MULTI_CHOICE',
    'WRITING',
    'FILL_IN_BLANK',
    'FILL_IN_BLANK_WITH_CHOICES',
    'SUBMIT_FILE',
    'FILL_IN_BLANK_DRAG_AND_DROP'
    );


CREATE TABLE "exam"
(
    "id"          serial PRIMARY KEY,
    "title"       varchar(50),
    "description" text,
    "state"       varchar(20),
    "course_id"   int         NOT NULL,
    "created_at"  timestamp   NOT NULL,
    "created_by"  varchar(50) NOT NULL,
    "updated_at"  timestamp   NOT NULL,
    "updated_by"  varchar(50) NOT NULL
);

ALTER TABLE "quiz_class"
    ADD FOREIGN KEY ("exam_id") REFERENCES "exam" ("id");

ALTER TABLE "quiz_course"
    ADD FOREIGN KEY ("exam_id") REFERENCES "exam" ("id");

ALTER TABLE "exam"
    ADD CONSTRAINT "exam_course_id_fk" FOREIGN KEY ("course_id") REFERENCES "course" ("id");

CREATE TABLE "question"
(
    "id"    int PRIMARY KEY,
    "type"  varchar(50) NOT NULL,
    "point" int,
    "description" text NOT NULL,
    "attachment"  varchar,
    "note"        varchar
);

ALTER TABLE "question"
    ADD CONSTRAINT "base_question_type_check" CHECK ("type" IN (
                                                                'MULTI_CHOICE',
                                                                'WRITING',
                                                                'FILL_IN_BLANK',
                                                                'FILL_IN_BLANK_WITH_CHOICES',
                                                                'SUBMIT_FILE',
                                                                'FILL_IN_BLANK_DRAG_AND_DROP',
                                                                'GROUP',
                                                                'QUESTION_IN_GROUP'
        ));

-- Test shuffling will shuffle these sources instead of the question or the text themselves
CREATE TABLE "question_source"
(
    "id" serial PRIMARY KEY
);

ALTER TABLE "question"
    ADD CONSTRAINT "question_questionsource_fk" FOREIGN KEY ("id") REFERENCES "question_source" ("id")
        ON DELETE CASCADE;

CREATE TABLE "exam_question_source"
(
    "id"                 serial PRIMARY KEY,
    "exam_id"            int,
    "question_source_id" int,
    "sort_index"         int
);

ALTER TABLE "exam_question_source"
    ADD CONSTRAINT "fk_test_has_question" FOREIGN KEY ("exam_id") REFERENCES "exam" ("id")
        ON DELETE CASCADE;

ALTER TABLE "exam_question_source"
    ADD CONSTRAINT "fk_question_in_test" FOREIGN KEY ("question_source_id") REFERENCES "question_source" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_writing_question"
(
    "id"          int PRIMARY KEY
);

ALTER TABLE "quiz_writing_question"
    ADD CONSTRAINT "writing_question_base_question_id" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_multi_choice_question"
(
    "id"                 int PRIMARY KEY,
    "is_multiple_answer" boolean
);

ALTER TABLE "quiz_multi_choice_question"
    ADD CONSTRAINT "multi_choice_question_base_question_id" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_multi_choice_option"
(
    "id"          serial PRIMARY KEY,
    "question_id" int,
    "content"     varchar,
    "answer_key"  int NOT NULL,
    "is_correct"  boolean,
    "sort_index"  int
);

ALTER TABLE "quiz_multi_choice_option"
    ADD CONSTRAINT "multi_choice_option_question_fk" FOREIGN KEY ("question_id") REFERENCES "quiz_multi_choice_question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_question"
(
    "id"          int PRIMARY KEY
);

ALTER TABLE "quiz_fill_in_blank_question"
    ADD CONSTRAINT "fill_in_blank_question_base_question_id" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_option"
(
    "id"              serial PRIMARY KEY,
    "question_id"     int         not null,
    "expected_answer" varchar     not null,
    "match_strategy"  varchar(20) not null,
    "sort_index"      int         not null,
    "hint"            text
);

ALTER TABLE "quiz_fill_in_blank_option"
    ADD CONSTRAINT "quiz_fill_in_blank_option_question_fk" FOREIGN KEY ("question_id") REFERENCES "quiz_fill_in_blank_question" ("id")
        ON DELETE CASCADE;

ALTER TABLE "quiz_fill_in_blank_option"
    ADD CONSTRAINT "fill_in_blank_option_match_strategy_check" CHECK ( "match_strategy" IN ('EXACT', 'CONTAIN', 'REGEX'));


CREATE TABLE "quiz_fill_in_blank_multi_choice_question"
(
    "id"          int PRIMARY KEY
);

ALTER TABLE "quiz_fill_in_blank_multi_choice_question"
    ADD CONSTRAINT "fill_in_blank_multi_choice_question_base_question_id" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_multi_choice_blank"
(
    "id"                 serial PRIMARY KEY,
    "question_id"        int not null,
    "correct_answer_key" int not null,
    "hint"               text,
    "sort_index"         int not null
);

ALTER TABLE "quiz_fill_in_blank_multi_choice_blank"
    ADD CONSTRAINT "fill_in_blank_multi_choice_blank_question_fk" FOREIGN KEY ("question_id") REFERENCES "quiz_fill_in_blank_multi_choice_question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_multi_choice_option"
(
    "id"         serial PRIMARY KEY,
    "blank_id"   int     not null,
    "answer_key" int     not null,
    "content"    varchar not null,
    "sort_index" int
);

ALTER TABLE "quiz_fill_in_blank_multi_choice_option"
    ADD CONSTRAINT "fill_in_blank_multi_choice_blank_fk" FOREIGN KEY ("blank_id") REFERENCES "quiz_fill_in_blank_multi_choice_blank" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_drag_and_drop_question"
(
    "id"          int PRIMARY KEY
);

ALTER TABLE "quiz_fill_in_blank_drag_and_drop_question"
    ADD CONSTRAINT "fill_in_blank_drag_and_drop_question_base_question_id" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_drag_and_drop_answer"
(
    "id"          serial PRIMARY KEY,
    "question_id" int not null,
    "content"     varchar,
    "sort_index"  int,
    "key"         int
);

ALTER TABLE "quiz_fill_in_blank_drag_and_drop_answer"
    ADD CONSTRAINT "fill_in_blank_drag_and_drop_answer_question_fk" FOREIGN KEY ("question_id") REFERENCES "quiz_fill_in_blank_drag_and_drop_question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_fill_in_blank_drag_and_drop_blank"
(
    "id"          serial PRIMARY KEY,
    "question_id" int not null,
    "hint"        varchar,
    "sort_index"  int,
    "answer_key"  int
);

ALTER TABLE "quiz_fill_in_blank_drag_and_drop_blank"
    ADD CONSTRAINT "fill_in_blank_drag_and_drop_blank_question_fk" FOREIGN KEY ("question_id") REFERENCES "quiz_fill_in_blank_drag_and_drop_question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_group_question"
(
    "id"          int PRIMARY KEY
);

ALTER TABLE "quiz_group_question"
    ADD CONSTRAINT "group_question_id" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE "group_question_has_question"
(
    "id"         int PRIMARY KEY,
    "group_id"   int,
    "sort_index" int
);

ALTER TABLE "group_question_has_question"
    ADD CONSTRAINT "fk_group_question" FOREIGN KEY ("group_id") REFERENCES "quiz_group_question" ("id")
        ON DELETE CASCADE;

ALTER TABLE "group_question_has_question"
    ADD CONSTRAINT "fk_question_in_group" FOREIGN KEY ("id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE quiz_config
(
    "id"                          int PRIMARY KEY,
    "started_at"                  timestamp   NOT NULL,
    "valid_before"                timestamp,
    "have_time_limit"             boolean,
    "time_limit"                  int,
    "max_attempt"                 int,
    "view_previous_sessions"      boolean,
    "view_previous_sessions_time" timestamp,
    "pass_score"                  real,
    "create_at"                   timestamp   NOT NULL,
    "created_by"                  varchar(50) NOT NULL,
    "updated_at"                  timestamp   NOT NULL,
    "updated_by"                  varchar(50) NOT NULL
);

ALTER TABLE quiz_config
    ADD CONSTRAINT "quiz_config_id_fk" FOREIGN KEY ("id") REFERENCES "quiz_class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_session"
(
    "id"           uuid PRIMARY KEY,
    "quiz_id"      int       NOT NULL,
    "started_at"   timestamp NOT NULL,
    "submitted_at" timestamp,
    "user_id"      int       NOT NULL,
    "last_session" boolean   NOT NULL
);

ALTER TABLE "quiz_session"
    ADD CONSTRAINT "quiz_session_started_time_unique" UNIQUE ("started_at");

ALTER TABLE "quiz_session"
    ADD CONSTRAINT "quiz_session_quiz_id_fk" FOREIGN KEY ("quiz_id") REFERENCES "quiz_class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_session_result"
(
    "id"            uuid PRIMARY KEY,
    "score"         real,
    "graded_state"  varchar(20) NOT NULL,
    "final_verdict" varchar(20)
);

ALTER TABLE "quiz_session_result"
    ADD CONSTRAINT "quiz_session_result_graded_state_check" CHECK ( "graded_state" IN ('DONE', 'WAITING'));

ALTER TABLE "quiz_session_result"
    ADD CONSTRAINT "quiz_session_result_id_fk" FOREIGN KEY ("id") REFERENCES "quiz_session" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_session_result_question"
(
    "id"                serial PRIMARY KEY,
    "session_result_id" uuid NOT NULL,
    "question_id"       int  NOT NULL,
    "answer_id"         int,
    "earned_point"      real
);

ALTER TABLE "quiz_session_result_question"
    ADD CONSTRAINT "quiz_session_result_question_session_result_id_fk" FOREIGN KEY ("session_result_id") REFERENCES quiz_session_result ("id")
        ON DELETE CASCADE;

ALTER TABLE "quiz_session_result_question"
    ADD CONSTRAINT "quiz_session_result_question_question_fk" FOREIGN KEY ("question_id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE quiz_answer_question_temporary
(
    "id"                    serial PRIMARY KEY,
    "session_id"            uuid NOT NULL,
    "question_id"           int  NOT NULL,
    "optional_student_note" text,
    "attempts"              int
);

ALTER TABLE "quiz_session_result_question"
    ADD CONSTRAINT "quiz_session_result_question_answer_fk" FOREIGN KEY ("answer_id") REFERENCES quiz_answer_question_temporary ("id")
        ON DELETE CASCADE;

ALTER TABLE quiz_answer_question_temporary
    ADD CONSTRAINT "answer_question_temporary_session_fk" FOREIGN KEY ("session_id") REFERENCES "quiz_session" ("id")
        ON DELETE CASCADE;

ALTER TABLE quiz_answer_question_temporary
    ADD CONSTRAINT "answer_temporary_question_fk" FOREIGN KEY ("question_id") REFERENCES "question" ("id")
        ON DELETE CASCADE;

CREATE TABLE quiz_answer_temporary
(
    "id"                 serial PRIMARY KEY,
    "value"              text,
    "is_correct"         boolean,
    "sort_order"         int NOT NULL,
    "answer_question_id" int NOT NULL
);

ALTER TABLE quiz_answer_temporary
    ADD CONSTRAINT "answer_temporary_answer_fk" FOREIGN KEY ("answer_question_id") REFERENCES quiz_answer_question_temporary ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_session_flag"
(
    "session_id"  uuid,
    "question_id" int
);

ALTER TABLE "quiz_session_flag"
    ADD CONSTRAINT "quiz_session_flag_pk" PRIMARY KEY ("session_id", "question_id");

CREATE TABLE "quiz_result"
(
    "id"                       int PRIMARY KEY,
    "number_of_participants"   int              NOT NULL,
    "gpa"                      double precision NOT NULL,
    "number_of_passed_student" int
);

ALTER TABLE "quiz_result"
    ADD CONSTRAINT "quiz_result_quiz_fk" FOREIGN KEY ("id") REFERENCES "quiz_class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "quiz_result_student"
(
    "id"                serial PRIMARY KEY,
    "exam_result_id"    int              NOT NULL,
    "grade"             double precision NOT NULL,
    "result"            varchar(20)      NOT NULL,
    "number_of_attempt" int
);

ALTER TABLE "quiz_result_student"
    ADD CONSTRAINT "quiz_result_student_exam_fk" FOREIGN KEY ("exam_result_id") REFERENCES "quiz_result" ("id")
        ON DELETE CASCADE;

ALTER TABLE "exam_textbook"
    ADD CONSTRAINT "exam_textbook_quiz_fk" FOREIGN KEY ("exam_id") REFERENCES "exam" (id)
        ON DELETE CASCADE;

ALTER TABLE "exam_textbook"
    ADD CONSTRAINT "exam_textbook_textbook_fk" FOREIGN KEY ("textbook_id") REFERENCES "textbook" (id)
        ON DELETE CASCADE;