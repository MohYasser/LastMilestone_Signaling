package com.example.shoppingapplast;

public class Link {
    private String id;
    private String shop_id;
    private String product_id;
    private double price;
    private String sp_offers;

    public Link(String shop_id, String product_id, double price, String sp_offers){
        this.shop_id = shop_id;
        this.product_id = product_id;
        this.price = price;
        this.sp_offers = sp_offers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSp_offers() {
        return sp_offers;
    }

    public void setSp_offers(String sp_offers) {
        this.sp_offers = sp_offers;
    }
}
