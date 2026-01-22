package UMC_7th.Closit.global.validation.validator;

import UMC_7th.Closit.global.validation.annotation.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;

    @Override
    public void initialize(EnumValid annotation) {
        this.enumClass = annotation.enumClass();
        this.ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false; // null 값이면 검증 실패

        Enum<?>[] enumValues = enumClass.getEnumConstants();
        if (ignoreCase) {
            return Arrays.stream(enumValues)
                    .map(Enum::name)
                    .anyMatch(enumValue -> enumValue.equalsIgnoreCase(value));
        } else {
            return Arrays.stream(enumValues)
                    .map(Enum::name)
                    .anyMatch(enumValue -> enumValue.equals(value));
        }
    }
}