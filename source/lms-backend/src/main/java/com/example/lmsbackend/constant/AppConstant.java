package com.example.lmsbackend.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Locale;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstant {
    public static final String API_PREFIX = "/v1";

    public static final String PARAM_DELIMITER = ",";
    public static final String VALUE_DELIMITER = ".";
    public static final String EVENT_DESCRIPTION_DELIMITER = "|";

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    public static final String FETCH_GRAPH = "javax.persistence.fetchgraph";

    public static final Integer TOP = -1;
    public static final Integer BOTTOM = -2;

    public static final Integer ALL = 0;
    public static final Integer STAFF = -1;
    public static final Integer TEACHER = -2;
    public static final Integer STUDENT = -3;

    public static final String ATTENDANCE_TAG = "Điểm danh";

    public static final String CLASS_SESSION_DESCRIPTION_TEMPLATE = "Phòng: %s, \n%s";
    public static final String TEACHER_TEMPLATE = "Giáo viên: %s %s";

    public static final String NEW_SESSIONS_TEMPLATE_FILE = "new_session_created_updated.ftl";
    public static final String REALLOCATE_ACCOUNT_TEMPLATE_FILE = "reallocate_account.ftl";
    public static final String EXTEND_SERVICE_FAIL_NOTI = "extend_service_fail_noti.ftl";
    public static final String ANNOUNCEMENT = "announcement.ftl";

    public static final String DATE_TIME_PATTERN = "EEEE, dd/MM/yyyy HH:mm:ss";

    public static SimpleDateFormat getDateTimeFormatter() {
        return new SimpleDateFormat(DATE_TIME_PATTERN, new Locale("vi", "VN"));
    }

}
