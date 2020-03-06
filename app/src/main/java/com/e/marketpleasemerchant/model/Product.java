package com.e.marketpleasemerchant.model;

public class Product {
    private int productId;
    private String productName;
    private String productSlug;
    private int productQty;
    private String productImage;
    private String productDesc;
    private int productPrice;
    Category category;
    Merchant merchant;

    public Product(int productId, String productName, String productSlug, int productQty, String productImage, String productDesc, int productPrice, Category category, Merchant merchant) {
        this.productId = productId;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productQty = productQty;
        this.productImage = productImage;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.category = category;
        this.merchant = merchant;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public int getProductQty() {
        return productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public Category getCategory() {
        return category;
    }

    public Merchant getMerchant() {
        return merchant;
    }
}
