package com.baviux.homeassistant.launcher;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.baviux.homeassistant.HassWebView;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = "HomeAssistantLauncher";
    private final static long SESSION_TIMEOUT_MILLIS = 10000;
    private final static int REQUEST_PIN_CODE = 4100;

    private Toolbar mToolbar;
    private HassWebView mWebView;
    private EditText mUrlEditText;

    private String mUrl = null;
    private long mSessionExpireMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar_main);
        mUrlEditText = findViewById(R.id.urlEditText);

        setSupportActionBar(mToolbar);

        Preferences.load(this);
        mUrlEditText.setText(Preferences.getUrl());
        mToolbar.setVisibility(Preferences.getHideToolbar() ? View.GONE : View.VISIBLE);
        setupWebView();

        mUrlEditText.setOnEditorActionListener(mUrlEditTextOnEditorActionListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Preferences.getUseLockScreen() && isSessionFinished()) {
            Intent intent = new Intent(this, EnterPinActivity.class);
            startActivityForResult(intent, REQUEST_PIN_CODE);
        }
    }

    @Override
    protected void onStop() {
        if (mSessionExpireMillis > 0) {
            resetSessionExpireMillis();
        }

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_lock_screen).setChecked(Preferences.getUseLockScreen());
        menu.findItem(R.id.menu_back_key_behavior).setChecked(Preferences.getAdjustBackKeyBehavior());
        menu.findItem(R.id.menu_hide_admin_menu_items).setChecked(Preferences.getHideAdminMenuItems());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable()){
            item.setChecked(!item.isChecked());
        }

        switch (item.getItemId()) {
            case R.id.menu_lock_screen:
                Preferences.setUseLockScreen(item.isChecked());
                break;
            case R.id.menu_back_key_behavior:
                Preferences.setAdjustBackKeyBehavior(item.isChecked());
                mWebView.setAdjustBackKeyBehavior(Preferences.getAdjustBackKeyBehavior());
                break;
            case R.id.menu_hide_admin_menu_items:
                Preferences.setHideAdminMenuItems(item.isChecked());
                mWebView.setHideAdminMenuItems(Preferences.getHideAdminMenuItems());
                break;
            case R.id.menu_hide_top_bar:
                askHidingTopBar();
                break;
        }

        Preferences.save(this);

        return true;
    }

    private void askHidingTopBar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.hide_top_bar_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Preferences.setHideToolbar(true);
                        Preferences.save(MainActivity.this);
                        mToolbar.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private TextView.OnEditorActionListener mUrlEditTextOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                Preferences.setUrl(mUrlEditText.getText().toString());
                Preferences.save(MainActivity.this);
                setupWebView();

                mUrlEditText.clearFocus();
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);

                return true;
            }

            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PIN_CODE:
                if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                    finish();
                }
                else if (resultCode == RESULT_OK){
                    resetSessionExpireMillis();
                }
                break;
        }
    }

    private void setupWebView(){
        if(mWebView == null) {
            mWebView = findViewById(R.id.webView);

            mWebView.setHideAdminMenuItems(Preferences.getHideAdminMenuItems());
            mWebView.setAdjustBackKeyBehavior(Preferences.getAdjustBackKeyBehavior());
            mWebView.setEventHandler(new HassWebView.IEventHandler() {
                @Override
                public void onFinish() {
                    MainActivity.this.finish();
                }
            });
        }

        String url = Preferences.getUrl();
        if (url != null && url.trim().length() > 0 && !url.equals(mUrl)){
            mUrl = url;
            mWebView.loadUrl(mUrl);
        }
    }

    private boolean isSessionFinished(){
        return (System.currentTimeMillis() > mSessionExpireMillis);
    }

    private void resetSessionExpireMillis(){
        mSessionExpireMillis = System.currentTimeMillis() + SESSION_TIMEOUT_MILLIS;
    }

    @Override
    public void onBackPressed() {
        if (mWebView == null){
            super.onBackPressed();
            return;
        }

        mWebView.goBack();
    }
}
