package in.sportscafe.nostragamus;

import android.Manifest;
import android.bluetooth.le.ScanRecord;

/**
 * Created by Jeeva on 30/3/15.
 */
public interface Constants {

    String IMAGE_TYPE = "png";

    interface ImageFolders {
        String MAIN = "Nostragamus_Images";

        String PROFILE = "Profile_Images";
    }

    interface NotificationKeys {
        String RESULTS_LEADERBOARD = "results_leaderboard";
        String FROM_NOTIFICATION = "fromNotification";

        String NEW_CHALLENGE_ID = "challenge_id";
    }

    interface Screens {
        int CHALLENGES = 1;
        int GROUP = 2;
        int LEADER_BOARD = 3;
        int PROFILE = 4;
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
        String NUMBER_OF_MATCH_PLAYED = "numberofmatchplayed";
        String NUMBER_OF_TOTAL_GROUPS = "numberoftotalgroups";
        String NUMBER_OF_POWERUPS_USED = "numberofpowerupsused";
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
        String NORMAL_PAID_UPDATE_VERSION = "normalPaidUpdateVersion";
        String FORCE_PAID_UPDATE_VERSION = "forcePaidUpdateVersion";
        String NORMAL_PAID_UPDATE_ENABLED = "normalPaidUpdateEnabled";
        String NORMAL_PAID_UPDATE_MESSAGE = "normalPaidUpdateMessage";
        String FORCE_PAID_UPDATE_MESSAGE = "forcePaidUpdateMessage";
        String NORMAL_PAID_UPDATE_SHOWN_TIME = "normalPaidUpdateShownTime";
        String PAID_FORCE_UPDATE_APK_LINK = "paidForceUpdateApkLink";
        String PAID_NORMAL_UPDATE_APK_LINK = "paidNormalUpdateApkLink";
        String FORCE_UPDATE_ENABLED = "forceUpdateEnabled";
        String REQUIRED_UPDATE_VERSION = "requiredUpdateVersion";
        String EDIT_PROFILE_SHOWN_TIME = "editProfileShownTime";

        String INITIAL_FORM_SHOWN = "initialFormShown";
        String ACCESS_TOKEN = "accessToken";
        String TOKEN_EXPIRY = "tokenExpiry";
        int TOURNAMENT_ID = 0;
        String INSTALL_CHANNEL = "installChannel";
        String BANK_INFO_SHOWN = "bankInfoShown";
        String FIRST_MATCH_PLAYED = "firstMatchPlayed";
        String SECOND_MATCH_PLAYED = "secondMatchPlayed";
        String FIFTH_MATCH_PLAYED = "fifthMatchPlayed";
        String SEVENTH_MATCH_PLAYED = "seventhMatchPlayed";
        String VISITED_LEADERBOARDS = "VisitedLeaderboards";
        String VISITED_SUBMIT_QUESTION = "VisitedSubmitQuestion";
        String POWERUP_APPLIED = "PowerUpApplied";
        String DUMMY_GAME_SHOWN = "dummyGameShown";
        String FEEDBACK = "feedback";
        String PRO_FEEDBACK = "proFeedback";
        String DOWNLOAD_PAID_APP = "downloadPaidApp";
        String ASK_FRIEND = "askFriendText";
        String DISCLAIMER_TEXT = "disclaimerText";
        String PAYMENT_DETAILS_SCREEN_SHOWN_ONCE_ON_HOME = "paymentDetailsShownOnHome";
        String IS_PROFILE_DISCLAIMER_ACCEPTED = "isProfileDisclaimerAccepted";
        String INSTALL_REFERRAL_CAMPAIGN_KEY = "installReferralCampaignKey";

