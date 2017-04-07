package com.example.adebola.sherlockholmes;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.IntentFilter;

import static com.example.adebola.sherlockholmes.R.id.textView;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class History extends AppCompatActivity {

    //Notification builder for each notification
   // NotificationCompat.Builder notification;

 //   private static String TAG = "History";

    //Notification ID number. so the system knows how to handle each notifications
  //  private static final int uniqueID = 45612;

    private static String TAG = "History";

    String eventText;

   // DatagramSocket dsocket;

    public Button call_button;
    TextView textViewobj;


    @Override
    //Shows History of the events that had ocurred
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Start the Server Service Intent
        Intent ServiceIntent = new Intent(this, ServerService.class);
        startService(ServiceIntent);

        textViewobj = (TextView) findViewById(R.id.textView);
        //Trying to Receive the eventText from Service Activity
        Log.i(TAG, "Trying to Receive event from service");
       // Intent intent3 = getIntent();
        // Bundle extras = intent3.getExtras();

       // event = extras.getString("eventText");
       // event = intent3.getStringExtra(ServerService.EXTRA_MESSAGE);
       // extras.putString("eventText", event);



        //eventText = ServerService.getEvent().getString();
        //To display the event on the page
        textViewobj = new TextView(this);
        textViewobj.setTextSize(200);
        textViewobj.setText(eventText);
        //textViewobj.post(Mainpage.getService());
        Log.i(TAG, "After trying to receive");




        call_button = (Button) findViewById(R.id.callbutton);


        /*
        //Building the notification
        notification = new NotificationCompat.Builder(this);
        //we want to cancel the notification after the user has seen it and clicked
        notification.setAutoCancel(true);
        */

      call_button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view) {


              Intent callIntent = new Intent(Intent.ACTION_CALL);
              callIntent.setData(Uri.parse("tel:6132996836"));

              //Gives the App Permission to Dial the Number
              if (ActivityCompat.checkSelfPermission(History.this,
                      Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                  return;
              }

              //Start callIntent to dial number
              startActivity(callIntent);
          }
      });
    }



    /*

        //Method to build the notification and
        public void sendNotification ( String event){

        //Notification Image
        notification.setSmallIcon(R.drawable.sherlockholmes);

        //Notification Sound
        notification.setTicker("this is the Ticket");
        //Time of the notification
        notification.setWhen(System.currentTimeMillis());



        notification.setContentTitle("URGENT!!! CHILD IN TROUBLE");
        notification.setContentText("");


        //What happens when the user clicks on the notification. send the user to the mainpage
        Intent intent1 = new Intent(this, History.class);
        //Giving the device access the intent in our app
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //send out the Notification and issue it(sending it to device)
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //building it and sending it out

            //Sends out notifcation only when event occurs
                nm.notify(uniqueID, notification.build());

    }*/
    /*

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("DATA");
            textViewobj.setText(str);
        }
    };*/

    /*
    void ondestroy(){
        unregisterReceiver(b);
    }*/


}

