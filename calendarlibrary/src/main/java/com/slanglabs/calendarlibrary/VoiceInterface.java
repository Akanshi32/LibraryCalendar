package com.slanglabs.calendarlibrary;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;



import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import in.slanglabs.platform.SlangBuddy;
import in.slanglabs.platform.SlangBuddyOptions;
import in.slanglabs.platform.SlangEntity;
import in.slanglabs.platform.SlangException;
import in.slanglabs.platform.SlangIntent;
import in.slanglabs.platform.SlangLocale;
import in.slanglabs.platform.SlangSession;
import in.slanglabs.platform.action.SlangMultiStepIntentAction;
import in.slanglabs.platform.action.SlangUtteranceAction;
import in.slanglabs.platform.prompt.SlangMessage;
import in.slanglabs.platform.ui.SlangBuiltinUI;
import in.slanglabs.platform.ui.SlangUIDelegate;
import in.slanglabs.platform.ui.SlangUIProvider;

import static in.slanglabs.platform.action.SlangAction.Status.FAILURE;
import static in.slanglabs.platform.action.SlangAction.Status.SUCCESS;

public class VoiceInterface  {
    private static Context sAppContext;
    static SlangUIDelegate delegate;


    private static boolean dateFlag, monthFlag, yearFlag;
    static public void init(final Application appContext) {
        sAppContext = appContext;

        try {
            SlangBuddyOptions options = new SlangBuddyOptions.Builder()
                    .setApplication(appContext)
                    .setBuddyId("08f01719aac6419cb3176d3f5764074b")
                    .setAPIKey("c556645091f84242b8879f3178c029fd")
                    .setListener(new V1Action.BuddyListener())
                    .setIntentAction(new V1Action(sAppContext))
                    .setConfigOverrides(getConfigOverrides())
                    .setRequestedLocales(SlangLocale.getSupportedLocales())
                    .setDefaultLocale(SlangLocale.LOCALE_ENGLISH_IN)
                    .setEnvironment(SlangBuddy.Environment.STAGING)
                    .setUtteranceAction(new SlangUtteranceAction() {
                        @Override
                        public void onUtteranceDetected(String userUtterance, SlangSession session) {

                            Log.i("AA", "onUtteranceUnresolved: " + userUtterance);
                        }

                        @Override
                        public Status onUtteranceUnresolved(String userUtterance, SlangSession session) {

                            Log.i("AAA", "onUtteranceUnresolved: " + userUtterance);

                            return FAILURE;
                        }
                    })
                    .setUIProvider(new SlangUIProvider() {

                        MyView bActivity;
                        Activity activity;

                        @Override
                        public void notifyUser(String s, Locale locale) {

                        }

                        @Override
                        public void onDisabled(SlangException e) {

                        }

                        @Override
                        public void onCreate(Activity cactivity, SlangUIDelegate slangUIDelegate) {



                            MyView bActivity = new MyView(sAppContext);
                            bActivity.init(slangUIDelegate, cactivity);

                            activity = cactivity;

                        }

                        @Override
                        public void onUserSessionStarted() {

                        }

                        @Override
                        public void onListeningStarted() {


                        }

                        @Override
                        public void onUserTextAvailable(String s, Locale locale) {
                            bActivity.textAvailable(s);
                        }

                        @Override
                        public void onListeningEnded()
                        {
                        //    bActivity.stopListening();
                        }

                        @Override
                        public void onListeningTimedOut() {
                        }

                        @Override
                        public void onListeningError(String s, Locale locale) {
                        }

                        @Override
                        public void onSlangProcessingStarted() {

                            //bActivity.startProcessing();

                        }

                        @Override
                        public void onSlangProcessingEnded() {
                            //bActivity.stopProcessing();
                        }

                        @Override
                        public void onSlangProcessingError(String s, Locale locale) {
                        }

                        @Override
                        public void onSlangTextAvailable(String s, Locale locale) {
                        }

                        @Override
                        public void onUserSessionEnded() {

                            //bActivity.stopListening();
                        }

                        @Override
                        public void onDismissed() {

                        }

                        @Override
                        public void onHelpRequested() {
                            //bActivity.showHelp();
                        }

                        @Override
                        public void onHelpRequested(Set<String> set) {

                        }

                        @Override
                        public void onDestroy() {

                        }

                    })
                    .build();

            SlangBuddy.initialize(options);
        } catch (SlangBuddyOptions.InvalidOptionException e) {
            Log.e("VoiceInterface", e.getLocalizedMessage());
        } catch (SlangBuddy.InsufficientPrivilegeException e) {
            Log.e("VoiceInterface", e.getLocalizedMessage());
        }

    }
    private static Map<String, Object> getConfigOverrides() {
        HashMap<String, Object> config = new HashMap<>();
        if (shouldForceDevTier()) {
            config.put("internal.common.io.server_host", "infer.slanglabs.in");
            config.put("internal.common.io.analytics_server_host", "analytics.slanglabs.in");
        }
        return config;
    }
    private static boolean shouldForceDevTier() {
        return true;
    }

