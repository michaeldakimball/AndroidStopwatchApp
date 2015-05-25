package com.cloudformers.bae;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {
    private Button loginB, signupB;
    private EditText emailTB, passwordTB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseHelper.startConnection(this);
        loginB = (Button) findViewById(R.id.loginButton);
        signupB = (Button) findViewById(R.id.signupButton);
        emailTB = (EditText) findViewById(R.id.emailText);
        passwordTB = (EditText) findViewById(R.id.passwordText);
        loginB.setOnClickListener(new LoginListener(this));
        signupB.setOnClickListener(new SignUpListener(this));
    }
    public ParseObject getLogin(){
        ParseObject accessInfo = new ParseObject(ParseHelper.loginObj);
        accessInfo.put(ParseHelper.email, emailTB.getText().toString());
        accessInfo.put(ParseHelper.password, passwordTB.getText().toString());
        return accessInfo;
    }
    private class LoginListener implements View.OnClickListener {
        MainActivity context;
        public LoginListener(MainActivity main){
            this.context = main;
        }
        @Override
        public void onClick(View v) {
            boolean loginFound = ParseHelper.findLogin(context.getLogin());
            if(loginFound){
                Intent i = new Intent(context, StopwatchActivity.class);
                startActivity(i);
            }else{
                Toast noMatch = Toast.makeText(context, "Email/Password combination not found!", Toast.LENGTH_LONG);
                noMatch.show();
            }
        }
    }
    private class SignUpListener implements View.OnClickListener {
        MainActivity context;
        public SignUpListener(MainActivity context){
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, SignUpActivity.class);
            startActivity(i);
        }
    }
}
