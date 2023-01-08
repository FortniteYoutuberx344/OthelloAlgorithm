/*
 * Othello class project competition
 * Copyright 2017 Roger Jaffe
 * All rights reserved
 */
package com.mrjaffesclass.othello;

/**
 * Othello class project competition
 * @author Roger Jaffe
 * @version 1.0
 */
public class Othello {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
/*
    Nguyen player = new Nguyen(Constants.BLACK);
    Board board = new Board();
    player.getNextMove(board);
*/

    Controller c = new Controller( 
        new Algorithm(Constants.BLACK), 
        new Algorithm(Constants.WHITE)
        );
    c.displayMatchup();
    int result = c.run();
    System.exit(0);
  }
  
}
