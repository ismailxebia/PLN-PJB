package xebia.ismail.plnpjb.config;

import android.support.annotation.Nullable;

/**
 * Created by Admin on 6/14/2017.
 */

public interface IConfig {

    @Nullable
    String feedbackEmail();

    @Nullable
    String feedbackSubjectLine();

    boolean feedbackEnabled();
}
