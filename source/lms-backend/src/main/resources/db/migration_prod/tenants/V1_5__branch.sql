CREATE TABLE "branch"
(
    "id"         serial PRIMARY KEY,
    "name"       varchar,
    "address"    varchar,
    "manager_id" int NOT NULL
);

CREATE TABLE "branch_has_user"
(
    "user_id"   int,
    "branch_id" int,
    PRIMARY KEY ("user_id", "branch_id")
);

ALTER TABLE "branch"
    ADD FOREIGN KEY ("manager_id") REFERENCES "users" ("id");

ALTER TABLE "branch_has_user"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "branch_has_user"
    ADD FOREIGN KEY ("branch_id") REFERENCES "branch" ("id");