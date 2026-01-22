package UMC_7th.Closit.global.validation.validator;

import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.validation.annotation.ExistUser;
import UMC_7th.Closit.global.validation.annotation.ExistUserClositId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserClositIdExistValidator implements ConstraintValidator<ExistUserClositId, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(ExistUserClositId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String userClositId, ConstraintValidatorContext context) {
        if (userClositId == null) {
            return true; // null은 검증하지 않음
        }

        boolean isValid = userRepository.existsByClositId(userClositId);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("USER_NOT_FOUND")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
