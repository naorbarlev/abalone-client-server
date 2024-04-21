/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Naor Bar Lev
 */
public class Game
{
    private State currentState;
    private char playerTurn;
    private ClientData player1;
    private ClientData player2;

    
    public Game(ClientData player1, ClientData player2) 
    {
        this.currentState = new State();
        this.playerTurn = 'w';
        this.player1 = player1;
        this.player2 = player2;
    }

    public State getCurrentState() 
    {
        return currentState;
    }

    public void setCurrentState(State currentState) 
    {
        this.currentState = new State(currentState);
        this.currentState.printModelBoard();
    }

    public char getPlayerTurn()
    {
        return playerTurn;
    }

    public void setPlayerTurn(char playerTurn) 
    {
        this.playerTurn = playerTurn;
    }

    public ClientData getPlayer1() 
    {
        return player1;
    }

    public void setPlayer1(ClientData player1)
    {
        this.player1 = player1;
    }

    public ClientData getPlayer2()
    {
        return player2;
    }

    public void setPlayer2(ClientData player2)
    {
        this.player2 = player2;
    }
    
}
