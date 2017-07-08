package xebia.ismail.plnpjb.sms;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import xebia.ismail.plnpjb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 6/11/2017.
 */

public class SmsActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText smsBody;
    private Button sendMessage;
    private Button getData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sms_activity);

        phoneNumber = (EditText) findViewById(R.id.number_phone);
        smsBody = (EditText) findViewById(R.id.sms_custom);
        sendMessage = (Button) findViewById(R.id.sms_send);
        getData = (Button) findViewById(R.id.get_send);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsCustom();
            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetData();
            }
        });

    }

    public void sendSmsCustom() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber.getText().toString(),
                    null,
                    smsBody.getText().toString(),
                    null,
                    null);
            Toast.makeText(getApplicationContext(), "SMS Sending", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Sending SMS Failed", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    // Send sms number and message has determined
    public void sendGetData (){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+6289607719949",null,"GET DATA",null,null);
            Toast.makeText(getApplicationContext(), "SMS Sending", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Sending SMS Failed", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    /*@Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        SmsData sms = (SmsData)getListAdapter().getItem(position);

        Toast.makeText(getApplicationContext(), sms.getBody(), Toast.LENGTH_LONG).show();
    }*/



}
