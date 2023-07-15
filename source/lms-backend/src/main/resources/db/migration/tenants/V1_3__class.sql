CREATE TABLE "class"
(
    "id"           serial PRIMARY KEY,
    "name"         varchar(50) NOT NULL,
    "code"         varchar(20) NOT NULL,
    "days_of_week" text,
    "avatar"       text,
    "started_at"   date,
    "ended_at"     date,
    "status"       varchar(20),
    "type"         varchar(20) NOT NULL,
    "course_id"    int         NOT NULL,
    "created_at"   timestamp   NOT NULL,
    "updated_at"   timestamp
);

ALTER TABLE "class"
    ADD CONSTRAINT "class_type" CHECK ( type IN ('ONLINE', 'OFFLINE', 'HYBRID') );

ALTER TABLE "class"
    ADD CONSTRAINT "status" CHECK ( status IN ('CREATED', 'ONGOING', 'ENDED') );

ALTER TABLE "class"
    ADD CONSTRAINT "class_unique_code" UNIQUE ("code");

ALTER TABLE "class"
    ADD CONSTRAINT "class_course_id_fk" FOREIGN KEY ("course_id") REFERENCES "course" ("id");

CREATE TABLE "class_learning_content"
(
    "id" int PRIMARY KEY
);

ALTER TABLE "class_learning_content"
    ADD CONSTRAINT "class_learning_content_fk" FOREIGN KEY ("id") REFERENCES "class" (id)
        ON DELETE CASCADE;

CREATE TABLE "chapter_class"
(
    "id"                  serial PRIMARY KEY,
    "title"               varchar(100),
    "chapter_course_id"   int,
    "learning_content_id" int NOT NULL,
    "sort_index"          int NOT NULL
);

ALTER TABLE "chapter_class"
    ADD CONSTRAINT "chapter_class_course_fk" FOREIGN KEY ("chapter_course_id") REFERENCES "chapter_course" ("id")
        ON DELETE SET NULL;

ALTER TABLE "chapter_class"
    ADD FOREIGN KEY ("learning_content_id") REFERENCES "class_learning_content" ("id");

CREATE TABLE "unit_class"
(
    "id"             serial PRIMARY KEY,
    "title"          varchar(100),
    "state"          varchar(20) NOT NULL,
    "unit_course_id" int,
    "class_id"       int         NOT NULL,
    "content"        text,
    "attachment"     text
);

ALTER TABLE "unit_class"
    ADD CONSTRAINT "unit_class_course_fk" FOREIGN KEY ("unit_course_id") REFERENCES "unit_course" ("id")
        ON DELETE SET NULL;

ALTER TABLE "unit_class"
    ADD CONSTRAINT "unit_class_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "unit_class_text_book"
(
    "unit_id"     int not null,
    "textbook_id" int not null,
    "note"        varchar(100)
);

ALTER TABLE "unit_class_text_book"
    ADD CONSTRAINT "unit_class_text_book_pk" PRIMARY KEY ("unit_id", "textbook_id");

ALTER TABLE "unit_class_text_book"
    ADD CONSTRAINT "unit_class_fk" FOREIGN KEY ("unit_id") REFERENCES "unit_class" (id)
        ON DELETE CASCADE;

ALTER TABLE "unit_class_text_book"
    ADD CONSTRAINT "textbook_class_fk" FOREIGN KEY ("textbook_id") REFERENCES "textbook" (id)
        ON DELETE CASCADE;

CREATE TABLE "quiz_class"
(
    "id"             serial PRIMARY KEY,
    "title"          varchar(100) NOT NULL,
    "description"    text,
    "tag_id"         int,
    "exam_id"        int,
    "quiz_course_id" int,
    "class_id"       int          NOT NULL,
    "state"          varchar(20)  NOT NULL,
    "create_at"      timestamp    NOT NULL,
    "created_by"     varchar(50)  NOT NULL,
    "updated_at"     timestamp    NOT NULL,
    "updated_by"     varchar(50)  NOT NULL
);

ALTER TABLE "quiz_class"
    ADD CONSTRAINT "quiz_class_course_fk" FOREIGN KEY ("quiz_course_id") REFERENCES "quiz_course" ("id")
        ON DELETE SET NULL;

ALTER TABLE "quiz_class"
    ADD CONSTRAINT "quiz_class_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "chapter_activity_class"
(
    "activity_id"   int,
    "activity_type" varchar(20),
    "chapter_id"    int,
    "sort_index"    int NOT NULL
);

ALTER TABLE "chapter_activity_class"
    ADD CONSTRAINT "chapter_action_index_class_pk" PRIMARY KEY ("activity_id", "activity_type", "chapter_id");

ALTER TABLE "chapter_activity_class"
    ADD CONSTRAINT "chapter_activity_class_chapter_fk" FOREIGN KEY ("chapter_id") REFERENCES "chapter_class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "class_textbook"
(
    "class_id"    int,
    "textbook_id" int
);

ALTER TABLE "class_textbook"
    ADD CONSTRAINT "class_textbook_pk" PRIMARY KEY ("class_id", "textbook_id");

ALTER TABLE "class_textbook"
    ADD CONSTRAINT "class_textbook_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

ALTER TABLE "class_textbook"
    ADD CONSTRAINT "class_textbook_textbook_fk" FOREIGN KEY ("textbook_id") REFERENCES "textbook" ("id")
        ON DELETE CASCADE;