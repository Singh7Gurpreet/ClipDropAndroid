package helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Browser;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class Authentication {
     static String token;
     String SECRET_KEY="SECRET";
    private Authentication() {

    }

    public static void pollSession(int uuid, CustomCallback<String> callback) {
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
                    public void onSuccess(String result) {
                        if (result != null && !result.isEmpty()) {
                            token = result;
                            callback.onSuccess(token);
                            Log.i("TOKEN",token);
                            // stop polling
                        } else {
                            retry();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        retry();
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

        handler.post(pollingTask[0]); // start the polling
    }


    public static void initialize(Context context, CustomCallback<String> callback) {
        // it will check for token first from localfile
        // if exists simply check for it authentication
        // if passed simply proceed to our wanted fragment
        // else open this intent with poll session

        // to-do on success store this key somewhere alright!

        int id = 12;
        String url = "https://personalbin.onrender.com/auth/google?uuid=" + id;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);

        pollSession(id,callback);
    }
    public static String getToken() {
        return token;
    }
}
