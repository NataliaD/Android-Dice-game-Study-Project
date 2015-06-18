package ru.devobjet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import ru.devprojet.diceapp4.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GameResultActivity extends Activity{
	
	String[] playerNames;
	int[]    playerScores;
	int soundSample;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_result);
		ListView listScores  = (ListView) findViewById(R.id.listViewScores);
		TextView labelWinner = (TextView) findViewById(R.id.textViewGameWinner);
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			playerNames  = extras.getStringArray("ru.devobjet.MainGameActivity.PlayerNames");
			playerScores = extras.getIntArray("ru.devobjet.MainGameActivity.PlayerScores");
		}
		
		ArrayList<Score> scoreList = new ArrayList<Score>();
		for (int i = 0; i < playerNames.length; i++) {
			scoreList.add(new Score(playerScores[i], playerNames[i]));
		}
		Collections.sort(scoreList); 
		
		Context appContext = getApplicationContext();
		
		listScores.setAdapter(new ScoreListAdapter(scoreList, appContext, false));
		
		labelWinner.setText(String.format(getString(R.string.msgWinnerName), scoreList.get(0).getName()));
		
		// log the Highscore for the overall list
		HighscoreSQLiteOpenHelper sqlite;
		sqlite = new HighscoreSQLiteOpenHelper(appContext);
		sqlite.writeHighscoreData(scoreList.get(0));

		// Init sound: A fanfare should be played at display of the result screen.
		// the SoundPool.load() method loads the sound file in the background, the code
		// of this method here continues even before the sound is really loaded.
		// I add an "OnCompleteListener", which is fired once the file is loadad,
		// and play it then.
		SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		soundSample = soundPool.load(this, R.raw.fanfare, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				if (sampleId == soundSample) {
					soundPool.play(sampleId, 0.99f, 0.99f, 0, 0, 1);
				}
			}
		});
	}
	
	public void returnMain(View v) {
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void restart(View v) {
		Intent intent = new Intent(GameResultActivity.this, MainGameActivity.class);
		intent.putExtra("ru.devobjet.EditPlayersActivity.PlayerArray", playerNames);
		startActivity(intent);
		finish();
	}

}
