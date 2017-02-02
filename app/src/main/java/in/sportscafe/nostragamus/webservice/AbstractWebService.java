package in.sportscafe.nostragamus.webservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.jeeva.android.converter.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import in.sportscafe.nostragamus.NostragamusDataHandler;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by Jeeva on 15/3/16.
 */
public abstract class AbstractWebService<T> {

    private ObjectMapper mObjectMapper;

    public T init(String baseUrl, Class<T> service) {
        // Initialize ObjectMapper
        initObjectMapper();

        // Initialize SportsCafe Api service using retrofit
        return initService(baseUrl, service);
    }

    private T initService(String baseUrl, Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(mObjectMapper))
                .client(getCustomHttpClient())
                .build();
        return retrofit.create(service);
    }

    private OkHttpClient getCustomHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        httpClient.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                String token = NostragamusDataHandler.getInstance().getAccessToken();
                if(null != token) {
                    builder.addHeader("Authorization", token);
                    Log.d("Authorization", token);
                }

                return chain.proceed(builder.build());
            }
        });

        return httpClient.build();
    }

    private void initObjectMapper() {
        mObjectMapper = new ObjectMapper();
        mObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> String getJsonStringFromObject(T object) {
        try {
            return mObjectMapper.writeValueAsString(object);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public <T> T getObjectFromJson(String json, Class<T> classType) {
        try {
            return mObjectMapper.readValue(json, classType);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public <T> T getObjectFromJson(String json, CollectionType classType) {
        try {
            return mObjectMapper.readValue(json, classType);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public <T> T getObjectFromJson(String json, TypeReference<T> typeReference) {
        try {
            return mObjectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
        return null;
    }


}