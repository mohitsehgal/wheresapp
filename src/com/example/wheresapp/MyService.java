package com.example.wheresapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract.CommonDataKinds;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MyService extends Service {
	static boolean ring=false;
    static boolean callReceived=false;
    static String callerNumber="";
	TelephonyManager telephonyManager;
	SmsManager smsManager;
	public MyService() {
	}

	HashSet<String> numbersSet;
	HashSet<String> contactSet;
	public HashSet<String> getContactSet()
	{
		
		SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences(ContactsActivity.WA_CONTACTS_PREFERENCE, Context.MODE_PRIVATE);
		contactSet=(HashSet<String>) sharedPreferences.getStringSet(ContactsActivity.WA_CONTACTS_SET,new HashSet<String>());
		
		return contactSet;
		
	}
	
	
	public HashSet<String> getNumbersSet()
	{
		
		List<String> list=getPhoneNumberList();
		numbersSet.addAll(list);
		return numbersSet;
		
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(getBaseContext(), "MyService Started", Toast.LENGTH_LONG).show();
	
		numbersSet=new HashSet<String>();
		contactSet=new HashSet<String>();
		contactSet=getContactSet();
		numbersSet=getNumbersSet();
		
	}
	
	
	private ArrayList<String> getPhoneNumberList()
	{
		ArrayList<String> phones = new ArrayList<String>();

		
		for(String id: contactSet)
		{
				Cursor cursor = getContentResolver().query(	CommonDataKinds.Phone.CONTENT_URI,null, CommonDataKinds.Phone.CONTACT_ID +" = ?", 
		        new String[]{id}, null);

				while (cursor.moveToNext()) 
				{
					phones.add(cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)));
				} 
		
				cursor.close();
		}
		
		return phones;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
	int a= super.onStartCommand(intent, flags, startId);
	telephonyManager=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
	smsManager=SmsManager.getDefault();
	telephonyManager.listen(new PhoneStateListener(){
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			if(state==TelephonyManager.CALL_STATE_RINGING)
			{
				ring=true;

				Toast.makeText(getBaseContext(), "Ringing "+incomingNumber, Toast.LENGTH_LONG).show();
				callerNumber=incomingNumber;
			}
			else if(state==TelephonyManager.CALL_STATE_OFFHOOK)
			{

				Toast.makeText(getBaseContext(), "Off Hook"+incomingNumber, Toast.LENGTH_LONG).show();
				callReceived=true;
			}
			else if(state==TelephonyManager.CALL_STATE_IDLE)
			{

				Toast.makeText(getBaseContext(), "Now its Idle", Toast.LENGTH_LONG).show();
				if(ring==true&&callReceived==false)
				{
					//missed call
					Toast.makeText(getBaseContext(), "Missed call from "+callerNumber, Toast.LENGTH_LONG).show();
					List<String> list=getPhoneNumberList();
					Toast.makeText(getBaseContext(), "List of numbers "+list, Toast.LENGTH_LONG).show();
					if(list.contains(callerNumber))
					{
							
						Toast.makeText(getBaseContext(), "This Caller Needs  Information about me", Toast.LENGTH_LONG).show();
						String status=MainActivity.getStatus();
						
						smsManager.sendTextMessage(callerNumber, null, status, null, null);
						
						Toast.makeText(getBaseContext(), "Sending message to "+callerNumber, Toast.LENGTH_LONG).show();
						
					}
				}
				ring=false;callReceived=false;
			}
		}
	}, PhoneStateListener.LISTEN_CALL_STATE);
	return a;
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
