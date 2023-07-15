CREATE TABLE "calendar"
(
    "id"      serial PRIMARY KEY,
    "type"    varchar(20) NOT NULL,
    "type_id" int         NOT NULL
);

ALTER TABLE "calendar"
    ADD CONSTRAINT "calendar_type_in" CHECK ( type IN ('USER', 'CLASS') );

ALTER TABLE "calendar"
    ADD CONSTRAINT "calendar_type_id_unique" UNIQUE ("type", "type_id");

CREATE TABLE "event"
(
    "event_type"  varchar(50),
    "event_id"    int,
    "summary"     varchar(100) NOT NULL,
    "description" text,
    "started_at"  timestamp    NOT NULL,
    "ended_at"    timestamp,
    "hidden"      boolean,
    "calendar_id" int          NOT NULL
);

ALTER TABLE "event"
    ADD CONSTRAINT "event_pk" PRIMARY KEY ("event_type", "event_id");

ALTER TABLE "event"
    ADD CONSTRAINT "event_type_in" CHECK ( "event_type" IN ('CLASS_SESSION', 'QUIZ') );

ALTER TABLE "event"
    ADD CONSTRAINT "event_calendar_fk" FOREIGN KEY ("calendar_id") REFERENCES "calendar" ("id");

CREATE TABLE "class_session"
(
    "id"           serial PRIMARY KEY,
    "is_scheduled" boolean     NOT NULL,
    "teacher_id"   int,
    "class_id"     int         NOT NULL,
    "unit_id"      int,
    "room"         text,
    "started_at"   timestamp,
    "finished_at"  timestamp,
    "note"         text,
    "strategy"     varchar(20) NOT NULL,
    "created_at"   timestamp   NOT NULL,
    "created_by"   varchar(50) NOT NULL,
    "updated_at"   timestamp   NOT NULL,
    "updated_by"   varchar(50) NOT NULL
);

ALTER TABLE "class_session"
    ADD CONSTRAINT "class_session_teacher_fk" FOREIGN KEY ("teacher_id") REFERENCES "staff" ("id");

ALTER TABLE "class_session"
    ADD CONSTRAINT "class_session_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

ALTER TABLE "class_session"
    ADD CONSTRAINT "class_session_unit_fk" FOREIGN KEY ("unit_id") REFERENCES "quiz_class" ("id");