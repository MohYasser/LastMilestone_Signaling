package com.example.shoppingapplast;

public class Product {
    private String id;
    private final String product_name;
    private String description;
    private String image_URL;

    public Product (String id, String product_name, String description, String image_URL){
        this.id = id ;
        this.product_name = product_name;
        this.description = description;
        this.image_URL = image_URL;
    }

    public String getId() {
        return id;
    }


    public String getProduct_name() {
        return product_name;
    }


    public String getDescription() {
        return description;
    }


    public String getImage_URL() {
        return image_URL;
    }

}