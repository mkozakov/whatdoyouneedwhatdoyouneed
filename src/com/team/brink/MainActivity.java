package com.team.brink;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	/**
	 * @param savedInstanceState
	 * Move to login screen immediately.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Check if already logged in
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			moveToLogin();			
		} else {
			moveToRequest(currentUser);
		}
	}

	/**
	 * Move to the Login Activity
	 */
	public void moveToLogin() {
		Log.d("mainFragment", "moveToLogin");
		Intent toLogin = new Intent(this, LoginActivity.class);
		startActivity(toLogin);
	}
	
	public void moveToRequest(ParseUser currentUser) {
		Bundle bundle = new Bundle();
		bundle.putString("userName", currentUser.getUsername());
		Intent toRequest = new Intent(this, RequestActivity.class);
		toRequest.putExtras(bundle);

		startActivity(toRequest);
	}

}