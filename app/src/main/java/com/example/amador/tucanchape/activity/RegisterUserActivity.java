package com.example.amador.tucanchape.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import mehdi.sakout.fancybuttons.FancyButton;

public class RegisterUserActivity extends AppCompatActivity {

    private MaterialEditText nombres, apellidos, correo, pass;
    private FancyButton registrar;
    private TextView login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgress;
    private DatabaseReference databaseUsuario;
    private AppPreferences prefs;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        prefs = new AppPreferences(this);

        nombres = findViewById(R.id.et_nam);
        apellidos = findViewById(R.id.et_ape);
        correo = findViewById(R.id.et_corr);
        pass = findViewById(R.id.et_pass);
        registrar = findViewById(R.id.btn_reg);
        login = findViewById(R.id.tv_log);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    prefs.setUserAuth(true);
                    prefs.setUserType("A");
                    Intent intent = new Intent(RegisterUserActivity.this, RegisterEmpresaActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterUserActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void startRegister() {
        final String name = nombres.getText().toString().trim();
        final String apellido = apellidos.getText().toString().trim();
        final String corr = correo.getText().toString().trim();
        final String passw = pass.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(corr) && !TextUtils.isEmpty(passw)) {
            mProgress.setMessage("Uniéndote a nuestra familia...");
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(corr, passw)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgress.dismiss();
                            if (task.isSuccessful()) {

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("user");
                                DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                                currentUserDB.child("nombres").setValue(name);
                                currentUserDB.child("apellidos").setValue(apellido);
                                currentUserDB.child("correo").setValue(corr);


                                Toast.makeText(RegisterUserActivity.this, "Exito al registrar", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterUserActivity.this, "Error al registrarte", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }
    }
}
