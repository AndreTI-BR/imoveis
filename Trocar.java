package com.example.imobiliaria;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Trocar extends AppCompatActivity {
    private EditText edit_senha,edit_senha1,edit_senha2;
    private Button gravar;
    String[] mensagens = {"Preenche todos os campos","A nova senha e a senha de confirmação não estão iguais","Senha antiga está errada"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocar);
        IniciarComponentes();



        gravar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String senha = edit_senha.getText().toString();
                String senha1 = edit_senha1.getText().toString();
                String senha2 = edit_senha2.getText().toString();

                if(senha.isEmpty() || senha1.isEmpty() || senha2.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else if(senha1.equals(senha2) ){

                    AutenticarUsuario(v);
                }else{
                    Snackbar snackbar = Snackbar.make(v,mensagens[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });



    }
    private void AutenticarUsuario(View v){
        String senha = edit_senha.getText().toString();
        String senha1 = edit_senha1.getText().toString();
        Bundle extras = getIntent().getExtras();
        String userid = null;
        userid = extras.getString("userid");

        FirebaseAuth.getInstance().signInWithEmailAndPassword(userid,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {




                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String newPassword = senha1;

                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(Trocar.this, Login.class);
                                                startActivity(intent);
                                             finish();
                                            }
                                        }
                                    });


                        }
                    }, 3000);

                }else{

                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao gravar";
                        Snackbar snackbar = Snackbar.make(v,mensagens[2],Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            }
        });
    }

    private void IniciarComponentes(){
        edit_senha = findViewById(R.id.edit_senha);
        edit_senha1 = findViewById(R.id.edit_senha1);
        edit_senha2 = findViewById(R.id.edit_senha2);
        gravar = findViewById(R.id.gravar);

    }

}