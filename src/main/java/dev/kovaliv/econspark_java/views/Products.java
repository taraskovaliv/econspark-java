package dev.kovaliv.econspark_java.views;

import j2html.tags.specialized.DivTag;

import java.util.List;
import java.util.Optional;

import static j2html.TagCreator.*;

public class Products {

    public static DivTag getProduct(Integer id, String pic, String price, boolean cart) {
        return div(
                div(
                        div(
                                img().withSrc(pic).withStyle("width: 100%; aspect-ratio: 4/5").withClasses("img-responsive")
                        ).withClasses("card-image"),
                        div(
                                div(
                                        div(price + "₴").withClasses("column", "col-6"),
                                        div(
                                                cart ? Cart.getRemove(id) : Cart.getBuy(id)
                                        ).withClasses("column", "col-6", "text-right")
                                ).withClasses("card-title", "h5", "columns"),
                                div("Product " + id).withClasses("card-subtitle", "text-gray")
                        ).withClasses("card-header")
                ).withClasses("card")
        ).withClasses("product", "column", "col-3", "col-xs-6", "p-1");
    }

    public static DivTag getProductList(List<Product> products, Optional<Integer> offset, boolean cart) {
        Integer o = offset.orElse(0);
        return div(
                each(products, p -> getProduct(p.id, p.pic, p.price, p.inCart)),
                a("Next page >>")
                        .withHref((cart ? "/cart?offset=" : "/?offset=") + o)
                        .attr("ts-trigger", "visible")
                        .attr("ts-req", "")
                        .attr("ts-req-selector", "children #products")
        ).withId("products").withClasses("columns");
    }

    public static class Product {
        public Integer id;
        public String pic;
        public String price;
        public Boolean inCart;

        public Product(Integer id, Integer price, String pic, Boolean inCart) {
            this.id = id;
            this.price = price.toString();
            this.pic = pic;
            this.inCart = inCart;
        }
    }
}
