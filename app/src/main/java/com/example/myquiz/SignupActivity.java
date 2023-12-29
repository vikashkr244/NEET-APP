package com.example.myquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.ActionMode;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myquiz.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    FirebaseAuth auth;
    FirebaseFirestore database;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.getRoot().setSaveEnabled(true);
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We are creating your account...");

        SpannableStringBuilder spannable = new SpannableStringBuilder(
                "By signing up you agree to our ");

        int privacyPolicyStart = spannable.length();
        spannable.append("privacy policy ");
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), privacyPolicyStart,
                spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int termsStart = spannable.length();
        spannable.append("and terms and conditions.");
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), termsStart,
                spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvPrivacy.setText(spannable);

        binding.tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String privacyPolicyUrl = "https://sites.google.com/view/myquiz-privacy-policy";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(privacyPolicyUrl));
                startActivity(intent);
            }
        });

        binding.createNewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, name, cpassword;
                email = binding.emailbox.getText().toString();
                password = binding.passwordBox.getText().toString();
                cpassword= binding.confirmpasswordBox.getText().toString();
                name = binding.nameBox.getText().toString();


                if (email.isEmpty() || password.isEmpty() || name.isEmpty()|| cpassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter your details completely", Toast.LENGTH_SHORT).show();
                } else if (!cpassword.equals(password)) {
                    Toast.makeText(SignupActivity.this, "Please confirm your password correctly", Toast.LENGTH_SHORT).show();

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        binding.emailbox.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
                        binding.passwordBox.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);

                    }

                    final User user = new User(name, email, password);
                dialog.show();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            database
                                    .collection("users")
                                    .document(uid)
                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }
}