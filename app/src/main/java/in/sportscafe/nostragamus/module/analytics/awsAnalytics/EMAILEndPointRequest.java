package in.sportscafe.nostragamus.module.analytics.awsAnalytics;

/**
 * Created by deepanshi on 4/4/18.
 */

import com.amazonaws.services.pinpoint.AmazonPinpointClient;
import com.amazonaws.services.pinpoint.model.ChannelType;
import com.amazonaws.services.pinpoint.model.EndpointDemographic;
import com.amazonaws.services.pinpoint.model.EndpointRequest;
import com.amazonaws.services.pinpoint.model.EndpointUser;
import com.amazonaws.services.pinpoint.model.GetEndpointRequest;
import com.amazonaws.services.pinpoint.model.GetEndpointResult;
import com.amazonaws.services.pinpoint.model.UpdateEndpointRequest;
import com.amazonaws.services.pinpoint.model.UpdateEndpointResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

public class EMAILEndPointRequest {

    private static EMAILEndPointRequest emailEndPointRequest = new EMAILEndPointRequest();

    public static EMAILEndPointRequest getInstance() {
        return emailEndPointRequest;
    }

    public void createEndpoint(final AmazonPinpointClient client, final String appId) {

        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        String endpointId ="";
        if (userInfo != null) {
            endpointId = String.valueOf(userInfo.getId())+"EMAIL";
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

            if (userInfo.getUserPropertyInfo() != null) {
                Gson gson = new Gson();
                String json = gson.toJson(userInfo.getUserPropertyInfo());
                Type type = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> map = gson.fromJson(json, type);
                for (String key : map.keySet()) {
                    customAttributes.put(key, Arrays.asList(new String[]{String.valueOf(map.get(key))}));
                }
            }

            EndpointDemographic demographic = new EndpointDemographic()
                    .withAppVersion(Nostragamus.getInstance().getAppVersionName());

            EndpointUser user = new EndpointUser()
                    .withUserId(String.valueOf(userInfo.getId()));

            endpointRequest.withAddress(userInfo.getEmail())
                    .withChannelType(ChannelType.EMAIL)
                    .withAttributes(customAttributes)
                    .withDemographic(demographic)
                    .withOptOut("NONE")
                    .withRequestId(String.valueOf(userInfo.getId()))
                    .withUser(user);


        }

        return endpointRequest;
    }


}
