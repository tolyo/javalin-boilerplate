package web.layout;

import com.fasterxml.jackson.databind.ObjectMapper;
import j2html.tags.specialized.FooterTag;
import j2html.tags.specialized.HeaderTag;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static j2html.TagCreator.*;
import static j2html.TagCreator.meta;

public class Layout {
    public static HeaderTag header() throws IOException {
        return j2html.TagCreator.header(
                meta()
                        .attr("charset", "utf-8"),

                meta()
                        .attr("name", "viewport")
                        .attr("content", "width=device-width, initial-scale=1, shrink-to-fit=no"),

                meta()
                        .attr("name", "google")
                        .attr("content", "notranslate"),
                link().attr("rel", "stylesheet")
                        .attr("href", "/public/web/app.css"),
                each(libs(), s -> script().withSrc(s))
        );
    }
    public static FooterTag footer() {
        return j2html.TagCreator.footer(
                script().withCondAsync(true).withSrc("http://localhost:3000/browser-sync/browser-sync-client.js?v=2.27.10"),
                script()
                        .attr("type", "module")
                        .attr("src", "/public/web/app.js")
        );
    }

    public static ArrayList<String> libs() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = Layout.class.getClassLoader().getResourceAsStream("web/layout/cdn-libs.json");
        ArrayList<String> urlList = objectMapper.readValue(inputStream, ArrayList.class);
        return urlList;
    }
}