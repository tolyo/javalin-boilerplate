package web.home;

import static j2html.TagCreator.*;
import static web.utils.UiRouterMapping.uiRoute;
import static web.utils.ViewHelpers.render;
import static web.utils.components.Partial.partial;

import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import web.Routes;
import web.demo.DemoController;
import web.docs.DocsController;
import web.utils.UiRouterMapping;

public class HomeController {
  public static final UiRouterMapping HOME = uiRoute("home", "/", "/_home");
  public static final UiRouterMapping SUBVIEW = uiRoute("home.subview", "/", "/_subview");

  public static Context home(Context ctx) {

    return render(
        ctx,
        div()
            .attr("id", "home")
            .with(
                h1("Javalin Boilerplate"),
                div("A starter template"),
                partial(SUBVIEW.serverPath),
                section(
                    a("Demo").withHref(DemoController.URL), a("Docs").withHref(DocsController.URL)),
                h6("Available controllers:"),
                ul().with(
                        each(
                            filter(
                                Routes.routes,
                                routeMapping ->
                                    !"".equals(routeMapping.url)
                                        && !routeMapping.isUiRoute()
                                        && routeMapping.method == HandlerType.GET),
                            routeMapping ->
                                li(a(routeMapping.defaultName()).withHref(routeMapping.url))))));
  }

  public static Context subview(Context ctx) {
    return render(ctx, div("for scalable development"));
  }
}
