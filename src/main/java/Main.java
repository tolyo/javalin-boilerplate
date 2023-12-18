import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import web.Routes;

public class Main {

  public static void main(String[] args) {
    Javalin javalin =
        Javalin.create(
            config -> {
              config.staticFiles.add(
                  staticFiles -> {
                    staticFiles.hostedPath =
                        "/public"; // change to host files on a subpath, like '/assets'
                    staticFiles.directory = "/"; // the directory where your files are located
                    staticFiles.location = Location.CLASSPATH;
                  });
            });

    Routes.init(javalin).start(4000);
  }
}
