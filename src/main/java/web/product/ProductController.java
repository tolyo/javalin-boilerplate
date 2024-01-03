package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import web.utils.StateService;
import web.utils.ValidationHelper;

public class ProductController {

  public static Context newForm(@NotNull Context ctx) {
    return render(
        ctx, main(createForm(Optional.empty(), "/products", StateService.created("products"))));
  }

  public static Context updateForm(@NotNull Context ctx) throws SQLException {
    String id = ctx.pathParam("id");
    Optional<Product> product =
        Db.queryVal(Product.class, "SELECT * FROM products WHERE id = ?", new BigInteger(id));
    if (product.isEmpty()) {
      return render(ctx, div("Not found"));
    } else {
      return render(
          ctx,
          main(createForm(product, "/products/update/" + id, StateService.get("products", id))));
    }
  }

  private static DomContent createForm(
      Optional<Product> product, String dataAction, String dataSuccess) {
    List<String> fields = getFieldNames(Product.class);
    return form()
        .attr("data-action", dataAction)
        .attr("data-success", dataSuccess)
        .with(
            each(
                filter(fields, f -> !"id".equals(f)),
                f ->
                    label(f)
                        .with(
                            product
                                .map(
                                    p ->
                                        input()
                                            .withName(f)
                                            .withValue(getFieldValue(p, f).toString()))
                                .orElse(input().withName(f)))),
            button("Submit").withType("submit"));
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
    ValidationHelper validation = new ValidationHelper(product);
    if (validation.isValid()) {
      String res = Db.create("products", product);
      IdReq resBody = new IdReq();
      resBody.id = res;
      return ctx.json(resBody).status(201);
    } else {
      return ctx.json(validation.getErrorMap()).status(422);
    }
  }

  public static Context update(@NotNull Context ctx) throws SQLException, IllegalAccessException {
    String id = ctx.pathParam("id");
    Product product = ctx.bodyAsClass(Product.class);
    ValidationHelper validation = new ValidationHelper(product);
    if (validation.isValid()) {
      Db.update("products", id, product);
      return ctx.json("").status(204);
    } else {
      return ctx.json(validation.getErrorMap()).status(422);
    }
  }

  public static void delete(@NotNull Context ctx) {
    String id = ctx.bodyAsClass(IdReq.class).id;
    try {
      Db.delete("products", id);
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
