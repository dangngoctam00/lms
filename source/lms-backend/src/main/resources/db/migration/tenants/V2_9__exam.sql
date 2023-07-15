INSERT INTO exam (id, title, description, state, course_id, created_at, created_by, updated_at, updated_by)
VALUES (default, 'Kiểm tra kiến thức đầu vào', 'Bài kiểm tra đầu vào để phân loại học sinh', 'PUBLIC', 1,
        '2022-04-07 00:56:19.406009', 'lmslms1', '2022-04-07 00:56:19.406009', 'lmslms1');

INSERT INTO exam (id, title, description, state, course_id, created_at, created_by, updated_at, updated_by)
VALUES (default, 'Kiểm tra ngữ pháp', 'Bài kiểm tra ngữ pháp', 'PUBLIC', 1,
        '2022-04-07 00:56:19.406009', 'lmslms1', '2022-04-07 00:56:19.406009', 'lmslms1');

INSERT INTO question_source (id)
VALUES (default);
INSERT INTO question_source (id)
VALUES (default);
INSERT INTO question_source (id)
VALUES (default);
INSERT INTO question_source (id)
VALUES (default);
INSERT INTO question_source (id)
VALUES (default);
INSERT INTO question_source (id)
VALUES (default);


INSERT INTO exam_question_source (id, exam_id, question_source_id, sort_index)
VALUES (default, 2, 1, 1);
INSERT INTO exam_question_source (id, exam_id, question_source_id, sort_index)
VALUES (default, 2, 2, 2);
INSERT INTO exam_question_source (id, exam_id, question_source_id, sort_index)
VALUES (default, 2, 3, 3);
INSERT INTO exam_question_source (id, exam_id, question_source_id, sort_index)
VALUES (default, 2, 4, 4);
INSERT INTO exam_question_source (id, exam_id, question_source_id, sort_index)
VALUES (default, 2, 5, 5);
INSERT INTO exam_question_source (id, exam_id, question_source_id, sort_index)
VALUES (default, 2, 6, 6);


INSERT INTO question (id, type, point, description, attachment, note)
VALUES (1, 'MULTI_CHOICE', 2, '<p>[Minh;Lan;Can] was the house''s breadwinner</p>', null, 'question_notes');
INSERT INTO question (id, type, point, description, attachment, note)
VALUES (2, 'FILL_IN_BLANK', 2, 'He [] a good father. He [] tries his best to earn enough money to support his family. The [] was built with most of its budget was paid by', null, 'notes');
INSERT INTO question (id, type, point, description, attachment, note)
VALUES (3, 'FILL_IN_BLANK_WITH_CHOICES', 2, 'He [chool;pen;cat;dog] a good father. He [chool;pen;cat;dog] tries his best to earn enough money to support his family. The [chool;pen;cat;dog] was built with most of its budget was paid by [chool;pen;cat;dog]',
        null, 'question_notes');
INSERT INTO question (id, type, point, description, attachment, note)
VALUES (4, 'FILL_IN_BLANK_DRAG_AND_DROP', 2, 'Human [] contains lots of calcium in compare with human []', null, 'question_notes');
INSERT INTO question (id, type, point, description, attachment, note)
VALUES (5, 'WRITING', 2, 'question 1 description', null, 'question_notes');
INSERT INTO question (id, type, point, description, attachment, note)
VALUES (6, 'MULTI_CHOICE', 2, '<p>[Spring;Summer;Autumn;Winter] is the first season of a year</p>', null, 'question_notes');


INSERT INTO quiz_multi_choice_question (id, is_multiple_answer)
VALUES (1, false);
INSERT INTO quiz_multi_choice_question (id, is_multiple_answer)
VALUES (6, false);

INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 1, '<p>Minh</p>', 1, false, 1);
INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 1, '<p>Lan</p>', 2, true, 2);
INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 1, '<p>Can</p>', 3, false, 3);
INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 6, '<p>Spring</p>', 1, false, 1);
INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 6, '<p>Summer</p>', 2, false, 2);
INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 6, '<p>Autumn</p>', 3, true, 3);
INSERT INTO quiz_multi_choice_option (id, question_id, content, answer_key, is_correct, sort_index)
VALUES (default, 6, '<p>Winter</p>', 4, false, 4);

INSERT INTO quiz_fill_in_blank_question (id)
VALUES (2);

INSERT INTO quiz_fill_in_blank_option (id, question_id, expected_answer, match_strategy, sort_index, hint)
VALUES (default, 2, 'always', 'CONTAIN', 2, 'this is hint');
INSERT INTO quiz_fill_in_blank_option (id, question_id, expected_answer, match_strategy, sort_index, hint)
VALUES (default, 2, 'is', 'EXACT', 1, 'this is hint');
INSERT INTO quiz_fill_in_blank_option (id, question_id, expected_answer, match_strategy, sort_index, hint)
VALUES (default, 2, '/([A-Z])\w+', 'REGEX', 3, 'this is hint');

INSERT INTO quiz_fill_in_blank_multi_choice_question (id)
VALUES (3);

INSERT INTO quiz_fill_in_blank_multi_choice_blank (id, question_id, correct_answer_key, hint, sort_index)
VALUES (default, 3, 0, 'this is hint', 1);
INSERT INTO quiz_fill_in_blank_multi_choice_blank (id, question_id, correct_answer_key, hint, sort_index)
VALUES (default, 3, 1, 'this is hint', 2);
INSERT INTO quiz_fill_in_blank_multi_choice_blank (id, question_id, correct_answer_key, hint, sort_index)
VALUES (default, 3, 2, 'this is hint', 3);
INSERT INTO quiz_fill_in_blank_multi_choice_blank (id, question_id, correct_answer_key, hint, sort_index)
VALUES (default, 3, 3, 'this is hint', 4);

INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 1, 0, 'school', 1);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 1, 1, 'pen', 2);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 1, 2, 'cat', 3);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 1, 3, 'dog', 4);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 2, 0, 'school', 1);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 2, 1, 'pen', 2);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 2, 2, 'cat', 3);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 2, 3, 'dog', 4);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 3, 0, 'school', 1);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 3, 1, 'pen', 2);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 3, 2, 'cat', 3);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 3, 3, 'dog', 4);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 4, 0, 'school', 1);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 4, 1, 'pen', 2);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 4, 2, 'cat', 3);
INSERT INTO quiz_fill_in_blank_multi_choice_option (id, blank_id, answer_key, content, sort_index)
VALUES (default, 4, 3, 'dog', 4);

INSERT INTO quiz_fill_in_blank_drag_and_drop_question (id)
VALUES (4);

INSERT INTO quiz_fill_in_blank_drag_and_drop_blank (id, question_id, hint, sort_index, answer_key)
VALUES (default, 4, 'this is h12e2int', 2, 3);
INSERT INTO quiz_fill_in_blank_drag_and_drop_blank (id, question_id, hint, sort_index, answer_key)
VALUES (default, 4, 'this is h12e2int', 1, 1);

INSERT INTO quiz_fill_in_blank_drag_and_drop_answer (id, question_id, content, sort_index, key)
VALUES (default, 4, 'bones', 1, 1);
INSERT INTO quiz_fill_in_blank_drag_and_drop_answer (id, question_id, content, sort_index, key)
VALUES (default, 4, 'skin', 2, 2);
INSERT INTO quiz_fill_in_blank_drag_and_drop_answer (id, question_id, content, sort_index, key)
VALUES (default, 4, 'nail', 3, 3);

INSERT INTO quiz_writing_question (id)
VALUES (5);