package web;
import org.jetbrains.annotations.NotNull;

/**
 *  name: "home",
 *  url: "/",
 *  serverPath: "/_home",
 */
public class UiRouterRouteConfig {
    public String name;
    public String url;
    public String serverPath;

    UiRouterRouteConfig(@NotNull String name, @NotNull String url, @NotNull String serverPath) {
        this.name = name;
        this.url = url;
        this.serverPath = serverPath;
    }

    public static UiRouterRouteConfig spaRoute(@NotNull String name, @NotNull String url, @NotNull String serverPath) {
        return new UiRouterRouteConfig(name, url, serverPath);
    }
}
