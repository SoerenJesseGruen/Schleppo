package moco.schleppo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by franziskaschmidt on 07.12.16.
 */

public class FCM_Instance_ID_Service extends FirebaseInstanceIdService {

    final String tokenPreferenceKey = "fcm_token";

    final static String kennzeichenTopicName = "K-SK-266";

      public void onTokenRefresh(){

          super.onTokenRefresh();

          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
          sharedPreferences.edit().putString(tokenPreferenceKey, FirebaseInstanceId.getInstance().getToken()).apply();


          FirebaseMessaging.getInstance().subscribeToTopic(kennzeichenTopicName);

      }



}
