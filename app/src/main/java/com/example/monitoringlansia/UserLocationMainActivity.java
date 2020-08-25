package com.example.monitoringlansia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class UserLocationMainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback
, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {


    GoogleApiClient client;

    FirebaseAuth auth;
    GoogleMap mMap;
    LocationRequest request;
    LatLng latLng;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String current_user_name;
    String current_user_email;
    String current_user_imageUrl;
    TextView t1_currentName,t2_currentEmail;
    ImageView i1;

    private ActionBarDrawerToggle toggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);
        t1_currentName = header.findViewById(R.id.title_text);
        t2_currentEmail = header.findViewById(R.id.email_text);
        i1 =header.findViewById(R.id.imageView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_user_name = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                current_user_email = dataSnapshot.child(user.getUid()).child("email").getValue(String.class);
                current_user_imageUrl = dataSnapshot.child(user.getUid()).child("imageUrl").getValue(String.class);

                t1_currentName.setText(current_user_name);
                t2_currentEmail.setText(current_user_email);

                Picasso.get().load(current_user_imageUrl).into(i1);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_location_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
       return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_joinCircle) {
            Intent myIntent = new Intent(UserLocationMainActivity.this,JoinCircleActivity.class);
            startActivity(myIntent);

        }
        else if (id == R.id.nav_shareLoc)
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT,"My location is :"+"https://www.google.com/maps/@"+
                    latLng.latitude+","+latLng.longitude+",17z");
            startActivity(i.createChooser(i,"Share using: "));

            String uri = "http://maps.google.com/maps?q="+latLng.latitude+","+latLng.longitude;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,  uri);
            startActivity(Intent.createChooser(sharingIntent, "Share in..."));

        }
        else if (id == R.id.nav_sensor_suhu){

            Intent myIntent = new Intent(UserLocationMainActivity.this,SensorSuhu1.class);
            startActivity(myIntent);

        }
        else if (id == R.id.nav_sensor_max){
            Intent myIntent = new Intent(UserLocationMainActivity.this,SensorMaxActivity.class);
            startActivity(myIntent);

        }
        else if (id == R.id.nav_sensor_gula_darah) {
            Intent myIntent = new Intent(UserLocationMainActivity.this,SensorGulaActivity.class);
            startActivity(myIntent);

        }
        else if (id == R.id.nav_signOut)
        {
            FirebaseUser user = auth.getCurrentUser();
            if(user != null)
            {
                auth.signOut();
                finish();
                Intent myIntent = new Intent(UserLocationMainActivity.this,MainActivity.class);
                startActivity(myIntent);
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        client = new GoogleApiClient.Builder(this)
        .addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

        client.connect();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(3000);


        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
        {
            Toast.makeText(getApplicationContext(), "Tidak bisa mendapatkan lokasi", Toast.LENGTH_SHORT).show();
        }
        else
        {
            latLng = new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions options = new MarkerOptions();
            options.position(latLng);
            options.title("current location");
            mMap.addMarker(options);
        }
    }
}
