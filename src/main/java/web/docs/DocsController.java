package web.docs;

import io.javalin.http.Context;
import web.layout.Layout;

import static j2html.TagCreator.div;
import static web.utils.ViewHelpers.render;

public class DocsController {

    public static String GET = "/docs";
    public static  Context get(Context ctx) {
        return render(ctx,
            Layout.layout(
                div("Docs")
            )
        );
    }
}
