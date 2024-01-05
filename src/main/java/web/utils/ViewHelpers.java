package web.utils;

import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ViewHelpers {

  public static Context render(Context ctx, DomContent dc) {
    return ctx.html(dc.render());
  }

  public static void view(Context ctx, DomContent dc) {
    ctx.html(dc.render());
  }

  public static List<String> getFieldNames(Class<?> clazz) {
    ArrayList list = new ArrayList();
    for (Field i : clazz.getDeclaredFields()) {
      list.add(i.getName());
    }
    return list;
  }

  public static Object getFieldValue(Object item, String fieldName) {
    try {
      return Product.class.getDeclaredField(fieldName).get(item);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      return "";
    }
  }
}
