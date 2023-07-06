package lms.mailservice.exception;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class SendEmailError extends RuntimeException {

    private static final long serialVersionUID = -8326224450458053489L;
    private static final String MESSAGE_TEXT =
            "There was an error returned when sending email: ${errorMessage}.";

    private final String errorMessage;

    public SendEmailError(Throwable cause) {
        super(cause);
        this.errorMessage = cause.getMessage();
    }

    @Override
    public String getMessage() {
        return formatMessage();
    }

    public Map<String, String> getPlaceholderValues() {
        return Map.of("errorMessage", errorMessage);
    }

    protected String formatMessage() {
        return StringSubstitutor.replace(SendEmailError.MESSAGE_TEXT, getPlaceholderValues());
    }
}
