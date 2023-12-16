package web.demo;

import io.javalin.http.Context;
import web.layout.Layout;

import static j2html.TagCreator.*;

public class DemoController {
    public static String GET = "/demo";
    public static  Context get(Context ctx) {
        return ctx.html(
                Layout.layout(
                        div("Demo controller")
                )
        );
    }
}