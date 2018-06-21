package com.zangqa.picsell;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import  com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.zangqa.picsell.Model.Customer;

public class SigninActivity extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button signin, signup;

    RelativeLayout relative1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relative1.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        relative1 = findViewById(R.id.relative1);

        handler.postDelayed(runnable, 2000);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);

        //Initialise Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_customer = database.getReference("customer");

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialogue = new ProgressDialog(SigninActivity.this);
                mDialogue.setMessage("Please waiting");
                mDialogue.show();
                table_customer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Check if user not exist in Database
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            // Get user information
                            mDialogue.dismiss();
                            Customer customer = dataSnapshot.child(edtPhone.getText().toString()).getValue(Customer.class);
                            if (customer.getPassword().equals(edtPassword.getText().toString())) {
                                Toast.makeText(SigninActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SigninActivity.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialogue.dismiss();
                            Toast.makeText(SigninActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
