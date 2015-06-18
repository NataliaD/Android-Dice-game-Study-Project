package ru.devobjet;

import java.util.Date;

class Score implements Comparable<Score> {
	
    int score;
    String name;
    String creation_date;
    
    public Score(int score, String name) {
        this(score, name, "");
    }

    public Score(int score, String name, String date) {
        this.score = score;
        this.name = name;
        this.creation_date = date;
    }

    public String getCreation_date() {
		return creation_date;
	}

	@Override
    public int compareTo(Score o) {
        return score < o.score ? 1 : score > o.score ? -1 : 0;
    }

	public int getScore() {
		return score;
	}

	public String getName() {
		return name;
	}
    
    
}
