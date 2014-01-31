package com.mohitsehgal.wheresapp;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.mohitsehgal.wheresapp.R;

public class MainActivity extends Activity implements ShowStatusFragment.OnShowStatusFragmentInteractionListener,UpdateStatusFragment.OnUpdateStatusFragmentInteractionListener {


	public static final String WA_STATUS_PREF="WAStatusPreference";
	public static final String USER_STATUS="UserStatus";
	
	
	public static SharedPreferences statusSharedPreferences;
	public static SharedPreferences.Editor statusSharedPreferencesEditor;
	
	public static String status;
	
	public void initializeSharedPreference()
	{
		statusSharedPreferences=getSharedPreferences(WA_STATUS_PREF, MODE_PRIVATE);
		statusSharedPreferencesEditor=statusSharedPreferences.edit();
	
		
	}
	public static  SharedPreferences getStatusSharedPreferences() {
		return statusSharedPreferences;
	}
	public static SharedPreferences.Editor getStatusSharedPreferencesEditor() {
		return statusSharedPreferencesEditor;
	}
	public static  String getStatus() {
		status=statusSharedPreferences.getString(USER_STATUS, "No Status Yet");
		return status;
	}
	
	public static void setStatus(String status) {
		MainActivity.status = status;
		getStatusSharedPreferencesEditor().putString(USER_STATUS, status).commit();
		
		
		
		
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        initializeSharedPreference();
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
           ShowStatusFragment firstFragment = new ShowStatusFragment();
            
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
           Bundle bundle=new Bundle();
           bundle.putString("status", getStatus());
            firstFragment.setArguments(bundle);
            
            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
            
            if(!isServiceRunning(MyService.class.getName()))
            {	
            	Intent serviceIntent=new Intent(this,MyService.class);
            	startService(serviceIntent);
            	Toast.makeText(this, "Service Started fram Main Activity", Toast.LENGTH_LONG).show();
            }
            else
            {
            	Toast.makeText(this, "Service not Started fram Main Activity", Toast.LENGTH_LONG).show();
                
            }
            
            
        }
    }

    public  boolean isServiceRunning(String serviceClassName){ 
        final ActivityManager activityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);     
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);      
      for (RunningServiceInfo runningServiceInfo : services) {         
        if (runningServiceInfo.service.getClassName().equals(serviceClassName)){             
           return true;         
        }     
      }     
    return false; 
   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onEditStatus() {
		UpdateStatusFragment updateStatusFragment=new UpdateStatusFragment();
		 Bundle bundle=new Bundle();
         bundle.putString("status", getStatus());
        updateStatusFragment
        .setArguments(bundle);
		 getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
         .replace(R.id.fragment_container, updateStatusFragment).commit();
		
	}


	@Override
	public void onPostStatus(String status) {
		ShowStatusFragment showStatusFragment=new ShowStatusFragment();
		Bundle args=new Bundle();
		args.putString("status",status);
		Toast.makeText(this, "Committing Status "+status, Toast.LENGTH_LONG).show();
		getStatusSharedPreferencesEditor().putString(USER_STATUS, status).commit();
		showStatusFragment.setArguments(args);
		 getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
         .replace(R.id.fragment_container, showStatusFragment).commit();
	}
	
	
	public void showContactsActivity(View view)
	{
		Intent contactsServiceIntent=new Intent(this, ContactsActivity.class);
		startActivity(contactsServiceIntent);
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		//getStatusSharedPreferencesEditor().putString(USER_STATUS, status).commit();

	//	Toast.makeText(this, "Committing Status "+status, Toast.LENGTH_LONG).show();
	}
    
}
