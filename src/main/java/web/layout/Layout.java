package web.layout;

import static j2html.TagCreator.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import j2html.tags.DomContent;
import j2html.tags.specialized.FooterTag;
import j2html.tags.specialized.HtmlTag;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import web.Routes;
import web.utils.UiRouterMapping;
import web.utils.components.UiView;

public class Layout {

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
        each(libs(), s -> script().withSrc(s)),
        // Define ui-router routes
        script("window.routes = " + convertToJsonArray(Routes.spaRoutes)));
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
    ObjectMapper objectMapper = new ObjectMapper();
    InputStream inputStream =
        Layout.class.getClassLoader().getResourceAsStream("web/layout/cdn-libs.json");
    ArrayList<String> urlList = null;
    try {
      urlList = objectMapper.readValue(inputStream, ArrayList.class);
    } catch (IOException e) {
      throw new RuntimeException("cdn-libs.json not found in classpath");
    }
    try {
      inputStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return urlList;
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
