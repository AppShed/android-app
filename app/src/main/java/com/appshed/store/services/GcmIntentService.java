package com.appshed.store.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.appshed.store.R;
import com.appshed.store.activities.LaunchActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

/**
 * Created by Anton Maniskevich on 24.07.2014.
 */
public class GcmIntentService extends IntentService {

	public static final String TAG = GcmIntentService.class.getName();
	private NotificationManager notificationManager;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				sendNotification();
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification() {
		notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		//Intent intent = new Intent(this, UpdateActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, new Random().nextInt(10000), getIntentByType(), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Some title")
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setContentText("Some message")
				.setAutoCancel(true);

		mBuilder.setContentIntent(contentIntent);
		notificationManager.notify(new Random().nextInt(10000), mBuilder.build());
	}

	private Intent getIntentByType() {
		Intent intent = new Intent(this, LaunchActivity.class);
		intent.putExtra(String.class.getSimpleName(), "Notify message");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return intent;
	}

}
