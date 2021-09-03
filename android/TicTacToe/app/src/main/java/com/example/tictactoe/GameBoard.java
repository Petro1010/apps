package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//import android.widget.GridLayout;
import android.widget.TextView;
import androidx.gridlayout.widget.GridLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class GameBoard extends AppCompatActivity {
    private Board board;
    private HashMap<String, Integer> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        board = new Board();
        buttons = new HashMap<String, Integer>();

        // Get all touchable views
        ArrayList<View> layoutButtons = findViewById(R.id.gridLayout).getTouchables();

        // add all buttons to hash map
        for(View v : layoutButtons){
            String position = ((Button) v).getText().toString();
            buttons.put(position, v.getId());
        }

    }

    public void make_move(View v){
        String position = ((Button) v).getText().toString();
        String[] coords = position.split(",");
        Tuple coord = new Tuple(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));  //create tuple of coordinate selected by user
        //buttons.put(position, v.getId());
        update_text(v, board.get_is_x_turn());
        board.make_move(coord);
        v.setEnabled(false);

        check_game(board.get_is_x_turn());
    }

    public void update_text(View v, boolean x_turn){
        ((Button) v).setTextColor(this.getResources().getColor(R.color.white));  //changing colour of text
        if (x_turn) ((Button) v).setText("X");
        else ((Button) v).setText("O");
    }

    public void check_game(boolean x_turn){
        if (board.game_tie()){
            findViewById(R.id.game_end).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.game_end)).setText("Tie!");    //display game tie game text
            //display play again button
            findViewById(R.id.reset_button).setVisibility(View.VISIBLE);
            disableButtons();
            return;
        }

        if (board.game_won()){
            findViewById(R.id.game_end).setVisibility(View.VISIBLE);
            if (x_turn){
                ((TextView) findViewById(R.id.game_end)).setText("O Wins!");
            }
            else{
                ((TextView) findViewById(R.id.game_end)).setText("X Wins!");
            }

            //display play again button
            findViewById(R.id.reset_button).setVisibility(View.VISIBLE);
            disableButtons();
            return;
        }

        //otherwise do nothing


    }

    public void play_again(View v){

        //could try to switch activity to the same one (resets it??)
        
        //when play again is clicked
        board = new Board();
        //make grid enabled
        enableButtons();
        //reset texts
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                String x = i + "," + j;
                Button b = (Button) findViewById(buttons.get(x));
                b.setText(x);
                b.setTextColor(this.getResources().getColor(R.color.button_colour));

            }
        }
        //hide play again and text views
        findViewById(R.id.reset_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.game_end).setVisibility(View.INVISIBLE);
    }

    private void disableButtons() {

        for(Integer v : buttons.values()){
            if( ((Button) findViewById(v)).isEnabled() ) {
                ((Button) findViewById(v)).setEnabled(false);
                System.out.println("n");
            }
        }
    }

    private void enableButtons() {

        for(Integer v : buttons.values()){
            if( !((Button) findViewById(v)).isEnabled() ) {
                ((Button) findViewById(v)).setEnabled(true);
                System.out.println("e");
            }
        }
    }
}