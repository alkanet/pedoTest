package com.ebs.test;

import android.content.SharedPreferences;

/**
 * Created by yyh on 15-7-31.
 */
public class PedoSettings {
    SharedPreferences mSettings;

    public static int M_NONE = 1;
    public static int M_PACE = 2;
    public static int M_SPEED = 3;

    public PedoSettings(SharedPreferences settings) {
        mSettings = settings;
    }

    public boolean isMetric() {
        return mSettings.getString("units", "imperial").equals("metric");
    }

    public float getStepLength() {
        try {
            return Float.valueOf(mSettings.getString("step_length", "20").trim());
        }
        catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }

    public float getBodyWeight() {
        try {
            return Float.valueOf(mSettings.getString("body_weight", "50").trim());
        }
        catch (NumberFormatException e) {
            // TODO: reset value, & notify user somehow
            return 0f;
        }
    }

    public void saveServiceRunningWithTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", Utils.currentTimeInMillis());
        editor.commit();
    }

    public void saveServiceRunningWithNullTimestamp(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public void clearServiceRunning() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", false);
        editor.putLong("last_seen", 0);
        editor.commit();
    }

    public boolean isServiceRunning() {
        return mSettings.getBoolean("service_running", false);
    }

    public boolean isNewStart() {
        // activity last paused more than 10 minutes ago
        return mSettings.getLong("last_seen", 0) < Utils.currentTimeInMillis() - 1000*60*10;
    }

    public void savePaceOrSpeedSetting(int maintain, float desiredPaceOrSpeed) {
        SharedPreferences.Editor editor = mSettings.edit();
        if (maintain == M_PACE) {
            editor.putInt("desired_pace", (int)desiredPaceOrSpeed);
        }
        else
        if (maintain == M_SPEED) {
            editor.putFloat("desired_speed", desiredPaceOrSpeed);
        }
        editor.commit();
    }

}
