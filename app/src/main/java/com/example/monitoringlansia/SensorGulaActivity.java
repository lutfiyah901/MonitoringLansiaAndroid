package com.example.monitoringlansia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorGulaActivity extends AppCompatActivity {


    TextView GULA_DARAH;
    DatabaseReference dref2;
    String status3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_gula);

        GULA_DARAH = (TextView) findViewById(R.id.nilai_gula);
        dref2 = FirebaseDatabase.getInstance().getReference();
        dref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status3 = dataSnapshot.child("GULA_DARAH").getValue().toString();
                GULA_DARAH.setText(status3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
