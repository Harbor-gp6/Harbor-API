package gp6.harbor.harborapi.dto.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class OnlyOneValidator implements ConstraintValidator<OnlyOne, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(OnlyOne constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field firstField = value.getClass().getDeclaredField(firstFieldName);
            Field secondField = value.getClass().getDeclaredField(secondFieldName);

            firstField.setAccessible(true);
            secondField.setAccessible(true);

            Object firstValue = firstField.get(value);
            Object secondValue = secondField.get(value);

            boolean isFirstFieldSet = firstValue != null;
            boolean isSecondFieldSet = secondValue != null;

            return isFirstFieldSet ^ isSecondFieldSet; // XOR: Only one of them should be set

        } catch (Exception e) {
            // Handle potential reflection exceptions here
            return false;
        }
    }
}
