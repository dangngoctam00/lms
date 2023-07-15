CREATE TABLE "post"
(
    "id"         serial PRIMARY KEY,
    "created_by" int          NOT NULL,
    "title"      varchar(100) NOT NULL,
    "content"    text         NOT NULL,

    "created_at" timestamp    NOT NULL,
    "updated_at" timestamp    NOT NULL,

    "class_id"   int          NOT NULL
);

ALTER TABLE "post"
    ADD CONSTRAINT "post_created_by_fk" FOREIGN KEY ("created_by") REFERENCES "users" ("id");

ALTER TABLE "post"
    ADD CONSTRAINT "post_class_id_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "comment"
(
    "id"         serial PRIMARY KEY,
    "content"    text      NOT NULL,
    "post_id"    int       NOT NULL,
    "parent_id"  int,
    "created_by" int       NOT NULL,
    "created_at" timestamp NOT NULL,
    "updated_at" timestamp NOT NULL
);

ALTER TABLE "comment"
    ADD CONSTRAINT "comment_parent_fk" FOREIGN KEY ("parent_id") REFERENCES "comment" ("id")
        ON DELETE CASCADE;

ALTER TABLE "comment"
    ADD CONSTRAINT "comment_created_by_fk" FOREIGN KEY ("created_by") REFERENCES "users" ("id")
        ON DELETE CASCADE;

ALTER TABLE "comment"
    ADD CONSTRAINT "comment_post_id_fk" FOREIGN KEY ("post_id") REFERENCES "post" ("id")
        ON DELETE CASCADE;

CREATE TABLE "post_interaction"
(
    "user_id" int,
    "post_id" int,
    "type"    varchar(20)
);

ALTER TABLE "post_interaction"
    ADD CONSTRAINT "post_interaction_pk" PRIMARY KEY ("user_id", "post_id");

ALTER TABLE "post_interaction"
    ADD CONSTRAINT "post_interaction_post_id_fk" FOREIGN KEY ("post_id") REFERENCES "post" ("id")
        ON DELETE CASCADE;

ALTER TABLE "post_interaction"
    ADD CONSTRAINT "post_interaction_user_id_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "post_interaction"
    ADD CONSTRAINT "post_interaction_type_enum" CHECK ( "type" IN ('UP_VOTE', 'DOWN_VOTE'));