package in.sportscafe.nostragamus;

/**
 * Created by Jeeva on 30/3/15.
 */
public interface Constants {

    interface DatabaseKeys {
    }

    interface NotificationKeys {

        String GROUP_ID = "group_id";
        String JOIN_GROUP_REQUEST = "join_group_request";
        String DAILY_NOTIFICATION_REQUEST = "daily_notification";
        String HOURLY_NOTIFICATION_REQUEST = "hourly_notification";
        String APPROVED_GROUP_REQUEST = "approved_group_request";
        String RESULTS_LEADERBOARD = "results_leaderboard";
        String GROUP_PERSONID = "group_personID";
        String ADMIN_USERID = "admin_userID";
        String BADGE_REQUEST = "badges";
        String REQUEST_ACCEPTED = "REQUEST ACCEPTED";
        String REQUEST_REJECTED = "REQUEST REJECTED";

    }

    interface SharedKeys {
        String PREVIOUS_APP_VERSION_CODE = "previousAppVersionCode";
        String ALL_SPORTS = "allSports";
        String ALL_TOURNAMENTS = "allTournaments";
        String ALL_GROUPS = "allGroups";
        String SELECTED_TOURNAMENTS = "selectedTournaments";
        String FAVORITE_SPORTS = "favoriteSports";
        String INITIAL_SPORTS_AVAILABLE = "initialSportsAvailable";
        String GRP_INFOS = "grpInfos";
        String MY_RESULT_MATCH_IDS = "myResultMatchIds";
        String USER_INFO = "userInfo";
        String USER_ID = "userId";
        String LOGGED_USER = "loggedUser";
        String COOKIE = "cookie";
        String NUMBER_OF_POWERUPS = "numberofpowerups";
        String NUMBER_OF_NONEGS_POWERUPS = "numberofnonegspowerups";
        String NUMBER_OF_AUDIENCE_POLL_POWERUPS = "numberofaudiencepollpowerups";
        String NUMBER_OF_BADGES = "numberofbadges";
        String NUMBER_OF_GROUPS = "numberofgroups";
        String ALL_BADGES = "allbadges";
        String INSTALL_GROUP_CODE = "installGroupCode";
        String INSTALL_GROUP_NAME = "installGroupName";
        String NORMAL_UPDATE_VERSION = "normalUpdateVersion";
        String FORCE_UPDATE_VERSION = "forceUpdateVersion";
        String NORMAL_UPDATE_ENABLED = "normalUpdateEnabled";
        String NORMAL_UPDATE_MESSAGE = "normalUpdateMessage";
        String FORCE_UPDATE_MESSAGE = "forceUpdateMessage";
        String NORMAL_UPDATE_SHOWN_TIME = "normalUpdateShownTime";
        String INITIAL_FORM_SHOWN = "initialFormShown";
    }

    interface Alerts {
//        String NO_NETWORK_CONNECTION = "No internet connection available";
        String NO_NETWORK_CONNECTION = "Check your internet connection and try again";
        String NO_QUESTIONS = "No Questions Found";
        String NO_TOURNAMENTS = "No Tournaments , Please choose any other sport.";
        String DATA_NOT_FOUND = "There is problem in your internet connection";
        String JSON_PARSING = "There is problem in your internet connection";
        String TIME_OUT = "There is problem in your internet connection";
        String OTP_CODE_SEND = "Barter sent OTP";
        String UNKNOWN_HOST = "There is problem in your internet connection";
        String SERVER_DOWN = "There is problem in your internet connection";
        String SESSION_EXPIRED = "Session Expired";
        String NO_ARTICLE_FOUND = "No articles found";
        String NO_RESULTS_FOUND = "No results found";
        String NO_WHATSAPP = "Whatsapp have not been installed";
        String LOGIN_FAILED = "Login failed";
        String EMPTY_TOURNAMENT_SELECTION = "Please pick atleast one Tournament";
        String EMPTY_SPORT_SELECTION = "Please pick atleast one Sport";
        String PASSED_QUESTION_ALERT = "Now it's time to predict for all the passed questions";
        String NO_LEADERBOARD = "No points found";
        String EDIT_PROFILE_FAILED = "Profile update failed. Try again.";
        String NAME_EMPTY = "Please enter name";
        String NICKNAME_EMPTY = "Please enter nickname";
        String NICKNAME_NOT_VALID = "Username should be between 3 and 15 characters long";
        String NICKNAME_NO_UPPERCASE = "Oops ,Please use lowercase";
        String NICKNAME_CONFLICT = "Oops , this username is already taken";
        String USERNAME_EMPTY = "Please enter username";
        String NO_QUESTIONS_FOUND = "No questions found";
        String NO_FEEDS_FOUND = "No upcoming matches.";
        String NO_RESULTS = "No Results yet.";
        String JOIN_GROUP_SUCCESS = "Your request sent to the group admin for the approval";
        String INVALID_GROUP_CODE = "Invalid group code";
        String EMPTY_GROUP_NAME = "Please enter group name";
        String MEMBERS_EMPTY = "No members found";
        String REMOVE_PERSON = "Removed successfully";
        String MAKE_ADMIN = "You made the person as admin successfully";
        String APPROVE_EMPTY = "No pending requests found";
        String LOGIN_CANCELLED = "Facebook login cancelled";
        String LEAVE_GROUP_SUCCESS = "You have been removed from the group successfully";
        String CANNOT_LEAVE_GROUP = "Make anyone in the group as Admin, If you want to leave the group";
        String PERMISSION_QUIT_TEXT ="Exit App";
        String PERMISSION_HELP ="Help";
        String PERMISSION_SETTINGS ="Setting";
        String PERMISSION_HELP_TEXT = "The permission is missing.\\n\\nTo grant this permission click on Settings, then Permissions and turn the permission on.\\n\\nThen click back twice to return to the app";
        String IMAGE_UPLOAD_TEXT="Select Image to Upload";
        String IMAGE_RESELECT="Click on Image to Reselect";
        String IMAGE_UNABLE_TO_LOAD="Unable to Load Image";
        String IMAGE_UNABLE_TO_PICK="Unable to Pick Image";
        String IMAGE_UPLOADING="Uploading.. Wait for a while...";
        String IMAGE_FILEPATH_EMPTY = "Please Select Image";
        String AUDIENCE_POLL_FAIL = "Not enough responses for a meaningful poll, you can try after some time";
    }

