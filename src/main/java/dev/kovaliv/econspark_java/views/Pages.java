package dev.kovaliv.econspark_java.views;

import j2html.tags.specialized.HtmlTag;

import java.util.List;
import java.util.Optional;

import static dev.kovaliv.econspark_java.views.Base.getPage;
import static dev.kovaliv.econspark_java.views.Products.getProductList;
import static j2html.TagCreator.h1;

public class Pages {

    public static HtmlTag getIndex(List<Products.Product> products, Optional<Integer> offset) {
        return getPage(
                "EcomSpark",
                    h1("Products"),
                    getProductList(products, offset, false)
                );
    }

    public static HtmlTag getCart(List<Products.Product> products) {
        return getPage(
                "Cart - EcomSpark",
                h1("Cart"),
                getProductList(products, Optional.empty(), true)
        );
    }
}
