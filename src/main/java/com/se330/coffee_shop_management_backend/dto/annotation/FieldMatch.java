package com.se330.coffee_shop_management_backend.dto.annotation;

import com.se330.coffee_shop_management_backend.dto.validator.FieldMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FieldMatchValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface FieldMatch {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String first();

    String second();

    String message() default "The fields must match";

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}
