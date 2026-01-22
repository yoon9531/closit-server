package UMC_7th.Closit.global.validation.validator;

import UMC_7th.Closit.domain.follow.repository.FollowRepository;
import UMC_7th.Closit.global.validation.annotation.ExistFollow;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowExistValidator implements ConstraintValidator<ExistFollow, Long> {

    private final FollowRepository followRepository;

    @Override
    public void initialize(ExistFollow constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long followId, ConstraintValidatorContext context) {
        if (followId == null) {
            return true; // null은 검증하지 않음
        }

        boolean isValid = followRepository.existsById(followId);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("FOLLOW_NOT_FOUND")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
