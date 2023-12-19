package web.product;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class ProductController implements CrudHandler {
  @Override
  public void create(@NotNull Context context) {}

  @Override
  public void delete(@NotNull Context context, @NotNull String s) {}

  @Override
  public void getAll(@NotNull Context context) {
    //        List<Product> productList = Db.list("products");

  }

  @Override
  public void getOne(@NotNull Context context, @NotNull String s) {}

  @Override
  public void update(@NotNull Context context, @NotNull String s) {}
}
