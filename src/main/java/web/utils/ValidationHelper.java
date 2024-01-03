package web.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

public class ValidationHelper {
  public static Validator validator =
      Validation.byDefaultProvider()
          .configure()
          .messageInterpolator(new ParameterMessageInterpolator())
          .buildValidatorFactory()
          .getValidator();
  private Set<ConstraintViolation<Object>> violations;

  public ValidationHelper(Object object) {
    this.violations = validator.validate(object);
  }

  public boolean isValid() {
    return this.violations.size() == 0;
  }

  public Map<String, String> getErrorMap() {
    Map<String, String> errorsMap = new HashMap<>();
    // Populate the Map with field names and error messages
    for (ConstraintViolation<Object> violation : violations) {
      String fieldName = violation.getPropertyPath().toString();
      String errorMessage = violation.getMessage();
      errorsMap.put(fieldName, errorMessage);
    }
    return errorsMap;
  }
}
