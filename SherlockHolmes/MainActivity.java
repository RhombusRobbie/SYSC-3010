package com.example.adebola.sherlockholmes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.lang.String;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements Runnable{


    EditText Username, Password;
    TextView WrongPassword;

    //Variable to check for validity of the username and password from server
    public Boolean Login_valid = false;

    public Button Login_button;

    // Port the Server is sitting on
    public int server_Port = 12345;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //udpConnect = new Thread(new udpReceive())
        // This is just to display wrong password Text for invalid password
        WrongPassword = (TextView)findViewById(R.id.textView3);
        WrongPassword.setVisibility(View.GONE);

        Login_button = (Button) findViewById(R.id.button);

        Login_button.setOnClickListener(new View.OnClickListener() {

            //When the Login button is clicked, it sends the username and password to the server
            //to check if its is valid or not and server sends back a boolean
            @Override
            public void onClick(View v) {

                try {
                    DatagramSocket s = new DatagramSocket(server_Port);
                    InetAddress local = InetAddress.getByName("192.168.1.102");
                    int Username_length = Username.getText().toString().length();
                    int Password_length = Password.getText().toString().length();

                    //getting the actual message to be sent
                    byte[] message1 = Username.getText().toString().getBytes();

                    byte[] message2 = Password.getText().toString().getBytes();

                    //Used to implement the packet the delivery service. Information for the delivery is
                    //contained within the datagramPacket
                    DatagramPacket p = new DatagramPacket(message1, Username_length, local, server_Port);
                    s.send(p);

                    //Send the Password to the Server
                    DatagramPacket q = new DatagramPacket(message2, Password_length, local, server_Port);
                    s.send(q);

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
    @Override
    public void run(){
        boolean run = true;
            while (run){
                try {
                    DatagramSocket udpSocket = new DatagramSocket(server_Port);
                    byte[] message = new byte[8000];

                    DatagramPacket packet = new DatagramPacket(message, message.length);
                    Log.i("UDP client: ", "Waiting to receive from Server");
                    udpSocket.receive(packet);


                    String text = new String(message, 0, packet.getLength());

                    //ASk Husin how to receive String from Server
                    Login_valid = new Boolean(message, 0, packet.getLength());
                    Log.d("Received data", text);
                } catch (SocketException e) {
                    e.printStackTrace();
                    run = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    run = false;
                }
            }
    }
}
