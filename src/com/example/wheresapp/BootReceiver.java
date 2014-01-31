package com.example.wheresapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {

        Intent serviceIntent=new Intent(context,MyService.class);
        context.startService(serviceIntent);
    	Toast.makeText(context, "Service Started fram Boot Receiver", Toast.LENGTH_LONG).show();

	}
}
