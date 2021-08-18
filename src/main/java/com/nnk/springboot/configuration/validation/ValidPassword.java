package com.nnk.springboot.configuration.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Your password must have at least 1 uppercase letter, 1 number, 1 special character, 8 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
