package com.shoponlineback.product.productManagement;

public enum ScrapUrl {

    PC_URL("https://www.cdkeys.com/pl_pl/pc"),
    PSN_URL("https://www.cdkeys.com/pl_pl/playstation-network-psn"),

    XBOX_URL("https://www.cdkeys.com/pl_pl/xbox-live"),
    NINTENDO_URL("https://www.cdkeys.com/pl_pl/nintendo"),
    TOP_UP_URL("https://www.cdkeys.com/pl_pl/top-up-cards");
    public final String value;

     ScrapUrl(String value) {
        this.value = value;
    }

}
