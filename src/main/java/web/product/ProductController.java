package web.product;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

import app.db.Db;
import app.models.Product;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
                    Stream.concat(
                            getFieldNames(Product.class).stream()
                                .filter(f -> f != "id")
                                .map(f -> label(f).with(input().withName(f))),
                            Stream.of(button("Submit").withType("submit")))
                        .toArray(DomContent[]::new))));
  }

  public static Context updateForm(@NotNull Context ctx) {
    return render(
        ctx,
        main(
            form()
                .attr("data-action", "/products/create")
                .attr("data-success", StateService.created("products"))
                .with()));
  }

  private static class IdReq {
    public String id;
  }

  public static void delete(@NotNull Context ctx) {
    String id = ctx.bodyAsClass(IdReq.class).id;
    try {
      Db.execute("DELETE FROM products WHERE id = ?", new BigInteger(id));
      ctx.status(204);
    } catch (SQLException e) {
      System.out.println(e);
      ctx.status(404);
    }
  }

  public static Context getAll(@NotNull Context ctx) {
    Class clazz = Product.class;
    List<Product> items = Db.queryList(clazz, "SELECT * FROM products");
    List<String> fields = getFieldNames(clazz);

    return render(
        ctx,
        main(
            createProductTable(fields, items),
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
                  getFieldNames(Product.class).stream()
                      .map(f -> tr(th(f), td(getFieldValue(product.get(), f).toString())))
                      .toArray(DomContent[]::new)),
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

  private static List<String> getFieldNames(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .map(field -> field.getName())
        .collect(Collectors.toList());
  }

  private static DomContent createProductTable(List<String> fields, List<Product> items) {
    return table(createTableHeader(fields), createTableBody(fields, items));
  }

  private static DomContent createTableHeader(List<String> fields) {
    return thead(
        Stream.concat(
                fields.stream().map(ProductController::createTableHeaderCell), Stream.of(th()))
            .toArray(DomContent[]::new));
  }

  private static DomContent createTableHeaderCell(String field) {
    return th(field);
  }

  private static DomContent createTableBody(List<String> fields, List<Product> items) {
    return tbody(
        items.stream().map(item -> createTableRow(fields, item)).toArray(DomContent[]::new));
  }

  private static DomContent createTableRow(List<String> fields, Product item) {
    return tr(
        Stream.concat(
                fields.stream().map(f -> createTableCell(String.valueOf(getFieldValue(item, f)))),
                Stream.of(createViewAction(item)))
            .toArray(DomContent[]::new));
  }

  private static DomContent createTableCell(String value) {
    return td(value);
  }

  private static Object getFieldValue(Product item, String fieldName) {
    try {
      return Product.class.getDeclaredField(fieldName).get(item);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private static DomContent createViewAction(Product item) {
    return td(a("View").attr("onclick", StateService.get("products", item.id.toString())));
  }
}
