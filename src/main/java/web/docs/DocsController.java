package web.docs;

import io.javalin.http.Context;

public class DocsController {

    public static String GET = "/_docs";
    public static  Context get(Context ctx) {
        return ctx.html("Docs");
    }
}
