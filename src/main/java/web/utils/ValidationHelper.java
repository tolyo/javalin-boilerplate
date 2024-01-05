package web.utils;

import static web.utils.ViewHelpers.getFieldValue;

import io.javalin.json.JavalinJackson;
import io.javalin.validation.BodyValidator;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

public abstract class ValidationHelper<T> extends BodyValidator {

  public ValidationHelper(String body, Class<T> clazz) {
    super(body, clazz, new JavalinJackson());
  }

  public static String NULL_NOT_EMPTY_MESSAGE = "Cannot be empty";

  @NotNull
  public static Function1<Object, Boolean> notNullOrEmpty(@NotNull String field) {
    return it -> getFieldValue(it, field) != null && getFieldValue(it, field) != "";
  }

  public abstract T validate();
}