    private static class V1Action implements SlangMultiStepIntentAction {
        private final Context mContext;
        V1Action(Context ctx) {
            mContext = ctx;
        }

        @Override
        public Status action(SlangIntent intent, SlangSession session) {
            Activity activity = session.getCurrentActivity();
            //resolveQuotes(activity,session);

            switch (intent.getName()) {
                case "date":
                    handleDate(intent);
                    break;
            }

            return SUCCESS;
        }


        private void handleDate(SlangIntent intent) {
            long date, month, year;
            String x = null;
            date = month = year = 0;
            if (dateFlag) {
                // Case for brown eyeglasses
                x = intent.getEntity("date").getValue();
                date = Long.parseLong(x);
            } else if (monthFlag) {
                // case for sunglasses
                x = intent.getEntity("month").getValue();
                month = Long.parseLong(x);
            }
            else if (yearFlag) {
                x = intent.getEntity("year").getValue();
                year = Long.parseLong(x);
            }
            MyView bActivity = new MyView(sAppContext);
            bActivity.setDate(date,month,year);
        }
        @Override
        public void onIntentResolutionBegin(SlangIntent intent, SlangSession session) {
            switch (intent.getName()) {
                case "date":
                    dateFlag = false;
                    monthFlag = false;
                    yearFlag = false;
                    break;

            }
        }

        @Override
        public Status onEntityUnresolved(SlangEntity entity, SlangSession session) {
            return SUCCESS;
        }

        @Override
        public Status onEntityResolved(SlangEntity entity, SlangSession session) {
        if (entity.getIntent().getName().equals("date")) {
                if (entity.getName().equals("date")) {
                    dateFlag = true;
                } else if (entity.getName().equals("month")) {
                    monthFlag = true;
                }else if (entity.getName().equals("year")) {
                    yearFlag = true;
                }
            }
            return SUCCESS;
        }

        @Override
        public void onIntentResolutionEnd(SlangIntent intent, SlangSession session) {

        }


        private static class BuddyListener implements SlangBuddy.Listener {
            @Override
            public void onInitialized() {

                SlangBuddy.getBuiltinUI().setPosition(SlangBuiltinUI.SlangUIPosition.LEFT_BOTTOM, true);
                HashMap<Locale, String> strings = new HashMap<>();
                strings.put(SlangLocale.LOCALE_ENGLISH_IN, "What would you like to do today?");
                strings.put(SlangLocale.LOCALE_ENGLISH_US, "What would you like to do today?");
                strings.put(SlangLocale.LOCALE_HINDI_IN, "आप आज क्या करना चाहेंगे?");
            }

            @Override
            public void onInitializationFailed(final SlangBuddy.InitializationError e) {
                new Handler(sAppContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(sAppContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onLocaleChanged(final Locale newLocale) {
                new Handler(sAppContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(sAppContext, "Changed locale to: " + newLocale.getDisplayName(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onLocaleChangeFailed(final Locale newLocale, SlangBuddy.LocaleChangeError e) {
                new Handler(sAppContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(sAppContext, "Failed to change locale to: " + newLocale.getDisplayName(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

}