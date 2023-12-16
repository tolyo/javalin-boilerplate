package web.home;

import io.javalin.http.Context;
import web.UiRouterRouteConfig;
import web.demo.DemoController;
import web.docs.DocsController;


import static j2html.TagCreator.*;
import static web.UiRouterRouteConfig.spaRoute;
import static web.utils.components.Partial.partial;
import static web.utils.ViewHelpers.render;

public class HomeController {
    public static final UiRouterRouteConfig HOME =  spaRoute("home", "/", "/_home");
    public static Context get(Context ctx) {
        return render(ctx,
            div(
                    h1("Javalin Boilerplate"),
                    div("A starter template"),
                    partial(SUBVIEW.serverPath),
                    section(
                            a("Demo").withHref(DemoController.GET),
                            a("Docs").withHref(DocsController.GET)
                    )
            ).attr("id", "home")
        );
    }


    public static UiRouterRouteConfig SUBVIEW = spaRoute("home.subview", "/", "/_subview");;
    public static Context subview(Context ctx) {
        return ctx.html(
                html(
                    div("for scalable development")
                ).render()
        );
    }
}
