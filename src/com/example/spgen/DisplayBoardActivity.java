package com.example.spgen;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayBoardActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        // Get the message from the intent
        Intent intent = getIntent();
        String dimension = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
        String piece = intent.getStringExtra(MainActivity.EXTRA_MESSAGE2);
        String obstaclesStr = intent.getStringExtra(MainActivity.EXTRA_MESSAGE3);
        String boardType = intent.getStringExtra(MainActivity.EXTRA_MESSAGE4);
        String startStop = intent.getStringExtra(MainActivity.EXTRA_MESSAGE5);
        
        Pattern p = Pattern.compile("(\\d+)x(\\d+)");
        Matcher m = p.matcher(dimension);
        String width = "";
        while (m.find()) { // Find each match in turn; String can't do this.
        	width = m.group(1);
        	//String hight = m.group(2);
        }
        if(boardType.equals("special")){ 
        	obstaclesStr = "5,4 5,5 6,3 6,4 6,2";
        	startStop = "2,2 6,5";
        }
        List<List<Integer>> obstacles = new ArrayList<List<Integer>>();
        if(!boardType.equals("plain")){
            Pattern p2 = Pattern.compile("(\\d+),(\\d+)");
            Matcher m2 = p2.matcher(obstaclesStr);
            String y, x; 
            while (m2.find()) { // Find each match in turn; String can't do this.
            	y = m2.group(1);
            	x = m2.group(2);
            	List<Integer> pair = new ArrayList<Integer>();
            	pair.add( Integer.parseInt(y));
            	pair.add( Integer.parseInt(x));
            	obstacles.add(pair);
            } 
            if(boardType.equals("random")){
	            Random generator = new Random();
	            if(boardType.equals("random")){
	            	for(int y1 = 0; y1 < Integer.parseInt(width) - 1 ; y1++){
	            		for(int x1 = 0; x1 < Integer.parseInt(width) - 1 ; x1++){
	            			if(generator.nextInt(8) == 0){
	                        	List<Integer> pair = new ArrayList<Integer>();
	                        	pair.add(y1);
	                        	pair.add(x1);
	                        	obstacles.add(pair);
	            			}
	            			if(generator.nextInt(8) == 1){
	                        	List<Integer> pair = new ArrayList<Integer>();
	                        	pair.add(y1);
	                        	pair.add(x1);
	                        	obstacles.add(pair);
	                        	List<Integer> pair1 = new ArrayList<Integer>();
	                        	pair1.add(y1+1);
	                        	pair1.add(x1);
	                        	obstacles.add(pair);
	                        	List<Integer> pair2 = new ArrayList<Integer>();
	                        	pair2.add(y1);
	                        	pair2.add(x1+1);
	                        	obstacles.add(pair);
	            			}
	            		}
	            	}
	            }
            }
        }
        
        Pattern p3 = Pattern.compile("(\\d+),(\\d+)");
        Matcher m3 = p3.matcher(startStop);
        String y, x;
        int counter = 0;
        int start[] = new int[2];
        int stop[] = new int[2];
        
        while (m3.find()) { // Find each match in turn; String can't do this.
        	y = m3.group(1);
        	x = m3.group(2);
        	if (counter == 0){
        		start[0] = Integer.parseInt(y);
        		start[1] = Integer.parseInt(x);
        	}else{
            	stop[0] = Integer.parseInt(y);
            	stop[1] = Integer.parseInt(x);
        	}
        	counter++;
        } 

        
        /*
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(dimension);

        // Set the text view as the activity layout
        setContentView(textView);
        Button submit = new Button(this);
        submit.setText("Submit");
        */
	
        BoardView mBoardView;
        
    	mBoardView = new BoardView(this, Integer.parseInt(width), obstacles, piece, start, stop);
    	setContentView(mBoardView);
    	//mBoardView.move();
    	//mBoardView.move();
    }
}
