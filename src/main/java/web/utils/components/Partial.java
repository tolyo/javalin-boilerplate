package web.utils.components;

import j2html.tags.ContainerTag;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Partial extends ContainerTag<Partial> {
    private static final Random rand = new Random();

    public Partial(@NotNull String url) {
        super("partial-component");
        this.attr("id", String.valueOf(rand.nextInt(1000)));
        this.withData("url", url);
    }

    public static Partial partial(@NotNull String url) {
        return new Partial(url);
    }

}
