package web.utils;

import io.javalin.http.Context;
import j2html.tags.DomContent;

public class ViewHelpers {

  public static Context render(Context ctx, DomContent dc) {
    return ctx.html(dc.render());
  }
}
