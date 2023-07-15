CREATE TABLE "grade_formula"
(
    "id"            serial PRIMARY KEY,
    "formula"       varchar NOT NULL,
    expression      varchar not null,
    "tag_result_id" int     not null UNIQUE
);


CREATE TABLE "formula_use_grade_tag"
(
    formula_id int not null,
    tag_id     int not null
);

ALTER TABLE "formula_use_grade_tag"
    ADD CONSTRAINT "formula_use_grade_tag_pk" PRIMARY KEY ("formula_id", "tag_id");

ALTER TABLE "grade_formula"
    ADD CONSTRAINT "tag_result_id_fk" FOREIGN KEY ("tag_result_id") REFERENCES "grade_tag" ("id")
        ON DELETE CASCADE;

ALTER TABLE "formula_use_grade_tag"
    ADD CONSTRAINT "formula_id_fk" FOREIGN KEY ("formula_id") REFERENCES "grade_formula" ("id")
        ON DELETE CASCADE;

ALTER TABLE "formula_use_grade_tag"
    ADD CONSTRAINT "tag_id_fk" FOREIGN KEY ("tag_id") REFERENCES "grade_tag" ("id")
        ON DELETE CASCADE;