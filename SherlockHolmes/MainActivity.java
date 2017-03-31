package com.example.adebola.sherlockholmes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity{


    EditText Username, Password;
    TextView WrongPassword;

    //Tag name for checking the Log
    private static final String TAG = "MainActivity";

    //Variable to check for validity of the username and password from server
    public String Login_valid;

    public Button Login_button;

    // Port the Server is sitting on
    public int server_Port = 5002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Printout Logs of the activity on MainPage
        Log.i(TAG, "OnCreate");

        run();

        // This is just to display wrong password Text for invalid password
        WrongPassword = (TextView)findViewById(R.id.textView3);
        WrongPassword.setVisibility(View.GONE);

        Login_button = (Button) findViewById(R.id.button);

        //Check if the Login button is clicked
        Login_button.setOnClickListener(new View.OnClickListener() {

            //When the Login button is clicked, it sends the username and password to the server
            //to check if its is valid or not and server sends back a boolean
            @Override
            public void onClick(View v) {

                //Printout Logs of the activity on MainPage
                Log.i(TAG, "OnClick: udpSend");

                try {

                    //Printout Logs of the activity on MainPage
                    Log.i(TAG, "OnClick: udpSend: Sent");

                    DatagramSocket s = new DatagramSocket(server_Port);
                    InetAddress local = InetAddress.getByName("127.0.0.1");
                    int Username_length = Username.getText().toString().length();
                    int Password_length = Password.getText().toString().length();

                    //getting the actual message to be sent
                    byte[] message1 = Username.getText().toString().getBytes();

                    byte[] message2 = Password.getText().toString().getBytes();

                    byte[] actual_message = new byte[100];
                    actual_message[0] = 3;

                    System.arraycopy(message1, 0, actual_message, 1, Username_length);
                    actual_message[Username_length+1] = 0;

                    System.arraycopy(message2,0, actual_message, Username_length+2, Password_length);
                    actual_message[Username_length+Password_length+2] = 0;



                    //Used to implement the packet the delivery service. Information for the delivery is
                    //contained within the datagramPacket
                    DatagramPacket p = new DatagramPacket(actual_message, Username_length+Password_length+3, local, server_Port);
                    s.send(p);


                    //Printout Logs of the activity on MainPage
                    Log.i(TAG, "OnClick: udpSend: Sent");

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    /*Called when the User Clicks the Login Button*/
    public void Login(View view){

        //Printout Logs of the activity on MainPage
        Log.i(TAG, "Login");

        Username = (EditText) findViewById(R.id.editText);
        Password = (EditText) findViewById(R.id.editText2);

        //Get Username from the User and Send to the server
        String Username1 = Username.getText().toString();
        String Password1 = Password.getText().toString();


        //allow Login when the Correct Password is Pressed
        if(Username.getText().toString().equals("sherlock") &&
                Password.getText().toString().equals("holmes")){
            //Correct Password
            Intent intent = new Intent(this, Mainpage.class);
            startActivity(intent);
        }else {
            //Wrong Password
            WrongPassword.setVisibility(View.VISIBLE);
            WrongPassword.setBackgroundColor(Color.MAGENTA);

        }
    }


    //Receive Data  from server.

    // Trying to get it to Receive a string then check this string if its true or false
    public void run(){

        //Printout Logs to let us know that it is receiving from the Server
        Log.i(TAG, "run: ReceivingFromServer");

        boolean run = true;
            while (run){
                try {
                    DatagramSocket udpSocket = new DatagramSocket(server_Port);
                    byte[] message = new byte[8000];

                    DatagramPacket packet = new DatagramPacket(message, message.length);
                    Log.i("UDP client: ", "Waiting to receive from Server");
                    udpSocket.receive(packet);

                    // Getting string from the byte Array. i.e Turning byte Array to String
                    Login_valid = new String(packet.getData());

                    //Check if the String contains YES or NO. If YES then it let us Login and
                    //If No then it doesn't allow Login and if anything else then it makes a toast to
                    //Tell us that
                    if(Login_valid.contains("YES")) {
                        Intent intent = new Intent(this, Mainpage.class);
                        startActivity(intent);

                    } else if(Login_valid.contains("NO")){

                        //Wrong Password
                        WrongPassword.setVisibility(View.VISIBLE);
                        WrongPassword.setBackgroundColor(Color.MAGENTA);

                        //Calls the run method again until right password is pressed
                        run();

                        run = true;
                    }else{
                        Context context = getApplicationContext();
                        CharSequence text = "Wrong Message From Server";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        //Calls the run method again until right password is pressed
                        run();
                    }

                    String text = new String(message, 0, packet.getLength());

                    Log.d("Received data", text);
                } catch (SocketException e) {
                    e.printStackTrace();
                    run = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    run = false;
                }

                //Printout Logs to let us know that it is receiving from the Server
                Log.i(TAG, "run: ReceivingFromServer");
            }
    }
}
