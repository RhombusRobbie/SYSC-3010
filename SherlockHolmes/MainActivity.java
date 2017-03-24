package com.example.adebola.sherlockholmes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*Called when the User Clicks the Login Button*/
    public void Login(View view){

        //Intent class does something when this method is called. Takes two parameters
        //the context and the class. MainPage is entered when user clicks button
        Intent intent = new Intent(this, Mainpage.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();

        //Add the editText value to the intent. Carry the key and value.
        //Key is the EXTRA_MESSAGE(use app package name to make it unique)
        //The key is used to retrieve the text value
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
}
