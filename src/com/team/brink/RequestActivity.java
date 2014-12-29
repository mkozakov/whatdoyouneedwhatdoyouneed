package com.team.brink;

import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.text.format.Time;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

public class RequestActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener,
	LocationListener{

	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_request);

		userName = (String) getIntent().getExtras().get("userName");	
		
		// Setting up location connection
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(LocationServices.API)
		.build();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (result == ConnectionResult.SUCCESS) {
			// Connect if possible
			mGoogleApiClient.connect();			
		} else {
			// Show error dialog
			GooglePlayServicesUtil.getErrorDialog(result, this, 1234).show();
		}
	}
	
	@Override
	public void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}
	
	public void searchNearby(View view){
		if (mLastLocation != null) {
			Log.v("searchNearby", "I have a location!");
			
			// Send the information associated with a request to the Parse server
			ParseObject request = new ParseObject("Request");
			// Item being requested
			request.put("item", "Android Charger");
			// TODO - get username of borrower
			request.put("userName", userName);
			// Location of the user
			ParseGeoPoint parseLocation = new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
			request.put("location", parseLocation);
			// Timestamp associated with request
			Time time = new Time(Time.getCurrentTimezone());
			request.put("timestamp", time.toMillis(true));
			// Save to the server
			request.saveInBackground();
			
			// Send a push notification to nearby users through Parse
			
			// Find users near a given location
			ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
			userQuery.whereWithinKilometers("location", parseLocation, 1.0);
			// Find devices associated with these users
			ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
			pushQuery.whereMatchesQuery("user", userQuery);
			// Send push notification to query
			ParsePush push = new ParsePush();
			push.setQuery(pushQuery); // Set our Installation query
			push.setMessage("Somebody needs an android charger!");
			push.sendInBackground();
			
			Toast.makeText(getApplicationContext(), "Your request was sent.", Toast.LENGTH_LONG).show();
			Log.v("survived", "I survived brink 2014");
		} else {
			Toast.makeText(getApplicationContext(), "Your request could not be sent.", Toast.LENGTH_LONG).show();
			Log.v("NO", "Didn't even try");
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.e("ConnectionFailed", "Could not connect)");
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Get the user's current location through the Location API	
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
	}

	@Override
	public void onLocationChanged(Location newLocation) {
		// Update the location if it has changed
		mLastLocation = newLocation;
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
	}
	
	public void logout(View view) {
		ParseUser.logOut();
		Intent logoutIntent = new Intent(this, LoginActivity.class);
		startActivity(logoutIntent);
	}
}
