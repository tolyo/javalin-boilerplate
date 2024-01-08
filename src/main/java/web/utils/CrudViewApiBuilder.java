package web.utils;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.security.RouteRole;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public class CrudViewApiBuilder extends ApiBuilder {

  public static void crudViews(@NotNull CrudViewHandler crudHandler) {
    CrudViewApiBuilder.crudViews(crudHandler, new RouteRole[0]);
  }

  public static void crudViews(@NotNull CrudViewHandler crudHandler, @NotNull RouteRole... roles) {
    String path = crudHandler.getPath() + "/{id}";
    String publicPath = prefixPath(path);
    String serverPath = "/_" + publicPath.substring(1);
    String[] subPaths =
        Arrays.stream(serverPath.split("/")).filter(it -> !it.isEmpty()).toArray(String[]::new);
    if (subPaths.length < 2) {
      throw new IllegalArgumentException(
          "CrudHandler requires a path like '/resource/{resource-id}'");
    }
    String resourceId = subPaths[subPaths.length - 1];
    if (!(resourceId.startsWith("{") && resourceId.endsWith("}"))) {
      throw new IllegalArgumentException(
          "CrudHandler requires a path-parameter at the end of the provided path, e.g. '/users/{user-id}'");
    }
    String resourceBase = subPaths[subPaths.length - 2];
    if (resourceBase.startsWith("{")
        || resourceBase.startsWith("<")
        || resourceBase.endsWith("}")
        || resourceBase.endsWith(">")) {
      throw new IllegalArgumentException(
          "CrudHandler requires a resource base at the beginning of the provided path, e.g. '/users/{user-id}'");
    }

    staticInstance()
        .get(serverPath.replace(resourceId, "") + "new", ctx -> crudHandler.newForm(ctx), roles);
    staticInstance()
        .get(serverPath, ctx -> crudHandler.getOne(ctx, ctx.pathParam(resourceId)), roles);
    staticInstance().get(serverPath.replace(resourceId, ""), crudHandler::getAll, roles);
    staticInstance().post(publicPath.replace(resourceId, ""), crudHandler::create, roles);
    staticInstance()
        .put(publicPath, ctx -> crudHandler.update(ctx, ctx.pathParam(resourceId)), roles);
    staticInstance()
        .delete(publicPath, ctx -> crudHandler.delete(ctx, ctx.pathParam(resourceId)), roles);

    staticInstance()
        .get(
            "/_" + crudHandler.getPath().substring(1) + "/edit/{id}",
            ctx -> crudHandler.updateForm(ctx, ctx.pathParam(resourceId)),
            roles);

    String catchAllPath =
        publicPath
                .replace(resourceId, "")
                .substring(0, publicPath.replace(resourceId, "").length() - 1)
            + "**";
    staticInstance().get(catchAllPath, ctx -> crudHandler.get(ctx), roles);
  }
}
