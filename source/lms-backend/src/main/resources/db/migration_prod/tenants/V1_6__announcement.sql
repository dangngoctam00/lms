CREATE TABLE "announcement"
(
    "id"                    serial PRIMARY KEY,
    "sender_id"             int     NOT NULL,
    "subject"               varchar(200),
    "content"               text,
    "attachment"            text,
    "scope"                 varchar(30),
    "scope_id"              int     NOT NULL,
    "sent_at"               timestamp,
    "to_all_members"        boolean NOT NULL,
    "is_visible_for_sender" boolean NOT NULL
);

ALTER TABLE announcement
    ADD CONSTRAINT "announcement_sender_fk" FOREIGN KEY ("sender_id") REFERENCES "users" ("id");

CREATE TABLE "announcement_user"
(
    "receiver_id"     int NOT NULL,
    "announcement_id" int NOT NULL,
    "seen"            boolean default FALSE
);

ALTER TABLE "announcement_user"
    ADD CONSTRAINT "announcement_user_pk" PRIMARY KEY ("receiver_id", "announcement_id");

ALTER TABLE "announcement_user"
    ADD CONSTRAINT "announcement_user_user_fk" FOREIGN KEY ("receiver_id") REFERENCES "users" ("id");

CREATE TABLE "tag"
(
    "id"   serial PRIMARY KEY,
    "name" varchar(30)
);

ALTER TABLE "tag"
    ADD CONSTRAINT "tag_unique" UNIQUE ("name");

CREATE TABLE "announcement_tag"
(
    "tag_id"          int,
    "announcement_id" int
);

ALTER TABLE "announcement_tag"
    ADD CONSTRAINT "announcement_tag_pk" PRIMARY KEY ("tag_id", announcement_id);

ALTER TABLE "announcement_tag"
    ADD CONSTRAINT "announcement_tag_tag_pk" FOREIGN KEY ("tag_id") REFERENCES "tag" ("id");

ALTER TABLE "announcement_tag"
    ADD CONSTRAINT "announcement_tag_announcement_pk" FOREIGN KEY (announcement_id) REFERENCES "announcement" ("id");