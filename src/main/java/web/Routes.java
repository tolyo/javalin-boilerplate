package web;

import static io.javalin.http.HandlerType.*;

import io.javalin.Javalin;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import web.calculator.CalculatorController;
import web.demo.DemoController;
import web.docs.DocsController;
import web.home.HomeController;
import web.layout.Layout;
import web.product.ProductController;
import web.utils.CrudViewApiBuilder;
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
    new RouteMapping(POST, CalculatorController.URL, CalculatorController::post)
  };

  static {
    List.of(mappings).forEach(route -> routes.add(route));
  }

  public static Javalin init(Javalin javalin) {
    routes.forEach(
        routeMapping ->
            javalin.addHandler(routeMapping.method, routeMapping.url, routeMapping.handler));

    javalin.routes(
        () -> {
          CrudViewApiBuilder.crudViews(new ProductController());
        });

    javalin.exception(
        ValidationException.class,
        (exception, ctx) -> {
          Map<String, String> errorsMap = new HashMap<>();
          Map<String, List<ValidationError<Object>>> errors = exception.getErrors();
          errors.keySet().forEach(k -> errorsMap.put(k, errors.get(k).get(0).getMessage()));
          ctx.status(422).json(errorsMap);
        });

    return javalin;
  }
}
