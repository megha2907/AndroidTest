package com.jeeva.android.facebook;

public interface FacebookConstants {

    int FacebookTaskKey = 555;

    interface ApiKeys {

        String FIELDS = "fields";

        String ACCESS_TOKEN = "access_token";

        String MESSAGE = "message";

        String PICTURE = "picture";

        String LINK = "link";

        String PHOTOS = "photos";

        String WIDTH = "width";

        String HEIGHT = "height";

        String MUSIC = "music";
    }

    interface Alerts {

        String DATA_NOT_FOUND = "There is problem in your internet connection";

        String JSON_PARSING = "There is problem in your internet connection";

        String TIME_OUT = "There is problem in your internet connection";

        String UNKNOWN_HOST = "There is problem in your internet connection";

        String SERVER_DOWN = "There is problem in your internet connection";

        String ACCESS_TOKEN_NOT_FOUND = "Facebook access token not found";

        String NO_MUTUAL_FRIENDS = "No mutual friends";

        String NO_MUTUAL_INTERESTS = "No mutual interests";
    }
}