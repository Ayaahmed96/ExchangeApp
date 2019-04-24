package com.example.exchangeapp;

public class Product {
    private String productKey;
    private String productTitle;
    private String productDescription;
    private String productPhoto;
    private String preferedProducttoExchage;
    private String productCateogry;
    private String productOwnerId;
    public Product(){

    }


    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductKey() {
        return productKey;
    }

    public Product(String productTitle, String productDescription, String productPhoto, String preferedProducttoExchage, String productCateogry, String productOwnerId) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productPhoto = productPhoto;
        this.preferedProducttoExchage = preferedProducttoExchage;
        this.productCateogry = productCateogry;
        this.productOwnerId = productOwnerId;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public void setPreferedProducttoExchage(String preferedProducttoExchage) {
        this.preferedProducttoExchage = preferedProducttoExchage;
    }

    public void setProductCateogry(String productCateogry) {
        this.productCateogry = productCateogry;
    }

    public void setProductOwnerId(String productOwnerId) {
        this.productOwnerId = productOwnerId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public String getPreferedProducttoExchage() {
        return preferedProducttoExchage;
    }

    public String getProductCateogry() {
        return productCateogry;
    }

    public String getProductOwnerId() {
        return productOwnerId;
    }
}
