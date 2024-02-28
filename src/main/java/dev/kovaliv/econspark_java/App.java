package dev.kovaliv.econspark_java;

import dev.kovaliv.econspark_java.views.Products;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;

import java.util.*;

import static dev.kovaliv.econspark_java.views.Cart.*;
import static dev.kovaliv.econspark_java.views.Pages.getCart;
import static dev.kovaliv.econspark_java.views.Pages.getIndex;
import static dev.kovaliv.econspark_java.views.Products.getProductList;

public class App {

    public static Javalin app() {
        return Javalin.create(config -> {
                    config.staticFiles.add("/static", Location.CLASSPATH);
                })
                .get("/", ctx -> render(ctx, products(ctx)))
                .get("/cart", ctx -> render(ctx, cartView(ctx)))
                .post("/cart/add", ctx -> render(ctx, cartAdd(ctx)))
                .delete("/cart/add", ctx -> render(ctx, cartAdd(ctx)));
    }

    public static Products.Product getProduct(Integer id, Boolean inCart) {
        return new Products.Product(
                id,
                new Random(id).nextInt(80) + 10,
                String.format("https://picsum.photos/seed/%s/240/300", id.hashCode()),
                inCart
        );
    }

    private static Pair products(Context ctx) {
        Integer offset = Optional.ofNullable(ctx.queryParam("offset")).map(Integer::parseInt).orElse(0);
        Integer[] cart = ctx.sessionAttribute("cart");
        int nextOffset = offset + 24;
        List<Products.Product> products = new ArrayList<>();
        for (int i = offset; i < nextOffset; i++) {
            products.add(getProduct(i, cart != null && Arrays.asList(cart).contains(i)));
        }
        return Pair.of(
                getIndex(products, Optional.of(nextOffset)).render(),
                getProductList(products, Optional.of(nextOffset), false).render()
        );
    }

    private static Pair cartView(Context ctx) {
        Integer[] cart = ctx.sessionAttribute("cart");
        List<Products.Product> products = new ArrayList<>();
        if (cart != null) {
            for (Integer i : cart) {
                products.add(getProduct(i, true));
            }
        }
        return Pair.of(
                getCart(products).render(),
                getHeaderCart(Optional.of(cart != null ? cart.length : 0)).render()
        );
    }

    private static Pair cartAdd(Context ctx) {
        String idString = ctx.formParam("id");
        if (idString == null) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return products(ctx);
        }
        Integer id = Integer.parseInt(idString);
        Integer[] cart = ctx.sessionAttribute("cart");
        if (ctx.method().equals(HandlerType.POST)) {
            if (cart == null) {
                cart = new Integer[]{id};
                ctx.sessionAttribute("cart", cart);
            } else {
                List<Integer> newCart = new ArrayList<>(Arrays.asList(cart));
                newCart.add(id);
                cart = newCart.toArray(new Integer[0]);
                ctx.sessionAttribute("cart", cart);
            }
            return Pair.of(
                    cartView(ctx).full,
                    getBuyResult(cart.length)
            );
        } else if (ctx.method().equals(HandlerType.DELETE)) {
            if (cart != null) {
                List<Integer> newCart = new ArrayList<>(Arrays.asList(cart));
                newCart.remove(id);
                cart = newCart.toArray(new Integer[0]);
                ctx.sessionAttribute("cart", cart);
            }
            return Pair.of(
                    cartView(ctx).full,
                    getRemoveResult(cart != null ? cart.length : 0)
            );
        } else {
            ctx.status(HttpStatus.METHOD_NOT_ALLOWED);
            return products(ctx);
        }
    }

    private static boolean accepts(Context ctx, String type) {
        String accept = ctx.header("accept");
        return accept != null && accept.startsWith(type);
    }

    private static void render(Context ctx, Pair pair) {
        if (accepts(ctx, "text/html+partial")) {
            ctx.html(pair.partial);
        } else if (accepts(ctx, "text/html")) {
            ctx.html(pair.full);
        } else {
            ctx.status(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private static class Pair {
        String full;
        String partial;

        private Pair(String full, String partial) {
            this.full = full;
            this.partial = partial;
        }

        public static Pair of(String full, String partial) {
            return new Pair(full, partial);
        }
    }
}
