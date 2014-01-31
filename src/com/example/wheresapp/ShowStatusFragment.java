package com.example.wheresapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.TextView;


public class ShowStatusFragment extends Fragment {
	TextView statusTextView;
	public ShowStatusFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		String status=getArguments().getString("status");
		View view = inflater.inflate(R.layout.fragment_show_status,container, false);
		statusTextView=(TextView)view.findViewById(R.id.statusTextView);
		if(statusTextView!=null)
		statusTextView.setText(status);
		else
			Log.d("wheresapp","statustext is empty");//just for debugging
		Button button = (Button) view.findViewById(R.id.editStatus);
		button.setOnClickListener(new View.OnClickListener() {
			      @Override
			      public void onClick(View v) {
			        onEditPressed(v);
			      }
	    });
		
		return view;
	}
	
	
	private OnShowStatusFragmentInteractionListener mListener;

	


	public void onEditPressed(View view) {
		//Log.d("Hanji","Hanji ji");
		if (mListener != null) {
			mListener.onEditStatus();
		}
	}

	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnShowStatusFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+ " must implement OnShowStatusFragmentInteractionListener  + Here is Mohit's Message");
		}
	}

	
	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnShowStatusFragmentInteractionListener {

		public void onEditStatus();
	}


}
