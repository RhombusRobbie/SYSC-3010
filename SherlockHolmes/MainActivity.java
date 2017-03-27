package com.example.adebola.sherlockholmes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText Username, Password;
    TextView WrongPassword;


    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WrongPassword = (TextView)findViewById(R.id.textView3);
        WrongPassword.setVisibility(View.GONE);
    }

    /*Called when the User Clicks the Login Button*/
    public void Login(View view){

        Username = (EditText) findViewById(R.id.editText);
        Password = (EditText) findViewById(R.id.editText2);



        //allow Login when the Correct Password is Pressed
        if(Username.getText().toString().equals("sherlock") &&
                Password.getText().toString().equals("holmes")){
            //Correct Password
            Intent intent = new Intent(this, Mainpage.class);
            startActivity(intent);
        }else{
            //Wrong Password
            WrongPassword.setVisibility(View.VISIBLE);
            WrongPassword.setBackgroundColor(Color.MAGENTA);

        }


    }
}
