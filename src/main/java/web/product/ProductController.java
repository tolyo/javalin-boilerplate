package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ProductController {

  public static void create(@NotNull Context ctx) {}

  public static void delete(@NotNull Context ctx, @NotNull String s) {}

  public static Context getAll(@NotNull Context ctx) {
    List<Product> items = Db.queryList(Product.class, "SELECT * FROM products");
    String[] fields =
        Arrays.stream(Product.class.getDeclaredFields())
            .map(field -> field.getName())
            .toArray(String[]::new);

    return render(
        ctx,
        table(
            thead(Arrays.stream(fields).map(field -> th(field)).toArray(DomContent[]::new)),
            tbody(
                items.stream()
                    .map(
                        item ->
                            tr(
                                Arrays.stream(fields)
                                    .map(
                                        f -> {
                                          try {
                                            return td(
                                                String.valueOf(
                                                    Product.class.getDeclaredField(f).get(item)));
                                          } catch (IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                          } catch (NoSuchFieldException e) {
                                            throw new RuntimeException(e);
                                          }
                                        })
                                    .toArray(DomContent[]::new)))
                    .toArray(DomContent[]::new))));
  }

  public static void getOne(@NotNull Context ctx, @NotNull String s) {}

  public static void update(@NotNull Context ctx, @NotNull String s) {}
}
