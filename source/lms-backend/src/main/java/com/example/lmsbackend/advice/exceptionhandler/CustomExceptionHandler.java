package com.example.lmsbackend.advice.exceptionhandler;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.enums.StatusCode;
import com.example.lmsbackend.exceptions.*;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.exceptions.aclass.*;
import com.example.lmsbackend.exceptions.client.ExpiredShareFileTokenException;
import com.example.lmsbackend.exceptions.client.InvalidShareFileTokenException;
import com.example.lmsbackend.exceptions.course.ChapterNotFoundException;
import com.example.lmsbackend.exceptions.course.CourseCodeAlreadyExistException;
import com.example.lmsbackend.exceptions.course.CourseHasClassOpenedException;
import com.example.lmsbackend.exceptions.course.DragAndDropIsNotAllowedException;
import com.example.lmsbackend.exceptions.exam.*;
import com.example.lmsbackend.exceptions.files.ReadFileHasException;
import com.example.lmsbackend.exceptions.grade_formula.NotEnoughGradeException;
import com.example.lmsbackend.exceptions.grade_formula.NotFoundGradeFormulaException;
import com.example.lmsbackend.exceptions.grade_formula.PrimitiveTagException;
import com.example.lmsbackend.exceptions.notification.AnnouncementNotFoundException;
import com.example.lmsbackend.exceptions.program.ProgramCodeAlreadyExistsException;
import com.example.lmsbackend.exceptions.program.ProgramNotFoundException;
import com.example.lmsbackend.exceptions.resource.TextbookNotFoundException;
import com.example.lmsbackend.exceptions.role.DeleteDefaultRoleException;
import com.example.lmsbackend.exceptions.role.RoleNotFoundException;
import com.example.lmsbackend.exceptions.staff.StaffNotFoundException;
import com.example.lmsbackend.exceptions.student.StudentNotFoundException;
import com.example.lmsbackend.multitenancy.exception.CannotCreateDomainException;
import com.example.lmsbackend.multitenancy.exception.DomainAlreadyExistsException;
import com.example.lmsbackend.multitenancy.exception.VerifyUrlInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {


    @ExceptionHandler(SQLException.class)
    public ResponseEntity<BaseResponse> conflict(SQLException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.SQL);
        var message = NestedExceptionUtils.getMostSpecificCause(e).getMessage();
        response.setMessage(String.format("%s -> %s", message, "Dev should check tenant ID"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<BaseResponse> handleUnauthorizedException(UnauthorizedException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.UNAUTHORIZED);
        if (StringUtils.isNoneBlank(exception.getMessage())) {
            response.setMessage(exception.getMessage());
        }
        log.warn(response.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(value = {RePasswordNotEqualsPasswordException.class})
    public ResponseEntity<BaseResponse> handleRePasswordNotEqualsPasswordException(RePasswordNotEqualsPasswordException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.REPASSWORD_NOT_EQUALS_PASSWORD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UsernameExistedException.class})
    public ResponseEntity<BaseResponse> handleUserAlreadyExistedException(UsernameExistedException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USERNAME_EXISTED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<BaseResponse> handleUserNotFoundException(UserNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {IncorrectPasswordException.class})
    public ResponseEntity<BaseResponse> handleIncorrectPasswordException(IncorrectPasswordException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INCORRECT_PASSWORD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<BaseResponse> handleInvalidRequestData(MethodArgumentNotValidException exception) {
        var message = String.format("Invalid input: %s - %s", exception.getFieldError().getField(), exception.getFieldError().getDefaultMessage());
        log.debug(message);
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INVALID_INPUT);
        if (exception.getFieldError() != null) {
            response.setMessage(message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ProgramNotFoundException.class})
    public ResponseEntity<BaseResponse> handleProgramNotFoundException(ProgramNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.PROGRAM_NOT_FOUND);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ProgramCodeAlreadyExistsException.class})
    public ResponseEntity<BaseResponse> handleProgramNotFoundException(ProgramCodeAlreadyExistsException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.PROGRAM_CODE_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ClassCodeAlreadyExistException.class})
    public ResponseEntity<BaseResponse> handleClassCodeAlreadyExistException(ClassCodeAlreadyExistException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.CLASS_CODE_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {CourseCodeAlreadyExistException.class})
    public ResponseEntity<BaseResponse> handleProgramNotFoundException(CourseCodeAlreadyExistException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.COURSE_CODE_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {RoleNotFoundException.class})
    public ResponseEntity<BaseResponse> handleRoleNotFoundException(RoleNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.ROLE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @ExceptionHandler(value = {ExpiredShareFileTokenException.class})
    public ResponseEntity<BaseResponse> handleExpiredShareFileTokenException(ExpiredShareFileTokenException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXPIRED_SHARE_FILE_TOKEN);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {InvalidShareFileTokenException.class})
    public ResponseEntity<BaseResponse> handleInvalidShareFileTokenException(InvalidShareFileTokenException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INVALID_SHARE_FILE_TOKEN);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {MemberAlreadyInClassException.class})
    public ResponseEntity<BaseResponse> handleMemberIsAlreadyInClassException(MemberAlreadyInClassException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.MEMBER_ALREADY_IN_CLASS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {StaffNotFoundException.class})
    public ResponseEntity<BaseResponse> handleStaffNotFoundException(StaffNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.STAFF_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(value = {AnnouncementNotFoundException.class})
    public ResponseEntity<BaseResponse> handleNotificationNotFoundException(AnnouncementNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.NOTIFICATION_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(value = {ExamNotFoundException.class})
    public ResponseEntity<BaseResponse> handleExamNotFoundException(ExamNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXAM_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(value = {UnsupportedQuestionTypeException.class})
    public ResponseEntity<BaseResponse> handleUnsupportedQuestionTypeException(UnsupportedQuestionTypeException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.UNSUPPORTED_QUESTION_TYPE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {TextbookNotFoundException.class})
    public ResponseEntity<BaseResponse> handleTextbookNotFoundException(TextbookNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.TEXTBOOK_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(value = {QuizSessionExpiredException.class})
    public ResponseEntity<BaseResponse> handleExamSessionExpiredException(QuizSessionExpiredException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXAM_SESSION_EXPIRED);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizHasAlreadyClosedException.class})
    public ResponseEntity<BaseResponse> handleExamInstanceHasAlreadyClosedException(QuizHasAlreadyClosedException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXAM_INSTANCE_HAS_ALREADY_CLOSED);
        response.setMessage(StatusCode.EXAM_INSTANCE_HAS_ALREADY_CLOSED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizHasNotScoredForStudentYet.class})
    public ResponseEntity<BaseResponse> handleExamInstanceHasAlreadyClosedException(QuizHasNotScoredForStudentYet exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXAM_INSTANCE_HAS_ALREADY_CLOSED);
        response.setMessage(StatusCode.EXAM_INSTANCE_HAS_ALREADY_CLOSED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizNotFoundInClassException.class})
    public ResponseEntity<BaseResponse> handleExamInstanceHasAlreadyClosedException(QuizNotFoundInClassException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.QUIZ_NOT_FOUND_IN_CLASS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {FinishedTimeNotValidException.class})
    public ResponseEntity<BaseResponse> handleExamInstanceHasAlreadyClosedException(FinishedTimeNotValidException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.FINISHED_TIME_NOT_VALID);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ClassNotFoundException.class})
    public ResponseEntity<BaseResponse> handleClassNotFoundException(ClassNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.CLASS_NOT_FOUND);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {StudentNotFoundException.class})
    public ResponseEntity<BaseResponse> handleStudentNotFoundException(StudentNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.STUDENT_NOT_FOUND_EXCEPTION);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(value = {EmailUsedException.class})
    public ResponseEntity<BaseResponse> handleEmailUsedException(EmailUsedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EMAIL_USED_EXCEPTION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {DragAndDropIsNotAllowedException.class})
    public ResponseEntity<BaseResponse> handleDragAndDropIsNotAllowedException(DragAndDropIsNotAllowedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DRAG_AND_DROP_IS_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {VotingIsNotAllowedToAddChoiceException.class})
    public ResponseEntity<BaseResponse> handleVotingIsNotAllowedToAddChoiceException(VotingIsNotAllowedToAddChoiceException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.ADD_CHOICE_IS_NOT_ALLOWED);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {VotingNotFoundException.class})
    public ResponseEntity<BaseResponse> handleVotingNotFoundException(VotingNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.VOTING_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {VotingChoiceNotFoundException.class})
    public ResponseEntity<BaseResponse> handleVotingChoiceNotFoundException(VotingChoiceNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.VOTING_CHOICE_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UserIsNotAllowedToUpdateChoiceException.class})
    public ResponseEntity<BaseResponse> handleUserIsNotAllowedToUpdateChoiceException(UserIsNotAllowedToUpdateChoiceException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USER_IS_NOT_ALLOWED_TO_UPDATE_VOTING_CHOICE);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {PostNotFoundException.class})
    public ResponseEntity<BaseResponse> handlePostNotFoundException(PostNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.POST_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ResponseEntity<BaseResponse> handleCommentNotFoundException(CommentNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.COMMENT_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {InvalidDataException.class})
    public ResponseEntity<BaseResponse> handleInvalidDataException(InvalidDataException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INVALID_INPUT);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizIsNotAllowedToPublishException.class})
    public ResponseEntity<BaseResponse> handleQuizIsNotAllowedToPublishException(QuizIsNotAllowedToPublishException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.QUIZ_IS_NOT_ALLOWED_TO_PUBLISH);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UnsupportedContentTypeException.class})
    public ResponseEntity<BaseResponse> handleUnsupportedContentTypeException(UnsupportedContentTypeException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.UNSUPPORTED_CONTENT_TYPE);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ChapterNotFoundException.class})
    public ResponseEntity<BaseResponse> handleChapterNotFoundException(ChapterNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.CHAPTER_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {SessionHasNotEndedException.class})
    public ResponseEntity<BaseResponse> handleSessionHasNotEndedException(SessionHasNotEndedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.SESSION_HAS_NOT_ENDED);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {StudentHasAlreadyInClassException.class})
    public ResponseEntity<BaseResponse> handleStudentHasAlreadyInCourseException(StudentHasAlreadyInClassException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.STUDENT_HAS_ALREADY_IN_CLASS);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {TeacherHasAlreadyInClassException.class})
    public ResponseEntity<BaseResponse> handleTeacherHasAlreadyInClassException(TeacherHasAlreadyInClassException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.TEACHER_HAS_ALREADY_IN_CLASS);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ScoringManuallyNotSupportedForQuestionTypeException.class})
    public ResponseEntity<BaseResponse> handleScoringManuallyNotSupportedForQuestionTypeException(ScoringManuallyNotSupportedForQuestionTypeException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.SCORING_MANUALLY_NOT_SUPPORTED);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {DomainAlreadyExistsException.class})
    public ResponseEntity<BaseResponse> handleDomainAlreadyExistsException(DomainAlreadyExistsException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DOMAIN_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {CannotCreateDomainException.class})
    public ResponseEntity<BaseResponse> handleCannotCreateDomainException(CannotCreateDomainException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DOMAIN_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {VerifyUrlInvalidException.class})
    public ResponseEntity<BaseResponse> handleVerifyUrlInvalidException(VerifyUrlInvalidException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DOMAIN_ALREADY_EXISTS);
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(value = {com.example.lmsbackend.multitenancy.exception.IncorrectPasswordException.class})
    public ResponseEntity<BaseResponse> handleIncorrectPasswordException(com.example.lmsbackend.multitenancy.exception.IncorrectPasswordException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INCORRECT_PASSWORD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(value = {com.example.lmsbackend.multitenancy.exception.UserNotFoundException.class})
    public ResponseEntity<BaseResponse> handleUserNotFoundException(com.example.lmsbackend.multitenancy.exception.UserNotFoundException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {PrimitiveTagException.class})
    public ResponseEntity<BaseResponse> handlePrimitiveTagException(PrimitiveTagException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.PRIMITIVE_TAG_EXCEPTION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {NotFoundGradeFormulaException.class})
    public ResponseEntity<BaseResponse> handleNotFoundGradeFormulaException(NotFoundGradeFormulaException exception) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.GRADE_FORMULA_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UserIsNotStudentOfClassException.class})
    public ResponseEntity<BaseResponse> handleUserIsNotStudentOfClassException(UserIsNotStudentOfClassException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USER_IS_NOT_STUDENT_OF_CLASS);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ClassSessionNotFoundException.class})
    public ResponseEntity<BaseResponse> handleClassSessionNotFoundException(ClassSessionNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.CLASS_SESSION_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {SummaryAttendanceColumnCannotBeDeletedException.class})
    public ResponseEntity<BaseResponse> handleSummaryAttendanceColumnCannotBeDeletedException(SummaryAttendanceColumnCannotBeDeletedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.SUMMARY_ATTENDANCE_CANNOT_BE_DELETED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {GradeTagNotFoundException.class})
    public ResponseEntity<BaseResponse> handleGradeTagNotFoundException(GradeTagNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.GRADE_TAG_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizSessionNotFoundException.class})
    public ResponseEntity<BaseResponse> handleQuizSessionNotFoundException(QuizSessionNotFoundException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.QUIZ_SESSION_NOT_FOUND);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {TeacherNotFoundInClassException.class})
    public ResponseEntity<BaseResponse> handleTeacherNotFoundInClassException(TeacherNotFoundInClassException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.TEACHER_NOT_FOUND_IN_CLASS);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizSessionHasEndedException.class})
    public ResponseEntity<BaseResponse> handleQuizSessionHasEndedException(QuizSessionHasEndedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.QUIZ_SESSION_ENDED);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {QuizAttemptHasReachLimitException.class})
    public ResponseEntity<BaseResponse> handleQuizAttemptHasReachLimitException(QuizAttemptHasReachLimitException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.QUIZ_ATTEMPT_REACH_LIMIT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {DeleteDefaultRoleException.class})
    public ResponseEntity<BaseResponse> handleDeleteDefaultRoleException(DeleteDefaultRoleException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.DELETE_DEFAULT_ROLE_EXCEPTION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(value = {UsernameOrPasswordIncorrectException.class})
    public ResponseEntity<BaseResponse> handleUsernameOrPasswordIncorrectException(UsernameOrPasswordIncorrectException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.USERNAME_OR_PASSWORD_INCORRECT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {CourseHasClassOpenedException.class})
    public ResponseEntity<BaseResponse> handleCourseHasClassOpenedException(CourseHasClassOpenedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.COURSE_HAS_CLASS_OPENED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ExamIsBeingUsedException.class})
    public ResponseEntity<BaseResponse> handleUExamIsBeingUsedException(ExamIsBeingUsedException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXAM_BEING_USED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ExistingUnEndedQuizSessionException.class})
    public ResponseEntity<BaseResponse> handleUExistingUnEndedQuizSessionException(ExistingUnEndedQuizSessionException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.EXISTING_UNENDED_QUIZ_SESSION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UnableConfigureQuizException.class})
    public ResponseEntity<BaseResponse> handleUnableConfigureQuizException(UnableConfigureQuizException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.UNABLE_CONFIGURE_QUIZ);
        if (StringUtils.isNotBlank(e.getMessage())) {
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {PointInvalidException.class})
    public ResponseEntity<BaseResponse> handlePointInvalidException(PointInvalidException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.POINT_INVALID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {GradeTagIsPrimitiveException.class})
    public ResponseEntity<BaseResponse> handleGradeTagIsPrimitiveException(GradeTagIsPrimitiveException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INVALID_ACTION_WITH_PRIMITIVE_GRADE_TAG);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ReadFileHasException.class})
    public ResponseEntity<BaseResponse> handleReadFileHasException(ReadFileHasException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.FIlE_HAS_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {NotEnoughGradeException.class})
    public ResponseEntity<BaseResponse> handleNotEnoughGradeException(NotEnoughGradeException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.NOT_ENOUGH_GRADE_EXCEPTION);
        response.setMessage("Các cột điểm ".concat(String.join(", ", e.getGradeTagNames())).concat(" vẫn chưa có điểm nên không thể tính điểm được."));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {UpdateQuizException.class})
    public ResponseEntity<BaseResponse> handleUpdateQuizException(UpdateQuizException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.UPDATE_QUIZ_EXCEPTION);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(value = {ChangeAttendanceInvalidException.class})
    public ResponseEntity<BaseResponse> handleChangeAttendanceInvalidException(ChangeAttendanceInvalidException e) {
        BaseResponse response = new BaseResponse();
        response.setStatusCode(StatusCode.INVALID_CHANGE_ATTENDANCE);
        response.setMessage(StatusCode.INVALID_CHANGE_ATTENDANCE.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
