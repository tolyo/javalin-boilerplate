package web.utils;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.*;

import app.db.Db;
import app.models.Product;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.eclipse.jetty.http.HttpMethod;
import org.jetbrains.annotations.NotNull;
import web.Routes;
import web.layout.Layout;

public abstract class CrudViewHandler<T extends Model> implements CrudHandler {

  public CrudViewHandler() {
    Routes.crudRoutes.add(
        new UiRouterMapping(this.getName(), this.getPath(), this.getServerPath()));
  }

  @Override
  public void create(@NotNull Context ctx) {
    String res = Db.create(getCreateValidator(ctx.body()).validate());
    IdReq resBody = new IdReq();
    resBody.id = res;
    ctx.json(resBody).status(201);
  }

  @Override
  public void delete(@NotNull Context ctx, @NotNull String id) {
    Optional<T> product = Db.findById(getModelClass(), new BigInteger(id));
    if (product.isEmpty()) {
      ctx.status(404);
    } else {
      Db.delete(Db.getTableName(getModelClass()), id);
      ctx.status(204);
    }
  }

  @Override
  public void getAll(@NotNull Context ctx) {
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
  public void getOne(@NotNull Context ctx, @NotNull String id) {
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
                  a("Edit").attr("onclick", StateService.edit(getName(), item.get().getId())),
                  form()
                      .attr("data-action", "/" + getName() + "/" + item.get().getId())
                      .attr("data-method", HttpMethod.DELETE)
                      .attr("data-success", StateService.list(getName()))
                      .with(button("Delete").withClass("secondary")))));
    }
  }
  ;

  @Override
  public void update(@NotNull Context ctx, @NotNull String id) {
    Db.update(id, getUpdateValidator(ctx.body()).validate());
    ctx.json("").status(204);
  }
  ;

  public void newForm(@NotNull Context ctx) {
    view(
        ctx,
        main(
            createForm(
                Optional.empty(),
                HttpMethod.POST,
                "/" + getName(),
                StateService.created(getName()))));
  }

  public void updateForm(@NotNull Context ctx, @NotNull String id) {
    Optional<T> item = Db.findById(getModelClass(), new BigInteger(id));
    if (item.isEmpty()) {
      view(ctx, div("Not found"));
    } else {
      view(
          ctx,
          main(
              createForm(
                  item,
                  HttpMethod.PUT,
                  "/" + getName() + "/" + id,
                  StateService.get(getName(), id))));
    }
  }

  private DomContent createForm(
      Optional<T> item, HttpMethod method, String dataAction, String dataSuccess) {
    List<String> fields = getFieldNames(getModelClass());
    return form()
        .attr("data-action", dataAction)
        .attr("data-method", method.asString())
        .attr("data-success", dataSuccess)
        .with(
            each(
                filter(fields, f -> !"id".equals(f)),
                f ->
                    label(f)
                        .with(
                            item.map(
                                    p ->
                                        input()
                                            .withName(f)
                                            .withValue(getFieldValue(p, f).toString()))
                                .orElse(input().withName(f)))),
            button("Submit").withType("submit"));
  }

  public void get(@NotNull Context ctx) {
    Layout.get(ctx);
  }

  public abstract Class<T> getModelClass();

  public abstract ValidationHelper getCreateValidator(String body);

  public ValidationHelper getUpdateValidator(String body) {
    return getCreateValidator(body);
  }

  public String getPath() {
    return "/" + getName();
  }

  public String getServerPath() {
    return "/_" + getName();
  }

  public String getName() {
    return Db.getTableName(getModelClass());
  }

  public static void view(Context ctx, DomContent dc) {
    ctx.html(dc.render());
  }
}
