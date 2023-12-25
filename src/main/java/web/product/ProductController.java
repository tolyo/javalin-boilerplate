package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import web.utils.StateService;

public class ProductController {

  public static void create(@NotNull Context ctx) {}

  public static void delete(@NotNull Context ctx, @NotNull String s) {}

  public static Context getAll(@NotNull Context ctx) {
    List<Product> items = Db.queryList(Product.class, "SELECT * FROM products");
    List<String> fields =
        Arrays.stream(Product.class.getDeclaredFields())
            .map(field -> field.getName())
            .collect(Collectors.toList());

    return render(
        ctx,
        table(
            thead(
                Stream.concat(fields.stream().map(field -> th(field)), Stream.of(th()))
                    .toArray(DomContent[]::new)),
            tbody(
                items.stream()
                    .map(
                        item ->
                            tr(
                                Stream.concat(
                                        fields.stream()
                                            .map(
                                                f -> {
                                                  try {
                                                    return td(
                                                        String.valueOf(
                                                            Product.class
                                                                .getDeclaredField(f)
                                                                .get(item)));
                                                  } catch (IllegalAccessException e) {
                                                    throw new RuntimeException(e);
                                                  } catch (NoSuchFieldException e) {
                                                    throw new RuntimeException(e);
                                                  }
                                                }),
                                        Stream.of(
                                            td(
                                                a("View")
                                                    .attr(
                                                        "onclick",
                                                        StateService.get(
                                                            "products", item.id.toString())))))
                                    .toArray(DomContent[]::new)))
                    .toArray(DomContent[]::new))));
  }

  public static void getOne(@NotNull Context ctx, @NotNull String s) {}

  public static void update(@NotNull Context ctx, @NotNull String s) {}
}
