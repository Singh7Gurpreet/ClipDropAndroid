package helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import java.util.UUID;

public class Authentication {
     static String token;
     static String KEY_NAME = "JWT_TOKEN";
     static KeyStorage keyStorage;

    String SECRET_KEY="SECRET";
    private Authentication() {

    }

    public static void pollSession(String uuid, CustomCallback<String> callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        int POLL_INTERVAL_MS = 3000;
        final int[] pollCount = {0};
        int MAX_POLLS = 10;

        Runnable[] pollingTask = new Runnable[1]; // create array with 1 slot

        pollingTask[0] = new Runnable() {
            @Override
            public void run() {
                PersonalBinApiWrapper.getKey(uuid, new CustomCallback<String>() {
                    @Override
                    public boolean onSuccess(String result) {

                        if (result != null && !result.isEmpty()) {
                            keyStorage.saveKeyValue(KEY_NAME,result);
                            callback.onSuccess(result);
                        } else {
                            retry();
                        }
                        return false;
                    }

                    @Override
                    public boolean onFailure(String errorMessage) {
                        retry();
                        return false;
                    }

                    private void retry() {
                        pollCount[0]++;
                        if (pollCount[0] < MAX_POLLS) {
                            handler.postDelayed(pollingTask[0], POLL_INTERVAL_MS);
                        } else {
                            callback.onFailure("Max retries reached.");
                        }
                    }
                });
            }
        };

        handler.post(pollingTask[0]);
    }


    public static void initialize(Context context, CustomCallback<String> callback) {
        keyStorage = new KeyStorage(context);
        String result = getToken();
        if (result == null) {
            String id = UUID.randomUUID().toString();
            String url = "https://personalbin.onrender.com/auth/google?uuid=" + id;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
            pollSession(id,callback);
        } else {
            setToken(result);
            callback.onSuccess(result);
        }
    }
    public static String getToken() {
        String result = keyStorage.getKeyValue(KEY_NAME);

        return result;
    }
    private static void setToken(String t) {
        keyStorage.saveKeyValue(KEY_NAME,t);
        token = t;
    }

}
