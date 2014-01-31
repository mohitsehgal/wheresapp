package com.mohitsehgal.wheresapp;

import java.util.HashSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.mohitsehgal.wheresapp.R;

public class ContactsActivity extends Activity {
	public static final int  CONTACT_PICKER_REQUEST=1;
	public static final String WA_CONTACTS_PREFERENCE="WAContact";
	public static final String WA_CONTACTS_SET="WAContactsSet";
	
	private ArrayAdapter<String> mArrayAdapter;
	SharedPreferences sharedPreferences;
	
	SharedPreferences.Editor sharedPreferencesEditor;
	HashSet<String> selectedContactsSet;
	HashSet<String> selectedContactsDisplayNameSet;
	private  ListView list;
	public ContactsActivity() {
		
	}

	private void resynchronizeSelectedContactsSet()
	{
		for(String contactId: selectedContactsSet)
		{
			Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		          new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
		          RawContacts.CONTACT_ID + "=?",
		          new String[]{String.valueOf(contactId)}, null);
		 	while(c.moveToNext())
		 	{
		 			String curName=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		 			selectedContactsDisplayNameSet.add(curName);//set instead of list
		 			break;//only 1 name needed
		 	}
		 
		}
		
		syncArrayAdapter();
	}
	
	
	private void syncArrayAdapter()
	{
		mArrayAdapter.clear();
		mArrayAdapter.addAll(selectedContactsDisplayNameSet);
		mArrayAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		sharedPreferences=getSharedPreferences(WA_CONTACTS_PREFERENCE, Context.MODE_PRIVATE);
		selectedContactsSet=(HashSet<String>) sharedPreferences.getStringSet(WA_CONTACTS_SET,new HashSet<String>());
		selectedContactsDisplayNameSet=new HashSet<String>();
		sharedPreferencesEditor=sharedPreferences.edit();
		mArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	//	mArrayAdapter.addAll(selectedContactsSet);//set instead of list//commented because no need
		list=(ListView)findViewById(R.id.list_contact);
		list.setAdapter(mArrayAdapter);
		resynchronizeSelectedContactsSet();
	
		
	}
	
	
	
	public void pickContact(View view)
	{
		Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
		pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, CONTACT_PICKER_REQUEST);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==CONTACT_PICKER_REQUEST)
		{
			if(data!=null)
			{
			  Uri uri=data.getData();
			   Cursor c=getContentResolver().query(uri, null, null, null, null);
			   if(c.moveToFirst())
			   {
				   String number=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				   String name=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				   String contact_id=c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
				   if(contact_id!=null)
				   {
					   	selectedContactsSet.add(contact_id);
					   	//commit the changes 
					   	sharedPreferencesEditor.putStringSet(WA_CONTACTS_SET, selectedContactsSet);
						sharedPreferencesEditor.commit();
						//
						
				   		selectedContactsDisplayNameSet.add(name);
				   			// syncArrayAdapter(); //commented to get more efficiency
				   		mArrayAdapter.add(name);
				   }
			   }
			}
		}
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		sharedPreferencesEditor.putStringSet(WA_CONTACTS_SET, selectedContactsSet);
		sharedPreferencesEditor.commit();
		Toast.makeText(this, "Selected Sets"+selectedContactsSet, Toast.LENGTH_LONG).show();
		
	}
	
}
