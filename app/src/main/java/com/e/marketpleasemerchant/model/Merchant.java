package com.e.marketpleasemerchant.model;

public class Merchant {
    private int merchantId;
    private String merchantName;
    private String merchantSlug;

    public int getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantSlug() {
        return merchantSlug;
    }

    public Merchant(int merchantId, String merchantName, String merchantSlug) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantSlug = merchantSlug;
    }
}
