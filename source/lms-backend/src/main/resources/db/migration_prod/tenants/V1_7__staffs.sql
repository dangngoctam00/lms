CREATE TABLE "staff"
(
    "id"          int PRIMARY KEY,
    "description" varchar,
    "manager_id"  bigint
);

ALTER TABLE "staff"
    ADD FOREIGN KEY ("id") REFERENCES "users" ("id");

ALTER TABLE "staff"
    ADD FOREIGN KEY ("manager_id") REFERENCES "staff" ("id");

CREATE TABLE "class_teacher"
(
    "class_id"   int,
    "teacher_id" int,
    "role"       varchar(20) NOT NULL
);

ALTER TABLE "class_teacher"
    ADD CONSTRAINT "class_teacher_pk" PRIMARY KEY ("class_id", "teacher_id");

ALTER TABLE "class_teacher"
    ADD CONSTRAINT "class_teacher_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

ALTER TABLE "class_teacher"
    ADD CONSTRAINT "class_teacher_student_fk" FOREIGN KEY ("teacher_id") REFERENCES "staff" ("id")
        ON DELETE CASCADE;