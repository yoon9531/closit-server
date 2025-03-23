package UMC_7th.Closit.global.validation.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckBlocked {
    String targetIdParam();
    String message() default "차단된 사용자입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
