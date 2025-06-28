package helper;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiEndPoints {

    // 1. Get a file using a session cookie
    @GET("/api/file")
    Call<PersonalBinObject> getFile(@Header("Cookie") String cookie);

    // 2. Post a file using a session cookie
    @POST("/api/file")
    Call<PersonalBinObject> postFile(@Header("Cookie") String cookie, @Body PersonalBinObject object);

    // 3. Verify JWT via Cookie
    @GET("/api/verifyjwt")
    Call<Void> verifyJWT(@Header("Cookie") String cookie);

    // 4. Get session key using UUID
    @GET("/session/key")
    Call<PersonalBinObject> getKey(@Query("uuid") Integer uuid);
}
