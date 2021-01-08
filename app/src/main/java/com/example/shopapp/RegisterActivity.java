package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText userFirstName, userLastName, userEmail, userPass, userConfPass, userPhone;
    Button registerBtn;
    TextInputLayout userFirstNameWrapper, userLastNameWrapper, userEmailWrapper, userPasswordWrapper,
            userConfPasswordWrapper, userContactNumWrapper;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        userFirstName = findViewById(R.id.userFirstName);
        userLastName = findViewById(R.id.userLastName);
        userEmail = findViewById(R.id.userEmailAddress);
        userPass = findViewById(R.id.userPassword);
        userConfPass = findViewById(R.id.userConfPassword);
        userPhone = findViewById(R.id.userContactNumber);


        userFirstNameWrapper = findViewById(R.id.userFirstNameWrapper);
        userLastNameWrapper = findViewById(R.id.userLastNameWrapper);
        userEmailWrapper = findViewById(R.id.userEmailAddressWrapper);
        userPasswordWrapper = findViewById(R.id.userPasswordWrapper);
        userConfPasswordWrapper = findViewById(R.id.userConfPasswordWrapper);
        userContactNumWrapper = findViewById(R.id.contactNumWrapper);


        registerBtn = findViewById(R.id.btnRegister);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null){

                }else{

                String firstname = userFirstName.getText().toString().trim();
                String lastname = userLastName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String password = userPass.getText().toString().trim();
                String confpassword = userConfPass.getText().toString().trim();
                String phone = userPhone.getText().toString().trim();

                if (firstname.isEmpty()){
                    userFirstNameWrapper.setError("Please Enter Your First Name...");
                    userFirstNameWrapper.requestFocus();
                    return;
                }

                if (lastname.isEmpty()){
                    userLastNameWrapper.setError("Please Enter Last Name...");
                    userLastNameWrapper.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    userEmailWrapper.setError("Please Enter Your Email...");
                    userEmailWrapper.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    userPasswordWrapper.setError("Please Enter Your Password...");
                    userPasswordWrapper.requestFocus();
                    return;
                }

                if (confpassword.isEmpty()){
                    userConfPasswordWrapper.setError("Please Confirm Your Password...");
                    userConfPasswordWrapper.requestFocus();
                    return;
                }

                if (!password.equals(confpassword)){
                    userConfPasswordWrapper.setError("Password Didn't match !!");
                    userConfPasswordWrapper.requestFocus();
                    return;
                }

                if (phone.isEmpty()){
                    userContactNumWrapper.setError("Please Enter Your Phone Number");
                    userContactNumWrapper.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()){
                        User user = new User(firstname, lastname, email, phone);
                       FirebaseDatabase.getInstance().getReference("Users")
                       .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                       .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()){
                                   Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                   startActivity(intent);
                               }else {
                                   Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                       });

                   }else {
                       Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
                    }
                });

                }

            }
        });






    }
}