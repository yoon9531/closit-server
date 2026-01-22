package UMC_7th.Closit.global.validation.validator;

import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.global.validation.annotation.ExistPost;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostExistValidator implements ConstraintValidator<ExistPost, Long> {

    private final PostRepository postRepository;

    @Override
    public void initialize(ExistPost constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long postId, ConstraintValidatorContext context) {
        if (postId == null) {
            return true; // null은 검증하지 않음
        }

        boolean isValid = postRepository.existsById(postId);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("POST_NOT_FOUND")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
