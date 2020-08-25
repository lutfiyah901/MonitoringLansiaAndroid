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

public class SensorMaxActivity extends AppCompatActivity {

    TextView BPM,SP02;
    DatabaseReference dref1;
    String status1,status2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_max);

        BPM = (TextView) findViewById(R.id.nilai_bpm);
        SP02 = (TextView) findViewById(R.id.nilai_saturasi);
        dref1 = FirebaseDatabase.getInstance().getReference();
        dref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                status1 = dataSnapshot.child("BPM").getValue().toString();
                BPM.setText(status1);
                status2 = dataSnapshot.child("SP02").getValue().toString();
                SP02.setText(status2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
