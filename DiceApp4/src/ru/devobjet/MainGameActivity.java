package ru.devobjet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import ru.devprojet.diceapp4.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGameActivity extends Activity {
	
	//////////////////////////
	// UI Component references
	//////////////////////////
	TextView labelCurrentPlayer;
	TextView labelCurrentScore;
	TextView labelCurrentWounds;
	Button btnPass;
	Button btnRollDice;
	ImageView[] imgDice; // references to UI controls to display the dices in
	final Drawable emptyDice = new PaintDrawable(); // a dummy graphics object to display on a dice place if there is no dice
	AlertDialog.Builder dialogBuilder;
	SoundPoolPlayer soundPlayer;
	
	///////////////////
	// Logic components
	///////////////////
	ArrayList<Player> listOfPlayers;
	int curPlayerIndex;
	int curPlayerPointsAdd;
	int curPlayerDamage;
	Player curPlayer;
	
	LinkedList<Dice> dices; // the pool of all the dices to draw from
	LinkedList<Dice> playersDices; // the dices the current player holds
	boolean gameFinished = false;
	Random rnd = new Random();
	
	private final static int MAX_DAMAGE = 3;
	private final static int MAX_SCORE  = 13;
	private final static int NUM_GREEN_DICES = 6;
	private final static int NUM_YELLOW_DICES = 4;
	private final static int NUM_RED_DICES = 3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load the list of players from the previous activity (which can be EditPlayerActivity or
		// GameResultActivity, if you restart with the same players again)
		String[] playerNames;
		listOfPlayers = new ArrayList<Player>();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerNames = extras.getStringArray("ru.devobjet.EditPlayersActivity.PlayerArray");
			for (int i = 0; i < playerNames.length; i++) {
				listOfPlayers.add(new Player(playerNames[i]));
			}
		}
		
		// Init UI
		setContentView(R.layout.activity_main_game);
		
	    // Instantiate an AlertDialog.Builder with its constructor. This is called whenever some
		// event occured like a player passed, a player is defeated or a player has reached 13 points.
		// The OK Button behaves different for those cases. In case of displaying the victory message,
		// it finishes the main game screen, in other cases, ist just switches to the next player.
		// This is done because the dialog does not stop execution of the code after .show(), so the
		// pressing of "OK" should do what happens after the dialog finished.
	    dialogBuilder = new AlertDialog.Builder(this);
	    dialogBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		// User clicked OK button
	    		if (gameFinished) {

	    			// gameFinished has been set to true, as the current player has reached 13 Points.
	    			// Switch activity to the game over screen now and pass player names and scores to it:
	    			Intent intent = new Intent(MainGameActivity.this, GameResultActivity.class);
	    			String[] playerNames  = new String[listOfPlayers.size()];
	    			int[]    playerScores = new int[listOfPlayers.size()];
	    			int idx = 0;
	    			for (Player p : listOfPlayers) {
	    				playerNames[idx]  = p.getName();
	    				playerScores[idx] = p.getScore();
	    				idx++;
	    			}
	    			intent.putExtra("ru.devobjet.MainGameActivity.PlayerNames",  playerNames);
	    			intent.putExtra("ru.devobjet.MainGameActivity.PlayerScores", playerScores);
	    			startActivity(intent);
	    			// finish the main game activity here.
	    			finish();
	    			
	    		} else {
	    			
	    			// "normal" case: a Player has passed or was defeated, the status message was displayed.
	    			// After the, it is the next Player's turn.
	    			nextPlayer();
	    			btnPass.setClickable(true);
	    			btnRollDice.setClickable(true);
	    		}
	    	}
	    });

	    // assign GUI elements to java objects
		labelCurrentPlayer = (TextView) findViewById(R.id.textViewCurPlayer);
		labelCurrentScore  = (TextView) findViewById(R.id.TextViewCurScore);
		labelCurrentWounds = (TextView) findViewById(R.id.TextViewCurWounds);
		btnPass            = (Button)   findViewById(R.id.btnPass);
		btnRollDice        = (Button)   findViewById(R.id.btnRollDice);
		
		// Prepare the display fields for the dices graphics
		imgDice            = new ImageView[13];
		imgDice[0]         = (ImageView) findViewById(R.id.imageViewDie1);
		imgDice[1]         = (ImageView) findViewById(R.id.imageViewDie2);
		imgDice[2]         = (ImageView) findViewById(R.id.imageViewDie3);
		imgDice[3]         = (ImageView) findViewById(R.id.imageViewDie4);
		imgDice[4]         = (ImageView) findViewById(R.id.imageViewDie5);
		imgDice[5]         = (ImageView) findViewById(R.id.imageViewDie6);
		imgDice[6]         = (ImageView) findViewById(R.id.imageViewDie7);
		imgDice[7]         = (ImageView) findViewById(R.id.imageViewDie8);
		imgDice[8]         = (ImageView) findViewById(R.id.imageViewDie9);
		imgDice[9]         = (ImageView) findViewById(R.id.imageViewDie10);
		imgDice[10]        = (ImageView) findViewById(R.id.imageViewDie11);
		imgDice[11]        = (ImageView) findViewById(R.id.imageViewDie12);
		imgDice[12]        = (ImageView) findViewById(R.id.imageViewDie13);
		
		// Init sounds
		soundPlayer = new SoundPoolPlayer(this); 
		
		// Display first player by starting fro -1 and going to next player
		curPlayerIndex = -1;
		nextPlayer();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		soundPlayer.release();		
	}

	public void nextPlayer() {
		
		// FIRST: Check if the current player has won after his current turn, by checking how many points he has now
		if (curPlayerIndex >= 0 && listOfPlayers.get(curPlayerIndex).getScore() >= MAX_SCORE) {
			dialogBuilder.setTitle(getString(R.string.msgPlayerVictoryTitle));
			dialogBuilder.setMessage(String.format(getString(R.string.msgPlayerVictoryText), 
					listOfPlayers.get(curPlayerIndex).getName(), 
					listOfPlayers.get(curPlayerIndex).getScore()));
			gameFinished = true; // this means Dialog will finish the main game activity and display the game ovr screen
			soundPlayer.playShortResource(R.raw.cheer);
			dialogBuilder.create().show();
			return; // Abort the rest
		}
		
		// Otherwise ("normal" case) switch to the next player
		curPlayerIndex += 1;
		if (curPlayerIndex >= listOfPlayers.size()) {
			curPlayerIndex = 0;
		}

		// Initialize the players new turn
		curPlayerDamage = 0; // no damage taken yet for this turn
		curPlayerPointsAdd = 0; // no points earned either
		playersDices = new LinkedList<Dice>(); // clear the player's dices

		// update the display
		labelCurrentPlayer.setText(listOfPlayers.get(curPlayerIndex).getName());
		labelCurrentScore.setText("" + listOfPlayers.get(curPlayerIndex).getScore()); // Use "" + to force the parameter to be a string. Otherwise a ressource ID with the score is searched...
		labelCurrentWounds.setText("");
		
		// Clear all the dice's image fields by showing the dummy graphics object
		for (int i = 0; i < imgDice.length  ; i++) {
			imgDice[i].setImageDrawable(emptyDice);			
		}
		
		// prepare the dices pool with 6 green, 4 yellow and 3 red dices
		dices = new LinkedList<Dice>();
		for (int i = 0; i < NUM_GREEN_DICES; i++) {
			dices.add(new Dice("g"));
		}
		for (int i = 0; i < NUM_YELLOW_DICES; i++) {
			dices.add(new Dice("y"));
		}
		for (int i = 0; i < NUM_RED_DICES; i++) {
			dices.add(new Dice("r"));
		}
		
	}
	
	public void passTurn (View v) {
		
		// Player chose to pass his turn.
		// Add the currently earned points to his account.
		int newScore = listOfPlayers.get(curPlayerIndex).addScore(curPlayerPointsAdd);
		// If the score is not high enough for victory yet, just perpare the status message to display the 
		// result of the players turn and show it. The dialogs OK button handler handles the passing to the next player.
		if (newScore < MAX_SCORE) {
			dialogBuilder.setTitle(getString(R.string.msgTurnPassedTitle));
			dialogBuilder.setMessage(String.format(getString(R.string.msgTurnPassedText), 
					listOfPlayers.get(curPlayerIndex).getName(), 
					(curPlayerPointsAdd > 0 ? "" + curPlayerPointsAdd : getString(R.string.msgZeroWord))));
			soundPlayer.playShortResource(R.raw.scribble);
			dialogBuilder.create().show();
		} else {
			// if the player alread has enough points, skip the status message. Victory condition is handeled in the 
			// nextPlayer() method.
			nextPlayer();
		}
	}
	
	public void rollDice (View v) {
		
		// Add three dices from the pool if available
		// Fun version: for (int i = 0; i < 3 && dices.size() > 0; i++) {
		// Boring version:
		for (int i = playersDices.size(); i < 3 && dices.size() > 0; i++) {
			playersDices.add(dices.remove(rnd.nextInt(dices.size())));
		}

		// ROLL the dices and display the result.
		// play some nice littee sound effect, two versions
		soundPlayer.playShortResource((rnd.nextInt(2) == 0) ? R.raw.dice_throw1 : R.raw.dice_throw2);
		int numCasseroleDices = 0;
		int playerDiceCount = playersDices.size();
		// Iterate over all the dices display positions:
		for (int i = imgDice.length - 1; i >= 0 ; i--) {
			if (i < playerDiceCount) {
				// the current dice position is used by a player dice. Roll that dice now and hanlde the result.
				Dice d = playersDices.get(i);
				d.roll();
				imgDice[i].setImageResource(d.getImgRessourceID());
				if (d.getFace().equals("p")) {
					// "pate" was rolled, add a (temporary) point for that and remove the dice
					curPlayerPointsAdd++;
					playersDices.remove(d);
				} else if (d.getFace().equals("f")) {
					// "fourchette" was rolled, add one damage point and remove the dice
					curPlayerDamage++;
					playersDices.remove(d);
				} else if (d.getFace().equals("c")) {
					// "casserol" was rolled. Keep the dice for the next roll.
					// Remember the presence of a casserolle for a very particular case: If the player has already
					// used ALL dices from the 13 dice pool, and has rolled a casserol, he should be allowed to 
					// roll that dice again, although there are no more dices to draw from the pool.
					numCasseroleDices++;
				}
			} else {
				// the current dice position is vacant, display the dummy graphics object here.
				imgDice[i].setImageDrawable(emptyDice);
			}			
		}
		
		// update the display of suffered injuries 
		String woundsDisplay = "";
		for (int i = 0; i < curPlayerDamage; i++) {
			woundsDisplay += "X ";
		}
		labelCurrentWounds.setText(woundsDisplay);

		// display players secured points AND possibly added points 
		if (curPlayerPointsAdd > 0) {
			labelCurrentScore.setText("" + listOfPlayers.get(curPlayerIndex).getScore() + " + " + curPlayerPointsAdd);		
		}
		
		boolean showDialog = false;
		
		if (curPlayerDamage >= MAX_DAMAGE) {
			// Player has taken at least three injuries, his turn is over. Prepare a status display dialog.
			dialogBuilder.setTitle(getString(R.string.msgPlayerKilledTitle));
			dialogBuilder.setMessage(String.format(getString(R.string.msgPlayerKilledText),
					listOfPlayers.get(curPlayerIndex).getName(),
					curPlayerDamage,
					(curPlayerPointsAdd > 0 ? "" + curPlayerPointsAdd : getString(R.string.msgZeroWord)) ));
			soundPlayer.playShortResource(R.raw.argh);
			showDialog = true;
		} else if (dices.size() == 0 && numCasseroleDices == 0) {
			// Very special and rare case: The player has actually thrown ALL available 13 dices without dying 
			// (and has no more casserol dices to roll again). As there are no more new dices to draw for the next
			// turn, he is forced to pass his turn. Display a special status message for that case.
			dialogBuilder.setTitle(getString(R.string.msgAllDicesTitle));
			dialogBuilder.setMessage(String.format(getString(R.string.msgAllDicesText),
					listOfPlayers.get(curPlayerIndex).getName(),
					(curPlayerPointsAdd > 0 ? "" + curPlayerPointsAdd : getString(R.string.msgZeroWord)) ));
			listOfPlayers.get(curPlayerIndex).addScore(curPlayerPointsAdd);
			soundPlayer.playShortResource(R.raw.impressed);
			showDialog = true;			
		}
			
		if (showDialog) {
			// disable the action buttons and delay the game for two seconds.
			// This is done to give the player some time to realize what happened
			// before the status message is displayed.
			btnPass.setClickable(false);
			btnRollDice.setClickable(false);
			Handler handler = new Handler(); 
			// delayed display of the status dialog:
		    handler.postDelayed(new Runnable() { 
		         public void run() { 
		 			dialogBuilder.create().show(); 
		         } 
		    }, 2000); 
			
		}
	    
	}
	
 }
