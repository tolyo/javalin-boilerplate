package web.docs;

import io.javalin.http.Context;
import web.layout.Layout;

import static j2html.TagCreator.div;
import static j2html.TagCreator.html;

public class DocsController {

    public static String GET = "/docs";
    public static  Context get(Context ctx) {
        return ctx.html(
            Layout.layout(
                div("Docs")
            )
        );
    }
}
