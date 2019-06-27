package fr.dotscreen.tictactoe;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] tic_toc_btns = new Button[3][3];
    private boolean player1on = true;
    private int no_of_rounds;
    private int player1pts;
    private int player2pts;

    private TextView player1_score;
    private TextView player2_score;
    private TextView winner;

    private TextView timerText;
    private ImageView img1, img2;
    private Button restart;

    private CountDownTimer countDownTimer;
    private long timeInMilliseconds = 180000; //3 mins
    private boolean timerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1_score = (TextView) findViewById(R.id.text_view_score1);
        player2_score = (TextView) findViewById(R.id.text_view_score2);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);

        winner = (TextView) findViewById(R.id.winner);

        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();

            }
        });

        timerText = (TextView) findViewById(R.id.countdown_text);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                tic_toc_btns[i][j] = findViewById(resID);
                tic_toc_btns[i][j].setOnClickListener(this);
            }
        }

        manageTimer();

    }

    public void manageTimer(){
        if(timerStatus){
            stopTimer();

        } else {
            startTimer();

        }
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeInMilliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (int) millisUntilFinished / 60000;
                int seconds = (int) millisUntilFinished % 60000 / 1000;

                String remainingTime;

                remainingTime = "0" + minutes;
                remainingTime += ":";

                if(seconds < 10)
                    remainingTime += "0";

                remainingTime += seconds;
                timerText.setText(remainingTime);

            }

            public void onFinish() {
                timerText.setText("done!");

                int p1score = Integer.parseInt(player1_score.getText().toString());
                int p2score = Integer.parseInt(player2_score.getText().toString());

                if(p1score > p2score)
                    winner.setText("Player1 wins!!");

                else if(p1score > p2score)
                    winner.setText("Player2 wins!!");

                else if(p1score == p2score)
                    winner.setText("It's a draw!!");

            }
        }.start();

        timerStatus = true;
    }

    public void stopTimer() {
        if(countDownTimer != null)
            countDownTimer.cancel();
        timerStatus = false;

    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("") || timerStatus == false){
            return;
        }

        if (player1on){
            ((Button) v).setText("X");
            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.VISIBLE);

        } else {
            ((Button) v).setText("O");
            img2.setVisibility(View.INVISIBLE);
            img1.setVisibility(View.VISIBLE);
        }

        no_of_rounds++;
        gameStatus();

    }

    private void gameStatus() {
        if(checkWinner()) {
            if(player1on){
                player1Win();
            }  else {
                player2Win();
            }

        } else if (no_of_rounds == 9) {
            draw();
        } else {
            player1on = !player1on;
        }

    }

    private boolean checkWinner(){
        String[][] area = new String[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                area[i][j] = tic_toc_btns[i][j].getText().toString();

            }
        }

        for(int i = 0; i < 3; i++){
            if(area[i][0].equals(area[i][1])
                    && area[i][0].equals(area[i][2])
                    && !area[i][0].equals("")){
                return true;
            }

        }
        for(int i = 0; i < 3; i++) {
            if (area[0][i].equals(area[1][i])
                    && area[0][i].equals(area[2][i])
                    && !area[0][i].equals("")) {
                return true;
            }
        }
        if (area[0][0].equals(area[1][1])
                && area[0][0].equals(area[2][2])
                && !area[0][0].equals("")) {
            return true;
        }
        if (area[0][2].equals(area[1][1])
                && area[0][2].equals(area[2][0])
                && !area[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Win() {
        player1pts++;
        Toast.makeText(MainActivity.this, "Player 1 wins!!", Toast.LENGTH_SHORT).show();
        updatePoints();
        resetTicToc();

    }

    private void player2Win() {
        player2pts++;
        Toast.makeText(MainActivity.this, "Player 2 wins!!", Toast.LENGTH_SHORT).show();
        updatePoints();
        resetTicToc();
    }

    private void draw() {Toast.makeText(MainActivity.this, "Draw!!", Toast.LENGTH_SHORT).show();

        resetTicToc();
    }

    private void updatePoints() {
        player2_score.setText(String.valueOf(player2pts));
        player1_score.setText(String.valueOf(player1pts));

    }

    private void resetTicToc() {
        for(int i = 0; i < 3; i ++){
            for(int j = 0; j < 3; j++) {
                tic_toc_btns[i][j].setText("");
            }
        }

        no_of_rounds = 0;
        player1on = true;
    }

    private void resetGame() {
        player1pts = 0;
        player2pts = 0;
        updatePoints();
        resetTicToc();
        img1.setVisibility(View.VISIBLE);
        img2.setVisibility(View.INVISIBLE);

        timerStatus = false;
        timerText.setText("00 : 00");
        winner.setText("");

        if(countDownTimer != null)
            countDownTimer.cancel();

        manageTimer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}
