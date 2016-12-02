package in.sportscafe.nostragamus.module.user.login;

import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.login.dto.JwtToken;
import in.sportscafe.nostragamus.webservice.MyWebService;
import in.sportscafe.nostragamus.webservice.NostragamusCallBack;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by deepanshi on 12/2/16.
 */

public class RefreshTokenModelImpl {

    private RefreshTokenModelImpl() {
    }

    public static RefreshTokenModelImpl newInstance() {
        return new RefreshTokenModelImpl();
    }

    public void refreshToken() {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            callRefreshTokenApi();
        }
    }

    private void callRefreshTokenApi() {
        MyWebService.getInstance().getRefreshTokenRequest()
                .enqueue(new NostragamusCallBack<JwtToken>() {
                    @Override
                    public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                        super.onResponse(call, response);
                        if(response.isSuccessful()) {
                            handleRefreshTokenResponse(response.body());
                        }
                    }
                });
    }

    private void handleRefreshTokenResponse(JwtToken newToken) {
        NostragamusDataHandler.getInstance().setJwtToken(newToken);
    }
}
