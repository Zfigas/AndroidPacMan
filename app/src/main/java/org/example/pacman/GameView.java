package org.example.pacman;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View {

	Pacman pacman = new Pacman(0, 0);
	List<Enemy> enemies = new ArrayList<>();
	GoldCoin coin = new GoldCoin(300, 300);

	Paint paint = new Paint();
	int h, w;
	int highScore = 0;
	String username = "";
	int score = 0;
	boolean finished = false;
	String orientation = "right";

	EditText username_input = new EditText(getContext());

	//PACMAN movement;
	Bitmap tempPacmanLeft = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_left);
	Bitmap bitmapPacmanLeft = Bitmap.createScaledBitmap(tempPacmanLeft, 75, 75, false);

	Bitmap tempPacmanRight = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_right);
	Bitmap bitmapPacmanRight = Bitmap.createScaledBitmap(tempPacmanRight, 75, 75, false);

	Bitmap tempPacmanUp = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_up);
	Bitmap bitmapPacmanUp = Bitmap.createScaledBitmap(tempPacmanUp, 75, 75, false);

	Bitmap tempPacmanDown = BitmapFactory.decodeResource(getResources(), R.drawable.pacman_down);
	Bitmap bitmapPacmanDown = Bitmap.createScaledBitmap(tempPacmanDown, 75, 75, false);

	//Monster movement
	Bitmap tempEnemy = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
	Bitmap bitmapEnemy = Bitmap.createScaledBitmap(tempEnemy, 75, 75, false);

	//Coin look
	Bitmap tempcoin = BitmapFactory.decodeResource(getResources(), R.drawable.goldcoin);
	Bitmap bitcoin = Bitmap.createScaledBitmap(tempcoin, 80, 80, false);

	public GameView(Context context) {
		super(context);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}




	public void moveRight(int x) {
		if (pacman.pacX + x + bitmapPacmanLeft.getWidth() <= w)
			pacman.moveRight(x);
		orientation = "right";
		invalidate();
	}

	public void moveLeft(int x) {

		if (pacman.pacX - x >= 0)
			pacman.moveLeft(x);
		orientation = "left";
		invalidate();
	}

	public void moveTop(int y) {

		if (pacman.pacY - y >= 0)
			pacman.moveTop(y);
		orientation = "top";
		invalidate();
	}

	public void moveBottom(int y) {

		if (pacman.pacY + y + bitmapPacmanLeft.getHeight() <= h)
			pacman.moveBottom(y);
		orientation = "bottom";
		invalidate();
	}
	public void moveEnemies(int x, int y) {
		Random rand = new Random();
		for (Enemy enemy : enemies) {
			if (enemy.movePossiblity == 0) {
				enemy.moveDirection = rand.nextInt(4);
				enemy.movePossiblity = rand.nextInt(10) + 1;
			}
			switch (enemy.moveDirection) {
				case 0:
					if (enemy.enemyX + x + bitmapEnemy.getWidth() <= w) {
						enemy.moveRight(x);
						enemy.orientation = "right";
					}
					else {
						enemy.movePossiblity = 1;
						enemy.moveLeft(x);
						enemy.orientation = "left";
					}
					invalidate();
					break;
				case 1:
					if (enemy.enemyX - x >= 0) {
						enemy.moveLeft(x);
						enemy.orientation = "left";
					}
					else {
						enemy.movePossiblity = 1;
						enemy.moveRight(x);
						enemy.orientation = "right";
					}
					invalidate();
					break;
				case 2:
					if (enemy.enemyY - y >= 0) {
						enemy.moveTop(y);
						enemy.orientation = "top";
					}
					else {
						enemy.movePossiblity = 1;
						enemy.moveBottom(x);
						enemy.orientation = "bottom";
					}
					invalidate();
					break;
				case 3:
					if (enemy.enemyY + y + bitmapEnemy.getHeight() <= h) {
						enemy.moveBottom(y);
						enemy.orientation = "bottom";
					}
					else {
						enemy.movePossiblity = 1;
						enemy.moveTop(x);
						enemy.orientation = "top";
					}
					invalidate();
					break;
				default:
					break;
			}
			enemy.movePossiblity--;
		}
	}


	@Override
	protected void onDraw(Canvas c) {

		h = c.getHeight();
		w = c.getWidth();
		for (Enemy enemy : enemies) {

			if (((75 > pacman.pacX - enemy.enemyX && pacman.pacX - enemy.enemyX > -75) && (75 > pacman.pacY - enemy.enemyY && pacman.pacY - enemy.enemyY > -75))) {
				finished = true;
				if (score > highScore) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getContext());
					// set title
					alertDialogBuilder.setTitle("New highscore");
					// set dialog message
					alertDialogBuilder
							.setMessage("Enter your name")
							.setView(username_input)
							.setPositiveButton("Save",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									username = username_input.getText().toString();
									if (username.equals("")) {
										username = "Not given username";
									}
									dialog.dismiss();
									username_input = new EditText(getContext());
								}
							})
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									username = "Not given username";
									dialog.dismiss();
									username_input = new EditText(getContext());
								}
							})
							.setCancelable(false)
							.show();
					highScore = score;
				}
				score = 0;
			}

				}


		if (((80 > pacman.pacX - coin.goldX && pacman.pacX - coin.goldX > -80) && (80 > pacman.pacY - coin.goldY && pacman.pacY - coin.goldY > -80))) {
			Integer level = MainActivity.currentLevel;
				score += 5 + (level * 5);

			coin = new GoldCoin(w, h);
		}
		c.drawBitmap(bitcoin, coin.goldX,coin.goldY, paint);

        //redraw pacman
		switch(orientation){
			case "right":
				c.drawBitmap(bitmapPacmanRight, pacman.pacX, pacman.pacY, paint);
				break;
			case "left":
				c.drawBitmap(bitmapPacmanLeft, pacman.pacX, pacman.pacY, paint);
				break;
			case "top":
				c.drawBitmap(bitmapPacmanUp, pacman.pacX, pacman.pacY, paint);
				break;
			case "bottom":
				c.drawBitmap(bitmapPacmanDown, pacman.pacX, pacman.pacY, paint);
				break;
			default:
				c.drawBitmap(bitmapPacmanRight, pacman.pacX, pacman.pacY, paint);
		}
		//redraw enemy
		for (Enemy enemy : enemies) {
			switch (enemy.orientation){
				default:
					c.drawBitmap(bitmapEnemy, enemy.enemyX, enemy.enemyY, paint);
			}
		}
		super.onDraw(c);
	}

}