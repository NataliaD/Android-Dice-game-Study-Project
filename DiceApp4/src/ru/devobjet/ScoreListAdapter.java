package ru.devobjet;

import java.util.List;

import ru.devprojet.diceapp4.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScoreListAdapter extends ArrayAdapter<Score> {

	private List<Score> scoreList;
	private Context context;
	private boolean showDate;
	private int layoutID;
	public ScoreListAdapter(List<Score> scoreList, Context ctx, boolean showDate) {
		
		super(ctx, (showDate ? R.layout.textview_highscore_list : R.layout.textview_score_list), scoreList);
		layoutID = (showDate ? R.layout.textview_highscore_list : R.layout.textview_score_list);
		this.scoreList = scoreList;
		this.context = ctx;
		this.showDate = showDate;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutID, parent, false);
		}
		// Now we can fill the layout with the right values
		TextView posView   = (TextView) convertView.findViewById(R.id.scoreList_Pos);
		TextView nameView  = (TextView) convertView.findViewById(R.id.scoreList_Name);
		TextView scoreView = (TextView) convertView.findViewById(R.id.scoreList_Score);
		TextView dateView  = (showDate ? (TextView) convertView.findViewById(R.id.scoreList_Date) : null);
		Score scr = scoreList.get(position);

		posView.setText((position + 1)+ ".");
		nameView.setText(scr.getName());
		scoreView.setText("" + scr.getScore());
		if (showDate) {
			dateView.setText(scr.getCreation_date());
		}
		
		return convertView;
	}
	
}
