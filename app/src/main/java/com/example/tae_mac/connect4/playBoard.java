package com.example.tae_mac.connect4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class playBoard extends ActionBarActivity implements Runnable {
    int turn, i, j, roomid =0;
    int last_turn=1,status,last_placeid=0,placecol,placerow;
    String winState="playing";
    int count[] = {0,0,0,0,0,0,0};
    int board[][] = new int[7][6];
    Handler handler;
    boolean active;
    boolean fetching;

    @Override
    public void onStart(){
        super.onStart();
        active = true;
    }

    public void onStop(){
        super.onStop();
        active= false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i1, j1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_board);
        for(i=0;i<=6;i++){
            for(j=0;j<=5;j++){
                int textViewID = getResources().getIdentifier("c"+i+j, "id", getPackageName());
                board[i][j] = textViewID;
            }
        }

        GetRoomTask task = new GetRoomTask();
        task.execute();

        handler = new Handler();
        handler.postDelayed(this, 5000);
    }

    public void run() {
        if(!active){
            return;
        }
        if(status==0) {
            GetRoomTask task = new GetRoomTask();
            task.execute();
            handler.postDelayed(this, 5000);
        } else {
            if(last_turn==turn && winState=="playing" && !fetching){
                GetGameTask task = new GetGameTask();
                task.execute();
                handler.postDelayed(this, 3000);
            }
        }
    }

    public void onButtonClicked(View V) {

            switch (V.getId()) {
                //case R.id.bt_0:
                case R.id.c00:
                case R.id.c01:
                case R.id.c02:
                case R.id.c03:
                case R.id.c04:
                case R.id.c05:
                    place_chip(0,count[0]);
                    break;
                //case R.id.bt_1:
                case R.id.c10:
                case R.id.c11:
                case R.id.c12:
                case R.id.c13:
                case R.id.c14:
                case R.id.c15:
                    place_chip(1,count[1]);
                    break;
                //case R.id.bt_2:
                case R.id.c20:
                case R.id.c21:
                case R.id.c22:
                case R.id.c23:
                case R.id.c24:
                case R.id.c25:
                    place_chip(2,count[2]);
                    break;
                //case R.id.bt_3:
                case R.id.c30:
                case R.id.c31:
                case R.id.c32:
                case R.id.c33:
                case R.id.c34:
                case R.id.c35:
                    place_chip(3,count[3]);
                    break;
                //case R.id.bt_4:
                case R.id.c40:
                case R.id.c41:
                case R.id.c42:
                case R.id.c43:
                case R.id.c44:
                case R.id.c45:
                    place_chip(4,count[4]);
                    break;
                //case R.id.bt_5:
                case R.id.c50:
                case R.id.c51:
                case R.id.c52:
                case R.id.c53:
                case R.id.c54:
                case R.id.c55:
                    place_chip(5,count[5]);
                    break;
                //case R.id.bt_6:
                case R.id.c60:
                case R.id.c61:
                case R.id.c62:
                case R.id.c63:
                case R.id.c64:
                case R.id.c65:
                    place_chip(6,count[6]);
                    break;
                case R.id.exit:
                    AlertDialog.Builder b = new AlertDialog.Builder(playBoard.this);
                    CharSequence s = "Are you sure ?";
                    b.setTitle("Quit");
                    b.setMessage(s);
                    b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    b.setPositiveButton("Cancel", null);
                    b.show();
                    break;
            }

    }

    public void place_chip(int col,int row){
        int cxx;
        if (count[col] <= 5 && status==1&& last_turn!=turn) {
            cxx = board[col][row];
            count[col] = row+1;
            function_1(cxx,turn);
            checkWin(col,row);

            last_turn = turn;
            ((TextView)findViewById(R.id.TurnView)).setText("Opponent's turn.");
            turnImg();
            PostChipTask p = new PostChipTask();
            p.execute(new String[]{col+"",row+""});
        }
    }
    public void place_chip2(int col,int row){
        int cxx = board[col][row];
        count[col] = row+1;
            function_1(cxx,last_turn);
        ((TextView)findViewById(R.id.TurnView)).setText("Your turn.");
        turnImg();
    }

    public void function_1(int cxx,int placeturn){
        if(placeturn==0) {
            ImageView cell = (ImageView)findViewById(cxx);//where cxx = {c11,c12,...,c16}
            cell.setImageResource(R.drawable.redchip);
            cell.setTag(placeturn);
            function_2(cxx);
        }
        else if(placeturn==1) {
            ImageView cell = (ImageView)findViewById(cxx);
            cell.setImageResource(R.drawable.yellowchip);
            cell.setTag(placeturn);
            function_2(cxx);
        }
    }

    public void function_2(int cxx){
        TextView update = (TextView)findViewById(R.id.exit);
        ImageView cellRC = (ImageView)findViewById(cxx);
        String label1 = cellRC.getResources().getResourceName(cellRC.getId());
        String row =  label1.substring(label1.length()-1,label1.length());
        String col = label1.substring(label1.length()-2,label1.length()-1);
        //update.setText("EXIT turn=" + turn + "row= " + row + "col= " + col);
    }

    public void turnImg(){
        if(turn==0){
            if(last_turn != turn){
                ((ImageView)findViewById(R.id.TurnImg)).setImageResource(R.drawable.redchip0);
            }
            else{
                ((ImageView)findViewById(R.id.TurnImg)).setImageResource(R.drawable.redchip1);
            }
        }
        else {
            if (last_turn != turn) {
                ((ImageView) findViewById(R.id.TurnImg)).setImageResource(R.drawable.yellowchip0);
            } else {
                ((ImageView) findViewById(R.id.TurnImg)).setImageResource(R.drawable.yellowchip1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_board, menu);
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

    class GetRoomTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            BufferedReader reader;
            StringBuilder buffer = new StringBuilder();
            String line;

            try {

                Log.e("GetRoomTask", "");
                URL u;
                if(roomid==0) {
                    u = new URL("http://ict.siit.tu.ac.th/~u5522781004/connect4/get_room.php");
                } else {
                    u = new URL("http://ict.siit.tu.ac.th/~u5522781004/connect4/get_room.php?roomid=" + roomid);
                }
                HttpURLConnection h = (HttpURLConnection)u.openConnection();
                h.setRequestMethod("GET");
                h.setDoInput(true);
                h.connect();

                int response = h.getResponseCode();
                if (response == 200) {
                    reader = new BufferedReader(new InputStreamReader(h.getInputStream()));
                    while((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    Log.e("GetRoomTask", buffer.toString());
                    //Parsing JSON and displaying messages

                    //To append a new message:


                    JSONObject json = new JSONObject(buffer.toString());
                    if(roomid==0) {
                        roomid = json.getInt("roomid");
                        turn = json.getInt("turn");
                    } else {
                        int roomStatus = json.getInt("status");
                        if(roomStatus==1){
                            status=1;
                        }

                    }

                }
            } catch (MalformedURLException e) {
                Log.e("GetRoomTask", "Invalid URL");
            } catch (IOException e) {
                Log.e("GetRoomTask", "I/O Exception");
            } catch (JSONException e) {
                Log.e("GetRoomTask", "Invalid JSON");
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(status==1){
                getConnected();
            }
            /*
            if (result) {
                adapter.notifyDataSetChanged();
                lastUpdate = System.currentTimeMillis();
                Toast t = Toast.makeText(MessageActivity.this.getApplicationContext(),
                        "Updated the timeline",
                        Toast.LENGTH_SHORT);
                t.show();
            }
            */
        }
    }

    public void getConnected(){
        TextView sta = (TextView)findViewById(R.id.status);
        String a;
        if(turn==0){
            a = "You play RED";
            ((TextView)findViewById(R.id.TurnView)).setText("Your turn.");
            turnImg();
        } else {
            a = "You play YELLOW";
            ((TextView)findViewById(R.id.TurnView)).setText("Opponent's turn.");
            turnImg();
        }
            sta.setText("Connected | " + a);
    }

    class PostChipTask extends AsyncTask<String, Void, Boolean> {
        String line;
        StringBuilder buffer = new StringBuilder();

        @Override
        protected Boolean doInBackground(String... params) {


            HttpClient h = new DefaultHttpClient();
            HttpPost p = new HttpPost("http://ict.siit.tu.ac.th/~u5522781004/connect4/post_game.php");
            String col = params[0];
            List<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("roomid", roomid+""));
            values.add(new BasicNameValuePair("turn", turn+""));
            values.add(new BasicNameValuePair("col",col));
            values.add(new BasicNameValuePair("row",params[1]));
            try {
                p.setEntity(new UrlEncodedFormEntity(values));
                HttpResponse response = h.execute(p);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }



                return true;
            } catch (UnsupportedEncodingException e) {
                Log.e("Error", "Invalid encoding");
            } catch (ClientProtocolException e) {
                Log.e("Error", "Error in posting a message");
            } catch (IOException e) {
                Log.e("Error", "I/O Exception");
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                run();
            }
        }
    }
    class GetGameTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            BufferedReader reader;
            StringBuilder buffer = new StringBuilder();
            String line;

            try {
                URL u = new URL("http://ict.siit.tu.ac.th/~u5522781004/connect4/get_game.php?last_placeid=" + last_placeid +
                                "&turn=" + turn + "&roomid=" + roomid);
                HttpURLConnection h = (HttpURLConnection)u.openConnection();
                h.setRequestMethod("GET");
                h.setDoInput(true);
                h.connect();

                int response = h.getResponseCode();
                if (response == 200) {
                    reader = new BufferedReader(new InputStreamReader(h.getInputStream()));
                    while((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    Log.e("LoadMessageTask", buffer.toString());
                    //Parsing JSON and displaying messages

                    //To append a new message:


                    JSONObject json = new JSONObject(buffer.toString());



                    if(json.getInt("new")==1 || json.getInt("new")==2) {

                        if(last_placeid>=json.getInt("last_placeid")) {
                            return false;
                        }

                        if(json.getInt("new")==2){
                            loseGame();
                        }

                            last_placeid = json.getInt("last_placeid");
                            last_turn = json.getInt("last_turn");
                            placecol = json.getInt("col");
                            placerow = json.getInt("row");
                    }
                }
            } catch (MalformedURLException e) {
                Log.e("LoadMessageTask", "Invalid URL");
            } catch (IOException e) {
                Log.e("LoadMessageTask", "I/O Exception");
            } catch (JSONException e) {
                Log.e("LoadMessageTask", "Invalid JSON");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(last_turn!=turn && placecol>=0){
                place_chip2(placecol,placerow);
            }
        }
    }

    void checkWin(int col, int row){
        if(checkDown(col,row)==4 || (checkLeft(col,row)+checkRight(col,row)-1)==4 || (checkUpLeft(col,row)+checkDownRight(col,row)-1)==4 || (checkDownLeft(col,row)+checkUpRight(col,row)-1)==4){
            PostChipTask p = new PostChipTask();
            p.execute(new String[]{9+"",0+""});
            winGame();
        }
    }

    int checkDown(int col, int row){
        if(row<0){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkDown(col,row-1);
        } else {
            return 0;
        }
    }

    int checkLeft(int col,int row){
        if(col<0){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkLeft(col-1,row);
        } else {
            return 0;
        }
    }

    int checkRight(int col,int row){
        if(col>6){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkRight(col+1,row);
        } else {
            return 0;
        }
    }

    int checkUpLeft(int col,int row){
        if(col<0 || row>5){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkUpLeft(col-1,row+1);
        } else {
            return 0;
        }
    }

    int checkDownLeft(int col,int row){
        if(col<0 || row<0){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkDownLeft(col-1,row-1);
        } else {
            return 0;
        }
    }
    int checkUpRight(int col,int row){
        if(col>6 || row>5){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkUpRight(col+1,row+1);
        } else {
            return 0;
        }
    }
    int checkDownRight(int col,int row){
        if(col>6 || row<0){
            return 0;
        }
        int cxx = board[col][row];
        ImageView cell = (ImageView)findViewById(cxx);
        if(cell.getTag()==turn){
            return 1 + checkDownRight(col+1,row-1);
        } else {
            return 0;
        }
    }

    void winGame(){
        winState = "WON";
        Intent res = new Intent();
        res.putExtra("retValue", "YOU WIN");
        setResult(RESULT_OK, res);
        finish();
    }

    void loseGame(){
        winState = "LOST";
        Intent res = new Intent();
        res.putExtra("retValue", "YOU LOST");
        setResult(RESULT_OK, res);
        finish();
    }
}
