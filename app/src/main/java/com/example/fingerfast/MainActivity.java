package com.example.fingerfast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

     private TextView txtTimer;
     private Button  btnTap;
     private TextView txtThousand;

     private CountDownTimer mCountDownTimer;

     private long initialCountDownInMillis=60000;
     private int timerInterval=1000;
     private long remianingTime;
     private int thousand=10;
     private final String REMAINING_TIME_KEY="remaining time key ";
     private final String THOUSAND_KEY="thousand key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer=findViewById(R.id.btnTime);
        btnTap=findViewById(R.id.btnTap);
        txtThousand=findViewById(R.id.txtThousand);
        txtThousand.setText(thousand+"");

        if(  savedInstanceState!=null){
            remianingTime=savedInstanceState.getInt(REMAINING_TIME_KEY);
            thousand=savedInstanceState.getInt(THOUSAND_KEY);

            restoreTheGame();
        }
        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thousand--;
                txtThousand.setText(thousand + "");
                if (remianingTime >= 0 && thousand <= 0) {
                    showToast("Winner");
                    showAlert("Congratulations", "please reset the game");
                    mCountDownTimer.cancel();
                }
            }
        });


       if(savedInstanceState==null) {
           mCountDownTimer = new CountDownTimer(initialCountDownInMillis, timerInterval) {

               @Override
               public void onTick(long millisUntilFinished) {

                   remianingTime = (int) millisUntilFinished / 1000;
                   txtTimer.setText(remianingTime + "");

               }

               @Override
               public void onFinish() {
                   showToast("Countdown Finished");
                   showAlert("Do better next time", "Would you like to try again");

               }
           };
           mCountDownTimer.start();
       }
    }

    private void restoreTheGame() {

        txtTimer.setText(remianingTime+"");
        txtThousand.setText(thousand+"");
        mCountDownTimer=new CountDownTimer(remianingTime*1000,timerInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remianingTime=(int)millisUntilFinished/1000;
                txtTimer.setText(remianingTime+"");
            }

            @Override
            public void onFinish() {
                showToast( "Countdown Finished");
                showAlert("Do better next time","Would you like to try again");

            }
        };
        mCountDownTimer.start();
    }

    private void resetGame(){
        if(mCountDownTimer!=null){
            mCountDownTimer.cancel();
            mCountDownTimer=null;
        }
        thousand=10;
        txtThousand.setText(thousand+"");
        txtTimer.setText(remianingTime+"");

        mCountDownTimer=new CountDownTimer(initialCountDownInMillis,timerInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remianingTime=(int)millisUntilFinished/1000;
                txtTimer.setText(remianingTime+"");
            }

            @Override
            public void onFinish() {
                    showToast("Countdown Finished" );
                    showAlert("Do better next time","Would you like to try again");
                }


        };
        mCountDownTimer.start();
}

// Functions to avoid long codes
private void showAlert(String title, String message){
    AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                       resetGame();
                }
            }).show();
    alertDialog.setCancelable(false);
}

private void showToast(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putLong(REMAINING_TIME_KEY,remianingTime);
        outState.putInt(THOUSAND_KEY,thousand);
        mCountDownTimer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.info_item){
            showToast("This is the current version"+BuildConfig.VERSION_NAME);
        }
        return true;
    }
}