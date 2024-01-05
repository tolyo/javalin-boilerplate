package web.utils;

import static j2html.TagCreator.*;
import static web.product.ProductController.createForm;
import static web.utils.ViewHelpers.*;

import app.db.Db;
import app.models.Product;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.eclipse.jetty.http.HttpMethod;
import org.jetbrains.annotations.NotNull;
import web.layout.Layout;

public interface CrudViewHandler<T extends Model> extends CrudHandler {
  @Override
  default void create(@NotNull Context ctx) {
    String res = Db.create(getCreateValidator(ctx.body()).validate());
    IdReq resBody = new IdReq();
    resBody.id = res;
    ctx.json(resBody).status(201);
  }

  @Override
  default void delete(@NotNull Context ctx, @NotNull String id) {
    Optional<T> product = Db.findById(getModelClass(), new BigInteger(id));
    if (product.isEmpty()) {
      ctx.status(404);
    } else {
      Db.delete(Db.getTableName(getModelClass()), id);
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
                                            StateService.get(getName(), item.getId()))))))),
            menu(button("Create").attr("onclick", StateService.create(getName())))));
  }

  @Override
  default void getOne(@NotNull Context ctx, @NotNull String id) {
    Optional<T> item = Db.findById(getModelClass(), new BigInteger(id));
    if (item.isEmpty()) {
      view(ctx, div("Not found"));
    } else {
      view(
          ctx,
          main(
              table(
                  each(
                      getFieldNames(Product.class),
                      f -> tr(th(f), td(getFieldValue(item.get(), f).toString())))),
              menu(
                  a("Edit").attr("onclick", StateService.edit(getPath(), item.get().getId())),
                  form()
                      .attr("data-action", "/" + getName() + "/" + item.get().getId())
                      .attr("data-method", HttpMethod.DELETE)
                      .attr("data-success", StateService.list(getName()))
                      .with(button("Delete").withClass("secondary")))));
    }
  }
  ;

  @Override
  default void update(@NotNull Context ctx, @NotNull String id) {
    Db.update(id, getUpdateValidator(ctx.body()).validate());
    ctx.json("").status(204);
  }
  ;

  default void newForm(@NotNull Context ctx) {
    view(
        ctx,
        main(
            createForm(
                Optional.empty(),
                HttpMethod.POST,
                "/" + getPath(),
                StateService.created(getPath()))));
  }

  default void updateForm(@NotNull Context ctx, @NotNull String id) {
    Optional item = Db.findById(getModelClass(), new BigInteger(id));
    if (item.isEmpty()) {
      view(ctx, div("Not found"));
    } else {
      view(
          ctx,
          main(
              createForm(
                  item,
                  HttpMethod.PUT,
                  "/" + getPath() + "/update/" + id,
                  StateService.get(getPath(), id))));
    }
  }

  default void get(@NotNull Context ctx) {
    Layout.get(ctx);
  }

  Class<T> getModelClass();

  ValidationHelper getCreateValidator(String body);

  default ValidationHelper getUpdateValidator(String body) {
    return getCreateValidator(body);
  }

  default String getPath() {
    return "/" + getName();
  }
  ;

  default String getName() {
    return Db.getTableName(getModelClass());
  }
}
