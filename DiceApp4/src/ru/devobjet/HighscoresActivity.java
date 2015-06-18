package ru.devobjet;

import java.util.ArrayList;
import java.util.Collections;

import ru.devprojet.diceapp4.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HighscoresActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_highscores);
		Context appContext = getApplicationContext();
		ListView listHighscores  = (ListView) findViewById(R.id.listViewHighscores);
		
		// Read the overall highscores list
		HighscoreSQLiteOpenHelper sqlite;
		sqlite = new HighscoreSQLiteOpenHelper(appContext);
		ArrayList<Score> scoreList = sqlite.getHighscoreList();
		if (scoreList == null) {
			scoreList = new ArrayList<Score>();
		}
		if (scoreList.size() == 0) {
			scoreList.add(new Score(0, "No scores yet", ""));
		}
		
		listHighscores.setAdapter(new ScoreListAdapter(scoreList, appContext, true));
		
	}
	
	public void returnMain(View v) {
		finish();
	}

}
