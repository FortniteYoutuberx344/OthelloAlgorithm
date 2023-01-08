/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mrjaffesclass.othello;

import java.util.ArrayList;
import java.util.Set;

public class Algorithm extends Player{
        
    public Algorithm(int color) {
        super(color);
    }
    
      @Override
    public Position getNextMove(Board board) {
       /* Your code goes here */
       Result bestMove = miniMax(board, 8, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
       return bestMove.move;
    }
    
    /*
    Minimax evaluates every possible move to a certain depth and picks best move assuming the opponent will also pick the best move
    It does this by mapping out every possible move using recusion and then comparing the evaluations to see which is either the max or min 
    (depending on which player's turn it is)
    */
    public Result miniMax(Board board, int depth, int a, int b, boolean maximize){
        if (depth < 1){
            Result result = new Result(null, evaluateBoard(board));
            return result;
        } else {
            Result bestValue = new Result(null, 0);
            if (maximize){
                bestValue.score = Integer.MIN_VALUE;
            }else{
                bestValue.score = Integer.MAX_VALUE;
            }
            ArrayList<Position> moves = this.getMoves(board);
            if (moves.size() == 0){ 
                // if there are no more moves to make when scanning the board, we just return the evaluation of the board because it is the only move to make
                Result result = new Result(null, evaluateBoard(board));
                return result;  
            }
            for (Position move : moves){
                NewBoard fakeBoard = new NewBoard(board);
                fakeBoard.makeMove(this, move);
                Result value = miniMax(fakeBoard, depth-1, a, b, !maximize); // recursion
                /*
                Value will not be assigned to anything until the lowest level is reached, where minimax returns the evaluation of the board because there are no more moves to find
                Since miniMax is assigned to "value", "value" from the level above will take in the evaluation of the board and compare them with other evaluations at the same level using the for loop
                miniMax will then return the best value, which is stored "value" from the level above and the comparison happens again
                This is repeated until the top level is reached, causing miniMax to return the best value
                */
                if (maximize){
                    if (value.score > bestValue.score){
                        bestValue.score = value.score;
                        bestValue.move = move;
                    }
                    if (value.score > a){
                        a = value.score;
                    }
                }else{
                    if (value.score < bestValue.score){
                        bestValue.score = value.score;
                        bestValue.move = move;
                    }
                    if (value.score < b){
                        b = value.score;
                    }
                }
                if (b <= a){ 
                    break;
                }
                /*
                This is the alpha beta cutoff.  It breaks out of the loop when the current value is worse than the best value a player is guaranteed.
                a represents the highest value the maximizing player is guaranteed that the minimizing player will allow
                b represents the lowest value the minimizing player is guaranteed that the maximizing player will allow
                if a >= b, we do not need to check the rest of the values for that tree since the minimizing player is already guaranteed a value more ideal than the one the maximizing player is about to pick
                to do this, we break out of the for loop, which eliminates the checking of the remaining moves, saving time in the recursion
                */
            }
            return bestValue; // This is stored in the value variable above when a minimax call finishes, leading to minimax "stepping up through the tree"
        }
    }
    
    public int evaluateBoard(Board board) {
        int black = board.countSquares(Constants.BLACK);
        int white = board.countSquares(Constants.WHITE);
        if (this.getColor() == Constants.BLACK){
            return black - white;
        } else {
            return white - black;
        }
    }
    
    public ArrayList<Position> getMoves(Board board){
        ArrayList<Position> moves = new ArrayList<>();
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) {
                Position position = new Position(row, col);
                if (!(board.noMovesAvailable(this)) && board.isLegalMove(this, position))
                {
                    moves.add(position);
                }
            }
        }
        return moves;
    }
}

class NewBoard extends Board{ 
    /*
    This class is made to copy the board by using inheriting the Board class to get access to the protected method setSquare()
    Trying to assign the board to new variable and acting on the new variable will still result in changes in the original board
    This is because board is an object and objects are assigned using reference rather than value
    */
    public NewBoard(Board board){
        for (int row = 0; row < Constants.SIZE; row++) {
            for (int col = 0; col < Constants.SIZE; col++) { // iterates through every position in the original board
                Position pos = new Position(row, col); // gets the position at the current row and column
                Square square = board.getSquare(pos); // gets the status of the square from the original board at the position
                int status = square.getStatus(); // stores the status of the square
                Player currentPlayer = null; 
                /*
                Since the setSquare method takes in a player object to use the player's color to set the square,
                we need to create a Player object to pass into the function
                */
                if (status == Constants.WHITE){ 
                    currentPlayer = new Player(Constants.WHITE);
                    this.setSquare(currentPlayer, pos); // copies the square's status into the new board

                } else if (status == Constants.BLACK){
                    currentPlayer = new Player(Constants.BLACK);
                    this.setSquare(currentPlayer, pos);
                }
                /*
                We do not need to account for blank squares because creating a NewBoard object calls both the parent (Board) constructor and the child constructor
                This means that the initBoard function is already called and blank spaces have already been created
                All we need to do is change those spaces to make the current state of the board
                */
            }
        }
    }
}

class Result {
    /*
    This class was made to store both the position and the score in one object as a way of returning both in minimax
    This way, it is easy to get the best move
    */
    public Position move = null;
    public int score = 0;
    
    public Result(Position pos, int score){
        this.move = pos;
        this.score = score;
    }
}