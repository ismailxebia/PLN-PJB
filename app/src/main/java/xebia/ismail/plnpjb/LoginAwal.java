package xebia.ismail.plnpjb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import xebia.ismail.plnpjb.adapter.LoginDataBaseAdapter;

/**
 * Created by Admin on 6/13/2017.
 */

public class LoginAwal extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        SharedPreferences settings = getSharedPreferences(MyPREFERENCES, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(LoginAwal.this, Dashboard.class);
            startActivity(intent);
        }

        final EditText editTextUserName = (EditText)findViewById(R.id.editTextUserNameToLogin);
        final EditText editTextPassword = (EditText)findViewById(R.id.editTextPasswordToLogin);

        TextView btnSignUp = (TextView) findViewById(R.id.regis);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAwal.this, RegistAwal.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        LinearLayout btnSignIn = (LinearLayout) findViewById(R.id.buttonSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String storedPassword = loginDataBaseAdapter
                        .getSinlgeEntry(userName);
                if (password.equals(storedPassword)) {
                    Toast.makeText(LoginAwal.this,
                            "Congrats: Login Successfull", Toast.LENGTH_LONG)
                            .show();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userName", userName);
                    editor.putString("password", password);
                    editor.putString("logged", "logged");
                    editor.commit();

                    Intent main = new Intent(LoginAwal.this, Dashboard.class);
                    main.putExtra("USERNAME", userName);
                    startActivity(main);
                } else {
                    Snackbar.make(v, "No. ID or Password does not match", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDataBaseAdapter.close();
    }
}
