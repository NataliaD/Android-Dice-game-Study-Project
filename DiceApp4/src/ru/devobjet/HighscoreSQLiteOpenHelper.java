package ru.devobjet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoreSQLiteOpenHelper extends SQLiteOpenHelper {
	
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "DiceGameDB";
	
	private SimpleDateFormat dateFormat;

	public HighscoreSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String CREATE_TABLES = "CREATE TABLE highscore ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"player  TEXT, " +
				"score   INTEGER,  " + 
				"cr_date TEXT );";

		// create books table
		db.execSQL(CREATE_TABLES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older books table if existed
		db.execSQL("DROP TABLE IF EXISTS highscore");

		// create fresh books table
		this.onCreate(db);
	}

	public void writeHighscoreData(Score scr){
		// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("player", scr.getName()); 
        values.put("score", scr.getScore());
        values.put("cr_date", (scr.getCreation_date().equals("") ? dateFormat.format(new Date()) : scr.getCreation_date()) );
 
        // 3. insert
        db.insert("highscore", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close(); 
	}

	// Get All Books
    public ArrayList<Score> getHighscoreList() {
        ArrayList<Score> scoreList = new ArrayList<Score>();
 
        // 1. build the query: Highest scores first, then earliest date first
        String query = "SELECT * FROM highscore ORDER BY score DESC, cr_date ASC";
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build score object and add it to list
        Score sc = null;
        if (cursor.moveToFirst()) {
            do {
                sc = new Score(cursor.getInt(2), cursor.getString(1), cursor.getString(3)); 
                // Add book to books
                scoreList.add(sc);
            } while (cursor.moveToNext() && scoreList.size() < 20);
        }
 
        return scoreList;
    }
 
}