        String LAST_SHOWN_APP_VERSION_CODE = "lastShownAppVersionCode";
        String UPDATE_TYPE = "updateType";
        String WALLET_INITIAL_AMOUNT = "walletInitialAmount";
        String MARKETING_CAMPAIGN = "marketingCampaign";
        String WHATS_NEW_SHOWN = "whatsNewShown";
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
        String NICKNAME_EMPTY = "Please enter username";
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
        String PERMISSION_QUIT_TEXT = "Cancel";
        String PERMISSION_HELP = "Help";
        String PERMISSION_SETTINGS = "Settings";
        String PERMISSION_HELP_TEXT = "The permission is missing.\n\nTo grant this permission click on Settings, then Permissions and turn the permission on.\n\nThen click back twice to return to the app.";
        String IMAGE_UPLOAD_TEXT = "Select Image to Upload";
        String IMAGE_RESELECT = "Click on Image to Reselect";
        String IMAGE_UNABLE_TO_LOAD = "Unable to Load Image";
        String IMAGE_UNABLE_TO_PICK = "Unable to Pick Image";
        String IMAGE_UPLOADING = "Uploading.. Wait for a while...";
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
        String INVALID_MOBILE_NUMBER = "Invalid mobile number";
        String CONFIRM_MOBILE_NUMBER_VARY = "Confirm mobile number not matching";
        String INVALID_EMAIL = "Invalid email id";
        String CONFIRM_EMAIL_VARY = "Confirm email id not matching";
        String PAYTM_ADD_DETAIL_SUCCESS = "Your paytm detail saved successfully";
        String QUESTION_LIMIT_ALERT = "The question should have atleast 10 characters";
        String CONTEXT_LIMIT_ALERT = "The context should have atleast 20 characters";
        String LEFT_OPTION_EMPTY = "The left option should not be empty";
        String RIGHT_OPTION_EMPTY = "The right option should not be empty";
        String SUBMIT_QUESTION_SUCCESS = "Your question is submitted successfully";
        String EMPTY_MATCHES = "Currently no upcoming matches";
        String DEFAULT_SHARE_MESSAGE = "Use paste, If you want to use the default share message!";
        String POLL_LIST_EMPTY = "No polls";
        String NOT_FREE_CHALLENGE = "Please use full version for the paid challenges";
        String EDIT_PROFILE_DISCLAIMER_CHECK_REQUIRED = "Oops! Select the checkbox to proceed as if you do not meet these restrictions, you are not allowed to play the full version of the game";
        String FORCE_UPDATE_PROFILE_MSG_FOR_PAID_VERSION = "Please update your profile";

        /*--- Paytm Msg ---*/
        String PAYTM_AUTHENTICATION_FAILED = "Could not transact, please try again";
        String PAYTM_TRANSACTION_CANCELLED = "You have cancelled transaction, can not continue to join";
        String PAYTM_TRANSACTION_FAILED = "Transaction failed, please try again";
        String PAYTM_FAILURE = "Could not initiate transaction, please try again";
        String SOMETHING_WRONG ="Something went wrong! Please try again!";
        String NO_UPDATES = "No New Updates Available";
        String INVALID_PHONE_NUMBER = "Enter 10 digit number";
        String PHONE_NUMBER_EXIST = "Mobile Number is already registered";
        String INVALID_OTP = "Invalid OTP";
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
        String TOURNAMENT_POWERUPS = "tournamentPowerups";
        String POPUP_DATA = "popup";
        String IN_APP_POPUP_DATA = "inAppPopup";
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
        String CHALLENGE_PHOTO = "challengePhoto";
        String MAX_TRANSFER_COUNT = "maxTransferCount";
        String CHALLENGE_INFO = "challengeInfo";
        String TAB_POSITION = "tabPosition";
        String DUMMY_QUESTION = "dummyQuestion";
        String DUMMY_INSTRUCTION = "dummyInstruction";
        String DUMMY_QUESTION_TYPE = "dummyQuestionType";
        String DIALOG_REQUEST_CODE = "dialogRequestCode";
        String IS_SEPARATE_SCREEN = "isSeparateScreen";
        String CHALLENGE_DATA = "challengeData";
        String TITLE = "title";
        String CHALLENGE_SWITCH_POS = "challengeSwitchPos";
        String CONFIG_INDEX = "configIndex";
        String CONFIG_NAME = "configName";
        String CHALLENGE_END_TIME = "challengeEndTime";
        String CHALLENGE = "challenge";
        String ENTRY_FEE = "entryFee";
        String JOINED_CHALLENGE_INFO = "joinedChallenegInfo";
        String OPEN_LEADERBOARD = "openLeaderboard";
        String USER_PAYMENT_INFO_PARCEL = "userPaymentInfoParcel";
        String EDIT_PROFILE_LAUNCHED_FROM = "editProfileLaunchedFrom";
        String TAB_ITEM_NAME = "tabItemName";
        String PLAYED_FIRST_MATCH = "PlayedFirstMatch";
        String SERVER_TIME = "serverTime";
        String TRANSACTION_AMOUNT = "transactionAmount";
        String BALANCE_AMOUNT = "balanceAmount";
        String CHALLENGE_CONFIG = "challengeConfig";
        String USER_REFERRAL_INFO = "userReferralInfo";
        String PHONE_NUMBER = "phoneNumber";
        String USER_PHOTO = "user_photo";
        String USER_NAME = "user_name";

