package com.jeeva.android.facebook.user;

/**
 * Created by hmspl on 28/11/15.
 */
public interface FacebookPermission {

    /**
     * Provides access to the person's primary email address.
     *
     *<p>
     * Note: Even if you request the email permission it is not guaranteed you will get an email address.
     * For example, if someone signed up for Facebook with a phone number instead of an email address,
     * the email field may be empty.
     *</p>
     *
     * <p>
     * Review: Your app may use this permission without review from Facebook.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-email">EMAIL</a>
     */
    String EMAIL = "email";

    /**
     * Provides access to publish Posts, Open Graph actions, achievements, scores and other activity on behalf of a person using your app.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-publish_actions">PUBLISH ACTIONS</a>
     */
    String PUBLISH_ACTIONS = "publish_actions";

    /**
     * When you also have the manage_pages permission, gives your app the ability to post,
     * comment and like as any of the Pages managed by a person using your app.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-publish_pages">PUBLISH PAGES</a>
     */
    String PUBLISH_PAGES = "publish_pages";

    /**
     * Provides access to the names of custom lists a person has created to organize their friends.
     * This is useful for rendering an audience selector when someone is publishing stories to Facebook from your app.
     *
     *<p>
     * Note: This permission does not give access to a list of person's friends. If you want to access a person's
     * friends who also use your app, you should use the user_friends permission.
     *</p>
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-read_custom_friendlists">READ CUSTOM FRIENDLISTS</a>
     */
    String READ_CUSTOM_FRIENDLISTS = "read_custom_friendlists";

    /**
     * Provides access to a person's personal description (the 'About Me' section on their Profile) through
     * the bio property on the User object.
     *
     *<p>
     * Note: This permission does not give access to a person's public profile data. A person's name, profile picture,
     * locale, age range and gender are included by default with the public_profile permission.
     *</p>
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_about_me">USER ABOUT ME</a>
     */
    String USER_ABOUT_ME = "user_about_me";

    /**
     * Provides access to all common books actions published by any app the person has used.
     * This includes books they've read, want to read, rated or quoted.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_actions_books">USER ACTIONS BOOKS</a>
     */
    String USER_ACTIONS_BOOKS = "user_actions.books";

    /**
     * Provides access to all common Open Graph fitness actions published by any app the person has used.
     * This includes runs, walks and bikes actions.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_actions_fitness">USER ACTIONS FITNESS</a>
     */
    String USER_ACTIONS_FITNESS = "user_actions.fitness";

    /**
     * Provides access to all common Open Graph music actions published by any app the person has used.
     * This includes songs they've listened to, and playlists they've created.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_actions_music">USER ACTIONS MUSIC</a>
     */
    String USER_ACTIONS_MUSIC = "user_actions.music";

    /**
     * Provides access to all common Open Graph news actions published by any app the person has used which publishes
     * these actions. This includes news articles they've read or news articles they've published.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_actions_news">USER ACTIONS NEWS</a>
     */
    String USER_ACTIONS_NEWS = "user_actions.news";

    /**
     * Provides access to all common Open Graph video actions published by any app the person has used which publishes
     * these actions. This includes videos they've watched, videos they've rated and videos they want to watch.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_actions_video">USER ACTIONS VIDEO</a>
     */
    String USER_ACTIONS_VIDEO = "user_actions.video";

    /**
     * Access the date and month of a person's birthday. This may or may not include the person's year of birth,
     * dependent upon their privacy settings and the access token being used to query this field.
     *
     *<p>
     * Note: Please note most integrations will only need age_range which comes as part of the public_profile permission.
     *</p>
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_birthday">USER BIRTHDAY</a>
     */
    String USER_BIRTHDAY = "user_birthday";

    /**
     * Provides access to the list of friends that also use your app. These friends can be found on the friends edge on the user object.
     *
     *<p>
     * Note: In order for a person to show up in one person's friend list, both people must have decided to share their
     * list of friends with your app and not disabled that permission during login. Also both friends must have been
     * asked for user_friends during the login process.
     *</p>
     *
     * <p>
     * Review: Your app may use this permission without review from Facebook.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_friends">USER FRIENDS</a>
     */
    String USER_FRIENDS = "user_friends";

    /**
     * Provides access to the list of all Facebook Pages and Open Graph objects that a person has liked.
     * This list is available through the likes edge on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_likes">USER LIKES</a>
     */
    String USER_LIKES = "user_likes";

    /**
     * Provides access to the photos a person has uploaded or been tagged in.
     * This is available through the photos edge on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_photos">USER PHOTOS</a>
     */
    String USER_PHOTOS = "user_photos";

    /**
     * Provides access to the posts on a person's Timeline. Includes their own posts, posts they are tagged in,
     * and posts other people make on their Timeline.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_posts">USER POSTS</a>
     */
    String USER_POSTS = "user_posts";

    /**
     * Provides access to a person's current city through the location field on the User object. The current city is set by a person on their Profile.
     *
     *<p>
     * Note: The current city is not necessarily the same as a person's hometown.
     *</p>
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_location">USER LOCATION</a>
     */
    String USER_LOCATION = "user_location";

    /**
     * Provides access to a person's relationship status, significant other and family members as fields on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_relationships">USER RELEATIONSHIPS</a>
     */
    String USER_RELATIONSHIPS = "user_relationships";

    /**
     * Provides access to a person's relationship interests as the interested_in field on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_relationship_details">USER RELATIONSHIP DETAILS</a>
     */
    String USER_RELATIONSHIP_DETAILS = "user_relationship_details";

    /**
     * Provides access to the Places a person has been tagged at in photos, videos, statuses and links.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_tagged_places">USER TAGGED PLACES</a>
     */
    String USER_TAGGED_PLACES = "user_tagged_places";

    /**
     * Provides access to a person's work history and list of employers via the work field on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_work_history">USER WORK HISTORY</a>
     */
    String USER_WORK_HISTORY = "user_work_history";

    /**
     * Provides access to a person's education history through the education field on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_education_history">USER EDUCATION HISTORY</a>
     */
    String USER_EDUCATION_HISTORY = "user_education_history";

    /**
     * Provides access to read a person's game activity (scores, achievements) in any game the person has played.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_games_activity">USER GAMES ACTIVITY</a>
     */
    String USER_GAMES_ACTIVITY = "user_games_activity";

    /**
     * Provides access to a person's religious and political affiliations.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_religion_politics">USER RELIGION POLITICS</a>
     */
    String USER_RELIGION_POLITICS = "user_religion_politics";

    /**
     * Provides access to the videos a person has uploaded or been tagged in.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_videos">USER VIDEOS</a>
     */
    String USER_VIDEOS = "user_videos";

    /**
     * Provides read-only access to the Events a person is hosting or has RSVP'd to.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_events">USER EVENTS</a>
     */
    String USER_EVENTS = "user_events";

    /**
     * Provides access to a person's hometown location through the hometown field on the User object.
     * This is set by the user on the Profile.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_hometown">USER HOMETOWN</a>
     */
    String USER_HOMETOWN = "user_hometown";

    /**
     * Enables your app to read the Groups a person is an admin of through the groups edge on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_managed_groups">USER MANAGED GROUPS</a>
     */
    String USER_MANAGED_GROUPS = "user_managed_groups";

    /**
     * Provides access to the person's personal website URL via the website field on the User object.
     *
     * <p>
     * Review: If your app requests this permission Facebook will have to review how your app uses it.
     * </p>
     *
     * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions/v2.5#reference-user_website">USER WEBSITE</a>
     */
    String USER_WEBSITE = "user_website";
}