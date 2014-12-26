package com.team.brink;

import com.parse.Parse;

import android.app.Application;
import android.util.Log;

public class Brink extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);
		Parse.initialize(this, "X7TUFu2E0KhWtFjeB0e8m1J7Jyu5w9OBvDscGivb", "T2Pih8zKZ5SahivPuE8z8weTvAKOZjUDFdHfjEeW");
    }
	
}
