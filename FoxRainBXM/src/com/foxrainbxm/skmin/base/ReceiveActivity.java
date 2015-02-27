package com.foxrainbxm.skmin.base;
/* http://techlovejump.in/2013/11/android-push-notification-using-google-cloud-messaging-gcm-php-google-play-service-library/
 * techlovejump.in
 * tutorial link
 * 
 *  */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ReceiveActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (isTaskRoot())
        {
            Intent startAppIntent = new Intent(getApplicationContext(), SplashActivity.class);
            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startAppIntent);
        }

        finish();
	}

}