        /* Wallet */
        String WALLET_WITHDRAWAL_AMT = "walletWithdrawAmt";
        String WITHDRAW_STATUS_CODE = "withdrawFailureCode";
        String WITHDRAW_AMOUNT = "withdrawAmount";
        String WITHDRAW_PAYOUT_TYPE = "withdrawPayoutType";
        String WALLET_INITIAL_AMOUNT = "wallet_init";
        String USER_REFERRAL_CODE = "user_referral_code";
        String USER_REFERRAL_NAME = "user_referral_name";
        String USER_REFERRAL_PHOTO = "user_referral_photo";
        String DIALOG_LAUNCH_MODE = "dialogLaunchMode";

        String SUCCESSFUL_REFERRAL = "successfulReferral";
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
        String FORMAT_DATE_T_TIME_ZONE_ZZ_ZZ = "yyyy-MM-dd'T'HH:mm:ssZ";
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
        String ACTION_NEW_CHALLENGE_ID = "in.sportscafe.nostragamus.intent.action.NEW_CHALLENGE_ID";
        String ACTION_ALL_CHALLENGE_DATA_LOADED = "in.sportscafe.nostragamus.intent.action.ALL_CHALLENGE_DATA_LOADED";
        String ACTION_SCROLL_CHALLENGE = "in.sportscafe.nostragamus.intent.action.SCROLL_CHALLENGE";
        String ACTION_RELOAD_CHALLENGES = "in.sportscafe.nostragamus.intent.action.RELOAD_CHALLENGES";
        String ACTION_OPEN_WEBVIEW= "in.sportscafe.nostragamus.intent.action.OPEN_WEBVIEW";
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

        int POWER_UP_TYPE_TAG = 11;
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
        String POWER_BANK = "Power Bank";
        String APP_OPENING = "App Opening";
        String REFERRAL_ACTION = "Referral Action";
        String CASH_REWARDS = "Cash Rewards";
        String CONFIGS = "Configs";
        String WHATS_NEW = "Whats New";
        String UPDATE_LATER = "Update Later";
        String WALLET_ADD_MONEY = "Add Money into Wallet";
        String WALLET_WITHDRAW_MONEY = "Withdraw from Wallet";
        String ONBOARDING_TIME = "OnBoarding Time";
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
        String ADDED = "Added";
        String SWITCH = "Switch";
        String PRO_APP = "Pro App";
        String NORMAL_APP = "Normal App";
        String NO_CASH_REWARDS = "Don't Want Cash Rewards";
        String OPENED = "Opened";
        String CLICKED = "Clicked";
        String ONBOARDING_TIME = "OnBoarding Time";
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
        String LAUNCHER = "Launcher";
        String NOTIFICATION = "Notification";
        String GROUP = "Group";
        String PROFILE = "Profile";
    }

    interface UserProperties {
        String NUMBER_OF_GROUPS = "No of Groups";
        String PRO_APP = "Pro App";
        String REFERRAL_CHANNEL = "Channel";
        String REFERRAL_CAMPAIGN = "Campaign";
        String WALLET_INIT = "Wallet Initial Amount";
    }

