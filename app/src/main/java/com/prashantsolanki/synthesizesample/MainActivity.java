package com.prashantsolanki.synthesizesample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.prashantsolanki.synthesize.lib.Synthesize;
import com.prashantsolanki.synthesize.lib.SynthesizeCommons;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SynthesizeCommons commons = new SynthesizeCommons(this, SynthesizeCommons.Commons.WEB_VIEW);
        commons.setParams(1080,1920);
        commons.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.setUrl("https://prashantcolanki.com");
            }
        });

        commons.addOnSaveListener(new Synthesize.OnSaveListener() {
            @Override
            public void onSuccess(File file) {
                Toast.makeText(getApplicationContext(),"Saved at"+file.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
