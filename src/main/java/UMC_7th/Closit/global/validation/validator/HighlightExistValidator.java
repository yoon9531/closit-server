package UMC_7th.Closit.global.validation.validator;

import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.global.validation.annotation.ExistHighlight;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HighlightExistValidator implements ConstraintValidator<ExistHighlight, Long> {

    private final HighlightRepository highlightRepository;

    @Override
    public void initialize(ExistHighlight constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long highlightId, ConstraintValidatorContext context) {
        if (highlightId == null) {
            return true; // null은 검증하지 않음
        }

        boolean isValid = highlightRepository.existsById(highlightId);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("HIGHLIGHT_NOT_FOUND")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
