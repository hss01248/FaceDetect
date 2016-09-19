package com.hss01248.facedetect.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*public void initToolBar(boolean homeAsUpEnabled, Toolbar toolbar, String resTitle) {
        toolbar.setTitle(resTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }*/

    /*public void initToolBar(boolean homeAsUpEnabled, Toolbar toolbar, int resTitle) {
        toolbar.setTitle(getString(resTitle));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }*/

  /*  public <P> P getSharePrefence(Class<P> spClass) {
        return Esperandro.getPreferences(spClass, this);
    }

    public FacePreferences getFacePreferences() {
        return Esperandro.getPreferences(FacePreferences.class, this);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
