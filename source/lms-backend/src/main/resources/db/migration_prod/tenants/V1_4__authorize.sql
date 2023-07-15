CREATE TABLE "permission"
(
    "id"                    int PRIMARY KEY,
    "title"                 varchar,
    "code"                  varchar,
    "description"           varchar,
    "has_limit_by_branch"   boolean,
    "has_limit_by_teaching" boolean,
    "has_limit_by_dean"     boolean,
    "has_limit_by_manager"  boolean,
    "has_limit_by_learn"    boolean,
    "has_limit_by_owner"    boolean
);

CREATE TABLE "role"
(
    "id"          SERIAL PRIMARY KEY,
    "description" varchar,
    "title"       varchar
);

CREATE TABLE "user_has_permission"
(
    "user_id"              int,
    "permission_id"        int,
    "is_limit_by_branch"   int,
    "is_limit_by_teaching" int,
    "is_limit_by_dean"     int,
    "is_limit_by_manager"  int,
    "is_limit_by_learn"    int,
    "is_limit_by_owner"    int,
    "valid_from"           timestamp,
    "expires_at"           timestamp,
    PRIMARY KEY ("user_id", "permission_id")
);

CREATE TABLE "role_has_permission"
(
    "role_id"              int,
    "permission_id"        int,
    "is_limit_by_branch"   int,
    "is_limit_by_teaching" int,
    "is_limit_by_dean"     int,
    "is_limit_by_manager"  int,
    "is_limit_by_learn"    int,
    "is_limit_by_owner"    int,
    PRIMARY KEY ("role_id", "permission_id")
);

CREATE TABLE "user_has_role"
(
    "user_id"    int,
    "role_id"    int,
    "valid_from" timestamp,
    "expires_at" timestamp,
    PRIMARY KEY ("user_id", "role_id")
);

ALTER TABLE "user_has_permission"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_has_permission"
    ADD FOREIGN KEY ("permission_id") REFERENCES "permission" ("id");

ALTER TABLE "role_has_permission"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id");

ALTER TABLE "role_has_permission"
    ADD FOREIGN KEY ("permission_id") REFERENCES "permission" ("id");

ALTER TABLE "user_has_role"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_has_role"
    ADD FOREIGN KEY ("role_id") REFERENCES "role" ("id");