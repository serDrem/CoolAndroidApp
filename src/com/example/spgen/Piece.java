package com.example.spgen;

import java.util.ArrayList;

public class Piece {
	public final String name;
	//piece is at zero y, zero x
	public ArrayList<int[]> moves;
	public final boolean special;
	public Piece(String name, ArrayList<int[]> moves, boolean special){
		this.name = name;
		this.moves = moves;
		this.special = special;
	}
}
