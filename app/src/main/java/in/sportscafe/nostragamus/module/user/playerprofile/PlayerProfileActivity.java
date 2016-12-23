package in.sportscafe.nostragamus.module.user.playerprofile;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jeeva.android.volley.Volley;
import java.util.List;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.RoundImage;


/**
 * Created by deepanshi on 12/22/16.
 */

public class PlayerProfileActivity extends NostragamusActivity implements PlayerProfileView {

    private String sportsFollowed;

    private String groupsCount;

    private String badgeCount;

    private PlayerProfilePresenter mProfilePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        this.mProfilePresenter = PlayerProfilePresenterImpl.newInstance(this);
        this.mProfilePresenter.onCreateProfile(getIntent().getExtras());

    }

    @Override
    public void setName(String name) {
        Typeface tftitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lato/Lato-Medium.ttf");
        TextView tvName=(TextView) findViewById(R.id.player_profile_tv_title);
        tvName.setText(name);
        tvName.setTypeface(tftitle);
    }

    @Override
    public void setProfileImage(String imageUrl) {
        ((RoundImage) findViewById(R.id.player_profile_iv_image)).setImageUrl(
                imageUrl,
                Volley.getInstance().getImageLoader(),
                false
        );
    }

    @Override
    public void setSportsFollowedCount(int sportsFollowedCount) {
//        TextView tvsports=(TextView) findViewById(R.id.profile_tv_sports_folld);
//        tvsports.setText(String.valueOf(sportsFollowedCount));
        sportsFollowed = String.valueOf(sportsFollowedCount);

    }

    @Override
    public void setGroupsCount(int GroupsCount) {
//        TextView tvGroup=(TextView) findViewById(R.id.profile_number_of_groups);
//        tvGroup.setText(String.valueOf(GroupsCount));
        groupsCount = String.valueOf(GroupsCount);
    }



    @Override
    public void setPoints(long points) {
        TextView tvPoints=(TextView) findViewById(R.id.player_profile_tv_points);
        tvPoints.setText(String.valueOf(points)+" Points");
    }

    @Override
    public void setBadgesCount(int badgesCount) {

        List<String> badgeList = NostragamusDataHandler.getInstance().getBadgeList();

        LinearLayout parent = (LinearLayout)findViewById(R.id.badges_ll);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams)parent.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        parent.setLayoutParams(layoutParams);

        if(badgeList.size() <= 8) {

            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.addView(layout2);
            layout2.setGravity(Gravity.CENTER_HORIZONTAL);


            for (int i = 0; i < badgeList.size(); i++) {
                String[] parts = badgeList.get(i).split("\\$");

                String badge_id = parts[0];

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                imageView.getLayoutParams().height = 60;
                imageView.getLayoutParams().width = 60;

                switch (badge_id) {
                    case "accuracy_streak":
                        imageView.setBackgroundResource(R.drawable.notification_accuracy_badge);
                        layout2.addView(imageView);
                        break;
                    case "table_topper":
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                    default:
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                }

            }

        }else if(badgeList.size()>8) {

            LinearLayout layout2 = new LinearLayout(getContext());
            layout2.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.addView(layout2);
            layout2.setGravity(Gravity.CENTER_HORIZONTAL);

            for (int i = 0; i < 8; i++) {
                String[] parts = badgeList.get(i).split("\\$");

                String badge_id = parts[0];

                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                imageView.getLayoutParams().height = 60;
                imageView.getLayoutParams().width = 60;

                switch (badge_id) {
                    case "accuracy_streak":
                        imageView.setBackgroundResource(R.drawable.notification_accuracy_badge);
                        layout2.addView(imageView);
                        break;
                    case "table_topper":
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                    default:
                        imageView.setBackgroundResource(R.drawable.notification_topper_badge);
                        layout2.addView(imageView);
                        break;
                }
            }


            LinearLayout layout3 = new LinearLayout(getContext());
            layout3.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            parent.addView(layout3);
            layout3.setGravity(Gravity.CENTER_HORIZONTAL);

            if(badgeList.size()<=16){

                for (int j = 8; j < badgeList.size(); j++) {

                    ImageView imageView2 = new ImageView(getContext());
                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView2.getLayoutParams().height = 60;
                    imageView2.getLayoutParams().width = 60;

                    String[] part = badgeList.get(j).split("\\$");
                    String badgeId = part[0];

                    switch (badgeId) {
                        case "accuracy_streak":
                            imageView2.setBackgroundResource(R.drawable.notification_accuracy_badge);
                            layout3.addView(imageView2);
                            break;
                        case "table_topper":
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                        default:
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                    }
                }

            }else if (badgeList.size()>16){

                for (int j = 8; j < 16; j++) {

                    ImageView imageView2 = new ImageView(getContext());
                    imageView2.setLayoutParams(new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView2.getLayoutParams().height = 60;
                    imageView2.getLayoutParams().width = 60;

                    String[] part = badgeList.get(j).split("\\$");
                    String badgeId = part[0];

                    switch (badgeId) {
                        case "accuracy_streak":
                            imageView2.setBackgroundResource(R.drawable.notification_accuracy_badge);
                            layout3.addView(imageView2);
                            break;
                        case "table_topper":
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                        default:
                            imageView2.setBackgroundResource(R.drawable.notification_topper_badge);
                            layout3.addView(imageView2);
                            break;
                    }
                }

                TextView textview = new TextView(getContext());
                RelativeLayout.LayoutParams lpTextView = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                textview.setLayoutParams(lpTextView);
                textview.setPadding(5,10,5,5);
                textview.setText("+ " + (badgeList.size()-16) + " More");
                textview.setTextColor(Color.WHITE);
                layout3.addView(textview);

            }

        }

        if (badgesCount==0){
            badgeCount = "0";
        }
        badgeCount = String.valueOf(badgesCount);

    }

}
