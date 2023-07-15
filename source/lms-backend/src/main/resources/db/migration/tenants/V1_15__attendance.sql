CREATE TABLE "attendance"
(
    "id"          serial PRIMARY KEY,
    "session_id"  int       NOT NULL,
    "is_official" boolean   NOT NULL,
    "created_at"  timestamp NOT NULL,
    "updated_at"  timestamp NOT NULL
);

ALTER TABLE "attendance"
    ADD CONSTRAINT "attendance_session_fk" FOREIGN KEY ("session_id") REFERENCES "class_session" ("id")
        ON DELETE CASCADE;

CREATE TABLE "attendance_student"
(
    "student_id"         int,
    "class_id"           int,
    "attendance_time_id" int,
    "state"              varchar(20) NOT NULL
);

ALTER TABLE "attendance_student"
    ADD CONSTRAINT "attendance_student_pk" PRIMARY KEY ("student_id", "class_id", "attendance_time_id");

ALTER TABLE "attendance_student"
    ADD CONSTRAINT "attendance_student_state_check" CHECK ( "state" IN ('PRESENT', 'ABSENT', 'LATE', 'NONE'));

ALTER TABLE "attendance_student"
    ADD CONSTRAINT "attendance_student_student_fk" FOREIGN KEY ("student_id", "class_id") REFERENCES "class_student" ("student_id", "class_id")
        ON DELETE CASCADE;

ALTER TABLE "attendance_student"
    ADD CONSTRAINT "attendance_student_attendance_fk" FOREIGN KEY ("attendance_time_id") REFERENCES "attendance" ("id")
        ON DELETE CASCADE;