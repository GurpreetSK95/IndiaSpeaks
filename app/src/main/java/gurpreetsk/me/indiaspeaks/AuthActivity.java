package gurpreetsk.me.indiaspeaks;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AuthActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText emailET, passwordET;
    String email, password;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private static Location mLastLocation;
    protected LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    double latitude, longitude;
    public static String pincode = "110041";

    private String TAG = "AuthActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (checkPlayServicesAvailablity()) {
            buildGoogleApiClient();
        }

        displayLocation();

        emailET = (EditText) findViewById(R.id.EmailET);
        passwordET = (EditText) findViewById(R.id.PasswordET);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FireAuth", "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(AuthActivity.this, FeedActivity.class));
//                    Toast.makeText(AuthActivity.this, "Already logged in "+ user, Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d("FireAuth", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void registerBtnClicked(View view) {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, "Authentication failed. User already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AuthActivity.this, FeedActivity.class));
                        }
                    }
                });
    }

    public void loginBtnClicked(View view) {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FireAuth", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("FireAuth", "signInWithEmail", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AuthActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AuthActivity.this, FeedActivity.class));
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            startLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Once connected with google api, get the location
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServicesAvailablity();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
//        createLocationRequest();
    }


    private boolean checkPlayServicesAvailablity() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
//            Toast.makeText(this, "Cant update location. Provide storage permission.", Toast.LENGTH_SHORT).show();

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(this, "Cant update location. Provide location permission.", Toast.LENGTH_SHORT).show();
                }
            }
            break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            Snackbar.make(findViewById(android.R.id.content), "Please provide location permission.", Snackbar.LENGTH_LONG).show();
        }
        try {
            startLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    pincode = addressList.get(0).getPostalCode();
                    Log.v(TAG, pincode);
                    Toast.makeText(this, pincode, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable connect to Geocoder", e);
            }

        } else {

//            Toast.makeText(this, "Couldn't get current location. Make sure location is enabled on the device", Toast.LENGTH_SHORT).show();
            pincode = "110041";

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
