package xebia.ismail.plnpjb.config;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import xebia.ismail.plnpjb.R;

/**
 * Created by Admin on 6/14/2017.
 */

public class Config implements IConfig {

    private Config(@Nullable Context context) {
        mR = null;
        mContext = context;
        if (context != null)
            mR = context.getResources();
    }

    private static Config mConfig;
    private Context mContext;
    private Resources mR;

    public static void init(@NonNull Context context) {
        mConfig = new Config(context);
    }

    public static void setContext(Context context) {
        if (mConfig != null) {
            mConfig.mContext = context;
            if (context != null)
                mConfig.mR = context.getResources();
        }
    }

    private void destroy() {
        mContext = null;
        mR = null;
    }

    public static void deinit() {
        if (mConfig != null) {
            mConfig.destroy();
            mConfig = null;
        }
    }

    @NonNull
    public static IConfig get() {
        if (mConfig == null)
            return new Config(null); // shouldn't ever happen, but avoid crashes
        return mConfig;
    }

    @Nullable
    @Override
    public String feedbackEmail() {
        if (mR == null) return null;
        return mR.getString(R.string.feedback_email);
    }

    @Nullable
    @Override
    public String feedbackSubjectLine() {
        if (mR == null || mContext == null) return null;
        return mR.getString(R.string.feedback_subject_line, mContext.getString(R.string.app_name));
    }

    @Override
    public boolean feedbackEnabled() {
        final String feedbackEmail = feedbackEmail();
        return feedbackEmail != null && !feedbackEmail.trim().isEmpty();
    }

}
