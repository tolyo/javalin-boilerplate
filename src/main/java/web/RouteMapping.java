package web;

import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

public class RouteMapping {
    public HandlerType method;
    public String url;
    public Handler handler;

    RouteMapping(@NotNull HandlerType method, @NotNull String url, @NotNull Handler handler) {
        this.method = method;
        this.url = url;
        this.handler = handler;
    }
}
