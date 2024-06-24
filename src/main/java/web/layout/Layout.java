package web.layout;

import static j2html.TagCreator.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import j2html.tags.specialized.FooterTag;
import j2html.tags.specialized.HtmlTag;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import web.Routes;
import web.utils.UiRouterMapping;
import web.utils.components.UiView;

public class Layout {

  /**
   * CDN libs to be loaded into the header of our main layout
   */
  private static final List<String> ULR_LIBS =
      Arrays.asList(
          "https://unpkg.com/alpinejs@3.13.1/dist/cdn.min.js",
          "https://cdn.jsdelivr.net/npm/@uirouter/core@6.1.0/_bundles/ui-router-core.min.js",
          "https://cdn.jsdelivr.net/npm/server-page-component/dist/server-page.umd.min.js");

  public static Context get(Context ctx) {
    return ctx.html(layout().render());
  }

  public static HtmlTag layout() {
    return html(head(), body(header(), new UiView().withId("root")), footer());
  }

  public static HtmlTag layout(@NotNull DomContent content) {
    return html(head(), body(header(), content), footer());
  }

  public static DomContent head() {
    return j2html.TagCreator.head(
        meta().attr("charset", "utf-8"),
        meta()
            .attr("name", "viewport")
            .attr("content", "width=device-width, initial-scale=1, shrink-to-fit=no"),
        meta().attr("name", "google").attr("content", "notranslate"),
        link().attr("rel", "stylesheet").attr("href", "/public/web/app.css"),
        each(libs(), s -> script().withSrc(s).isDefer()),
        // Define ui-router routes
        script("window.routes = " + convertToJsonArray(Routes.spaRoutes)),
        script("window.crudRoutes = " + convertToJsonArray(Routes.crudRoutes)));
  }

  public static DomContent header() {
    return j2html.TagCreator.header(nav(a(strong("SCALE APP")).withHref("/")));
  }

  public static FooterTag footer() {
    return j2html.TagCreator.footer(
        script()
            .withCondAsync(true)
            .withSrc("http://localhost:3000/browser-sync/browser-sync-client.js?v=2.27.10"),
        script().attr("type", "module").attr("src", "/public/web/app.js"));
  }

  public static List<String> libs() {
    return ULR_LIBS;
  }

  private static String convertToJsonArray(List<UiRouterMapping> routeMappings) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(routeMappings);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
