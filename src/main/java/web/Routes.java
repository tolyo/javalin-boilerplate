package web;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import static io.javalin.http.HandlerType.*;

import web.demo.DemoController;
import web.docs.DocsController;
import web.home.HomeController;
import web.layout.Layout;

import java.util.ArrayList;
import java.util.HashMap;


public class Routes {
    public static ArrayList<RouteMapping> routes = new ArrayList<>();
    public static HashMap<Object, String> handlers = new HashMap();

    static {
        routes.add(new RouteMapping(GET, "", Layout::get));
        routes.add(new RouteMapping(GET, HomeController.GET, HomeController::get));
        routes.add(new RouteMapping(GET, DemoController.GET, DemoController::get));
        routes.add(new RouteMapping(GET, DemoController.SUBVIEW, DemoController::subview));
        routes.add(new RouteMapping(GET, DocsController.GET, DocsController::get));
        routes.forEach(routeMapping -> handlers.put(routeMapping.handler, routeMapping.url));

    }

    public static Javalin init(Javalin javalin) {
        routes.forEach(routeMapping ->
            javalin.addHandler(
                    routeMapping.method,
                    routeMapping.url,
                    routeMapping.handler)
        );
        return javalin;
    }

}
