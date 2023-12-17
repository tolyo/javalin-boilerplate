package web.demo;

import io.javalin.http.Context;
import web.layout.Layout;

import static j2html.TagCreator.div;
import static web.utils.ViewHelpers.render;

public class DemoController {
    public static final String URL = "/demo";

    public static Context get(Context ctx) {
        return render(ctx,
                Layout.layout(
                        div("Demo controller")
                )
        );
    }
}
