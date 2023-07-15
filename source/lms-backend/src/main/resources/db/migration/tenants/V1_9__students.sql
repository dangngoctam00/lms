CREATE TABLE "student"
(
    "id"      int PRIMARY KEY,
    "address" varchar(255)
);

ALTER TABLE "student"
    ADD FOREIGN KEY ("id") REFERENCES "users" ("id");

CREATE TABLE "class_student"
(
    "class_id"   int,
    "student_id" int
);

ALTER TABLE "class_student"
    ADD CONSTRAINT "class_student_pk" PRIMARY KEY ("class_id", "student_id");

ALTER TABLE "class_student"
    ADD CONSTRAINT "class_student_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

ALTER TABLE "class_student"
    ADD CONSTRAINT "class_student_student_fk" FOREIGN KEY ("student_id") REFERENCES "student" ("id")
        ON DELETE CASCADE;
