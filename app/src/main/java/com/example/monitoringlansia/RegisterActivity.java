package com.example.monitoringlansia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RegisterActivity extends AppCompatActivity {

    EditText e4_email;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        e4_email = (EditText)findViewById(R.id.editText4);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
    }

    public void goToPasswordActivity(View v)
    {
            dialog.setMessage("Cek Email");
            dialog.show();
            //ngecek email udah register apa blumm
           auth.fetchSignInMethodsForEmail(e4_email.getText().toString())
                   .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                       @Override
                       public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                           if(task.isSuccessful())
                           {
                               dialog.dismiss();
                               boolean check = !task.getResult().getSignInMethods().isEmpty();

                               if (!check)
                               {
                                   //email gak ada jadi buat
                                   Intent myIntent = new Intent(RegisterActivity.this,PasswordActivity.class);
                                   myIntent.putExtra("email",e4_email.getText().toString());
                                   startActivity(myIntent);
                                   finish();
                               }
                               else
                               {
                                   dialog.dismiss();;
                                   Toast.makeText(getApplicationContext(),"Email Ini Sudah Digunakan",Toast.LENGTH_SHORT).show();
                               }

                           }
                       }
                   });
    }
}
