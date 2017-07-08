package xebia.ismail.plnpjb;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Admin on 6/12/2017.
 */

public class Bluetooth extends AppCompatActivity implements View.OnClickListener {
    private Button b;
    private BottomSheetBehavior bottomSheetBehavior;
    //private Toolbar toolbar;

    ImageButton closesheet;

    @Bind(R.id.fab)
    ImageButton fab;
    @Bind(R.id.confirm_save_container)
    ViewGroup confirmSaveContainer;
    @Bind(R.id.save_dribbble)
    Button saveDribbble;
    @Bind(R.id.save_designer_news)
    Button saveDesignerNews;
    @Bind(R.id.save_confirmed)
    Button closeButton;
    @Bind(R.id.results_scrim)
    View resultsScrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modul_bluetooth);
        ButterKnife.bind(this);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final ImageButton fab = (ImageButton) findViewById(R.id.fab);
        View llBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        closesheet = (ImageButton) findViewById(R.id.buttondown);
        closesheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // set All Activities bawah
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    fab.animate().scaleX(0).scaleY(0).setDuration(200).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    fab.animate().scaleX(1).scaleY(1).setDuration(200).start();
                    closesheet.animate().scaleX(0).scaleY(0).setDuration(100).start();
                    fab.setVisibility(View.VISIBLE);
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    fab.setVisibility(View.GONE);
                    closesheet.setVisibility(View.VISIBLE);
                    closesheet.animate().scaleX(1).scaleY(1).setDuration(200).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        b = (Button) findViewById(R.id.abouts);
        b.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(Bluetooth.this, AboutActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onBackPressed() {
        //
    }
}
