package com.mohitsehgal.wheresapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.mohitsehgal.wheresapp.R;


public class UpdateStatusFragment extends Fragment {
	private EditText statusEdittext;
	private OnUpdateStatusFragmentInteractionListener mListener;

	public UpdateStatusFragment() {
		// Required empty public constructor
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			String status=getArguments().getString("status");
			View view = inflater.inflate(R.layout.fragment_update_status,container, false);
			statusEdittext=(EditText)view.findViewById(R.id.statusText);
			statusEdittext.setText(status);
			Button button = (Button) view.findViewById(R.id.updateStatus);
			button.setOnClickListener(new View.OnClickListener() {
			      @Override
			      public void onClick(View v) {
			        onPostPressed(v);
			      }
			    });
			
			
			return view;
	}

	
	
	public void onPostPressed(View view) {
		String curStatus=statusEdittext.getText().toString();
		
		
		if (mListener != null) {
			mListener.onPostStatus(curStatus);
		}
	}

	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnUpdateStatusFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+ " must implement OnFragmentInteractionListener");
		}
	}

	
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	
	
	public interface OnUpdateStatusFragmentInteractionListener {
		public void onPostStatus(String cur);
	}

}
