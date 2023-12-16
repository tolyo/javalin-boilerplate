package web.home;

import io.javalin.http.Context;
import web.demo.DemoController;
import web.docs.DocsController;


import static j2html.TagCreator.*;
import static web.utils.components.Partial.partial;

public class HomeController {
    public static final String GET = "/_home";
    public static Context get(Context ctx) {
        return ctx.html(
                html(
                        div(
                                h1("Javalin Boilerplate"),
                                div("A starter template"),
                                partial(GET_SUBVIEW),
                                section(
                                    a("Demo").withHref(DemoController.GET),
                                    a("Docs").withHref(DocsController.GET)
                                )
                        ).attr("id", "home")
                ).render()
        );
    }


    public static String GET_SUBVIEW = "/_subview";
    public static Context subview(Context ctx) {
        return ctx.html(
                html(
                    div("for scalable development")
                ).render()
        );
    }
}
