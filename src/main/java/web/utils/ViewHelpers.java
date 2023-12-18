package web.utils;

import static j2html.TagCreator.html;

import io.javalin.http.Context;
import j2html.tags.DomContent;

public class ViewHelpers {

  public static Context render(Context ctx, DomContent... dc) {
    return ctx.html(html(dc).render());
  }
}
