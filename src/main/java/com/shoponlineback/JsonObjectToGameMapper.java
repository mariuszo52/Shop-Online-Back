package com.shoponlineback;

import org.json.JSONObject;

class JsonObjectToGameMapper {
    static Game map(JSONObject jsonObject){
        String name = jsonObject.getString("name");
        Long price = jsonObject.getLong("price") * 5;
        String coverImage = jsonObject.getString("coverImageOriginal");
        return new Game(name, price, coverImage);
    }
}
