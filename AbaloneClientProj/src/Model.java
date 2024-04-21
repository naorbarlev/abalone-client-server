

import java.util.ArrayList;


/**
 * Document : Model Created on : 07/02/2018, 11:53:28 Author : g43
 * מייצגת את הלוח הלוגי שמאחורי לוח הכפתורים
 */
public class Model {

    // תכונות
    private char currentPlayer;
    private State currentState;

    public Model() 
    {
        init();
    }
/**
 * איתחול מודל המשחק
 */
    public void init() {
        currentState = new State();
    }

    public State getCurrentState() {
        return currentState;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(char player)
    {
        currentPlayer = player;
    }
    /**
     * מקבלת מצב של לוח ומעדכנת את הלוח הנוכחי ובנוסף את הכדורים שיצאו החוצה
     * @param s 
     */
    public void updateState(State s)
    {
        currentState.setBoard(s.getModelBoard());
        currentState.setBlackPopedOut(s.getBlackPop());
        currentState.setWhitePopedOut(s.getWhitePop());
    }

     public void setState(State s)
    {
        currentState = s;
    }

    /**
     * מבצעת את ההזזה אם ניתן מחזירה אמת אם התבצע ושקר אם לא
     * @param loc
     * @param dest
     * @return
     */
    public boolean playerMove(ArrayList<Location> loc, int row, int col) {
        int dir = currentState.getMoveDirection(loc, row, col, currentPlayer);
        Location dest = new Location(row, col);

        switch (dir)
        {
            case 1:  //DLR
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateCross(loc, dest, currentPlayer);
                    return true;
                } else if (currentState.getValueAt(row, col) == currentState.getOpsitePlayer(currentPlayer))
                {
                    if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 1)
                    {
                        currentState.pushOpponetModelUpdate(loc, dest, currentPlayer);
                        return true;
                    } else if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 2)
                    {
                        currentState.popOutOpponetUpdate(loc, dest, currentPlayer);
                        return true;
                    }
                }
                break;

            case 2:  //DRL
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateCross(loc, dest, currentPlayer);
                    return true;
                } else if (currentState.getValueAt(row, col) == currentState.getOpsitePlayer(currentPlayer))
                {
                    if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 1)
                    {
                        currentState.pushOpponetModelUpdate(loc, dest, currentPlayer);
                        return true;
                    } else if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 2)
                    {
                        currentState.popOutOpponetUpdate(loc, dest, currentPlayer);
                        return true;
                    }
                }
                break;

            case 3:  //URL
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateCross(loc, dest, currentPlayer);
                    return true;
                } else if (currentState.getValueAt(row, col) == currentState.getOpsitePlayer(currentPlayer))
                {
                    if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 1)
                    {
                        currentState.pushOpponetModelUpdate(loc, dest, currentPlayer);
                        return true;
                    } else if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 2)
                    {
                        currentState.popOutOpponetUpdate(loc, dest, currentPlayer);
                        return true;
                    }
                }
                break;

            case 4:  //ULR
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateCross(loc, dest, currentPlayer);
                    return true;
                } else if (currentState.getValueAt(row, col) == currentState.getOpsitePlayer(currentPlayer))
                {
                    if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 1)
                    {
                        currentState.pushOpponetModelUpdate(loc, dest, currentPlayer);
                        return true;
                    } else if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 2)
                    {
                        currentState.popOutOpponetUpdate(loc, dest, currentPlayer);
                        return true;
                    }
                }
                break;

            case 5:  //ROW
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateRow(loc, dest, currentPlayer);
                    return true;
                } else if (currentState.getValueAt(row, col) == currentState.getOpsitePlayer(currentPlayer))
                {
                    if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 1)
                    {
                        currentState.pushOpponetModelUpdate(loc, dest, currentPlayer);
                        return true;
                    } else if (currentState.isPossibleToPushOpponet(loc, dest, currentPlayer) == 2)
                    {
                        currentState.popOutOpponetUpdate(loc, dest, currentPlayer);
                        return true;
                    }
                }
                break;

            case 6:  //CROSS ROW
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateCross(loc, dest, currentPlayer);
                    return true;
                }

                break;

            case 7:  //ROW CROSS
                if (currentState.getValueAt(row, col) == 'e' && !loc.isEmpty())
                {
                    currentState.updateRow(loc, dest, currentPlayer);
                    return true;
                }

                break;
        }

        return false;
    }
/**
 * הפונציה מקבלת שחקן ומחיזה את השחקן השני
 * @param player
 * @return 
 */
    public char getOtherPlayer(char player) {
        if (player == 'b')
        {
            return 'w';
        }

        return 'b';
    }
    public void printModelBoard() {
        for (int i = 0; i < Client.BOARD_ROW; i++)
        {
            for (int j = 0; j < Client.BOARD_COL; j++)
            {
                System.out.print(currentState.getModelBoard()[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public String toString() 
    {
        if(currentPlayer == 'w')
          return "White";
        else
            return "Black";
    }
}
