package ru.devobjet;

import java.util.Random;

public class Player {

	String name;
	int score;
	Random rnd;
	
	private final static int MAX_WOUNDS = 3;
	
	public Player(String name) {
		this.name = name;
		rnd = new Random();
	}
	
	public int addScore(int points) {
		score += points;
		return score;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}
}
