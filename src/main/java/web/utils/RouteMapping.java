package web.utils;

import static web.utils.ViewHelpers.capitalize;

import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;
import web.Routes;

public class RouteMapping {
  public HandlerType method;
  public String url;
  public Handler handler;
  boolean uiRoute = false;

  public RouteMapping(@NotNull HandlerType method, @NotNull String url, @NotNull Handler handler) {
    this.method = method;
    this.url = url;
    this.handler = handler;
    Routes.routes.add(this);
  }

  public RouteMapping(
      @NotNull HandlerType method,
      @NotNull UiRouterMapping routerRouteConfig,
      @NotNull Handler handler) {
    this.method = method;
    this.url = routerRouteConfig.serverPath;
    this.handler = handler;
    this.uiRoute = true;
    Routes.spaRoutes.add(routerRouteConfig);
  }

  @SuppressWarnings("StringSplitter")
  public String defaultName() {
    String[] split = this.url.split("/");
    String name = split[split.length - 1];
    return capitalize(name) + "Controller";
  }

  public boolean isUiRoute() {
    return this.uiRoute;
  }
}
