package xebia.ismail.plnpjb.bluetooth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import me.aflak.bluetooth.Bluetooth;
import xebia.ismail.plnpjb.R;
import xebia.ismail.plnpjb.util.AnimUtils;

public class Chat extends AppCompatActivity implements Bluetooth.CommunicationCallback, View.OnClickListener {
    private String name;
    private Bluetooth b;
    private EditText message;
    private ImageButton send;
    private TextView text;
    private ScrollView scrollView;
    private ImageView bukaa,tshare,tclear,tdisconnect;
    private boolean registered = false;

    private LinearLayout m1,m2,m3,m4,m5;

    private LinearLayout mRevealView;
    private boolean hidden = true;

    private View theMenu;
    private View overlay;
    private boolean menuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        theMenu = findViewById(R.id.the_menu);
        overlay = findViewById(R.id.overlay);

        text = (TextView) findViewById(R.id.text);
        message = (EditText) findViewById(R.id.message);
        bukaa = (ImageView) findViewById(R.id.buka);
        tshare = (ImageView)findViewById(R.id.tosent);
        tclear = (ImageView)findViewById(R.id.toclear);
        tdisconnect = (ImageView)findViewById(R.id.toexit);
        send = (ImageButton) findViewById(R.id.send);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        m1 = (LinearLayout)findViewById(R.id.a1);
        m2 = (LinearLayout)findViewById(R.id.a2);
        m3 = (LinearLayout)findViewById(R.id.a3);
        m4 = (LinearLayout)findViewById(R.id.a4);

        //OnClickListener(this);
        m1.setOnClickListener(this);
        m2.setOnClickListener(this);
        m3.setOnClickListener(this);
        m4.setOnClickListener(this);

        tshare.setOnClickListener(this);
        tclear.setOnClickListener(this);
        tdisconnect.setOnClickListener(this);

        //mRevealView = (LinearLayout)findViewById(R.id.revealmenu);

        text.setMovementMethod(new ScrollingMovementMethod());
        //send.setEnabled(false);

        b = new Bluetooth(this);
        b.enableBluetooth();

        b.setCommunicationCallback(this);

        int pos = getIntent().getExtras().getInt("pos");
        name = b.getPairedDevices().get(pos).getName();

        Display("Connecting...");
        b.connectToDevice(b.getPairedDevices().get(pos));

        /*
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                message.setText("");
                b.send(msg);
                Display("You: "+msg);
            }
        });
        */
        bukaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!menuOpen) {
                    revealMenu();
                } else {
                    hideMenu();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString();
                message.setText("");
                b.send(msg);
                Display("You : "+msg);
            }
        });
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registered) {
            unregisterReceiver(mReceiver);
            registered = false;
        }
    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sent:
                //
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String msg = text.getText().toString();

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

                try {
                    startActivity(Intent.createChooser(shareIntent, "Share To "));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Chat.this, "Bla", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.clear:
                text.setText(" ");
                text.setText("Clear Log");
                return true;
            case R.id.close:
                final AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                builder.setTitle("Close Connection");
                builder.setMessage("Do you want to exit ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        b.removeCommunicationCallback();
                        b.disconnect();
                        Intent intent = new Intent(Chat.this, Select.class);
                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    */

    public void Display(final String s) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.append(s + "\n");
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onConnect(BluetoothDevice device) {
        Display("Connected to " + device.getName() + " - " + device.getAddress());
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
        Display(name + ": " + message);
    }

    @Override
    public void onError(String message) {
        Display("Error: " + message);
    }

    @Override
    public void onConnectError(final BluetoothDevice device, String message) {
        Display("Error: " + message);
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

    public void onBackPressed() {
        if (menuOpen) {
            hideMenu();
        } else {
            //finishAfterTransition();
            new AlertDialog.Builder(this)
                    .setMessage("Are you want to Exit ? Your monitoring will end")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void
                        onClick(DialogInterface dialog, int id) {
                            b.disconnect();
                            b.disableBluetooth();
                            b.removeCommunicationCallback();

                            //Intent intent = new Intent(Chat.this, Dashboard.class);
                            //startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                //Intent intent1 = new Intent(Chat.this, Dashboard.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        if (registered) {
                            unregisterReceiver(mReceiver);
                            registered = false;
                        }
                        //startActivity(intent1);
                        Chat.this.finish();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if (registered) {
                            unregisterReceiver(mReceiver);
                            registered = false;
                        }
                        //startActivity(intent1);
                        Chat.this.finish();
                        break;
                }
            }
        }
    };

    public void revealMenu() {
        menuOpen = true;

        theMenu.setVisibility(View.INVISIBLE);
        int cx = theMenu.getRight() - 140;
        int cy = theMenu.getTop() - 220;
        int finalRadius = Math.max(theMenu.getWidth(), theMenu.getHeight());
        Animator anim =
                ViewAnimationUtils.createCircularReveal(theMenu, cx, cy, 0, finalRadius);
        anim.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(Chat.this));
        anim.setDuration(600L);
        theMenu.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);
        anim.start();


        // Animasi Tombol

        Animation popeup1 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup2 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup3 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup4 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup5 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        Animation popeup6 = AnimationUtils.loadAnimation(this, R.anim.popeup);
        popeup1.setStartOffset(350);
        popeup2.setStartOffset(300);
        popeup3.setStartOffset(250);
        popeup4.setStartOffset(300);
        m1.startAnimation(popeup1);
        m2.startAnimation(popeup2);
        m3.startAnimation(popeup3);
        m4.startAnimation(popeup4);
    }

    public void hideMenu() {
        menuOpen = false;
        int cx = theMenu.getRight() - 140;
        int cy = theMenu.getTop() - 100;
        int initialRadius = theMenu.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(theMenu, cx, cy, initialRadius, 0);
        anim.setInterpolator(AnimUtils.getFastOutSlowInInterpolator
                (Chat.this));
        anim.setDuration(400L);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                theMenu.setVisibility(View.INVISIBLE);
                theMenu.setVisibility(View.GONE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    public void overlayClick(View v) {
        hideMenu();
    }

    public void menuClick(View view) {
        hideMenu();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.a1:
                b.send("Hello");
                Display("You : Hello");
                hideMenu();
                break;
            case R.id.a2:
                b.send("Help");
                Display("You : Help ? ");
                hideMenu();
                break;
            case R.id.a3:
                b.send("Command");
                Display("You : Command! ");
                hideMenu();
                break;
            case R.id.a4:
                b.send("Close");
                Display("You : Close ");
                hideMenu();
                break;
            case R.id.tosent:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                String msg = text.getText().toString();

                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, msg);

                try {
                    startActivity(Intent.createChooser(shareIntent, "Share To "));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Chat.this, "Bla", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.toclear:
                text.setText(" ");
                text.setText("Clear Log");
                break;
            case R.id.toexit:
                final AlertDialog.Builder builder = new AlertDialog.Builder(Chat.this);
                builder.setTitle("Close Connection");
                builder.setMessage("Do you want to exit ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        b.removeCommunicationCallback();
                        b.disconnect();
                        b.disableBluetooth();
                        finish();
                        //Intent intent = new Intent(Chat.this, Dashboard.class);
                        //startActivity(intent);

                    }
                });

                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }
}
