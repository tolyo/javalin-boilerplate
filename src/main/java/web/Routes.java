package web;

import static io.javalin.http.HandlerType.GET;
import static io.javalin.http.HandlerType.POST;

import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.List;
import web.calculator.CalculatorController;
import web.demo.DemoController;
import web.docs.DocsController;
import web.home.HomeController;
import web.layout.Layout;
import web.product.ProductController;
import web.utils.RouteMapping;
import web.utils.UiRouterMapping;

public class Routes {
  public static ArrayList<RouteMapping> routes = new ArrayList<>();
  public static ArrayList<UiRouterMapping> spaRoutes = new ArrayList<>();
  static RouteMapping[] mappings = {
    new RouteMapping(GET, "", Layout::get), // root component
    new RouteMapping(GET, HomeController.HOME, HomeController::home),
    new RouteMapping(GET, HomeController.SUBVIEW, HomeController::subview),
    new RouteMapping(GET, DemoController.URL, DemoController::get),
    new RouteMapping(GET, DocsController.URL, DocsController::get),
    new RouteMapping(GET, CalculatorController.URL, CalculatorController::get),
    new RouteMapping(POST, CalculatorController.URL, CalculatorController::post),
    new RouteMapping(GET, "/products", Layout::get),
    new RouteMapping(GET, "/_products", ProductController::getAll)
  };

  static {
    List.of(mappings).forEach(route -> routes.add(route));
  }

  public static Javalin init(Javalin javalin) {
    routes.forEach(
        routeMapping ->
            javalin.addHandler(routeMapping.method, routeMapping.url, routeMapping.handler));
    return javalin;
  }
}
