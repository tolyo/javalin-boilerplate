package web.home;

import io.javalin.http.Context;

public class HomeController {
    public static Context get(Context ctx) {
        return ctx.html("test");
    }
}
