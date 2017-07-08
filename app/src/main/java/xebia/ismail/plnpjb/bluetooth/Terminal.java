package xebia.ismail.plnpjb.bluetooth;

/**
 * Created by acer on 5/29/2017.
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import xebia.ismail.plnpjb.R;

public class Terminal extends AppCompatActivity implements Bluetooth.CommunicationCallback {
    private String name;
    private Bluetooth b;
    private EditText message;
    private Button send;
    private Button button1, button2, button3, button4, button5;
    private Button save, clear, off;
    private TextView text;
    private TextView status, bearing, grease, timer;
    private ScrollView scrollView;
    private ScrollView scrollView2;
    private boolean registered=false;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terminal_layout);

        //For Text Initialisasi
        text = (TextView)findViewById(R.id.text);
        status = (TextView) findViewById(R.id.status);
        bearing = (TextView) findViewById(R.id.bearing);
        grease = (TextView) findViewById(R.id.grease);
        timer = (TextView) findViewById(R.id.timer);

        //Command message
        message = (EditText)findViewById(R.id.message);

        //Button in Apps
        send = (Button)findViewById(R.id.send);
        button1 = (Button)findViewById(R.id.button_1);
        button2 = (Button)findViewById(R.id.button_2);
        button3 = (Button)findViewById(R.id.button_3);
        button4 = (Button)findViewById(R.id.button_4);
        button5 = (Button)findViewById(R.id.button_5);
        save = (Button)findViewById(R.id.save_button);
        clear = (Button)findViewById(R.id.clear_button);
        off = (Button)findViewById(R.id.off_button);

        scrollView = (ScrollView) findViewById(R.id.scroll_terminal);
        scrollView2 = (ScrollView) findViewById(R.id.scroll_2);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coorlay_1);

        // data will scroll
        //text.setMovementMethod(new ScrollingMovementMethod());
        //send.setEnabled(false);
        //timer.setMovementMethod(new ScrollingMovementMethod());
        //send.setEnabled(false);

        b = new Bluetooth(this);
        b.enableBluetooth();

        b.setCommunicationCallback(this);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    return;
                }
                String msg = message.getText().toString();
                char[] chars = msg.toCharArray();
                byte[] bytes = new byte[chars.length];
                for (int i=0; i < chars.length; i++) {
                    bytes[i] = (byte) chars[i];
                }
                message.append(msg);
                b.send(msg);
                Display("You: "+msg);
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;

        // Function Button 1 - 5
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = button1.getText().toString();
                button1.setText("1");
                b.send(msg);
                Display("You: "+msg);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = button2.getText().toString();
                button2.setText("2");
                b.send(msg);
                Display("You: "+msg);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = button3.getText().toString();
                button3.setText("3");
                b.send(msg);
                Display("You: "+msg);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = button4.getText().toString();
                button4.setText("4");
                b.send(msg);
                Display("You: "+msg);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = button5.getText().toString();
                button5.setText("5");
                b.send(msg);
                Display("You: "+msg);
            }
        });

        // save your data monitoring
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Do you want to Save ?",
                        Snackbar.LENGTH_LONG).setAction("YES", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String msg = text.getText().toString();
                        String time = java.text.DateFormat.getDateTimeInstance().format(new Date());
                        String directory = new String(String.valueOf(Environment.getExternalStorageDirectory()));
                        write(msg,"MonitoringOnline" + time +".txt");
                        Toast.makeText(getApplicationContext(), "File Saved ! \n"+"MonitoringOnline "+directory+time+".txt", Toast.LENGTH_LONG).show();

                    }

                });
                snackbar.show();
                }
        });

        // Clear Terminal
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(" ");
            }
        });

        // Disconnect and Bluetooth will Off
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar snackbar1 = Snackbar.make(coordinatorLayout,"Do you want Disconnect ? Your monitoring will end",
                        Snackbar.LENGTH_LONG).setAction("YES", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        b.removeCommunicationCallback();
                        b.disconnect();
                        b.disableBluetooth();
                    }

                });
                snackbar1.show();
            }
        });

    }

    private void write (String msg, String myFile) {
        File file = new File(Environment.getExternalStorageDirectory(),
                myFile);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(msg.getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.close:
                b.removeCommunicationCallback();
                b.disconnect();
                Intent intent = new Intent(this, Select.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public void Display(final String s){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.append(s + "\n");
                timer.append(s + "\n");
                scrollView2.fullScroll(View.FOCUS_DOWN);
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        Display("Connected to "+device.getName()+" - "+device.getAddress());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                send.setEnabled(true);
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        Display("Disconnected!");
        Display("Connecting again...");
        b.connectToDevice(device);
    }

    @Override
    public void onMessage(String message) {
        Display(name+": "+message);
    }

    @Override
    public void onError(String message) {
        Display("Error: "+message);
    }

    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
        Display("Error: "+message);
        Display("Trying again in 3 sec.");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b.connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(Terminal.this, SelectDevice.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you want to Exit ? Your monitoring will end")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void
                    onClick(DialogInterface dialog, int id) {
                        b.disableBluetooth();
                        b.disconnect();
                        b.removeCommunicationCallback();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}