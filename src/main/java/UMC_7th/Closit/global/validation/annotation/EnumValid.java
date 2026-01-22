package UMC_7th.Closit.global.validation.annotation;

import UMC_7th.Closit.global.validation.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {

    String message() default "유효하지 않은 값입니다."; // 기본 에러 메시지

    Class<?>[] groups() default {}; // 그룹 설정

    Class<? extends Payload>[] payload() default {}; // 페이로드 설정

    Class<? extends Enum<?>> enumClass(); // Enum 클래스를 지정하기 위한 속성

    boolean ignoreCase() default false; // 대소문자 무시 여부
}