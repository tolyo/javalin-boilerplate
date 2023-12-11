package org.example;

import io.javalin.Javalin;
import static j2html.TagCreator.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.html(body(
                        h1("Hello, World!")
                ).renderFormatted()))
                .start(4000);
    }
}