    interface AppType {
        String PRO = "PRO";
        String PLAYSTORE = "PS";
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
        String TOURNAMENTS_ALL = "tournaments_all";// in.sportscafe.nostragamus.module.tournament.TourListFragment
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
        String DUMMY_GAME = "dummy_game"; //DummyGameActivity
        String PAYTM_CONNECT = "paytm_connect"; //in.sportscafe.nostragamus.module.paytm.WalletOrBankConnectActivity
        String PAYTM_ADD_DETAIL = "paytm_add_detail"; //in.sportscafe.nostragamus.module.paytm.AddPaytmDetailsActivity
        String QUESTION_ADD = "question_add"; //AddQuestionActivity
        String QUESTION_ADD_TOUR_LIST = "question_add_tour_list"; //TourListActivity
        String USER_PROFILE = "user_profile"; //in.sportscafe.nostragamus.module.user.myprofile.ProfileActivity
        String ADD_PAYMENT_BANK_DETAILS = "add_payment_bank_details";

        String SETTINGS = "settings";
        String ABOUT = "about";
        String HELP = "help";
        String WHATS_NEW = "whatsNew";
        String APP_UPDATE = "AppUpdate";
        String APP_FORCE_UPDATE = "AppForceUpdate";
        String WALLET_HOME = "wallet";
        String WALLET_ADD_MONEY = "addWalletMoney";
        String WALLET_WITHDRAW_MONEY = "withdrawWalletMoney";
        String WALLET_HISTORY = "walletHistory";
        String WALLET_PAYOUT_HOME = "walletPayoutHome";
        String WALLET_PAYOUT_CHOICE = "walletPayoutChoice";
        String DOWNLOADING_APP = "DownloadingApp";
        String REFER_FRIEND = "ReferFriend";
        String REFERRAL_CREDIT = "ReferralCredit";
        String SUCCESSFUL_REFERRAL = "successfulReferral";
        String POWERUP_BANK = "PowerupBank";
        String VERIFY_PROFILE = "VerifyProfile";
        String STORE = "Store";
    }

    interface InAppPopups {

        String IN_APP_POPUP_TYPE = "in_app_popup_type";
        String FIRST_MATCH_PLAYED = "firstMatchPlayed";
        String SECOND_MATCH_PLAYED_WITH_NO_POWERUP = "secondMatchPlayedWithNoPowerUp";
        String FIFTH_MATCH_PLAYED_WITH_NO_POWERUP = "fifthMatchPlayedWithNoPowerUp";
        String SEVENTH_MATCH_PLAYED_WITH_NO_GROUPS = "seventhMatchPlayedWithNoGroups";
        String LESS_POWERUPS = "lessPowerUps";
        String NOT_VISITED_OTHER_PROFILE = "notVisitedOtherProfile";
        String SUBMIT_QUESTION = "submitQuestion";


    }

    interface AppPermissions {

        String[] STORAGE = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    interface RequestCodes {

        int STORAGE_PERMISSION = 134;
    }

    interface PaytmParamsKeys {
        String REQUEST_TYPE = "REQUEST_TYPE";
        String MID = "MID";
        String ORDER_ID = "ORDER_ID";
        String CUST_ID = "CUST_ID";
        String CHANNEL_ID = "CHANNEL_ID";
        String INDUSTRY_TYPE_ID = "INDUSTRY_TYPE_ID";
        String WEBSITE = "WEBSITE";
        String TXN_AMOUNT = "TXN_AMOUNT";
        String EMAIL = "EMAIL";
        String MOBILE_NO = "MOBILE_NO";
        String CHECKSUMHASH = "CHECKSUMHASH";
        String CALLBACK_URL = "CALLBACK_URL";
    }

    interface PaytmParamValues {
        String REQUEST_TYPE_DEFAULT = "DEFAULT";
        String MID_VALUE = "GAMBIT01441193333466";
        String CHANNEL_ID_VALUE = "WAP";
        String INDUSTRY_TYPE_ID_VALUE = "Retail";
        String WEBSITE_VALUE = "APP_STAGING";
        String CALLBACK_URL_VALUE = "https://api-stage.sportscafe.in/v2/game/verifyChecksum"; //"https://api-stage.sportscafe.in/v2/game/callBackPaytm";
        String EMAIL_VALUE = "";
        String MOBILE_NO_VALUE = "";
    }

    interface PaytmSuccessResponseParamKeys {
        /* Taken from Paytm API documentation */

