package com.example.spgen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
	public final static String EXTRA_MESSAGE1 = "com.example.myfirstapp.MESSAGE";
	public final static String EXTRA_MESSAGE2 = "com.example.myfirstapp.MESSAGE2";
	public final static String EXTRA_MESSAGE3 = "com.example.myfirstapp.MESSAGE3";
	public final static String EXTRA_MESSAGE4 = "com.example.myfirstapp.MESSAGE4";
	public final static String EXTRA_MESSAGE5 = "com.example.myfirstapp.MESSAGE5";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_main);
        
    	Spinner board_spinner = (Spinner) findViewById(R.id.board_spinner);
    	Spinner board_spinner2 = (Spinner) findViewById(R.id.board_spinner2);
    	Spinner peaces_spinner = (Spinner) findViewById(R.id.peaces_spinner);
    	// Create an ArrayAdapter using the string array and a default spinner layout
    	ArrayAdapter<CharSequence>  board_spinner_adapter = ArrayAdapter.createFromResource(this,
    	        R.array.board_array, android.R.layout.simple_spinner_item);
    	ArrayAdapter<CharSequence>  board_spinner_adapter2 = ArrayAdapter.createFromResource(this,
    	        R.array.board_array2, android.R.layout.simple_spinner_item);
    	ArrayAdapter<CharSequence> peaces_spinner_adapter = ArrayAdapter.createFromResource(this,
    	        R.array.peaces_array, android.R.layout.simple_spinner_item);    	
    	// Specify the layout to use when the list of choices appears
    	board_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	board_spinner_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	peaces_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	// Apply the adapter to the spinner
    	board_spinner.setAdapter(board_spinner_adapter);  
    	board_spinner2.setAdapter(board_spinner_adapter2);  
    	peaces_spinner.setAdapter(peaces_spinner_adapter);      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
    	Intent intent = new Intent(this, DisplayBoardActivity.class); 
    	Spinner  boardText = (Spinner) findViewById(R.id.board_spinner);
    	Spinner  boardText2 = (Spinner) findViewById(R.id.board_spinner2);
    	Spinner  peaceText = (Spinner) findViewById(R.id.peaces_spinner);
    	EditText editText = (EditText)  findViewById(R.id.obstacles);
    	EditText startStop = (EditText)  findViewById(R.id.startStop);
    	String message1 = boardText.getSelectedItem().toString();
    	String message4 = boardText2.getSelectedItem().toString();
    	String message2 = peaceText.getSelectedItem().toString();
    	String message3 = editText.getText().toString();
    	String message5 = startStop.getText().toString();
    	if (message5.equals("")){
    		//message5 = "7,0 2,7";
    		message5 = "2,0 2,4";
    	}
    	intent.putExtra(EXTRA_MESSAGE1, message1);
    	intent.putExtra(EXTRA_MESSAGE2, message2);
    	intent.putExtra(EXTRA_MESSAGE3, message3);
    	intent.putExtra(EXTRA_MESSAGE4, message4);
    	intent.putExtra(EXTRA_MESSAGE5, message5);
    	startActivity(intent);
    }
}

