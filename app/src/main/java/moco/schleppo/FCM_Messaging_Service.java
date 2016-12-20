package moco.schleppo;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by franziskaschmidt on 07.12.16.
 */

public class FCM_Messaging_Service extends FirebaseMessagingService {


    public void onMessageReceived(RemoteMessage remoteMessage){

        super.onMessageReceived(remoteMessage);




        if(remoteMessage.getFrom().equals("/topics" + FCM_Instance_ID_Service.kennzeichenTopicName)){

displayNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("contentText"));
            Log.d("push", remoteMessage.getData().get("title"));



        }


    }

    private void displayNotification(String title, String contentText){
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(14, notification);



    }
}
