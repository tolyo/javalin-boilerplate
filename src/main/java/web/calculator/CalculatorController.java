package web.calculator;

import io.javalin.http.Context;
import web.layout.Layout;

import static j2html.TagCreator.*;
import static web.utils.ViewHelpers.render;

public class CalculatorController {
    public static final String URL = "/calculator";

    public static Context get(Context ctx) {
        return render(ctx,
                Layout.layout(
                        div(
                                form()
                                        .attr("data-action", URL)
                                        .attr("data-update", "#myDiv")
                                        .attr("data-onchange", "true")
                                        .with(
                                                label(
                                                        input().withName("value1")
                                                                .withType("integer").withValue("0")
                                                ),
                                                label(
                                                        input().withName("value2")
                                                                .withType("integer").withValue("0")
                                                )
                                        ),

                                div(attrs("#myDiv"))
                        )

                )
        );
    }

    public static Context post(Context ctx) {
        Request payload = ctx.bodyAsClass(Request.class);
        return render(ctx,
                div("Result " + (payload.value1 + payload.value2))
        );
    }

    static class Request {
        public long value1, value2;
    }
}
