CREATE TABLE "grade_tag"
(
    "id"           serial PRIMARY KEY,
    "title"        varchar(50) NOT NULL,
    "scope"        varchar(20) NOT NULL,
    "scope_id"     int         NOT NULL,
    "is_primitive" bool        NOT NULL default true,
    "has_graded"   bool        NOT NULL default false,
    "updated_at"   timestamp,
    "graded_at"    timestamp,
    "is_public"    bool        NOT NULL default true
);

ALTER TABLE "grade_tag"
    ADD CONSTRAINT "score_tag_scope_check" CHECK ( "scope" IN ('COURSE', 'CLASS') );

ALTER TABLE "quiz_course"
    ADD CONSTRAINT "quiz_course_tag_fk" FOREIGN KEY ("tag_id") REFERENCES "grade_tag" ("id");

ALTER TABLE "quiz_class"
    ADD CONSTRAINT "quiz_class_tag_fk" FOREIGN KEY ("tag_id") REFERENCES "grade_tag" ("id");

CREATE TABLE "grade_tag_student"
(
    "tag_id"     int,
    "student_id" int,
    "grade"      double precision
);

ALTER TABLE "grade_tag_student"
    ADD CONSTRAINT "grade_tag_student_pk" PRIMARY KEY ("tag_id", "student_id");

ALTER TABLE "grade_tag_student"
    ADD CONSTRAINT "grade_tag_student_tag_fk" FOREIGN KEY ("tag_id") REFERENCES "grade_tag" ("id")
        ON DELETE CASCADE;

ALTER TABLE "grade_tag_student"
    ADD CONSTRAINT "grade_tag_student_student_fk" FOREIGN KEY ("student_id") REFERENCES "student" ("id")
        ON DELETE CASCADE;