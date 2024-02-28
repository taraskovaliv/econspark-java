package dev.kovaliv.econspark_java.views;

import j2html.tags.specialized.ATag;
import j2html.tags.specialized.FormTag;

import java.util.Optional;

import static j2html.TagCreator.*;

public class Cart {
    public static ATag getHeaderCart(Optional<Integer> cartSize) {
        if (cartSize.isPresent()) {
            Integer size = cartSize.get();
            return a(
                    size > 0 ? span().withClass("chip").withText(String.valueOf(size)) : null
            )
                    .withHref("/cart")
                    .withId("cart")
                    .withClasses("btn", "btn-link")
                    .attr("ts-swap-push", "#cart")
                    .withText("Cart");
        } else {
            return a("Cart")
                    .withHref("/cart")
                    .withId("cart")
                    .withClasses("btn", "btn-link")
                    .attr("ts-trigger", "load")
                    .attr("ts-req", "/cart");
        }
    }

    public static String getBuyResult(Integer count) {
        String s = a("Cart")
                .withHref("/cart")
                .withClasses("btn", "btn-primary")
                .render()
                +
                getHeaderCart(Optional.of(count)).render();
        return s;
    }

    public static FormTag getBuy(Integer id) {
        return form(
                input().withType("hidden").withName("id").withValue(id.toString()),
                button("Buy").withClasses("btn", "btn-primary")
        )
                .withAction("/cart/add")
                .withMethod("post")
                .attr("ts-req", "");
    }

    public static String getRemoveResult(Integer count) {
        return button("Remove")
                .withClasses("btn", "btn-primary")
                .attr("disabled", "true")
                .attr("ts-trigger", "load")
                .attr("ts-action", "target 'parent .product', class+ fade, wait transitionend, remove")
                +
                getHeaderCart(Optional.of(count)).render();
    }

    public static FormTag getRemove(Integer id) {
        return form(
                input().withType("hidden").withName("_method").withValue("delete"),
                input().withType("hidden").withName("id").withValue(id.toString()),
                button("Remove").withClasses("btn", "btn-primary")
        )
                .withAction("/cart/add")
                .withMethod("post")
                .attr("ts-req", "")
                .attr("ts-req-method", "delete");
    }
}
