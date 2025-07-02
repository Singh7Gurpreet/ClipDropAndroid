package helper;

import android.util.Log;

import com.google.gson.Gson;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.AbstractMap;
import java.io.IOException;

public class PersonalBinApiWrapper {

    private static final String BASE_URL = "https://personalbin.onrender.com/";
    private static final Retrofit retrofit;
    private static final ApiEndPoints api;

    // Static initialization block
    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiEndPoints.class);
    }

    // STATIC method - async version NOT recommended for immediate return
    public static void getFile(String token, CustomCallback<PersonalBinObject> myCallback) {
        String cookie = "token=" + token;
        Log.e("cookie",cookie);
        Call<PersonalBinObject> call = api.getFile(cookie);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PersonalBinObject> call, Response<PersonalBinObject> response) {
                if (response.isSuccessful()) {
                    PersonalBinObject file = response.body();
                    myCallback.onSuccess(file);
                } else {
                    if (response.code() == 404) {
                        System.out.println("NOT FOUND" + "Download file not found");
                    } else {
                        System.out.println("UNAUTHORIZED" + "Unauthorized user not allowed");
                    }
                    myCallback.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<PersonalBinObject> call, Throwable t) {
                myCallback.onFailure(null);
                t.printStackTrace();
            }
        });
    }

    // STATIC synchronous POST
    public static String postFile(String token, String fileName) {
        try {
            String cookie = "token=" + token;
            PersonalBinObject object = new PersonalBinObject(null, fileName);

            Call<PersonalBinObject> call = api.postFile(cookie, object);
            Response<PersonalBinObject> response = call.execute();  // BLOCKS

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getLink();
            } else {
                System.out.println("Error code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // STATIC verify method using callback
    public static void verifyJwt(String token, CustomCallback<Void> myCallback) {
        String cookie = "token=" + token;
        Call<Void> call = api.verifyJWT(cookie);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    myCallback.onSuccess(null);
                } else {
                    myCallback.onFailure(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                myCallback.onFailure(null);
            }
        });
    }

    public static void getKey(String uuid, CustomCallback<String> myCallback) {
        Call<PersonalBinObject> call = api.getKey(uuid);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PersonalBinObject> call, Response<PersonalBinObject> response) {
                if (response.isSuccessful()) {
                    PersonalBinObject object = response.body();
                    assert object != null;
                    myCallback.onSuccess(object.getToken());
                } else {
                    myCallback.onFailure("Error Code:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<PersonalBinObject> call, Throwable t) {
                t.printStackTrace();
                myCallback.onFailure("Error Occurred");
            }
        });
    }
}
