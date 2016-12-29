package in.sportscafe.nostragamus;

/**
 * Created by Jeeva on 19/7/16.
 */
public class ImageConfig {

    public static final String KEY_IMAGE_FILL = "-cfill";

    public static final String KEY_IMAGE_FIT = "-cfit";

    private static final String KEY_IMAGE_WIDTH = "-w";

    private static final String KEY_IMAGE_HEIGHT = "-h";

    private static final String KEY_IMAGE_QUALITY = "-qn";

    private static final String BACK_SLASH = "/";

    private static final String KEY_GRAVITY = "-g";

    private static final String KEY_OFFSET_X = "-x";

    private static final String KEY_OFFSET_Y = "-y";

    public static int mImageQuality = 70;

    public static String getAuthorImageUrl(int width, int height, String path) {
        return getFormattedUrl(
                getCdnUrl(width, height),
                path
        );
    }

    public static String getTourImageUrl(int width, int height, String tourId) {
        return getFormattedUrl(
                getCdnUrl(width, height), "upcomingtournaments"
                        + BACK_SLASH + tourId.toLowerCase() + ".png"
        );
    }

    public static String getTeamImageUrl(int width, int height, String sportId, String flagId) {
        return getFormattedUrl(
                getCdnUrl(width, height), "sports"
                        + BACK_SLASH + sportId
                        + BACK_SLASH + flagId
                        + ".png"
        );
    }

    public static String getNavBarImageUrl(int width, int height, String iconUrl) {
        return getFormattedUrl(getCdnUrl(width, height), "common"
                + BACK_SLASH + "navbar-app"
                + BACK_SLASH + iconUrl
        );
    }

    public static String getCommentaryImageUrl(int width, int height, String sportId, String flagId) {
        return getFormattedUrl(
                getCdnUrl(width, height), "scweb/modules/matchcentre"
                        + BACK_SLASH + sportId
                        + BACK_SLASH + "commentary"
                        + BACK_SLASH + flagId
                        + ".png"
        );
    }

    public static String getSportImageUrl(int width, int height, String sportId) {
        return getFormattedUrl(getCdnUrl(width, height), "common"
                + BACK_SLASH + "app/icons"
                + BACK_SLASH + sportId
                + ".png"
        );
    }

    private static String getFormattedUrl(String cdnUrl, String path) {
        return (cdnUrl + BACK_SLASH + path).replace(" ", "%20");
    }

    private static String getCdnUrl(int width, int height) {
        return Config.IMAGE_BASE_URL + KEY_IMAGE_FIT
                + KEY_IMAGE_WIDTH + width
                + KEY_IMAGE_HEIGHT + height
                + KEY_IMAGE_QUALITY + mImageQuality;
    }

    private static String setOffset(String offsetKey, Integer offsetValue) {
        if (offsetValue != null && offsetValue != 0) {
            return offsetKey + String.valueOf(offsetValue);
        }
        return "";
    }
}