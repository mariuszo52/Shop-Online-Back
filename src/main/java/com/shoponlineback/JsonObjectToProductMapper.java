package com.shoponlineback;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.product.Product;
import com.shoponlineback.systemRequirements.SystemRequirements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JsonObjectToProductMapper {
    static Product map(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Double price = jsonObject.getDouble("price") * 5;
        String coverImage = jsonObject.optString("coverImageOriginal");
        String description = jsonObject.optString("description").trim();
        List<Genre> genres = getGenreList(jsonObject);
        String platform = jsonObject.getString("platform");
        JSONArray offers = jsonObject.getJSONArray("offers");
        JSONObject jsonObject1 = offers.getJSONObject(0);
        String releaseDate = jsonObject1.optString("releaseDate");
        Boolean isPreorder = jsonObject.getJSONArray("offers").getJSONObject(0).getBoolean("isPreorder");
        String regionalLimitations = jsonObject.getString("regionalLimitations");
        JSONArray systemRequirement = jsonObject.optJSONArray("systemRequirements");
        SystemRequirements systemRequirements = getSystemRequirements(systemRequirement);
        String ageRating = jsonObject.optString("ageRating");
        String activationDetails = jsonObject.optString("activationDetails");
        return Product.builder()
                .name(name)
                .price(price)
                .coverImage(coverImage)
                .description(description)
                .genres(genres)
                .releaseDate(releaseDate)
                .platform(platform)
                .isPreorder(isPreorder)
                .regionalLimitations(regionalLimitations)
                .systemRequirements(systemRequirements)
                .ageRating(ageRating)
                .ActivationDetails(activationDetails).build();
    }

    private static List<Genre> getGenreList(JSONObject jsonObject) {
        JSONArray genresJSON = jsonObject.getJSONArray("genres");
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genresJSON.length(); i++) {
            String genreName = genresJSON.getString(i);
            Genre genre = new Genre();
            genre.setName(genreName);
            genres.add(genre);
        }
        return genres;
    }

    private static SystemRequirements getSystemRequirements(JSONArray systemRequirement) {
        if (!systemRequirement.isEmpty()) {
            JSONObject jsonObjectSystem = systemRequirement.optJSONObject(0);
            String system = jsonObjectSystem.optString("system");
            String requirement = jsonObjectSystem.optJSONArray("requirement").toString();
            return new SystemRequirements(system, requirement);
        }
        else {
            return new SystemRequirements("None", "None");
        }
    }
}
