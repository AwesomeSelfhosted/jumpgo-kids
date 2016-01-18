/*
 * Copyright 2015 Josiah Horton
 *
 * Created by Josiah Horton on 1/13/2016.
 */
package com.jtechme.jumpgokids.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jtechme.jumpgokids.R;

import butterknife.ButterKnife;
import butterknife.Bind;

//This is the crazy bit

import android.animation.ArgbEvaluator;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.VideoView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.jtechme.jumpgokids.R;
import com.jtechme.jumpgokids.app.BrowserApp;
import com.jtechme.jumpgokids.bus.BookmarkEvents;
import com.jtechme.jumpgokids.bus.BrowserEvents;
import com.jtechme.jumpgokids.constant.BookmarkPage;
import com.jtechme.jumpgokids.constant.Constants;
import com.jtechme.jumpgokids.constant.HistoryPage;
import com.jtechme.jumpgokids.controller.BrowserController;
import com.jtechme.jumpgokids.database.BookmarkManager;
import com.jtechme.jumpgokids.database.HistoryDatabase;
import com.jtechme.jumpgokids.dialog.BookmarksDialogBuilder;
import com.jtechme.jumpgokids.object.ClickHandler;
import com.jtechme.jumpgokids.object.SearchAdapter;
import com.jtechme.jumpgokids.preference.PreferenceManager;
import com.jtechme.jumpgokids.receiver.NetworkReceiver;
import com.jtechme.jumpgokids.utils.PermissionsManager;
import com.jtechme.jumpgokids.utils.ProxyUtils;
import com.jtechme.jumpgokids.utils.ThemeUtils;
import com.jtechme.jumpgokids.utils.UrlUtils;
import com.jtechme.jumpgokids.utils.Utils;
import com.jtechme.jumpgokids.utils.WebUtils;
import com.jtechme.jumpgokids.view.AnimatedProgressBar;
import com.jtechme.jumpgokids.view.LightningView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    //@Bind(R.id.link_signup) TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        //_signupLink.setOnClickListener(new View.OnClickListener() {

            //@Override
            //public void onClick(View v) {
                // Start the Signup activity
                //Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
            //}
        //});
    }

    String loginEmail = _emailText.getText().toString();
    String loginPassword = _passwordText.getText().toString();

    public void login() {
        Log.d(TAG, "Login");

        final String email = _emailText.getText().toString();
        loginEmail = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        loginPassword = _passwordText.getText().toString();

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        //String email = _emailText.getText().toString();
        //String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        if (email.contentEquals(SignupActivity.UserEmail)) {
            if (password.contentEquals(SignupActivity.UserPassword)) {
                onLoginSuccess();
            } else {
                onLoginFailed();
            }
        } else {
            onLoginFailed();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        //if (email.contentEquals(SignupActivity.UserEmail)) {
                            //if (password.contentEquals(SignupActivity.UserPassword)) {
                                //onLoginSuccess();
                            //} else {
                                //onLoginFailed();
                            //}
                        //} else {
                            //onLoginFailed();
                        //}
                        //onLoginSuccess(startActivity(new Intent(this, SettingsActivity.class)););
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();

            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
        //startActivity(new Intent(this, BrowserActivity.class));
        startActivity(new Intent(this, MainActivity.class));
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        startActivity(new Intent(this, SettingsActivity.class));
        //finish();
    }

    //@Override
    //public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
            //startActivity(new Intent(this, BrowserActivity.class));
        //}
        //return true;
    //}

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);

        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
            
        }

        return valid;
    }
}
