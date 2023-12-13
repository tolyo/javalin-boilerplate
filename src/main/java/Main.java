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
                .get("/", ctx -> ctx.html(
                        html(
                                header(
                                        meta()
                                                .attr("charset", "utf-8"),

                                        meta()
                                                .attr("name", "viewport")
                                                .attr("content", "width=device-width, initial-scale=1, shrink-to-fit=no"),

                                        meta()
                                                .attr("name", "google")
                                                .attr("content", "notranslate")
                                ),
                                body(
                                        h1("Hello, world!")
                                ),
                                footer(
                                        script().withCondAsync(true).withSrc("http://localhost:3000/browser-sync/browser-sync-client.js?v=2.27.10"),
                                        script()
                                                .attr("type", "module")
                                                .attr("src", "/public/web/app.js")
                                )


                        ).renderFormatted()))
                .start(4000);
    }
}