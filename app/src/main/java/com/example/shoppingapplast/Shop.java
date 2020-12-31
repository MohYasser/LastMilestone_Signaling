package com.example.shoppingapplast;

public class Shop {
    private String id;
    private String shop_name;
    private float lattitude;
    private float longitude;

    public Shop(String id, String shop_name, float lattitude, float longitude){
        this.id=id;
        this.shop_name = shop_name;
        this.lattitude = lattitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    /*public void setId(String id) {
        this.id = id;
    }*/

    public String getShop_name() {
        return shop_name;
    }

    public float getLattitude() {
        return lattitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
