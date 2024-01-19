package com.shoponlineback.product.mapper;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.language.Language;
import com.shoponlineback.language.LanguageDto;
import com.shoponlineback.language.LanguageMapper;
import com.shoponlineback.language.LanguageRepository;
import com.shoponlineback.platform.dto.PlatformDto;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.screenshot.Screenshot;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsonObjectToProductMapper {
    private final static String VIDEO_PREFIX = "https://www.youtube.com/embed/";
    private final LanguageRepository languageRepository;

    public JsonObjectToProductMapper(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;

    }

    public ProductDto map(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(decimalFormat.format(jsonObject.getDouble("price") * 5 + 30)
                .replace(",", ".")));
        String coverImage = getCoverImage(jsonObject);
        String description = jsonObject.optString("description").trim();
        List<GenreDto> genres = getGenreList(jsonObject).stream()
                .map(genre -> new GenreDto(genre.getName()))
                .collect(Collectors.toList());
        String platformName = jsonObject.getString("platform");
        PlatformDto platform = getPlatform(platformName);
        JSONArray offers = jsonObject.getJSONArray("offers");
        JSONObject jsonObject1 = offers.getJSONObject(0);
        String releaseDate= jsonObject1.optString("releaseDate");
        String regionalLimitations = jsonObject.getString("regionalLimitations");
        String ageRating = jsonObject.optString("ageRating");
        String activationDetails = jsonObject.optString("activationDetails");
        int regionId = jsonObject.optInt("regionId");
        List<LanguageDto> languages = getLanguages(jsonObject);
        return ProductDto.builder()
                .name(name)
                .price(price)
                .coverImage(coverImage)
                .description(description)
                .genres(genres)
                .releaseDate(releaseDate)
                .platformDto(platform)
                .regionalLimitations(regionalLimitations)
                .ageRating(ageRating)
                .activationDetails(activationDetails)
                .regionId(regionId)
                .languages(languages)
                .cartQuantity(0L)
                .build();
    }


    private List<LanguageDto> getLanguages(JSONObject jsonObject) {
        JSONArray languages = jsonObject.getJSONArray("languages");
        List<LanguageDto> languagesList = new ArrayList<>();
        for (int i=0 ; i<languages.length(); i++){
            String languageName = languages.getString(i);
            Language language = languageRepository.findLanguageByName(languageName).orElseThrow();
            languagesList.add(LanguageMapper.map(language));
        }
        return languagesList;
    }

    private static PlatformDto getPlatform(String platformName) {
        return switch (platformName) {
            case "Steam", "Epic Games Store", "GOG (Good Old Games) Galaxy", "EA Origin",
                    "Uplay", "Battle.net (Blizzard)", "Microsoft Store", "Rockstar Games Launcher",
                    "Battlefield.net", "NCSoft Launcher", "Android" ->
                    new PlatformDto(platformName, "PC");
            case "PlayStation 4", "PlayStation 5" -> new PlatformDto(platformName, "PSN");
            case "XBOX ONE", "XBOX Series X|S", "XBOX 360" -> new PlatformDto(platformName, "XBOX");
            case "Nintendo" -> new PlatformDto(platformName, "NINTENDO");
            case "Rockstar Games" -> new PlatformDto(platformName, "ROCKSTAR GAMES");
            default -> new PlatformDto(platformName, "OTHERS");
        };

    }

    public List<Screenshot> getScreenshots(JSONObject jsonObject) {
        ArrayList<Screenshot> images = new ArrayList<>();
        JSONObject screens = jsonObject.optJSONObject("images");
        JSONArray screenshots = screens.getJSONArray("screenshots");
        for (int j = 0; j < screenshots.length(); j++) {
            JSONObject screenshotObject = screenshots.getJSONObject(j);
            String screenUrl = screenshotObject.optString("url");
            Screenshot screenshot = new Screenshot();
            screenshot.setUrl(screenUrl);
            images.add(screenshot);
        }
        return images;
    }

    private static String getCoverImage(JSONObject jsonObject) {
        String coverImage = jsonObject.optString("coverImageOriginal");
        if (coverImage.isEmpty()) {
            coverImage = "https://i0.wp.com/imicare.pl/wp-content/uploads/2018/06/brak-zdjecia_1030x578.jpg?ssl=1";
        }
        return coverImage;
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

}
