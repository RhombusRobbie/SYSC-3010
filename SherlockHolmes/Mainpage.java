package com.example.adebola.sherlockholmes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Mainpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
    }


     public void History(View view){
        //Intent class does something when this method is called. Takes two parameters
        //the context and the class. MainPage is entered when user clicks button

          Intent intent = new Intent(this, History.class);
          //Start the Next Activity that contains the history
          startActivity(intent);

        /**
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);
        */
    }
}
