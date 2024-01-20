package com.shoponlineback.product.productManagement;

public enum ScrapUrl {

    PC_URL("https://www.cdkeys.com/pc"),
    PSN_URL("https://www.cdkeys.com/playstation-network-psn"),

    XBOX_URL("https://www.cdkeys.com/xbox-live"),
    NINTENDO_URL("https://www.cdkeys.com/nintendo"),
    TOP_UP_URL("https://www.cdkeys.com/top-up-cards");
    public final String value;

     ScrapUrl(String value) {
        this.value = value;
    }

}
