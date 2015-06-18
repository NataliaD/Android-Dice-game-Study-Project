package ru.devobjet;

import java.util.Random;
import ru.devprojet.diceapp4.R;

public class Dice {
	
	static int dice_id = 0; // just an internal ID
	private static final int num_face = 6;
	String[] face = new String[num_face]; // a String represents the type of a dices face: "p" = "pate", "c" = "casserol" and "f" = "fourchette"
	int[] resId = new int[num_face]; // an integer represents the ID of the graphics resource to draw for each face. 
	String color = "";
	int shownFace;
	Random rnd;
	
	public Dice(String color) {
		dice_id ++;
		rnd = new Random();
		this.color = color;
		if(color.contentEquals("g")){
			face[0] = "p";
			resId[0] = R.drawable.dice_p_g;
			face[1] = "p";
			resId[1] = R.drawable.dice_p_g;
			face[2] = "p";
			resId[2] = R.drawable.dice_p_g;
			face[3] = "c";
			resId[3] = R.drawable.dice_c_g;
			face[4] = "c";
			resId[4] = R.drawable.dice_c_g;
			face[5] = "f";
			resId[5] = R.drawable.dice_f_g;
		}else if(color.contentEquals("y")){
			face[0] = "p";
			resId[0] = R.drawable.dice_p_y;
			face[1] = "p";
			resId[1] = R.drawable.dice_p_y;
			face[2] = "f";
			resId[2] = R.drawable.dice_f_y;
			face[3] = "f";
			resId[3] = R.drawable.dice_f_y;
			face[4] = "c";
			resId[4] = R.drawable.dice_c_y;
			face[5] = "c";
			resId[5] = R.drawable.dice_c_y;
		}else if(color.contentEquals("r")){
			face[0] = "p";
			resId[0] = R.drawable.dice_p_r;
			face[1] = "f";
			resId[1] = R.drawable.dice_f_r;
			face[2] = "f";
			resId[2] = R.drawable.dice_f_r;
			face[3] = "f";
			resId[3] = R.drawable.dice_f_r;
			face[4] = "c";
			resId[4] = R.drawable.dice_c_r;
			face[5] = "c";
			resId[5] = R.drawable.dice_c_r;
		}else{
			System.out.println("This color doesn't exist!");
		}
	}
	
	public int getImgRessourceID(){
		return resId[shownFace];
	}
	
	public int getNum_face() {
		return num_face;
	}

	public String getColor() {
		return color;
	}

	public String getFace(){
		return face[shownFace];
	}

	public int roll(){
		shownFace = rnd.nextInt(num_face);
		return shownFace;
	}
}
