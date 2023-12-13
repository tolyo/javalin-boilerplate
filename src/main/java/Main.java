import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

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
                .get("/", ctx -> ctx.html(body(
                        h1("Hello, world!")
                ).renderFormatted()))
                .start(4000);
    }
}