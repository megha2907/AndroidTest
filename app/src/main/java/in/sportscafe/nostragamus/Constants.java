package in.sportscafe.nostragamus;

import android.content.Intent;
import android.content.IntentFilter;

import static android.view.View.X;

/**
 * Created by Jeeva on 30/3/15.
 */
public interface Constants {

    String IMAGE_TYPE = "png";

    interface ImageFolders {
        String MAIN = "Nostragamus_Images";

        String PROFILE = "Profile_Images";
    }

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
        String CHALLENGE_NAME_NOTIFICATION = "challenge_name";

    }

    interface SharedKeys {
        String PREVIOUS_APP_VERSION_CODE = "previousAppVersionCode";
        String ALL_SPORTS = "allSports";
        String ALL_TOURNAMENTS = "allTournaments";
        String ALL_GROUPS = "allGroups";
        String MUTUAL_GROUPS = "mutualGroups";
        String SELECTED_TOURNAMENTS = "selectedTournaments";
        String FAVORITE_SPORTS = "favoriteSports";
        String INITIAL_SPORTS_AVAILABLE = "initialSportsAvailable";
        String GRP_INFOS = "grpInfos";
        String MY_RESULT_MATCH_IDS = "myResultMatchIds";
        String USER_INFO = "userInfo";
        String USER_ID = "userId";
        String LOGGED_USER = "loggedUser";
        String FIRST_TIME_USER = "firstTimeUser";
        String COOKIE = "cookie";
        String NUMBER_OF_2X_GLOBAL_POWERUPS = "numberof2xglobalpowerups";
        String NUMBER_OF_POWERUPS = "numberofpowerups";
        String NUMBER_OF_NONEGS_POWERUPS = "numberofnonegspowerups";
        String NUMBER_OF_AUDIENCE_POLL_POWERUPS = "numberofaudiencepollpowerups";
        String NUMBER_OF_REPLAY_POWERUPS = "numberofreplaypowerups";
        String NUMBER_OF_FLIP_POWERUPS = "numberofflippowerups";
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
        String ACCESS_TOKEN = "accessToken";
        String TOKEN_EXPIRY = "tokenExpiry";
        int TOURNAMENT_ID = 0;
        String INSTALL_CHANNEL = "installChannel";
        String BANK_INFO_SHOWN = "bankInfoShown";
    }

    interface Alerts {
//        String NO_NETWORK_CONNECTION = "No internet connection available";
        String GROUP_INFO_ERROR = "There was a problem retrieving the Group Information.";
        String RESULTS_INFO_ERROR = "There was a problem retrieving the Results Information.";
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
        String NO_RESULTS_FOUND = "No Results found";
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
        String NO_UPDATED_LEADERBOARDS = "Your leaderboard will update here after a match you have played is over";
        String NO_RESULTS = "No results yet";
        String JOIN_GROUP_SUCCESS = "Your request sent to the group admin for the approval";
        String INVALID_GROUP_CODE = "Invalid group code";
        String EMPTY_GROUP_NAME = "Please enter group name";
        String MEMBERS_EMPTY = "No members found";
        String REMOVE_PERSON = "Removed successfully";
        String MAKE_ADMIN = "You made the person as admin successfully";
        String APPROVE_EMPTY = "No pending requests found";
        String LOGIN_CANCELLED = "Facebook login cancelled";
        String LEAVE_GROUP_SUCCESS = "You have been removed from the group successfully";
        String CANNOT_LEAVE_GROUP = "You are the only Admin, you need to add another group member as Admin before you can leave";
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
        String POWERUP_FAIL = "Something went wrong! Please try again!";
        String API_FAIL = "Something went wrong! Please try again!";
        String MATCH_ALREADY_STARTED = "This match has already started!";
        String FLIP_POWERUP_OVER = "You have used all your flip powerups!";
        String REPLAY_POWERUP_OVER = "You have used all your replay powerups!";
        String ADD_PHOTO_FAILED = "Adding photo failed! Try again!";
        String FUZZY_SEARCH_FAILED = "Something went wrong! Please try again!";
        String GETTING_OTHERS_ANSWERS_FAILED = "Getting others answers failed! Try again!";
        String SELECTED_TOURNAMENTS_LIMIT = "Please Pick atleast one Tournament";
        String NOT_ADMIN = "Only Admin can change Tournaments";
        String RESET_LB_SUCCESS = "Success! Your group leaderboard have been reset!";
        String CANNOT_RESET_LB = "Something went wrong! Please try again!";
        String DELETE_GROUP_SUCCESS = "You have deleted the group successfully";
        String CANNOT_DELETE_GROUP = "You cannot delete the group since you are not the Admin";
        String NO_POWERUPS = "No powerups";
        String NO_BADGES = "No badges yet";
        String EMPTY_CHALLENGES = "No challenges currently created to play!";
        String BANK_TRANSFER_SUCCESS = "Selected powerups successfully transfered to your challenge";
    }

    interface BundleKeys {
        String GROUP = "group";
        String RESULTS = "results";
        String BADGES = "badges";
        String CONTEST_QUESTIONS = "contestQuestions";
        String CONTEST_NAME = "contestName";
        String GROUP_INFO = "groupInfo";
        String MY_POSITION_LIST = "myPositionList";
        String GROUP_ID = "group_id";
        String USER_REFERRAL_ID = "user_referral_id";
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
        String MUTUALGROUPSLIST = "mutualGroupsList";
        String PLAYERINFO = "playerInfo";
        String HOME_SCREEN = "homeScreen";
        String LOGIN_SCREEN = "loginScreen";
        String GROUP_CODE = "groupCode";
        String FEEDBACK_FORM_URL = "feedbackFormUrl";
        String FEED_SCREEN = "feedScreen";
        String PLAYER_ID = "playerId";
        String ADDED_NEW_IMAGE_PATH = "addedNewImagePath";
        String PLAYER_USER_ID = "playerUserId";
        String SHOW_ANSWER_PERCENTAGE = "showAnswerPercentage";
        String MATCH_DETAILS = "matchDetails";
        String PLAYER_USER_NAME = "playerUserName";
        String LB_LANDING_DATA = "lbLandingData";
        String LB_LANDING_TITLE = "lbLandingTitle";
        String LB_LANDING_TYPE = "lbLandingType";
        String LB_LANDING_KEY = "lbLandingKey";
        String IS_DUMMY_GAME = "isDummyGame";
        String TOURNAMENT_POWERUPS = "tournamentPowerups";
        String POPUP_DATA = "popup";
        String FROM_SETTINGS = "fromSettings";
        String SCREEN = "screen";
        String FROM_PLAY = "fromPlay";
        String IS_ALL_GROUPS = "isAllGroups";
        String POWERUPS = "powerUps";
        String FOLLOWED_TOURS = "followedTours";
        String IS_ADMIN = "isAdmin";
        String GROUP_MEMBERS = "groupMembers";
        String CLICK_POSITION = "clickPosition";
        String OPEN_PROFILE = "openProfile";
        String POINTS = "points";
        String NO_OF_MATCHES = "matches";
        String ACCURACY = "accuracy";
        String NO_OF_BADGES = "badges";
        String LEVEL = "level";
        String NO_OF_SPORTS_FOLLOWED = "sportsFollowed";
        String PLAYER_NAME = "playerName";
        String PLAYER_PHOTO = "playerPhoto";
        String CHALLENGE_LIST = "challengeList";
        String CHALLENGE_TAG_ID = "challengeTagId";
        String UPDATED_CHALLENGE_USER_INFO = "updatedPowerups";
        String CHALLENGE_NAME = "challengeName";
        String MAX_TRANSFER_COUNT = "maxTransferCount";
        String CHALLENGE_INFO = "challengeInfo";
        String TAB_POSITION = "tabPosition";
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

    interface IntentActions {
        String ACTION_SHARE_SCORE = "in.sportscafe.nostragamus.intent.action.SHARE_SCORE";
        String ACTION_FUZZY_PLAYER_CLICK = "in.sportscafe.nostragamus.intent.action.FUZZY_PLAYER_CLICK";
        String ACTION_FUZZY_LB_CLICK = "in.sportscafe.nostragamus.intent.action.FUZZY_LB_CLICK";
        String ACTION_DUMMY_GAME_PLAY = "in.sportscafe.nostragamus.intent.action.DUMMY_GAME_PLAY";
        String ACTION_DUMMY_GAME_PROCEED = "in.sportscafe.nostragamus.intent.action.DUMMY_GAME_PROCEED";
        String ACTION_GROUP_CLICK = "in.sportscafe.nostragamus.intent.action.GROUP_CLICK";
        String ACTION_CHALLENGE_CLICK = "in.sportscafe.nostragamus.intent.action.CHALLENGE_CLICK";
        String ACTION_POWERUPS_UPDATED = "in.sportscafe.nostragamus.intent.action.POWERUPS_UPDATED";
    }

    interface LeaderBoardPeriods {
        String ALL_TIME = "alltime";
        String MONTH = "monthly";
        String WEEK = "weekly";
    }

    interface LBLandingType {
        String SPORT = "sport";
        String GROUP = "group";
        String CHALLENGE = "challenge";
        String TOURNAMENT = "tournament";
    }

    interface GameAttemptedStatus {
        int NOT = 0;
        int PARTIALLY = 1;
        int COMPLETELY = 2;
    }

    interface AnswerIds {
        int LEFT = 1;
        int RIGHT = 2;
        int NEITHER = 3;
    }

    interface Powerups {
        String XX_GLOBAL = "2x_global";
        String XX = "2x";
        String NO_NEGATIVE = "no_negs";
        String AUDIENCE_POLL = "player_poll";
        String MATCH_REPLAY = "match_replay";
        String ANSWER_FLIP = "answer_flip";
    }

    interface AnalyticsCategory {
        String LOGIN = "Login";
        String DUMMY_GAME = "Dummy Game";
        String GROUP = "Group";
        String NEW_USERS = "New User";
        String POWERUP = "Powerup";
        String TIMELINE = "Timeline";
        String LEADERBOARD = "Leaderboard";
        String LOGOUT = "Logout";
        String EDIT_PROFILE = "Edit Profile";
        String PROFILE = "Profile";
        String PLAY = "Play";
        String BADGE = "Badge";
        String FLAVOR = "Flavor";
        String APP_UPDATE = "App Update";
    }

    interface AnalyticsActions {
        String STARTED = "Started";
        String SKIPPED = "Skipped";
        String COMPLETED = "Completed";
        String INVITE_GROUP = "Invite Group";
        String REFERRAL = "Referral";
        String ORGANIC = "Organic";
        String JOIN_GROUP = "Join Group";
        String NEW_GROUP = "New Group";
        String LEAVE_GROUP = "Leave Group";
        String DELETE_GROUP = "Delete Group";
        String TOURNAMENT = "Tournament";
        String VIEW_ANSWERS = "View Answers";
        String PLAY = "Play";
        String CONTINUE = "Continue";
        String RESULT_WAITING = "Waiting For Results";
        String DID_NOT_PLAY = "Did Not Play";
        String LB_DETAIL = "Detail";
        String MYSELF = "Me";
        String OTHER_USER = "Other";
        String ANSWERED = "Answered";
        String SHUFFLED = "Shuffled";
        String PHOTO = "Photo";
        String OTHERS = "Others";
        String TABS = "Tabs";
        String RECEIVED = "Received";
        String APPLIED = "Applied";
    }

    interface AnalyticsLabels {
        String FACEBOOK = "Facebook";
        String GOOGLE = "Google Plus";
        String SCREENS_SEEN = "Screens Seen";
        String GALLERY = "Gallery";
        String CAMERA = "Camera";
        String UPDATE = "Update";
        String DONE = "Done";
        String LEFT = "Left";
        String RIGHT = "Right";
        String TOP = "Top";
        String BOTTOM = "Bottom";
    }

    interface UserProperties {
        String NUMBER_OF_GROUPS = "No of Groups";
    }

    interface ScreenNames {
        String PROFILE = "profile"; //in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment
        String PROFILE_EDIT = "edit_profile"; //in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity
        String PLAYER_PROFILE = "player_profile"; //in.sportscafe.nostragamus.module.user.playerprofile.PlayerProfileActivity
        String LEADERBOARD_LANDING = "leaderboard_landing"; //in.sportscafe.nostragamus.module.user.lblanding.LBLandingFragment
        String LEADERBOARD_DETAIL = "leaderboard_detail"; // in.sportscafe.nostragamus.module.user.points.PointsActivity
        String GROUPS_ALL = "groups_all"; // in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment
        String GROUPS_INFO = "groups_info";// in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity
        String GROUPS_JOIN = "groups_join";// in.sportscafe.nostragamus.module.user.group.joingroup.JoinGroupActivity
        String GROUPS_EDIT_GROUP = "groups_edit";// in.sportscafe.nostragamus.module.user.group.editgroupinfo.EditGroupInfoActivity
        String GROUPS_CREATE_NEW = "groups_create_new";// in.sportscafe.nostragamus.module.user.group.newgroup.NewGroupActivity
        String TOURNAMENTS_ALL = "tournaments_all";// in.sportscafe.nostragamus.module.tournament.TournamentFragment
        String TOURNAMENTS_TIMELINE = "tournaments_timeline";// in.sportscafe.nostragamus.module.feed.FeedActivity
        String PLAY = "play"; // in.sportscafe.nostragamus.module.play.prediction.PredictionActivity
        String RESULTS = "results";// in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity
        String LOGIN = "login";// in.sportscafe.nostragamus.module.user.login.LogInActivity
        String HOME = "home"; //in.sportscafe.nostragamus.module.home.HomeActivity
        String ADMIN_MEMBERS = "admin_members"; //in.sportscafe.nostragamus.module.user.group.admin.adminmembers.AdminMembersActivity
        String FLIP_POWERUP = "flip_powerup"; //in.sportscafe.nostragamus.module.play.myresults.flipPowerup.FlipActivity
        String GOOGLE_FORM = "google_form"; // in.sportscafe.nostragamus.module.feedback.GoogleFormActivity
        String OTHER_ANSWERS = "other_answers"; // in.sportscafe.nostragamus.module.othersanswers.OthersAnswersActivity
        String SPORT_SELECTION = "sport_selection"; //in.sportscafe.nostragamus.module.user.sportselection.SportSelectionActivity
        String PLAYER_COMPARISON = "player_comparison"; //in.sportscafe.nostragamus.module.user.comparisons.PlayerComparisonActivity
        String RESULTS_PEEK = "results_peek"; //in.sportscafe.nostragamus.module.resultspeek.ResultsPeekActivity
    }
}