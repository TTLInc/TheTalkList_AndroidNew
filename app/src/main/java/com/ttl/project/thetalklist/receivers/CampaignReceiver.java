package com.ttl.project.thetalklist.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

/**
 * Created by Saubhagyam on 04/01/2018.
 */

public class CampaignReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        // do anything you want with referrer

        // When you're done, pass the intent to the Google Analytics receiver
        new CampaignTrackingReceiver().onReceive(context, intent);
    }
}
