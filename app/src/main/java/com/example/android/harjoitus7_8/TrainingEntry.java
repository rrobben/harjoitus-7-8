package com.example.android.harjoitus7_8;

import java.util.Date;

/**
 * Created by Roope on 07-Mar-18.
 */

public class TrainingEntry {
    private String mDate;
    private int mDuration;
    private int mRpe;
    private int mSharpness;
    private String mSport;

    public TrainingEntry() {}

    public TrainingEntry(String date, int duration, int rpe, int sharpness, String sport) {
        mDate = date;
        mDuration = duration;
        mRpe = rpe;
        mSharpness = sharpness;
        mSport = sport;
    }

    public String getDate() { return mDate; }
    public void setDate(String date) { mDate = date; }

    public int getDuration() { return mDuration; }
    public void setDuration(int duration) { mDuration = duration; }

    public int getRpe() { return mRpe; }
    public void setRpe(int rpe) { mRpe = rpe; }

    public int getSharpness() { return mSharpness; }
    public void setSharpness(int sharpness) { mSharpness = sharpness; }

    public String getSport() { return mSport; }
    public void setSport(String sport) { mSport = sport; }
}