    interface BundleKeys {
        String CONTEST_QUESTIONS = "contestQuestions";
        String CONTEST_NAME = "contestName";
        String GROUP_INFO = "groupInfo";
        String MY_POSITION_LIST = "myPositionList";
        String GROUP_ID = "group_id";
        String GROUP_NAME = "group_name";
        String SPORT_ID = "sportId";
        String SPORT_LIST = "sportList";
        String MATCH_LIST = "matchList";
        String FROM_PROFILE = "fromProfile";
        String TOURNAMENT_ID = "tour_id";
        String CHALLENGE_ID = "challengeId";
        String MATCH_ID = "match_id";
        String TOURNAMENT_NAME = "tourName";
        String TOURNAMENT_SUMMARY = "tourSummary";
        String TOURNAMENT_LIST = "tournamentList";
        String LEADERBOARD_KEY = "leaderboardKey";
        String LEADERBOARD_LIST = "leaderboardList";
        String HOME_SCREEN = "homeScreen";
        String LOGIN_SCREEN = "loginScreen";
        String GROUP_CODE = "groupCode";
        String FEEDBACK_FORM_URL = "feedbackFormUrl";
        String FEED_SCREEN = "feedScreen";
    }

    interface ParcelableKeys {
    }

    interface DateFormats {
        String CHAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
        String SERVER_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzzz";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String DD_MMM_YYYY = "dd MMM yyyy";
        String DAY_OF_WEEK = "EEEE";
        String H_MM_AA = "h:mm aa";
        String MMM_dd_EEE_HH_MM_AA = "MMM dd, EEE h:mm aa";
        String HOURS_24 = "HH";
        String FORMAT_DATE_T_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String GMT = "GMT";
        String DD_MM_YYYY = "dd/MM/yyyy";
        String MM_DD_YYYY = "MM/dd/yyyy";
        String HH_MM_AA = "hh:mm aa";
        String HH_MM_AA_D_MMM = "hh:mm aa, d'th' MMM";
        String YYYY_MMM_DD = "yyyy/MMM/dd";
    }

    interface BroadcastKeys {
    }

    interface LeaderBoardPeriods {
        String ALL_TIME = "alltime";
        String MONTH = "monthly";
        String WEEK = "weekly";
    }

    interface AnalyticsCategory {
        String LOGIN = "Login";
        String LOGOUT = "Logout";
        String EDIT_PROFILE = "Edit Profile";
        String USER_PROFILE = "User Profile";
        String NEW_GROUP = "New Group";
        String FEED = "Feed";
        String FLAVOR = "Flavor";
        String APP_UPDATE = "App Update";
    }

    interface AnalyticsActions {
        String FACEBOOK = "Facebook";
        String GOOGLE_PLUS = "Google Plus";
        String PHOTO = "Photo";
        String OTHERS = "Others";
        String TABS = "Tabs";
        String TOURNAMENT = "Tournament";
    }

    interface AnalyticsLabels {
        String GALLERY = "Gallery";
        String CAMERA = "Camera";
        String UPDATE = "Update";
        String DONE = "Done";
    }
}