        String MID = "MID";
        String ORDER_ID = "ORDERID";
        String TRANSACTION_ID = "TXNID";
        String BANK_TRANSACTION_ID = "BANKTXNID";
        String TRANSACTION_AMOUNT = "TXNAMOUNT";
        String CURRENCY = "CURRENCY";
        String STATUS = "STATUS";
        String RESPONSE_CODE = "RESPCODE";
        String RESPONSE_MESSAGE = "RESPMSG";
        String TRANSACTION_DATE = "TXNDATE";
        String GATEWAY_NAME = "GATEWAYNAME";
        String BANK_NAME = "BANK_NAME";
        String PAYMENT_MODE = "PAYMENTMODE";
        String CHECKSUM_HASH = "CHECKSUMHASH";
        String JOINED_CHALLENGE_INFO = "joined_challenge_info";
     }

     interface PaytmTransactionResponseStatusValues {
          /* NOTE: Values are from Paytm API documentation, should be compared against response_status to know transaction status
          * DO NOT CHANGE VALUES ... */

         String TRANSACTION_SUCCESS = "TXN_SUCCESS";
         String TRANSACTION_FAILURE = "TXN_FAILURE";
         String TRANSACTION_PENDING = "PENDING";
         String TRANSACTION_OPEN = "OPEN";
     }

     interface AddUserPaymentDetailsPaymentModes {
         String BANK = "bank";
         String PAYTM = "paytm";
     }

     interface AppUpdateTypes {
         String NORMAL_UPDATE = "Normal";
         String FORCE_UPDATE = "Force";
         String NORMAL_PAID_UPDATE = "Normal_Paid";
         String FORCE_PAID_UPDATE = "Force_Paid";
     }

    public interface MoneyFlow {
        String IN = "in";   // Debit
        String OUT = "out"; // credit

        String STATUS_CODE_INITIATED = "0";
        String STATUS_CODE_SUCCESS = "1";
        String STATUS_CODE_FAILURE = "2";
    }

    interface WalletHistory {
        String TRANSACTION_TYPE_WITHDRAW = "withdraw";
        String TRANSACTION_TYPE_DEPOSIT = "deposit";
        String TRANSACTION_TYPE_PROMO = "promo";
        String TRANSACTION_TYPE_JOINING = "joining";
        String TRANSACTION_TYPE_WINNING = "winning";

        String TRANSACTION_STATUS_INITIATED = "initiated";
        String TRANSACTION_STATUS_SUCCESS = "success";
        String TRANSACTION_STATUS_FAILED = "failed";

        String TRANSACTION_ACCOUNT_PAYTM = "paytm";
        String TRANSACTION_ACCOUNT_BANK = "bank";
    }

    interface ReferralHistory {
        String REFERRAL_HISTORY_TYPE_POWERUPS = "powerup";
        String REFERRAL_HISTORY_TYPE_MONEY = "promoMoney";
    }

    interface ChallengeTabs {
        String NEW = "New";
        String IN_PLAY = "In Play";
        String COMPLETED = "Completed";
    }

    interface WebPageUrls {
        String GAME_PLAY = "http://nostragamus.in/gameplayapp.html";
        String RULES = "http://nostragamus.in/rulesapp.html";
        String FAQ = "http://nostragamus.in/faqapp.html";
        String ABOUT_NOSTRAGAMUS = "http://nostragamus.in/about.html";
        String TERMS_SERVICE = "http://nostragamus.in/terms.html";
        String PRIVACY = "http://nostragamus.in/privacy.html";
        String REFERRAL_TERMS = "http://nostragamus.in/referralapp.html";

    }

    /* Sync with server */
    interface WithdrawFromWalletResponseCode {
        int SUCCESS = 1; // Payment will be made within 24 hrs
        int ERROR_INSUFICIENT_BALANCE = 2; // Withdrawal more than wallet balance
        int ERROR_MIN_BALANCE_REQUIRED = 3; // Need to maintain min balance
        int ERROR_UNKNOWN = 4;      // Unknown server side error
    }

    public static final String AMOUNT_DECIMAL_PATTERN = "#.00";
    public static final String RUPEE_SYMBOL = "₹ ";
}