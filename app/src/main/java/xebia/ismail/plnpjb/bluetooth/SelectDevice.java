package xebia.ismail.plnpjb.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xebia.ismail.plnpjb.R;


/**
 * Created by acer on 5/29/2017.
 */

public class SelectDevice extends AppCompatActivity  {

    private Bluetooth bt;
    private ListView listView;
    private Button not_found;
    private Button bluetooth_off;
    private FloatingActionButton fab_bluetooth;
    private List<BluetoothDevice> paired;
    //private PullToRefresh pull_to_refresh;
    private boolean registered=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device);

        listView = (ListView) findViewById(R.id.list);
        not_found = (Button) findViewById(R.id.not_found);

        // Enable Bluetooth
        fab_bluetooth = (FloatingActionButton) findViewById(R.id.fab_bluetooth);
        fab_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mReceiver, filter);
                registered = true;

                bt = new Bluetooth(SelectDevice.this);
                bt.enableBluetooth();
                Toast.makeText(getApplicationContext(), "Turn on Bluetooth", Toast.LENGTH_LONG).show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(SelectDevice.this, Terminal.class);
                        i.putExtra("pos", position);
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(i);
                        finish();
                    }
                });

        not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(SelectDevice.this, Scan.class);
               startActivity(i);

            }
        });
        addDevicesToList();
            }
        });

        //For Bluetooth OFF
        /*bluetooth_off = (Button) findViewById(R.id.blue_off);
        bluetooth_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt = new Bluetooth(SelectDevice.this);
                bt.disableBluetooth();
            }

            });*/

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SelectDevice.this, Terminal.class);
                i.putExtra("pos", position);
                if(registered) {
                    unregisterReceiver(mReceiver);
                    registered=false;
                }
                startActivity(i);
                finish();
            }
        });*/

       /* not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectDevice.this, Scan.class);
                startActivity(i);
            }
        });

        addDevicesToList();*/
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    private void addDevicesToList(){
        paired = bt.getPairedDevices();

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired){
            names.add(d.getName());
        }

        String[] array = names.toArray(new String[names.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, array);

        listView.setAdapter(adapter);

        not_found.setEnabled(true);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setEnabled(false);
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Turn on bluetooth", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addDevicesToList();
                                listView.setEnabled(true);
                            }
                        });
                        break;
                }
            }
        }
    };
}
