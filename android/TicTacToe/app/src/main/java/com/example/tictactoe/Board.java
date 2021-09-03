package com.example.tictactoe;

public class Board {

    private String[][] board;
    private boolean is_x_turn;

    public Board(){
        board = new String[3][3];
        is_x_turn = true;
    }

    public String[][] get_board(){
        return board;
    }

    public boolean get_is_x_turn(){
        return is_x_turn;
    }

    public boolean is_empty(Tuple coord){
        if (coord.get_x() > 2 || coord.get_y() > 2){
            return false;
        }
        return board[coord.get_x()][coord.get_y()] == null;
    }

    public void make_move(Tuple coord){
        if (!is_empty(coord)){
            return;
        }

        if (is_x_turn) {
            board[coord.get_x()][coord.get_y()] = "X";
            is_x_turn = false;
        }

        else{
            board[coord.get_x()][coord.get_y()] = "O";
            is_x_turn = true;
        }
    }

    public boolean full_board(){
        int count = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (board[i][j] == null){
                    count++;
                }
            }
        }
        return count == 0;
    }

    public boolean game_won(){
        return check_horizontals() || check_verticals() || check_diagonals();
    }

    public boolean game_tie(){
        return (!game_won() && full_board());
    }

    private boolean check_horizontals(){
        for (int i = 0; i < 3; i++){
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != null){
                return true;
            }
        }
        return false;
    }

    private boolean check_verticals(){
        for (int i = 0; i < 3; i++){
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != null){
                return true;
            }
        }
        return false;
    }

    private boolean check_diagonals(){
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != null) || (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != null)){
            return true;
        }
        return false;
    }


}
