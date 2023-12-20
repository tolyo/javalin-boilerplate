import app.db.Db;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import java.sql.SQLException;
import web.Routes;

@SuppressWarnings("DefaultPackage")
public class Main {

  public static void main(String[] args) throws SQLException {
    Db.init();
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
