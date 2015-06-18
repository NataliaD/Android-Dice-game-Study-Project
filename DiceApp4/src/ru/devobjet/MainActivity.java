package ru.devobjet;

import ru.devprojet.diceapp4.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
	public void onCreate(Bundle bn){
		super.onCreate(bn);
		// Set layout to dispolay
		setContentView(R.layout.activity_start_screen);
        
	}
	
    public void startPlayerActivity(View v) {
    	Intent intent = new Intent(MainActivity.this, EditPlayersActivity.class);
		startActivity(intent);
    }
    
    public void showHighscores(View v) {
    	Intent intent = new Intent(MainActivity.this, HighscoresActivity.class);
		startActivity(intent);
    }
    
    public void doExit(View v) {
    	finish();
    }
}
