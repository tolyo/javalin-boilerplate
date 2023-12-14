import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import j2html.tags.specialized.FooterTag;
import j2html.tags.specialized.UlTag;
import web.home.HomeController;
import web.layout.Layout;
import web.utils.components.UiView;

import static j2html.TagCreator.*;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
                    config.staticFiles.add(staticFiles -> {
                        staticFiles.hostedPath = "/public";                   // change to host files on a subpath, like '/assets'
                        staticFiles.directory = "/";                    // the directory where your files are located
                        staticFiles.location = Location.CLASSPATH;      // Location.CLASSPATH (jar) or Location.EXTERNAL (file system)
                    });
                })
                .get("/_home", HomeController::get)
                .get("/", ctx -> ctx.html(Layout.layout().render()))
                .start(4000);
    }
}