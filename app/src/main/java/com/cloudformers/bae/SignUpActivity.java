package com.cloudformers.bae;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;

/**
 * Created by Michael on 5/22/2015.
 */
public class SignUpActivity extends AppCompatActivity {
    private EditText firstNameTB, lastNameTB, emailTB, passwordTB;
    private Button submitB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstNameTB = (EditText) findViewById(R.id.firstNameText);
        lastNameTB = (EditText) findViewById(R.id.lastNameText);
        emailTB = (EditText) findViewById(R.id.emailText);
        passwordTB = (EditText) findViewById(R.id.passwordText);
        submitB = (Button) findViewById(R.id.submitButton);
        submitB.setOnClickListener(new SubmitListener(this));
    }
    public void saveInfo(){
        ParseObject info = new ParseObject(ParseHelper.loginObj);
        info.put(ParseHelper.firstName, firstNameTB.getText().toString());
        info.put(ParseHelper.lastName, lastNameTB.getText().toString());
        info.put(ParseHelper.email, emailTB.getText().toString());
        info.put(ParseHelper.password, passwordTB.getText().toString());
        if(!ParseHelper.findUsername(info)){
            info.saveInBackground();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }else{
            Toast usernameExists = Toast.makeText(this, "Email has already been used!", Toast.LENGTH_LONG);
            usernameExists.show();
        }
    }
    private class SubmitListener implements View.OnClickListener {
        SignUpActivity context;
        public SubmitListener(SignUpActivity context){
            this.context = context;
        }
        @Override
        public void onClick(View v) {
            context.saveInfo();
        }
    }
}
