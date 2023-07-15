CREATE TABLE "voting"
(
    "id"                       serial PRIMARY KEY,
    "title"                    text        NOT NULL,
    "description"              text,
    "is_allowed_to_add_choice" boolean,
    "class_id"                 int         NOT NULL,
    "create_at"                timestamp   NOT NULL,
    "created_by"               varchar(50) NOT NULL,
    "updated_at"               timestamp   NOT NULL,
    "updated_by"               varchar(50) NOT NULL
);

ALTER TABLE "voting"
    ADD CONSTRAINT "voting_class_fk" FOREIGN KEY ("class_id") REFERENCES "class" ("id")
        ON DELETE CASCADE;

CREATE TABLE "voting_choice"
(
    "id"         serial PRIMARY KEY,
    "content"    text        NOT NULL,
    "voting_id"  int         NOT NULL,
    "created_by" varchar(50) NOT NULL,
    "create_at"  timestamp   NOT NULL,
    "updated_at" timestamp   NOT NULL,
    "updated_by" varchar(50) NOT NULL
);

ALTER TABLE "voting_choice"
    ADD CONSTRAINT "voting_choice_voting_fk" FOREIGN KEY ("voting_id") REFERENCES "voting" ("id")
        ON DELETE CASCADE;

CREATE TABLE "user_voting_choice"
(
    "user_id"          int,
    "voting_choice_id" int
);

ALTER TABLE "user_voting_choice"
    ADD CONSTRAINT "user_voting_choice_pk" PRIMARY KEY ("user_id", "voting_choice_id");

ALTER TABLE "user_voting_choice"
    ADD CONSTRAINT "user_voting_choice_user_fk" FOREIGN KEY ("user_id") REFERENCES "users" ("id")
        ON DELETE CASCADE;

ALTER TABLE "user_voting_choice"
    ADD CONSTRAINT "user_voting_choice_choice_fk" FOREIGN KEY ("voting_choice_id") REFERENCES "voting_choice" ("id")
        ON DELETE CASCADE;