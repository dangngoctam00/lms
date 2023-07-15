CREATE TABLE "course"
(
    "id"          serial PRIMARY KEY,
    "name"        varchar(100),
    "code"        varchar(20) NOT NULL,
    "description" text,
    "level"       varchar(50) NOT NULL,
    "price"       int,
    "background"  text,
    "created_at"  timestamp   NOT NULL,
    "created_by"  varchar(50) NOT NULL,
    "updated_at"  timestamp   NOT NULL,
    "updated_by"  varchar(50) NOT NULL
);

CREATE TABLE "course_content"
(
    "id" int PRIMARY KEY
);

ALTER TABLE "course_content"
    ADD CONSTRAINT "course_content_fk" FOREIGN KEY ("id") REFERENCES "course" ("id")
        ON DELETE CASCADE;

CREATE TABLE "chapter_course"
(
    "id"                serial PRIMARY KEY,
    "title"             varchar(100),
    "course_content_id" int not null,
    "sort_index"        int
);

ALTER TABLE "chapter_course"
    ADD FOREIGN KEY ("course_content_id") REFERENCES "course_content" ("id")
        ON DELETE CASCADE;


CREATE TABLE "chapter_activity_course"
(
    "activity_id"   int,
    "activity_type" varchar(20),
    "chapter_id"    int,
    "sort_index"    int NOT NULL
);

ALTER TABLE "chapter_activity_course"
    ADD CONSTRAINT "chapter_activity_course_pk" PRIMARY KEY ("activity_id", "activity_type", "chapter_id");

ALTER TABLE "chapter_activity_course"
    ADD CONSTRAINT "chapter_activity_course_chapter_fk" FOREIGN KEY ("chapter_id") REFERENCES "chapter_course" ("id")
        ON DELETE CASCADE;

CREATE TABLE "unit_course"
(
    "id"         serial PRIMARY KEY,
    "title"      varchar(100),
    "course_id"  int NOT NULL,
    "content"    text,
    "attachment" text
);

ALTER TABLE "unit_course"
    ADD CONSTRAINT "unit_course_course_fk" FOREIGN KEY ("course_id") REFERENCES "course" ("id");

CREATE TABLE "quiz_course"
(
    "id"          serial PRIMARY KEY,
    "title"       varchar(100) NOT NULL,
    "tag_id"      int,
    "exam_id"     int,
    "description" text,
    "course_id"   int          NOT NULL
);

ALTER TABLE "quiz_course"
    ADD CONSTRAINT "quiz_course_course_fk" FOREIGN KEY ("course_id") REFERENCES "course" ("id");

CREATE TABLE "unit_course_text_book"
(
    "unit_id"     int NOT NULL,
    "textbook_id" int NOT NULL,
    "note"        varchar(100)
);

CREATE TABLE "exam_textbook"
(
    "exam_id"     int NOT NULL,
    "textbook_id" int NOT NULL,
    "note"        varchar(100)
);

CREATE TABLE "resource"
(
    "id"   serial PRIMARY KEY,
    "name" varchar(100) NOT NULL
);

CREATE TABLE "textbook"
(
    "id"          int PRIMARY KEY,
    "attachment"  text,
    "author"      varchar(50),
    "description" text
);

ALTER TABLE "textbook"
    ADD CONSTRAINT "id_fk" FOREIGN KEY ("id") REFERENCES "resource" ("id");

ALTER TABLE "unit_course_text_book"
    ADD CONSTRAINT "unit_text_book_pk" PRIMARY KEY ("unit_id", "textbook_id");

ALTER TABLE "unit_course_text_book"
    ADD CONSTRAINT "unit_fk" FOREIGN KEY ("unit_id") REFERENCES "unit_course" (id)
        ON DELETE CASCADE;

ALTER TABLE "unit_course_text_book"
    ADD CONSTRAINT "textbook_fk" FOREIGN KEY ("textbook_id") REFERENCES "textbook" (id)
        ON DELETE CASCADE;

ALTER TABLE "exam_textbook"
    ADD CONSTRAINT "quiz_textbook_pk" PRIMARY KEY ("exam_id", "textbook_id");

CREATE TABLE "chat_room"
(
    "id"              serial PRIMARY KEY,
    "url"             varchar(50),
    "name"            varchar(50),
    "type"            varchar(20) NOT NULL,
    "last_message_id" int
);

ALTER TABLE "chat_room"
    ADD CONSTRAINT type_contraint CHECK ("type" IN ('USER', 'GROUP'));

CREATE TABLE "chat_room_user"
(
    "number_of_unseen_messages" int default 0,
    "chat_room_id"              int NOT NULL,
    "user_id"                   int NOT NULL
);

ALTER TABLE "chat_room_user"
    ADD FOREIGN KEY ("chat_room_id") REFERENCES "chat_room" ("id");
ALTER TABLE "chat_room_user"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

CREATE TABLE "message"
(
    "id"           serial PRIMARY KEY,
    "content"      text      NOT NULL,
    "sender_id"    int       NOT NULL,
    "chat_room_id" int       NOT NULL,
    "created_at"   timestamp NOT NULL
);

ALTER TABLE "message"
    ADD FOREIGN KEY ("sender_id") REFERENCES "users" ("id");
ALTER TABLE "message"
    ADD FOREIGN KEY ("chat_room_id") REFERENCES "chat_room" ("id");

ALTER TABLE "chat_room"
    ADD FOREIGN KEY ("last_message_id") REFERENCES "message" ("id");

CREATE TABLE "program"
(
    "id"           serial PRIMARY KEY,
    "name"         varchar(50) NOT NULL,
    "code"         varchar(20) NOT NULL,
    "description"  text,
    "is_strict"    boolean,
    "is_published" boolean,
    "created_at"   timestamp   NOT NULL,
    "updated_at"   timestamp
);

ALTER TABLE "course"
    ADD CONSTRAINT "course_unique_code" UNIQUE ("code");

ALTER TABLE "program"
    ADD CONSTRAINT "program_unique_code" UNIQUE ("code");

CREATE TABLE "course_program"
(
    "program_id" int NOT NULL,
    "course_id"  int NOT NULL,
    "sort_order" int
);

ALTER TABLE "course_program"
    ADD FOREIGN KEY ("program_id") REFERENCES "program" ("id");
ALTER TABLE "course_program"
    ADD FOREIGN KEY ("course_id") REFERENCES "course" ("id");


CREATE TABLE "course_textbook"
(
    "course_id"   int,
    "textbook_id" int
);

ALTER TABLE "course_textbook"
    ADD CONSTRAINT "course_textbook_pk" PRIMARY KEY ("course_id", "textbook_id");

ALTER TABLE "course_textbook"
    ADD CONSTRAINT "course_textbook_course_fk" FOREIGN KEY ("course_id") REFERENCES "course" ("id")
        ON DELETE CASCADE;

ALTER TABLE "course_textbook"
    ADD CONSTRAINT "course_textbook_textbook_fk" FOREIGN KEY ("textbook_id") REFERENCES "textbook" ("id")
        ON DELETE CASCADE;