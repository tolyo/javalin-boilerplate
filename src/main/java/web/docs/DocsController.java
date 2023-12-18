package web.docs;

import static j2html.TagCreator.div;
import static web.utils.ViewHelpers.render;

import io.javalin.http.Context;
import web.layout.Layout;

public class DocsController {

  public static final String URL = "/docs";

  public static Context get(Context ctx) {
    return render(ctx, Layout.layout(div("Docs")));
  }
}
