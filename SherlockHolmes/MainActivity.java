package com.example.adebola.sherlockholmes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.lang.String;
import java.net.InetAddress;


public class MainActivity extends AppCompatActivity {

    EditText Username, Password;
    TextView WrongPassword;
    DatagramSocket s;
    //Tag name for checking the Log
    private static final String TAG = "MainActivity";
    private static final int server_Port = 2008;


    public Button Login_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username = (EditText) findViewById(R.id.editText);
        Password = (EditText) findViewById(R.id.editText2);
        //Printout Logs of the activity on MainPage
        Log.i(TAG, "OnCreate");
        try {
            s = new DatagramSocket();
        }catch(Exception e ){System.out.println(e);}

        // This is just to display wrong password Text for invalid password
        WrongPassword = (TextView)findViewById(R.id.textView3);
        WrongPassword.setVisibility(View.GONE);

        Login_button = (Button) findViewById(R.id.button);

    }

    // Called when the User Clicks the Login Button
    public void Login(View view){


        //Printout Logs of the activity on MainPage
        Log.i(TAG, "OnClick: udpSend");
        DatagramPacket p = null;

        //All us to be able to use the IP address
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            //Printout Logs of the activity on MainPage
            Log.i(TAG, "OnClick: udpSend: In the Try for Sending");

            int Username_length = Username.getText().length();
            int Password_length = Password.getText().length();

            //getting the actual message to be sent
            byte[] message1 = Username.getText().toString().getBytes();

            byte[] message2 = Password.getText().toString().getBytes();

            byte[] actual_message = new byte[100];
            actual_message[0] = 4;

            System.arraycopy(message1, 0, actual_message, 1, Username_length);
            actual_message[Username_length+1] = ' ';

            System.arraycopy(message2,0, actual_message, Username_length+2, Password_length);
            actual_message[Username_length+Password_length+2] = 0;

            Log.i(TAG, new String(actual_message));

            //Printout Logs of the activity on MainPage
            Log.i(TAG, "OnClick: about to Try the ip ");
            InetAddress local = InetAddress.getByName("172.17.152.193");
            //Used to implement the packet the delivery service. Information for the delivery is
            //contained within the datagramPacket
            Log.i(TAG, "OnClick: after ip ");


            p = new DatagramPacket(actual_message, Username_length+Password_length+3, local, server_Port);
            s.send(p);

            //DELETE THIS COMMENT when you ready to run the code
            /*
            //The program waits for a response from the server before proceeding
            //Receiving back the acknowledge that the username and password is correct
            byte[] data = new byte[2];
            p = new DatagramPacket(data, 2);
            s.receive(p);
             */
            //Printout Logs of the activity on MainPage
            Log.i(TAG, "OnClick: udpSend: Sent");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Response back from the server. That is, if the server send back 51 then the username
        //and password is correct else it displays an Error message
        if(p.getData()[1] == 1 && p.getData()[0] == 5){ // 51

            //Allow access when the right password has be received
            Intent intent = new Intent(this, Mainpage.class);
            startActivity(intent);
        }else { // 50
            //Wrong Password
            WrongPassword.setVisibility(View.VISIBLE);
            WrongPassword.setBackgroundColor(Color.MAGENTA);

            //Toast to display wrong Password
            Context context = getApplicationContext();
            CharSequence text = "Wrong Username and Password ";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Log.i(TAG, "Prompt for The right UserName and Password");
            //Call the intent again if the wrong password and username is entered
            Intent intent1 = new Intent(this, Mainpage.class);
            startActivity(intent1);

        }
    }

    /*

                        Context context = getApplicationContext();
                        CharSequence text = "Wrong Message From Server";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
    }*/
}
