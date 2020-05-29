package com.example.smarttravel;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText login_et, password_et;
    Button sign_in_btn, sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        login_et = findViewById(R.id.login);
        password_et = findViewById(R.id.password);
        sign_in_btn = findViewById(R.id.btn_sign_in);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_in(login_et.getText().toString(), password_et.getText().toString());
            }
        });
        sign_up_btn = findViewById(R.id.btn_sign_up);
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up(login_et.getText().toString(), password_et.getText().toString());
            }
        });
    }
    public void sign_in(String mail, String pas){
        mAuth.signInWithEmailAndPassword(mail, pas).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Вы вошли в систему!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, FromActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Проверьте правильность ввода данных!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void sign_up(String mail, String pas){
        mAuth.createUserWithEmailAndPassword(mail, pas).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Вы успешно зарегистирировались!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Что-то пошло не так, проверьте правильность ввода данных!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
