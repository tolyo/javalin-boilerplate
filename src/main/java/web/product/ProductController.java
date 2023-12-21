package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import org.jetbrains.annotations.NotNull;

public class ProductController {

  public static void create(@NotNull Context ctx) {}

  public static void delete(@NotNull Context ctx, @NotNull String s) {}

  public static Context getAll(@NotNull Context ctx) {

    return render(
        ctx,
        ul(
            Db.queryList(Product.class, "SELECT * FROM products").stream()
                .map(p -> li(p.title))
                .toArray(DomContent[]::new)));
  }

  public static void getOne(@NotNull Context ctx, @NotNull String s) {}

  public static void update(@NotNull Context ctx, @NotNull String s) {}
}
