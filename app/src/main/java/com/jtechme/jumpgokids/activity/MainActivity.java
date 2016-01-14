package com.jtechme.jumpgokids.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.jtechme.jumpgokids.R;
import com.jtechme.jumpgokids.preference.PreferenceManager;

@SuppressWarnings("deprecation")
public class MainActivity extends BrowserActivity {

    //Firstrun instance

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Perhaps set content view here

        prefs = getSharedPreferences("com.jtechme.jumpgokids", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
            startActivity(new Intent(this, SignupActivity.class));
        }
    }

    //End of firstrun instance

    @Override
    public void updateCookiePreference() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        cookieManager.setAcceptCookie(PreferenceManager.getInstance().getCookiesEnabled());
    }

    @Override
    public synchronized void initializeTabs() {
        restoreOrNewTab();
        // if incognito mode use newTab(null, true); instead
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveOpenTabs();
    }

    @Override
    public void updateHistory(String title, String url) {
        addItemToHistory(title, url);
    }

    @Override
    public boolean isIncognito() {
        return false;
    }

    @Override
    public void closeActivity() {
        closeDrawers();
        moveTaskToBack(true);
    }


}
