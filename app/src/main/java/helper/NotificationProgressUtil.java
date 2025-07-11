package helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.clipdrop.R;

public class NotificationProgressUtil {
    public static final String CHANNEL_ID = "my_channel_1";
    private static final int NOTIFICATION_ID = 1002;
    private Context myContext;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private UPLOAD_DOWNLOAD type;

    public NotificationProgressUtil(UPLOAD_DOWNLOAD type, Context context) {
        this.type = type;
        this.myContext = context;

        // Initialize NotificationManager only once
        notificationManager = (NotificationManager) myContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the Notification Channel if needed (Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Clipboard Notifications";
            String description = "Notification for showing progress";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(null, null);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void showNotification() {
        builder = new NotificationCompat.Builder(myContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(type.getValue() + " in Progress")
                .setContentText(type.getValue() + " is 0% complete")
                .setProgress(100, 0, false)  // max value, current value, indeterminate state
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public void updateProgress(int progress) {
        builder.setProgress(100, progress, false)
                .setContentText(type.getValue() + " is " + progress + "% complete");

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public void completeProgressNotification() {
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(type.getValue() + " Complete")
                .setContentText(type.getValue() + " has finished.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setProgress(0, 0, false);  // Reset progress

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());  // Notify with the completion message
        }
    }
}
