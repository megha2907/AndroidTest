package in.sportscafe.scgame;

import in.sportscafe.app.BaseConfig;


/**
 * Created by Jeeva on 17/3/16.
 * <p/>
 * It is going to have all the constant values which is used in our application
 */
public class Config extends BaseConfig {

    public static final String IMAGE_BASE_URL = "https://d22lvl9g4trg1l.cloudfront.net/img/es3";

    public static final String KEY_IMAGE_FILL = "-cfill";

    public static final String KEY_IMAGE_FIT = "-cfit";

    private static final String KEY_IMAGE_WIDTH = "-w";

    private static final String KEY_IMAGE_HEIGHT = "-h";

    private static final String KEY_IMAGE_QUALITY = "-qn";

    private static final String PATH_ARTICLES = "articles";

    private static final String BACK_SLASH = "/";

    private static final String KEY_GRAVITY = "-g";

    private static final String KEY_OFFSET_X = "-x";

    private static final String KEY_OFFSET_Y = "-y";

    public interface Sports {
        String BADMINTON = "badminton";
        String BOXING = "boxing";
        String CRICKET = "cricket";
        String FOOTBALL = "football";
        String HOCKEY = "hockey";
        String KABADDI = "kabaddi";
        String SHOOTING = "shooting";
        String SQUASH = "squash";
        String TENNIS = "tennis";
        String WRESTLING = "wrestling";
    }

    public static String[] getSports() {
        // Todo If you are adding any new sports don't forget to add it here
        return new String[] {
                Sports.BADMINTON,
                Sports.BOXING,
                Sports.CRICKET,
                Sports.FOOTBALL,
                Sports.HOCKEY,
                Sports.KABADDI,
                Sports.SHOOTING,
                Sports.SQUASH,
                Sports.TENNIS,
                Sports.WRESTLING
        };
    }

    public static String getImageURL(int width, int height, int quality, String path) {
        return (IMAGE_BASE_URL + KEY_IMAGE_FIT
                + KEY_IMAGE_WIDTH + width
                + KEY_IMAGE_HEIGHT + height
                + KEY_IMAGE_QUALITY + quality
                + BACK_SLASH + path).replace(" ","%20");
    }
}