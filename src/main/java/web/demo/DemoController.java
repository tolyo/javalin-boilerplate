package web.demo;

import io.javalin.http.Context;

public class DemoController {
    public static String GET = "/_demo";
    public static Context get(Context ctx) {
        return ctx.html("Demo");
    }

    public static String SUBVIEW = "/_subview";
    public static Context subview(Context ctx) {
        return ctx.html("for scalable development");
    }
}
