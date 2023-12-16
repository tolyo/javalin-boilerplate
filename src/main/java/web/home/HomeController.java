package web.home;

import io.javalin.http.Context;
import web.UiRouterMapping;
import web.demo.DemoController;
import web.docs.DocsController;

import static j2html.TagCreator.*;
import static web.UiRouterMapping.uiRoute;
import static web.utils.ViewHelpers.render;
import static web.utils.components.Partial.partial;

public class HomeController {
    public static final UiRouterMapping HOME = uiRoute("home", "/", "/_home");
    public static final UiRouterMapping SUBVIEW = uiRoute("home.subview", "/", "/_subview");

    public static Context home(Context ctx) {
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

    public static Context subview(Context ctx) {
        return ctx.html(
                html(
                        div("for scalable development")
                ).render()
        );
    }
}
