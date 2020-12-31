package com.example.shoppingapplast;

public class Shop_enhanced {
  //  private String uid;
    private String shop_name;
    private double shop_distance;
    private double shop_price;
    private String shop_sp_offers;

    public Shop_enhanced(String name, double price, double distance, String sp_offers){
//        this.uid = uid;
        this.shop_name = name;
        this.shop_distance = price;
        this.shop_price = distance;
        this.shop_sp_offers = sp_offers;

    }

    public String getShop_name() {
        return shop_name;
    }

    public double getShop_distance() {
        return shop_distance;
    }

    public double getShop_price() {
        return shop_price;
    }

    public String getShop_sp_offers() {
        return shop_sp_offers;
    }

   /* public String getUid() {
        return uid;
    }*/

}
