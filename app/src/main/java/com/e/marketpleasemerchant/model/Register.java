package com.e.marketpleasemerchant.model;

public class Register {

    private String first_name, last_name, email, password, confirm_password;
    private int is_merchant;
    private String merchant_name;

    public Register(String first_name, String last_name, String email, String password, String confirm_password, int is_merchant, String merchant_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
        this.is_merchant = is_merchant;
        this.merchant_name = merchant_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public int getIs_merchant() {
        return is_merchant;
    }

    public String getMerchant_name() {
        return merchant_name;
    }
}
