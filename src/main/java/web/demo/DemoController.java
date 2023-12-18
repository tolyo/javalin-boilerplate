package web.demo;

import static j2html.TagCreator.div;
import static web.utils.ViewHelpers.render;

import io.javalin.http.Context;
import web.layout.Layout;

public class DemoController {
  public static final String URL = "/demo";

  public static Context get(Context ctx) {
    return render(ctx, Layout.layout(div("Demo controller")));
  }
}
