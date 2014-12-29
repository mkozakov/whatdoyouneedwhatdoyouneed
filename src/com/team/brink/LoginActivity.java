package com.team.brink;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/**
	 * String[] containing username and password to be checked
	 */
	private String[] loginInfo;
	/**
	 *  EditText field for user input
	 */
	private EditText username;
	/**
	 *  EditText field for user input
	 */
	private EditText password;

	/**
	 * Creates the layout for the screen.
	 * Gets the strings entered in the username and password field.
	 * 
	 */

	private LoginActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Array size of 2 for username and password
		loginInfo = new String[2];
		// Get username and password from the edittexts.
		username = (EditText) findViewById(R.id.usernameInput);
		password = (EditText) findViewById(R.id.passwordInput);

		instance = this;
	}

	public void login(View view) {
		// Get strings from EditTexts
		loginInfo[0] = username.getText().toString();
		loginInfo[1] = password.getText().toString();

		// Create a bundle and add the login info as an extra
		Bundle bundle = new Bundle();
		bundle.putStringArray("info",
				new String[] { loginInfo[0], loginInfo[1] });

		ParseUser.logInInBackground(loginInfo[0], loginInfo[1], new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					// Hooray! The user is logged in.
					// Create a bundle and add the login info as an extra
					Bundle bundle = new Bundle();
					bundle.putStringArray("info",
							new String[] { loginInfo[0]});
					Intent login = new Intent(instance, RequestActivity.class);
					login.putExtras(bundle);
					startActivity(login);
				} else {
					// Login failed. Look at the ParseException to see what happened.
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					Log.v("Exception!!!", "Something went wrong with the login!");
				}
			}
		});
	}

	public void signUp(View view) {
		// Get strings from EditTexts
		loginInfo[0] = username.getText().toString();
		loginInfo[1] = password.getText().toString();

		ParseUser user = new ParseUser();
		user.setUsername(loginInfo[0]);
		user.setPassword(loginInfo[1]);

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					// Hooray! Let them use the app now.
					// Create a bundle and add the login info as an extra
					Bundle bundle = new Bundle();
					bundle.putString("userName", loginInfo[0]);
					Intent request = new Intent(instance, RequestActivity.class);
					request.putExtras(bundle);
					startActivity(request);
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					Log.v("Exception!!!", "Something went wrong with the signup!");
					e.printStackTrace();
				}
			}
		});
	}
}
