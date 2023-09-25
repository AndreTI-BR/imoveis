package com.example.imobiliaria;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login extends AppCompatActivity {
    private TextView text_tela_sobre;
    private EditText edit_email,edit_senha;
    private Button bt_entrar;
    private ProgressBar progressBar,progressBar2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String Permissao = "administrador";
    String teste;


    String[] mensagens = {"Preenche todos os campos","Login realizado com sucesso"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IniciarComponentes();

        text_tela_sobre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Sobre.class);
                startActivity(intent);
            }
        });
        bt_entrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();
                if(email.isEmpty() || senha.isEmpty() ){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else{
                    AutenticarUsuario(v);
                }
            }
        });
    }



    private void AutenticarUsuario(View v){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            TelaPrincipal();

                        }
                    }, 3000);

                }else{

                    String erro;
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro no Login";
                        Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBar2.setVisibility(View.INVISIBLE);

                    }
                }
            }
        });
    }

    private void TelaPrincipal(){

        String EmailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference documentReference = db.collection( "Usuario").document(EmailUsuario);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    teste = (documentSnapshot.getString("permissao"));

                    String userid = null;
                    userid = EmailUsuario;
                    if(teste.equals(Permissao)) {

                        finish();

                    }else if(teste.equals("locatario")) {
                        Intent intent = new Intent(Login.this, Userlist.class);
                        intent.putExtra("userid", userid);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(Login.this, Login.class);
                        intent.putExtra("email", EmailUsuario);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent = new Intent(Login.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }) ;


    }
    private void IniciarComponentes(){
        text_tela_sobre = findViewById(R.id.text_tela_sobre);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        progressBar = findViewById(R.id.progressbar);
        progressBar2 = findViewById(R.id.progressbar2);

    }
}