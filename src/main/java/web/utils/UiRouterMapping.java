package web.utils;

import org.jetbrains.annotations.NotNull;

/** name: "home", url: "/", serverPath: "/_home", */
public class UiRouterMapping {
  public String name;
  public String url;
  public String serverPath;

  public UiRouterMapping(@NotNull String name, @NotNull String url, @NotNull String serverPath) {
    this.name = name;
    this.url = url;
    this.serverPath = serverPath;
  }

  public static UiRouterMapping uiRoute(
      @NotNull String name, @NotNull String url, @NotNull String serverPath) {
    return new UiRouterMapping(name, url, serverPath);
  }
}
