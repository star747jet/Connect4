package com.example.tae_mac.connect4;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class playBoard extends ActionBarActivity {
    public int turn=0, i, j;
    public String winState="playing";
    public int count[] = {0,0,0,0,0,0,0};
    public int board[][] = new int[7][6];

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
    }

    public void onButtonClicked(View V) {
        if (winState=="playing") {
            switch (V.getId()) {
                //case R.id.bt_0:
                case R.id.c00:
                case R.id.c01:
                case R.id.c02:
                case R.id.c03:
                case R.id.c04:
                case R.id.c05:
                    if (count[0] <= 5) {
                            function_1(board[0][count[0]]);
                            count[0]++;
                    }
                    break;
                //case R.id.bt_1:
                case R.id.c10:
                case R.id.c11:
                case R.id.c12:
                case R.id.c13:
                case R.id.c14:
                case R.id.c15:
                    if (count[1] <= 5) {
                        function_1(board[1][count[1]]);
                        count[1]++;
                    }
                    break;
                //case R.id.bt_2:
                case R.id.c20:
                case R.id.c21:
                case R.id.c22:
                case R.id.c23:
                case R.id.c24:
                case R.id.c25:
                    if (count[2] <= 5) {
                        function_1(board[2][count[2]]);

                        count[2]++;
                    }
                    break;
                //case R.id.bt_3:
                case R.id.c30:
                case R.id.c31:
                case R.id.c32:
                case R.id.c33:
                case R.id.c34:
                case R.id.c35:
                    if (count[3] <= 5) {
                        function_1(board[3][count[3]]);
                        count[3]++;
                    }
                    break;
                //case R.id.bt_4:
                case R.id.c40:
                case R.id.c41:
                case R.id.c42:
                case R.id.c43:
                case R.id.c44:
                case R.id.c45:
                    if (count[4] <= 5) {
                        function_1(board[4][count[4]]);
                        count[4]++;
                    }
                    break;
                //case R.id.bt_5:
                case R.id.c50:
                case R.id.c51:
                case R.id.c52:
                case R.id.c53:
                case R.id.c54:
                case R.id.c55:
                    if (count[5] <= 5) {
                        function_1(board[5][count[5]]);
                        count[5]++;
                    }
                    break;
                //case R.id.bt_6:
                case R.id.c60:
                case R.id.c61:
                case R.id.c62:
                case R.id.c63:
                case R.id.c64:
                case R.id.c65:
                    if (count[6] <= 5) {
                        function_1(board[6][count[6]]);
                        count[6]++;
                    }
                    break;
                case R.id.surrender:
                    break;
                case R.id.exit:
                    if(winState=="red_win") {
                        Intent res = new Intent();
                        res.putExtra("retValue", "RED WIN");
                        setResult(RESULT_OK, res);
                        finish();
                    }
                    else if(winState=="yellow_win") {
                        Intent res = new Intent();
                        res.putExtra("retValue", "YELLOW WIN");
                        setResult(RESULT_OK, res);
                        finish();
                    }
                    else if(winState=="playing") {
                        Intent res = new Intent();
                        res.putExtra("retValue", "Test");
                        setResult(RESULT_OK, res);
                        finish();
                    }
                    break;
            }
        }
    }


    public void function_1(int cxx){
        if(turn==0) {
            ImageView cell = (ImageView)findViewById(cxx);//where cxx = {c11,c12,...,c16}
            cell.setImageResource(R.drawable.red1);
            turn=1;
            function_2(cxx);
        }
        else if(turn==1) {
            ImageView cell = (ImageView)findViewById(cxx);
            cell.setImageResource(R.drawable.yellow1);
            turn=0;
            function_2(cxx);
        }
    }

    public void function_2(int cxx){
        Button update = (Button)findViewById(R.id.exit);
        ImageView cellRC = (ImageView)findViewById(cxx);
        String label1 = cellRC.getResources().getResourceName(cellRC.getId());
        String row =  label1.substring(label1.length()-1,label1.length());
        String col = label1.substring(label1.length()-2,label1.length()-1);
        update.setText("EXIT turn=" + turn + "row= " + row + "col= " + col);
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
}
