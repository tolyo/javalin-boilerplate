package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import web.utils.StateService;

public class ProductController {

  public static Context newForm(@NotNull Context ctx) {

    return render(
        ctx,
        main(
            form()
                .attr("data-action", "/products")
                .attr("data-success", StateService.created("products"))
                .with(
                    each(
                        filter(getFieldNames(Product.class), f -> !"id".equals(f)),
                        f -> label(f).with(input().withName(f))),
                    button("Submit").withType("submit"))));
  }

  public static Context updateForm(@NotNull Context ctx) throws SQLException {
    String id = ctx.pathParam("id");
    List<String> fields = getFieldNames(Product.class);
    Optional<Product> product =
        Db.queryVal(Product.class, "SELECT * FROM products WHERE id = ?", new BigInteger(id));
    if (product.isEmpty()) {
      return render(ctx, div("Not found"));
    } else {
      return render(
          ctx,
          main(
              form()
                  .attr("data-action", "/products/update/" + id)
                  .attr("data-success", StateService.get("products", id))
                  .with(
                      each(
                          filter(fields, f -> !"id".equals(f)),
                          f ->
                              label(f)
                                  .with(
                                      input()
                                          .withName(f)
                                          .withValue(getFieldValue(product.get(), f).toString()))),
                      button("Submit").withType("submit"))));
    }
  }

  public static Context getAll(@NotNull Context ctx) {
    Class clazz = Product.class;
    List<Product> items = Db.queryList(clazz, "SELECT * FROM products");
    List<String> fields = getFieldNames(clazz);

    return render(
        ctx,
        main(
            table(
                thead(each(fields, f -> th(f)), th()),
                tbody(
                    each(
                        items,
                        item ->
                            tr(
                                each(fields, f -> td(String.valueOf(getFieldValue(item, f)))),
                                td(
                                    a("View")
                                        .attr(
                                            "onclick",
                                            StateService.get("products", item.id.toString()))))))),
            menu(button("Create").attr("onclick", StateService.create("products")))));
  }

  public static Context getOne(@NotNull Context ctx) throws SQLException {
    String id = ctx.pathParam("id");
    Optional<Product> product =
        Db.queryVal(Product.class, "SELECT * FROM products WHERE id = ?", new BigInteger(id));
    if (product.isEmpty()) {
      return render(ctx, div("Not found"));
    } else {
      return render(
          ctx,
          main(
              table(
                  each(
                      getFieldNames(Product.class),
                      f -> tr(th(f), td(getFieldValue(product.get(), f).toString())))),
              menu(
                  a("Edit")
                      .attr("onclick", StateService.edit("products", product.get().id.toString())),
                  form()
                      .attr("data-action", "/products/delete")
                      .attr("data-success", StateService.list("products"))
                      .with(
                          input().isHidden().withName("id").withValue(product.get().id.toString()),
                          button("Delete").withClass("secondary")))));
    }
  }

  public static Context create(@NotNull Context ctx) throws SQLException, IllegalAccessException {
    Product product = ctx.bodyAsClass(Product.class);
    String res = Db.create("products", product);
    IdReq resBody = new IdReq();
    resBody.id = res;
    return ctx.json(resBody).status(201);
  }

  public static Context update(@NotNull Context ctx) throws SQLException, IllegalAccessException {
    String id = ctx.pathParam("id");
    Product product = ctx.bodyAsClass(Product.class);
    Db.update("products", id, product);
    return ctx.json("").status(204);
  }

  public static void delete(@NotNull Context ctx) {
    String id = ctx.bodyAsClass(IdReq.class).id;
    try {
      Db.execute("DELETE FROM products WHERE id = ?", new BigInteger(id));
      ctx.status(204);
    } catch (SQLException e) {
      ctx.status(404);
    }
  }

  private static List<String> getFieldNames(Class<?> clazz) {
    ArrayList list = new ArrayList();
    for (Field i : clazz.getDeclaredFields()) {
      list.add(i.getName());
    }
    return list;
  }

  private static Object getFieldValue(Product item, String fieldName) {
    try {
      return Product.class.getDeclaredField(fieldName).get(item);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      return "";
    }
  }

  private static class IdReq {
    public String id;
  }
}
