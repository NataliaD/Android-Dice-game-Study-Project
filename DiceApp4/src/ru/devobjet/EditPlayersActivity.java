package ru.devobjet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.devprojet.diceapp4.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class EditPlayersActivity extends Activity {
	
	ArrayList<String> listOfPlayers;
	EditText textPlayer;
	AlertDialog.Builder dialogBuilder;
	ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_players);
		
		listview = (ListView) findViewById(R.id.listViewPlayers);
		textPlayer = (EditText) findViewById(R.id.editPlayerName);
		listOfPlayers = new ArrayList<String>();
		
		ArrayAdapter<String> aa=new ArrayAdapter<String>(getApplicationContext(), R.layout.textview_player_list, listOfPlayers);
		listview.setAdapter(aa);
			    
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	      @Override
	      public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	    	  String item = (String) parent.getItemAtPosition(position);
	    	  listOfPlayers.remove(item);
	    	  ((BaseAdapter)listview.getAdapter()).notifyDataSetChanged();
	      }

	    });

	    // Instantiate an AlertDialog.Builder with its constructor
	    dialogBuilder = new AlertDialog.Builder(this);
	    dialogBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	           }
	       });

	}
	
	public void addPlayer(View v) {
		listOfPlayers.add(textPlayer.getText().toString());
		textPlayer.getText().clear();
		((BaseAdapter)listview.getAdapter()).notifyDataSetChanged();
	}
	
	public void startNewGame(View v) {
		if (listOfPlayers.size() < 2) {
		    // Show an error dialog: At least 2 players have to be defined
			dialogBuilder.setTitle(R.string.msgAtLeastTwoTitle);
			dialogBuilder.setMessage(R.string.msgAtLeastTwoText);
		    AlertDialog dialog = dialogBuilder.create();
		    dialog.show();
		} else {
			String[] players = listOfPlayers.toArray(new String[listOfPlayers.size()]);
			Intent intent = new Intent(EditPlayersActivity.this, MainGameActivity.class);
			intent.putExtra("ru.devobjet.EditPlayersActivity.PlayerArray", players);
			startActivity(intent);
			finish();
		}
		
	}
}
