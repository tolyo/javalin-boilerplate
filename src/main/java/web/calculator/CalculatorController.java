package web.calculator;

import io.javalin.http.Context;
import web.layout.Layout;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

public class CalculatorController {
    static class Request {
        public int value1;
        public int value2;
    }

    public static final String GET = "/calculator";
    public static final String POST = "/calculator";

    public static Context get(Context ctx) {
        return render(ctx,
                Layout.layout(
                        div(
                                form(
                                        label(
                                                input().withName("value1")
                                                        .withType("integer").withValue("0")
                                        ),
                                        label(
                                                input().withName("value2")
                                                        .withType("integer").withValue("0")
                                        ),
                                        button("Add")
                                ).attr("data-action", POST)
                                        .attr("data-update", "#myDiv"),
                                div(attrs("#myDiv"))
                        )

                )
        );
    }

    public static Context post(Context ctx) {
        Request payload = ctx.bodyAsClass(Request.class);
        return render(ctx,
                div(Integer.toString(payload.value1 + payload.value2))
        );
    }
}
