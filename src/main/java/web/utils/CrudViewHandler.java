package web.utils;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.*;

import app.db.Db;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface CrudViewHandler<T extends Model, C> extends CrudHandler {
  @Override
  default void create(@NotNull Context ctx) {
    ValidationHelper validator = getValidator(ctx.body());
    String res = Db.create(validator.validate());
    IdReq resBody = new IdReq();
    resBody.id = res;
    ctx.json(resBody).status(201);
  }
  ;

  @Override
  default void delete(@NotNull Context ctx, @NotNull String id) {
    Optional<T> product = Db.findById(getModelClass(), id);
    if (product.isEmpty()) {
      ctx.status(404);
    } else {
      Db.delete("products", id);
      ctx.status(204);
    }
  }

  @Override
  default void getAll(@NotNull Context ctx) {
    List<T> items = Db.queryList(getModelClass());
    List<String> fields = getFieldNames(getModelClass());

    view(
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
                                            StateService.get("products", item.getId()))))))),
            menu(button("Create").attr("onclick", StateService.create("products")))));
  }

  @Override
  void getOne(@NotNull Context ctx, @NotNull String s);

  @Override
  void update(@NotNull Context ctx, @NotNull String s);

  Class<T> getModelClass();

  ValidationHelper getValidator(String body);
}
