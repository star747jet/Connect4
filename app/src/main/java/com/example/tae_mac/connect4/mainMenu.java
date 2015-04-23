package com.example.tae_mac.connect4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class mainMenu extends ActionBarActivity {
    int roomid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void onButtonClicked(View V){
        switch (V.getId()){
            case R.id.start:
                Intent i = new Intent(this, playBoard.class);
                startActivityForResult(i, 88);
                ;break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView tvResult = (TextView)findViewById(R.id.tvResult);
        ImageView imgResult = (ImageView)findViewById(R.id.imgResult);

        if (requestCode == 88) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("retValue");

                tvResult.setText(result);

                    imgResult.setImageResource(R.drawable.draw1);
                
            }
            else if (resultCode == RESULT_CANCELED) {
                tvResult.setText("CANCELED");

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
