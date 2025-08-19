package nbcc.utils;

import org.springframework.validation.BindingResult;

public final class Utilities {
    public static void setFieldError(String field, String message, BindingResult bindingResult) {
        bindingResult.rejectValue(field, "", message);
    }
}
