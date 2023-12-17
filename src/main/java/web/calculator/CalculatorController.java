package web.calculator;

import io.javalin.http.Context;
import web.layout.Layout;

import java.math.BigInteger;

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
                                                                .withType("number").withValue("0")
                                                ),
                                                label(
                                                        input().withName("value2")
                                                                .withType("number").withValue("0")
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
                div("Result " + (payload.value1.add(payload.value2)))
        );
    }

    static class Request {
        public BigInteger value1, value2;
    }
}
