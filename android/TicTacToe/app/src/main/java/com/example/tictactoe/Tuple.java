package com.example.tictactoe;

public class Tuple {

    private int[] tuple;

    public Tuple(int value1, int value2){
        tuple = new int[] {value1, value2};
    }

    public int get_x(){
        return tuple[0];
    }

    public int get_y(){
        return tuple[1];
    }
}
