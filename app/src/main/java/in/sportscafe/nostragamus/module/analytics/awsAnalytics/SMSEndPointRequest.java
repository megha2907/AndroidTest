package in.sportscafe.nostragamus.module.analytics.awsAnalytics;

/**
 * Created by deepanshi on 4/4/18.
 */

import com.amazonaws.services.pinpoint.AmazonPinpointClient;
import com.amazonaws.services.pinpoint.model.EndpointDemographic;
import com.amazonaws.services.pinpoint.model.EndpointRequest;
import com.amazonaws.services.pinpoint.model.EndpointUser;
import com.amazonaws.services.pinpoint.model.GetEndpointRequest;
import com.amazonaws.services.pinpoint.model.GetEndpointResult;
import com.amazonaws.services.pinpoint.model.UpdateEndpointRequest;
import com.amazonaws.services.pinpoint.model.UpdateEndpointResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

public class SMSEndPointRequest {

    private static SMSEndPointRequest nostragamusAwsPinPointRequest = new SMSEndPointRequest();

    public static SMSEndPointRequest getInstance() {
        return nostragamusAwsPinPointRequest;
    }

    public void createEndpoint(final AmazonPinpointClient client, final String appId) {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        String endpointId ="";
        if (userInfo != null) {
            endpointId = String.valueOf(userInfo.getId())+"SMS";
        }
        System.out.println("Endpoint ID: " + endpointId);

        EndpointRequest endpointRequest = createEndpointRequestData();

        final UpdateEndpointRequest updateEndpointRequest = new UpdateEndpointRequest()
                .withApplicationId(appId)
                .withEndpointId(endpointId)
                .withEndpointRequest(endpointRequest);

        final String finalEndpointId = endpointId;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UpdateEndpointResult updateEndpointResponse = client.updateEndpoint(updateEndpointRequest);
                    System.out.println("Update Endpoint Response: " + updateEndpointResponse.getMessageBody());
                    GetEndpointRequest getEndpointRequest = new GetEndpointRequest()
                            .withApplicationId(appId)
                            .withEndpointId(finalEndpointId);
                    GetEndpointResult getEndpointResult = client.getEndpoint(getEndpointRequest);

                    System.out.println("Got Endpoint: " + getEndpointResult.getEndpointResponse().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    private EndpointRequest createEndpointRequestData() {

        HashMap<String, List<String>> customAttributes = new HashMap<>();
        EndpointRequest endpointRequest = new EndpointRequest();

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null) {
            customAttributes.put(Constants.UserProperties.COUNT_REFERRALS, Arrays.asList(new String[]{String.valueOf(userInfo.getReferralCount())}));
            customAttributes.put(Constants.UserProperties.HAS_REFERRED, Arrays.asList(new String[]{String.valueOf(userInfo.isHasReferred())}));
            customAttributes.put(Constants.UserProperties.HAS_DEPOSITED, Arrays.asList(new String[]{String.valueOf(userInfo.isHasDeposited())}));
            customAttributes.put(Constants.UserProperties.COUNT_DEPOSITS, Arrays.asList(new String[]{String.valueOf(userInfo.getDepositCount())}));
            customAttributes.put(Constants.UserProperties.COUNT_CONTESTS_JOINED, Arrays.asList(new String[]{String.valueOf(userInfo.getContestJoinedCount())}));
            customAttributes.put(Constants.UserProperties.COUNT_PAID_CONTESTS_JOINED, Arrays.asList(new String[]{String.valueOf(userInfo.getPaidContestJoinedCount())}));
            customAttributes.put(Constants.UserProperties.MOST_PLAYED_SPORT, Arrays.asList(new String[]{String.valueOf(userInfo.getMostPlayedSport())}));

            EndpointDemographic demographic = new EndpointDemographic()
                    .withAppVersion(Nostragamus.getInstance().getAppVersionName());

            EndpointUser user = new EndpointUser()
                    .withUserId(String.valueOf(userInfo.getId()));

            endpointRequest.withAddress(userInfo.getOtpMobileNumber())
                    .withChannelType("SMS")
                    .withAttributes(customAttributes)
                    .withDemographic(demographic)
                    .withOptOut("NONE")
                    .withRequestId(String.valueOf(userInfo.getId()))
                    .withUser(user);


        }

        return endpointRequest;
    }


}
