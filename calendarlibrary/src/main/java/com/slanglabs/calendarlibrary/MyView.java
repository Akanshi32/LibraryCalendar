package com.slanglabs.calendarlibrary;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import in.slanglabs.platform.ui.SlangUIDelegate;

public class MyView extends RelativeLayout {

    SlangUIDelegate mDelegate;
    TextView tvSlang;
    ImageView trigger, activetrigger;
    CalendarView calendarView;

    public void init(SlangUIDelegate delegate, Activity activity) {
        mDelegate = delegate;
    }

    public MyView(Context context) {
        super(context);
        initialize(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(
            Context context){ inflate(context, R.layout.calendar, this);
            tvSlang = findViewById(R.id.tvSlang);
            activetrigger = findViewById(R.id.triggeractive);
            trigger = findViewById(R.id.trigger);
            activetrigger.setVisibility(GONE);
            setListener();
    }

    public void setListener() {
        trigger.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelegate.notifyUserStartedSession();
                activetrigger.setVisibility(VISIBLE);
            }
        });
    }

    public void textAvailable(String string) {
            tvSlang.setText(string);
    }

    public void setDate(long day, long month, long year) {
           calendarView = findViewById(R.id.calendar);
           long date = day + month + year;
           calendarView.setDate(date);


    }
}
