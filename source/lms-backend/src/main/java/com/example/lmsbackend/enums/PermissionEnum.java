package com.example.lmsbackend.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.example.lmsbackend.enums.ResourceType.*;

@Getter
public enum PermissionEnum {

    VIEW_ALL_COURSE(1, COURSE),
    CREATE_COURSE(2, COURSE),
    VIEW_DETAIL_COURSE(3, COURSE),
    UPDATE_COURSE(4, COURSE),
    DELETE_COURSE(5, COURSE),
    VIEW_LEARNING_CONTENT(6, COURSE),
    CREATE_LEARNING_CONTENT(7, COURSE),
    UPDATE_LEARNING_CONTENT(8, COURSE),
    DELETE_LEARNING_CONTENT(9, COURSE),

    VIEW_ALL_STAFF(21, STAFF),
    CREATE_STAFF(22, STAFF),
    VIEW_DETAIL_STAFF(23, STAFF),
    UPDATE_STAFF(24, STAFF),
    DELETE_STAFF(25, STAFF),

    VIEW_LIST_TEST(40, COURSE),
    VIEW_TEST(41, EXAM),
    CREATE_TEST(42, COURSE),
    UPDATE_TEST(43, EXAM),
    DELETE_TEST(44, EXAM),

    VIEW_LIST_CLASS(50, CLASS),
    VIEW_CLASS(51, CLASS),
    CREATE_CLASS(52, COURSE),
    UPDATE_CLASS(53, CLASS),

    VIEW_LIST_QUIZ_CLASS(60, CLASS),
    VIEW_QUIZ_CLASS(61, QUIZ_CLASS),
    CREATE_QUIZ_CLASS(62, CLASS),
    UPDATE_QUIZ_CLASS(63, QUIZ_CLASS),
    DELETE_QUIZ_CLASS(64, QUIZ_CLASS),
    DO_QUIZ(65, QUIZ_CLASS),
    VIEW_QUIZ_RESULT(66, QUIZ_SESSION),
    GRADE_QUIZ(67, QUIZ_CLASS),
    VIEW_QUIZ_RESULT_MANAGEMENT(68, QUIZ_CLASS),

    VIEW_LIST_POST(70, CLASS),
    VIEW_POST(71, POST),
    CREATE_POST(72, CLASS),
    UPDATE_POST(73, POST),
    DELETE_POST(74, POST),
    VIEW_LIST_COMMENT(75, POST),
    COMMENT_POST(76, POST),
    INTERACT_POST(77, POST),
    UPDATE_COMMENT(78, COMMENT),
    DELETE_COMMENT(79, COMMENT),

    VIEW_LIST_UNIT_CLASS(80, CLASS),
    VIEW_UNIT_CLASS(81, UNIT_CLASS),
    CREATE_UNIT_CLASS(82, CLASS),
    UPDATE_UNIT_CLASS(83, UNIT_CLASS),
    DELETE_UNIT_CLASS(84, UNIT_CLASS),

    VIEW_LIST_VOTING(90, CLASS),
    VIEW_VOTING(91, VOTING),
    CREATE_VOTING(92, CLASS),
    UPDATE_VOTING(93, VOTING),
    DELETE_VOTING(94, VOTING),


    VIEW_CLASS_SCHEDULER(100, CLASS),
    UPDATE_CLASS_SCHEDULER(101, CLASS),
    CREATE_CLASS_SESSION(102, CLASS),
    UPDATE_CLASS_SESSION(103, CLASS_SESSION),
    DELETE_CLASS_SESSION(104, CLASS_SESSION),

    CREATE_ANNOUNCEMENT_CENTRAL(120, CENTRAL),
    CREATE_ANNOUNCEMENT_CLASS(121, CLASS),
    VIEW_LIST_ANNOUNCEMENT_CENTRAL(122, CENTRAL),
    VIEW_LIST_ANNOUNCEMENT(123, CLASS),

    VIEW_LIST_STUDENT_IN_CLASS(130, CLASS),
    ADD_STUDENT_TO_CLASS(131, CLASS),


    VIEW_LIST_TEACHER_IN_CLASS(140, CLASS),
    ADD_TEACHER(141, CLASS),
    UPDATE_TEACHER(142, CLASS),
    DELETE_TEACHER(143, CLASS),
    UPDATE_STUDENT(144, STUDENT),
    DELETE_STUDENT(145, STUDENT),
    VIEW_USER_INFO(146, USER),
    DELETE_CLASS(147, CLASS),
    VIEW_DETAIL_STUDENT(148, STUDENT),
    ALLOCATE_ACCOUNT_STAFF(149, STAFF),
    DELETE_ACCOUNT_STAFF(150, STAFF),

    CREATE_ROLE(151, ROLE),
    UPDATE_ROLE(152, ROLE),
    VIEW_LIST_ROLE(153, ROLE),
    VIEW_DETAIL_ROLE(154, ROLE),
    DELETE_ROLE(155, ROLE),
    ADD_STUDENT(156, STUDENT),
    VIEW_LIST_STUDENT(157, STUDENT),
    VIEW_LIST_GRADE_TAG_IN_CLASS(158, CLASS),
    CREATE_GRADE_TAG_IN_CLASS(159, CLASS),
    UPDATE_GRADE_TAG(160, GRADE_TAG),
    DELETE_GRADE_TAG(161, GRADE_TAG),
    VIEW_DETAIL_GRADE_TAG(162, GRADE_TAG),
    VIEW_GRADE_RESULT_IN_CLASS(163, CLASS),
    GRANT_PERMISSION(164, USER),
    CALCULATE_GRADE_TAG(165, CLASS),

    REMOVE_STUDENT_FROM_CLASS(166, CLASS),

    VIEW_TEXTBOOK_CENTRAL(200, CENTRAL),
    ADD_TEXTBOOK_CENTRAL(201, TEXTBOOK),
    UPDATE_TEXTBOOK_CENTRAL(202, TEXTBOOK),
    DELETE_TEXTBOOK_CENTRAL(203, TEXTBOOK),
    VIEW_TEXTBOOK_COURSE(204, COURSE),
    ADD_TEXTBOOK_COURSE(205, COURSE),
    REMOVE_TEXTBOOK_COURSE(206, TEXTBOOK),
    VIEW_TEXTBOOK_CLASS(207, TEXTBOOK),

    VIEW_CLASS_ATTENDANCE(220, CLASS),
    VIEW_SESSION_ATTENDANCE(221, CLASS_SESSION),
    UPDATE_SESSION_ATTENDANCE(222, CLASS_SESSION);


    private Integer id;
    private ResourceType resourceType;

    private static Map<Integer, PermissionEnum> mapEnum = new HashMap<>();

    static {
        for (PermissionEnum permissionEnum : values()) {
            mapEnum.put(permissionEnum.id, permissionEnum);
        }
    }

    PermissionEnum(Integer id, ResourceType resourceType) {
        this.id = id;
        this.resourceType = resourceType;
    }

    public static PermissionEnum fromId(int id) {
        return mapEnum.get(id);
    }
}