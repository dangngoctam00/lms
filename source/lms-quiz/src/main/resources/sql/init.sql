CREATE TABLE "quiz_question"
(
    "id"       serial PRIMARY KEY,
    "context"  varchar(20) NOT NULL,
    "group_id" int,
    "data"     jsob,
    constraint "quiz_question_group_fk" foreign key (group_id) references quiz_question (id)
);