package web.home;

import io.javalin.http.Context;
import web.demo.DemoController;
import web.docs.DocsController;
import web.utils.components.Partial;


import static j2html.TagCreator.*;

public class HomeController {
    public static final String GET = "/_home";
    public static Context get(Context ctx) {
        return ctx.html(
                html(
                        div(
                                h1("Javalin Boilerplate"),
                                div("A starter template"),
                                new Partial("_subview"),
                                section(
                                    a("Demo").withHref(DemoController.GET),
                                    a("Docs").withHref(DocsController.GET)
                                )
                        ).attr("id", "home")
                ).render()
        );
    }
}
