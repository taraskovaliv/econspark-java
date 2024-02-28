package dev.kovaliv.econspark_java.views;

import j2html.tags.Tag;
import j2html.tags.specialized.FooterTag;
import j2html.tags.specialized.HeadTag;
import j2html.tags.specialized.HeaderTag;
import j2html.tags.specialized.HtmlTag;

import java.util.Arrays;
import java.util.Optional;

import static dev.kovaliv.econspark_java.views.Cart.getHeaderCart;
import static j2html.TagCreator.*;

public class Base {
    public static HeadTag getHead(String title) {
        return head(
                meta().withCharset("UTF-8"),
                meta().withName("viewport").withContent("width=device-width, initial-scale=1.0"),
                link().withRel("stylesheet").withHref("/static/spectre.css"),
                script().withSrc("/static/twinspark.js"),
                style().withText(".fade {opacity: 0; transition: opacity 0.5s ease-in-out; }"),
                title(title)
        );
    }

    public static HeaderTag getNavBar() {
        return header(
                section(
                        a("EcomSpark").withHref("/").withClasses("navbar-brand", "mr-2")
                ).withClass("navbar-section"),
                section(
                        getHeaderCart(Optional.empty())
                ).withClass("navbar-section")
        ).withClass("navbar");
    }

    public static FooterTag getFooter() {
        return footer();
    }

    public static HtmlTag getPage(String title, Tag... contents) {
        return html(
                getHead(title),
                body(
                        div(
                                div(
                                        getNavBar(),
                                        each(Arrays.asList(contents), c -> c),
                                        getFooter()
                                ).withClass("column col-6 col-xl-8 col-lg-10 col-md-12 col-mx-auto")
                        ).withClass("columns")
                ).withClass("container")
        );
    }
}
