package org.example.pacman;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private TextView scoreView;
    private TextView highScoreView;
    private TextView usernameView;
    private TextView levelUpTimeView;
    private TextView LevelUpView;
    private Button pause_start;

    public static final String pref = "PacMan" ;
    public static final String hscore = "highScore" ;
    public static final String username = "username" ;

    GameView gameView;

    private Timer moveTimer;
    private Timer enemyMoveTimer;
    private Timer levelUpTimer;

    private int timePassed = 0;
    private boolean running = false;
    private String direction = "right";
    private final int levelUpTime = 15;
    static int currentLevel = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        gameView =  findViewById(R.id.gameView);
        scoreView =  findViewById(R.id.score_num);
        highScoreView = findViewById(R.id.highscore);
        usernameView =  findViewById(R.id.username);
        LevelUpView =  findViewById(R.id.level);
        levelUpTimeView = findViewById(R.id.countdown_num);
        scoreView.setText(Integer.toString(gameView.score));
        levelUpTimeView.setText(Integer.toString(levelUpTime) + " sec");
        pause_start = findViewById(R.id.pause_start);
        LevelUpView.setText("Level: " + Integer.toString(currentLevel));


        //Listeners
        gameView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                if (running) {
                    direction = "top";
                }
            }
            public void onSwipeRight() {
                if (running) {
                    direction = "right";
                }
            }
            public void onSwipeLeft() {
                if(running) {
                    direction = "left";
                }
            }
            public void onSwipeBottom() {
                if(running) {
                    direction = "bottom";
                }
            }

        });

        pause_start.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {
                int action = motionevent.getAction();
                LevelUpView.setText("Level: "+ Integer.toString(currentLevel));
                if (action == MotionEvent.ACTION_DOWN) {
                    if (running) {
                        running = false;
                        pause_start.setText("Start");

                    } else {
                        running = true;
                        pause_start.setText("Pause");
                    }
                    if (gameView.finished) {
                        gameView.finished = false;
                    }
                }
                return false;
            }
        });


        // Timers
        moveTimer = new Timer();
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(Pacman_Move);
            }

        }, 0, 30);

        enemyMoveTimer = new Timer();
        enemyMoveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(Enemy_Move);
            }

        }, 0, 50);

        levelUpTimer = new Timer();
        levelUpTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(TimeCountdown);
            }

        }, 0, 1000);




//      Save and update highscore
        SharedPreferences sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        gameView.highScore = sharedpreferences.getInt(hscore, 0);
        gameView.username = sharedpreferences.getString(username, " ");
        highScoreView.setText(Integer.toString(gameView.highScore));
        usernameView.setText(gameView.username);

    }


    private Runnable TimeCountdown = new Runnable() {
        public void run() {
            if (running && !gameView.finished) {
                if (timePassed < levelUpTime) {
                    timePassed++;
                    levelUpTimeView.setText(Integer.toString(levelUpTime - timePassed) + " seconds");
                } else {
                    timePassed = 0;
                    currentLevel++;
                    running = false;
                    Enemy enemy = new Enemy(gameView.w / 2, gameView.h / 2);
                    gameView.enemies.add(enemy);
                    new CountDownTimer(3000, 1000) {
                        int tick = 3;
                        Toast toast;
                        public void onTick(long millisUntilFinished) {
                            if (tick == 3) {
                                toast = Toast.makeText(getApplicationContext(), "Level " + Integer.toString(currentLevel) + ". Starting in " + Integer.toString(tick) + " sec", Toast.LENGTH_SHORT);
                            } else {
                                toast.cancel();
                                toast = Toast.makeText(getApplicationContext(), "Level " + Integer.toString(currentLevel) + ". Starting in " + Integer.toString(tick) + " sec", Toast.LENGTH_SHORT);
                                LevelUpView.setText("Level: "+ Integer.toString(currentLevel));
                            }
                            tick--;
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        public void onFinish() {
                            running = true;
                        }
                    }.start();
                }
            }
        }
    };


    private Runnable Pacman_Move = new Runnable() {
        public void run() {
            if(!gameView.username.equals(highScoreView.getText())){
                usernameView.setText(gameView.username);
                highScoreView.setText(Integer.toString(gameView.highScore));
            }
            if (running && gameView.finished ) {
                running = false;
                gameView.pacman = new Pacman(0, 0);
                gameView.pacman.moveRight(0);
                final Button pause_start = findViewById(R.id.pause_start);
                pause_start.setText("Start");
                Toast toast = Toast.makeText(getApplicationContext(), "Game over, press start when ready", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                gameView.enemies = new ArrayList<>();
                currentLevel = 1;
                LevelUpView.setText("Level: "+ Integer.toString(currentLevel));
                timePassed = 0;
                direction = "right";
            }
            if (running && !gameView.finished) {
                switch (direction) {
                    case "right":
                        gameView.moveRight(10);
                        break;
                    case "left":
                        gameView.moveLeft(10);
                        break;
                    case "top":
                        gameView.moveTop(10);
                        break;
                    case "bottom":
                        gameView.moveBottom(10);
                        break;
                    default:
                        break;
                }
                scoreView.setText(Integer.toString(gameView.score));
                usernameView.setText(gameView.username);
            }

        }
    };
    private Runnable Enemy_Move = new Runnable() {
        public void run() {
            if (running) {
                if (gameView.enemies.size() < 2) {
                    for (int i = 0; i < 2; i++) {
                        Enemy enemy = new Enemy(gameView.w / 2, gameView.h / 2);
                        gameView.enemies.add(enemy);
                    }
                }
                gameView.moveEnemies(10, 10);

            }

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedpreferences = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(hscore, gameView.highScore);
        editor.putString(username, gameView.username);
        editor.commit();
        running = false;
        pause_start.setText("Start");

    }
    @Override
    protected void onStart() {
        super.onStart();

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if (id == R.id.action_newGame) {
             pause_start.setText("Start");
             gameView.score = 0;
             gameView.pacman = new Pacman(0, 0);
             gameView.enemies = new ArrayList<>();
             currentLevel = 1;
             direction = "right";
             gameView.moveRight(0);
             timePassed = 0;
             LevelUpView.setText("Level: "+ Integer.toString(currentLevel));
             running = false;
             Toast toast = Toast.makeText(this,"Press start when ready", Toast.LENGTH_LONG);
             toast.setGravity(Gravity.CENTER, 0, 0);
             toast.show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
