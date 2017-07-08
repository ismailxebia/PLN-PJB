package xebia.ismail.plnpjb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.aflak.bluetooth.Bluetooth;
import xebia.ismail.plnpjb.bluetooth.Select;
import xebia.ismail.plnpjb.util.AnimUtils;
import xebia.ismail.plnpjb.util.ViewUtils;

/**
 * Created by Admin on 6/12/2017.
 */

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout b;
    //private BottomSheetBehavior bottomSheetBehavior;
    //private Toolbar toolbar;

    //ImageButton closesheet;

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

    private Bluetooth bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        bt = new Bluetooth(this);
        bt.disableBluetooth();

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //final ImageButton fab = (ImageButton) findViewById(R.id.fab);

        b = (LinearLayout) findViewById(R.id.abouts);
        b.setOnClickListener(this);

        ImageView btnlogout = (ImageView) findViewById(R.id.logout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("LOGOUT");
                builder.setMessage("Do you want to LogOut ??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences sharedpreferences = getSharedPreferences(LoginAwal.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(Dashboard.this,LoginAwal.class);
                        startActivity(intent);
                        finish();

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
            }
        });

        LinearLayout bluetootha = (LinearLayout)findViewById(R.id.blmod);
        bluetootha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this,Select.class);
                startActivity(intent);
            }
        });

        saveDribbble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionShare();
                hideSaveConfimation();
                Snackbar.make(view, "Loading...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        saveDesignerNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionFeedback();
                hideSaveConfimation();
            }
        });

        Button closes = (Button)findViewById(R.id.save_confirmed);
        closes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSaveConfimation();
            }
        });
    }

    private void onOptionShare() {
        String[] TO = {"xebia.interface@gmail.com"};
        String[] CC = {""};
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setData(Uri.parse("mailto:"));
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "www.dribbble.com/ismail_xebia");

        try {
            startActivity(Intent.createChooser(shareIntent,"Share To "));
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(Dashboard.this,"Bla",Toast.LENGTH_SHORT).show();
        }
    }

    public void onOptionFeedback() {
        final Uri contactUri = null;
        String[] TO = {"xebia.interface@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email Here");

        try {
            startActivity(Intent.createChooser(emailIntent,"Send Email"));
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(Dashboard.this,"Bla",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(Dashboard.this, AboutActivity.class),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onBackPressed() {
        if (confirmSaveContainer.getVisibility() == View.VISIBLE) {
            hideSaveConfimation();
        } else {
            this.finish();
            //startActivity(new Intent(MainActivity.this, DialogExit.class),
            //        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    @OnClick(R.id.fab)
    protected void save() {
        // show the save confirmation bubble
        confirmSaveContainer.setVisibility(View.VISIBLE);
        resultsScrim.setVisibility(View.VISIBLE);

        // expand it once it's been measured and show a scrim over the search results
        confirmSaveContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                .OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // expand the confirmation
                confirmSaveContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                Animator reveal = ViewAnimationUtils.createCircularReveal(confirmSaveContainer,
                        confirmSaveContainer.getWidth() / 2,
                        confirmSaveContainer.getHeight() / 2,
                        fab.getWidth() / 2,
                        confirmSaveContainer.getWidth() / 2);
                reveal.setDuration(350L);
                reveal.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(Dashboard.this));
                reveal.start();

                // Menampilkan Background Transparant
                int centerX = (fab.getLeft() + fab.getRight()) / 2;
                int centerY = (fab.getTop() + fab.getBottom()) / 2;
                Animator revealScrim = ViewAnimationUtils.createCircularReveal(
                        resultsScrim,
                        centerX,
                        centerY,
                        0,
                        (float) Math.hypot(centerX, centerY));
                revealScrim.setDuration(600L);
                revealScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(Dashboard
                        .this));
                revealScrim.start();
                ObjectAnimator fadeInScrim = ObjectAnimator.ofArgb(resultsScrim,
                        ViewUtils.BACKGROUND_COLOR,
                        Color.TRANSPARENT,
                        ContextCompat.getColor(Dashboard.this, R.color.scrimgreen));
                fadeInScrim.setDuration(700L);
                fadeInScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(Dashboard
                        .this));
                fadeInScrim.start();

                closeButton.setAlpha(0f);
                closeButton.setTranslationX(closeButton.getWidth() * -0.6f);
                closeButton.animate().alpha(1f).translationX(0f).setDuration(400L).setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(Dashboard
                        .this));

                // ease in the checkboxes
                saveDribbble.setAlpha(0f);
                saveDribbble.setTranslationY(saveDribbble.getHeight() * 0.8f);
                saveDribbble.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(400L)
                        .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(Dashboard
                                .this));
                saveDesignerNews.setAlpha(0f);
                saveDesignerNews.setTranslationY(saveDesignerNews.getHeight() * 0.5f);
                saveDesignerNews.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(400L)
                        .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(Dashboard
                                .this));
                return false;
            }
        });
    }

    @OnClick(R.id.results_scrim)
    protected void hideSaveConfimation() {
        if (confirmSaveContainer.getVisibility() == View.VISIBLE) {
            // contract the bubble & hide the scrim
            AnimatorSet hideConfirmation = new AnimatorSet();
            hideConfirmation.playTogether(
                    ViewAnimationUtils.createCircularReveal(confirmSaveContainer,
                            confirmSaveContainer.getWidth() / 2,
                            confirmSaveContainer.getHeight() / 2,
                            confirmSaveContainer.getWidth() / 2,
                            fab.getWidth() / 2),
                    ObjectAnimator.ofArgb(resultsScrim,
                            ViewUtils.BACKGROUND_COLOR,
                            Color.TRANSPARENT));
            hideConfirmation.setDuration(250L);
            hideConfirmation.setInterpolator(AnimUtils.getFastOutSlowInInterpolator
                    (Dashboard.this));
            hideConfirmation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    confirmSaveContainer.setVisibility(View.GONE);
                    resultsScrim.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                }
            });
            hideConfirmation.start();

            closeButton.setAlpha(1f);
            closeButton.setTranslationY(closeButton.getWidth() * 0f);
            closeButton.animate().alpha(0f).translationY(0.6f).setDuration(200L).setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(Dashboard
                    .this));
        }
    }

    private void clearResults() {
        confirmSaveContainer.setVisibility(View.GONE);
        resultsScrim.setVisibility(View.GONE);
    }
}
