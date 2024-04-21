/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Document : Location
 * @author : Naor Bar-Lev
 */
public class State implements Serializable{

    private char[][] board;     //מטריצת תווים המייצגת את לוח המשחק הלוגי
    private int opositePlayerPower, curentPlayerPower;      //מספר הכדורים שיש לשחקן הנגדי כשהשחקן הנוכחיח רוצה לדחוף אותו
    private int blackPopOut = 0, whitePopOut = 0;   //כמות הכדורים שיצאו מהמשחק לכל שחקן
    
    //קבועים
    public static final int BOARD_ROW = 9;
    public static final int BOARD_COL = 17;
    static int NUM_TO_POP = 1; //מספר הכדורים שנדרש להוציא כדי לנצח
    
    /**
     * פעולה בונה מעתיקה
     * @param s 
     */
    public State(State s)
    {
        board = new char[BOARD_ROW][BOARD_COL];
        for (int row = 0; row < BOARD_ROW; row++) {
            for (int col = 0; col < BOARD_COL; col++) {
                board[row][col] = s.board[row][col];
            }
        }
        whitePopOut = s.whitePopOut;
        blackPopOut = s.blackPopOut;
    }

    /**
     * אתחול לוח המשחק
     */
    public State() {
        board = new char[BOARD_ROW][BOARD_COL];
        // איתחול לוח המשחק
        //שורה ראשונה ואחרונה
        for (int i = 4; i < 13; i++)
        {
            if (i % 2 == 0)
            {
                board[0][i] = 'w';
                board[8][i] = 'b';
            } else
            {

                board[0][i] = ' ';
                board[8][i] = ' ';

            }
        }
        //שורה שנייה ושישית
        for (int i = 3; i < 14; i++)
        {
            if (i % 2 == 0)
            {
                board[1][i] = ' ';
                board[7][i] = ' ';

                continue;
            }
            board[1][i] = 'w';
            board[7][i] = 'b';

        }

        //שורה שלישית וחמישית
        for (int i = 2; i < 15; i++)
        {
            if (i % 2 == 0)
            {
                board[2][i] = 'e';
                board[6][i] = 'e';

                if (i == 6 || i == 8 || i == 10)
                {
                    board[2][i] = 'w';
                    board[6][i] = 'b';
                }
            } else
            {

                board[2][i] = ' ';
                board[6][i] = ' ';

            }
        }
        //השורה הרביעית והשישית
        for (int i = 1; i < 16; i++)
        {
            if (i % 2 == 0)
            {;
                board[3][i] = ' ';
                board[5][i] = ' ';

                continue;
            }

            board[3][i] = 'e';
            board[5][i] = 'e';

        }

        //השורה האמצעית
        for (int i = 0; i < BOARD_COL; i++)
        {
            if (i % 2 == 0)
            {
                board[4][i] = 'e';
            } else
            {
                board[4][i] = ' ';

            }

        }

        for (int i = 0; i < BOARD_COL; i++)
        {
            if (i > 3 && i < 13)
            {
                continue;
            } else
            {
                board[0][i] = ' ';
                board[8][i] = ' ';

            }

        }

        for (int i = 0; i < BOARD_COL; i++)
        {
            if (i > 2 && i < 14)
            {
                continue;
            } else
            {
                board[1][i] = ' ';
                board[7][i] = ' ';

            }

        }

        for (int i = 0; i < BOARD_COL; i++)
        {
            if (i > 1 && i < 15)
            {
                continue;
            } else
            {
                board[2][i] = ' ';
                board[6][i] = ' ';

            }

        }

        for (int i = 0; i < BOARD_COL; i++)
        {
            if (i > 0 && i < 16)
            {
                continue;
            } else
            {
                board[3][i] = ' ';
                board[5][i] = ' ';

            }

        }

        for (int i = 0; i < BOARD_COL; i++)
        {
            if (i % 2 != 0)
            {
                board[4][i] = ' ';

            }

        }

    }
    /**
     * מקבלת לוח ומציבה אותו
     * @param board 
     */
    public void setBoard(char[][] board)
    {
        this.board = board;
    }
    
    /**
     * מקבלת מצב ומעדכנת את כל התכונות
     * @param s 
     */
    public void updateState(State s)
    {
        this.board = s.board;
        this.blackPopOut = s.blackPopOut;
        this.whitePopOut = s.whitePopOut;
    }

    /**
     * מעדכנת את כמות הכדורים שנהדפו החוצה לשחקן השחור
     * @param num 
     */
    public void setBlackPopedOut(int num) {
        this.blackPopOut = num;
    }
    
    /**
     * מעדכנת את כמות הכדורים שנהדפו החוצה לשחקן הלבן
     * @param num 
     */
    public void setWhitePopedOut(int num) {
        this.whitePopOut = num;
    }
    
    /**
     * מעלה באחד את מספר הכדורים שנהדפו לפי שחקן שהתקבל כפרמטר
     * @param player 
     */
    public void updatePopedBalls(char player) {
        if (player == 'w')
        {
            this.whitePopOut++;
        } else
        {
            this.blackPopOut++;
        }
    }
    
    /**
     * מציבה שחקן במיקום מסויים בלוח
     * @param row
     * @param col
     * @param player 
     */
    public void setPlayerAt(int row, int col, char player) {
        board[row][col] = player;
    }
    
/**
 * מקבלת מערך של מיקומים ושחקן ומשחזרת את המיקומים למקור שלהם
 * @param loc
 * @param currentPlayer 
 */
    public void restoreLocs(ArrayList<Location> loc, char currentPlayer) {
        for (int i = 0; i < loc.size(); i++)
        {
            board[loc.get(i).getRow()][loc.get(i).getCol()] = currentPlayer;
        }
    }
    

    /**
     * מחזירה את מספר הכדורים של השחקן הלבן נהדפו
     * @return 
     */
    public int getWhitePop() {
        return whitePopOut;
    }
    
    /**
     * מחזירה את מספר הכדורים של השחור שנהדפו
     * @return 
     */
    public int getBlackPop() {
        return blackPopOut;
    }
    
    /**
     * מחזירה את לוח התווים
     * @return 
     */
    public char[][] getModelBoard() {
        return board;
    }

    /**
     * מחזירה את המספר השמור במקום המתקבל כפרמטר
     * @param row
     * @param col
     * @return 
     */
    public char getValueAt(int row, int col) {
        return board[row][col];
    }

    /**
     * הפונקציה מעדכנת את לוח המודל לאחר קידום האלכסון
     * @param loc
     * @param currentPlayer
     * @param dest
     */
    public void updateCross(ArrayList<Location> loc, Location dest, char currentPlayer) {

        ArrayList<Location> withDest = new ArrayList<Location>();
        for (int i = 0; i < loc.size(); i++)
        {
            withDest.add(new Location(loc.get(i).getRow(), loc.get(i).getCol()));
        }

        withDest.add(dest);

        if (isCross(withDest))
        {
            for (int i = 1; i < withDest.size(); i++)
            {
                board[withDest.get(i).getRow()][withDest.get(i).getCol()] = currentPlayer;
            }
            board[withDest.get(0).getRow()][withDest.get(0).getCol()] = 'e';
            board[dest.getRow()][dest.getCol()] = currentPlayer;
            return;
        }

        //האם מדובר בהזזה של אלכסון לאלכסון מקביל
        if (isCrossRow(loc, dest, currentPlayer))
        {
            //האם המקום האחרון שנבחר באןתה שורה של מיקום היעד
            if (loc.get(loc.size() - 1).getRow() == dest.getRow())
            {
                //הזזה שמאלה
                if (loc.get(loc.size() - 1).getCol() > dest.getCol())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() - 2))
                        {
                            board[loc.get(i).getRow()][loc.get(i).getCol() - 2] = currentPlayer;
                            board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                        }

                    }
                    return;
                }
                //הזזה ימינה
                if (loc.get(loc.size() - 1).getCol() < dest.getCol())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() + 2))
                        {
                            board[loc.get(i).getRow()][loc.get(i).getCol() + 2] = currentPlayer;
                            board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                        }

                    }
                    return;
                }
            }

            if (upLeftToRightCross(loc) || downRightToLeftCross(loc))
            {
                //הזזה של אלכסון מקביל תחתון
                if (loc.get(loc.size() - 1).getRow() < dest.getRow())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() + 1))
                        {
                            board[loc.get(i).getRow() + 1][loc.get(i).getCol() + 1] = currentPlayer;
                            board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                        }
                    }
                    return;
                }
                //הזזה של אלכסון מקביל עליון
                if (loc.get(loc.size() - 1).getRow() > dest.getRow())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() - 1))
                        {
                            board[loc.get(i).getRow() - 1][loc.get(i).getCol() - 1] = currentPlayer;
                            board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                        }
                    }
                    return;
                }
            }

            if (upRightToLeftCross(loc) || downLeftToRightCross(loc))
            {
                //הזזה של אלכסון מקביל תחתון
                if (loc.get(loc.size() - 1).getRow() < dest.getRow())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() - 1))
                        {
                            board[loc.get(i).getRow() + 1][loc.get(i).getCol() - 1] = currentPlayer;
                            board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                        }
                    }
                    return;
                }
                //הזזה של אלכסון מקביל עליון
                if (loc.get(loc.size() - 1).getRow() > dest.getRow())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() + 1))
                        {
                            board[loc.get(i).getRow() - 1][loc.get(i).getCol() + 1] = currentPlayer;
                            board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                        }

                    }
                    return;
                }
            }

        }

    }

    /**
     * מציבה במיקום שהתקבל את השחקן בלוח הלוגי
     * @param row
     * @param col
     * @param ch 
     */
    public void updateModelBoard(int row, int col, char ch) {
        board[row][col] = ch;
    }

    /**
     * הפונקציה מעדכנת את לוח המודל לאחר קידום השורה
     *
     * @param loc
     * @param currentPlayer
     */
    public boolean updateRow(ArrayList<Location> loc, Location dest, char currentPlayer) {

        //רשימה התכיל את המיקום האחרון של הבחירות ומיקום היעד
        ArrayList<Location> tmp = new ArrayList<Location>();

        tmp.add(loc.get(loc.size() - 1));
        tmp.add(dest);

        //אלכסון יורד מימין לשמאל
        if (downRightToLeftCross(tmp))
        {
            //לאחר שהמיקומיים פנויים לעדכן את הלוח
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() - 1))
                {
                    board[loc.get(i).getRow() + 1][loc.get(i).getCol() - 1] = currentPlayer;
                    board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                }

            }
            return true;
        }

        //אלכסון עולה מימין לשמאל
        if (upRightToLeftCross(tmp))
        {
            //לאחר שהמיקומיים פנויים לעדכן את הלוח
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() - 1))
                {
                    board[loc.get(i).getRow() - 1][loc.get(i).getCol() - 1] = currentPlayer;
                    board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                }

            }
            return true;
        }

        //אלכסון יורד שמאל לימין
        if (downLeftToRightCross(tmp))
        {
            //לאחר שהמיקומיים פנויים לעדכן את הלוח
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() + 1))
                {
                    board[loc.get(i).getRow() + 1][loc.get(i).getCol() + 1] = currentPlayer;
                    board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                }
            }
            return true;
        }

        //אלכסון עולה שמאל לימין
        if (upLeftToRightCross(tmp))
        {
            //לאחר שהמיקומיים פנויים לעדכן את הלוח
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() + 1))
                {
                    board[loc.get(i).getRow() - 1][loc.get(i).getCol() + 1] = currentPlayer;
                    board[loc.get(i).getRow()][loc.get(i).getCol()] = 'e';
                }

            }
            return true;
        }

        //הזזות אופקיות
        //בדיקת הזזה שמאלה
        ArrayList<Location> withDest = new ArrayList<Location>();
        for (int i = 0; i < loc.size(); i++)
        {
            withDest.add(new Location(loc.get(i).getRow(), loc.get(i).getCol()));
        }

        withDest.add(dest);

        if (isRow(withDest))
        {
            for (int i = 1; i < withDest.size(); i++)
            {
                board[withDest.get(i).getRow()][withDest.get(i).getCol()] = currentPlayer;
            }
            board[loc.get(0).getRow()][loc.get(0).getCol()] = 'e';
            return true;
        }

        return true;
    }
/**
 *  מחזירה האם רצף של מיקומים הוא אלכסון
 * @param loc
 * @return 
 */
    public boolean isCross(ArrayList<Location> loc) {
        if (loc.size() == 1)
        {
            return true;
        }
        return upLeftToRightCross(loc) || upRightToLeftCross(loc) || downLeftToRightCross(loc) || downRightToLeftCross(loc);
    }
    
    /**
     * בודקת האם רצף של מיקומים הוא אלסכון יורד מימין לשמאל
     * @param loc
     * @return מחזיר האם הרצף חוקי או לא
     */
    public boolean downRightToLeftCross(ArrayList<Location> loc) {
        boolean flag = false;

        for (int i = 0; i < loc.size() - 1; i++)
        {
            //בדיקת רצף אלכסוני
            if (Math.abs(loc.get(i).getRow() - loc.get(i + 1).getRow()) == 1 && loc.get(i).getRow() < loc.get(i + 1).getRow())
            {
                if (Math.abs(loc.get(i).getCol() - loc.get(i + 1).getCol()) == 1 && loc.get(i).getCol() > loc.get(i + 1).getCol())
                {
                    flag = true;
                } else
                {
                    return false;
                }

            } //אין רצף אלכסוני 
            else
            {
                return false;
            }
        }
        return flag;
    }
    
    /**
     * בודקת האם רצף של מיקומים הוא אלכסון יורד משמאל לימין
     * @param loc
     * @return מחזיר האם הרצף חוקי או לא
     */
    public boolean downLeftToRightCross(ArrayList<Location> loc) {
        boolean flag = false;

        for (int i = 0; i < loc.size() - 1; i++)
        {
            //בדיקת רצף אלכסוני
            if (Math.abs(loc.get(i).getRow() - loc.get(i + 1).getRow()) == 1 && loc.get(i).getRow() < loc.get(i + 1).getRow())
            {
                if (Math.abs(loc.get(i).getCol() - loc.get(i + 1).getCol()) == 1 && loc.get(i).getCol() < loc.get(i + 1).getCol())
                {
                    flag = true;
                } else
                {
                    return false;
                }

            } //אין רצף אלכסוני 
            else
            {
                return false;
            }
        }
        return flag;
    }
    
    /**
     * בודקת האם רצף של מיקומים הוא אלכסון עולה משמאל לימין
     * @param loc
     * @return מחזיר האם הרצף חוקי או לא
     */
    public boolean upLeftToRightCross(ArrayList<Location> loc) {
        boolean flag = false;

        for (int i = 0; i < loc.size() - 1; i++)
        {
            //בדיקת רצף אלכסוני
            if (Math.abs(loc.get(i).getRow() - loc.get(i + 1).getRow()) == 1 && loc.get(i).getRow() > loc.get(i + 1).getRow())
            {
                if (Math.abs(loc.get(i).getCol() - loc.get(i + 1).getCol()) == 1 && loc.get(i).getCol() < loc.get(i + 1).getCol())
                {
                    flag = true;
                } else
                {
                    return false;
                }

            } //אין רצף אלכסוני 
            else
            {
                return false;
            }
        }
        return flag;
    }
    
    /**
     * בודקת האם רצף של מיוקמים הוא אלכסון עולה מימין לשמאל
     * @param loc
     * @return מחזיר האם הרצף חוקי או לא
     */
    public boolean upRightToLeftCross(ArrayList<Location> loc) {
        boolean flag = false;

        for (int i = 0; i < loc.size() - 1; i++)
        {
            //בדיקת רצף אלכסוני
            if (Math.abs(loc.get(i).getRow() - loc.get(i + 1).getRow()) == 1 && loc.get(i).getRow() > loc.get(i + 1).getRow())
            {
                if (Math.abs(loc.get(i).getCol() - loc.get(i + 1).getCol()) == 1 && loc.get(i).getCol() > loc.get(i + 1).getCol())
                {
                    flag = true;
                } else
                {
                    return false;
                }
            } //אין רצף אלכסוני 
            else
            {
                return false;
            }
        }
        return flag;
    }
    

    /**
     * הפונקציה בודקת האם הימקום הריק שנבחר מתאים להזזת שורה באלכסון
     *
     * @param loc
     * @return
     */
    public boolean isRowCross(ArrayList<Location> loc, Location dest, char currentPlayer) {
        if (getValueAt(dest.getRow(), dest.getCol()) == getOpsitePlayer(currentPlayer))
        {
            return false;
        }

        //רשימה התכיל את מיקומי הלחיצות האפשריים
        ArrayList<Location> possLocs = new ArrayList<Location>();

        //רשימה התכיל את המיקום האחרון של הבחירות ומיקום היעד
        ArrayList<Location> tmp = new ArrayList<Location>();
        tmp.add(loc.get(loc.size() - 1));
        tmp.add(dest);

        if (upLeftToRightCross(tmp) || downLeftToRightCross(tmp))
        {
            possLocs.add(new Location(loc.get(loc.size() - 1).getRow() - 1, loc.get(loc.size() - 1).getCol() + 1));
            possLocs.add(new Location(loc.get(loc.size() - 1).getRow() + 1, loc.get(loc.size() - 1).getCol() + 1));
        }

        if (upRightToLeftCross(tmp) || downRightToLeftCross(tmp))
        {
            possLocs.add(new Location(loc.get(loc.size() - 1).getRow() - 1, loc.get(loc.size() - 1).getCol() - 1));
            possLocs.add(new Location(loc.get(loc.size() - 1).getRow() + 1, loc.get(loc.size() - 1).getCol() - 1));
        }

        if (isRow(loc) == false)
        {
            return false;
        }
        if (getValueAt(dest.getRow(), dest.getCol()) == getOpsitePlayer(currentPlayer))
        {
            return false;
        }

        //אלכסון יורד מימין לשמאל
        if (downRightToLeftCross(tmp))
        {

            //אם לא כל מיקומי היעד פנויים תצא
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() - 1) && board[loc.get(i).getRow() + 1][loc.get(i).getCol() - 1] != 'e')
                {
                    for (int j = 0; j < loc.size(); j++)
                    {
                        board[loc.get(j).getRow()][loc.get(j).getCol()] = currentPlayer;
                    }
                    return false;
                }

            }

        }

        //אלכסון עולה מימין לשמאל
        if (upRightToLeftCross(tmp))
        {
            //אם לאכל מיקומי היעד פנויים תצא
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() - 1) && board[loc.get(i).getRow() - 1][loc.get(i).getCol() - 1] != 'e')
                {
                    for (int j = 0; j < loc.size(); j++)
                    {
                        board[loc.get(j).getRow()][loc.get(j).getCol()] = currentPlayer;
                    }
                    return false;
                }
            }
        }

        //אלכסון יורד שמאל לימין
        if (downLeftToRightCross(tmp))
        {
            //אם לא כל מיקומי היעד פנויים תצא
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() + 1) && board[loc.get(i).getRow() + 1][loc.get(i).getCol() + 1] != 'e')
                {
                    for (int j = 0; j < loc.size(); j++)
                    {
                        board[loc.get(j).getRow()][loc.get(j).getCol()] = currentPlayer;
                    }
                    return false;
                }

            }
        }

        //אלכסון עולה שמאל לימין
        if (upLeftToRightCross(tmp))
        {

            //אם לאכל מיקומי היעד פנויים תצא
            for (int i = 0; i < loc.size(); i++)
            {
                if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() + 1) && board[loc.get(i).getRow() - 1][loc.get(i).getCol() + 1] != 'e')
                {
                    for (int j = 0; j < loc.size(); j++)
                    {
                        board[loc.get(j).getRow()][loc.get(j).getCol()] = currentPlayer;
                    }
                    return false;
                }
            }
        }

        //הזזות אופקיות
        for (int i = 0; i < possLocs.size(); i++)
        {
            if (possLocs.get(i).getRow() == dest.getRow() && possLocs.get(i).getCol() == dest.getCol())
            {
                return true;
            }
        }
        return false;

    }

    /**
     * הפונקציה בודקת האם אפשר להזיז אלכסון לאלכסון מקביל אחר
     *
     * @param loc
     * @return
     */
    public boolean isCrossRow(ArrayList<Location> loc, Location dest, char currentPlayer) {
        boolean flag = false;

        //רשימה התכיל את המיקומים האפשריים שחוקיים ללחיצה
        ArrayList<Location> possibleLocs = new ArrayList<Location>();
        ArrayList<Location> withDest = new ArrayList<>(loc);
        withDest.add(dest);

        //יש מצבים שבהם הלחיצות האפשריות של הזזה של אלכסון במקביל מתאים גם להזזת שורה באלכסון 
        if (isRow(loc))
        {
            return false;
        }
        if (isCross(withDest))
        {
            return false;
        }

        if (upLeftToRightCross(loc))
        {
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() + 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() - 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() + 1, loc.get(loc.size() - 1).getCol() + 1));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() - 1, loc.get(loc.size() - 1).getCol() - 1));
        }

        if (downLeftToRightCross(loc))
        {
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() + 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() - 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() - 1, loc.get(loc.size() - 1).getCol() + 1));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() + 1, loc.get(loc.size() - 1).getCol() - 1));

        }
        if (downRightToLeftCross(loc))
        {
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() + 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() - 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() - 1, loc.get(loc.size() - 1).getCol() - 1));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() + 1, loc.get(loc.size() - 1).getCol() + 1));

        }

        if (upRightToLeftCross(loc))
        {
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() + 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow(), loc.get(loc.size() - 1).getCol() - 2));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() - 1, loc.get(loc.size() - 1).getCol() + 1));
            possibleLocs.add(new Location(loc.get(loc.size() - 1).getRow() + 1, loc.get(loc.size() - 1).getCol() - 1));
        }

        //בדיקה האם המיקום שנלחץ מתאים לאחד מהמיקומים האפשריים
        for (int i = 0; i < possibleLocs.size(); i++)
        {
            //האם המיקום שנבחר חוקי
            if (dest.getRow() == possibleLocs.get(i).getRow() && dest.getCol() == possibleLocs.get(i).getCol())
            {
                flag = true;
                break;
            }

        }
        if (flag == false)
        {
            return false;
        }

        if (upLeftToRightCross(loc) || downRightToLeftCross(loc))
        {
            //בדיקה האם המיקומים ריקים בהזזת האלכסון לאלכסון מקביל
            if (dest.getRow() == loc.get(loc.size() - 1).getRow())
            {
                //בדיקת הזזה ימינה
                if (dest.getCol() > loc.get(loc.size() - 1).getCol())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() + 2) == false)
                        {
                            return false;
                        }
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() + 2) && board[loc.get(i).getRow()][loc.get(i).getCol() + 2] != 'e')
                        {
                            return false;
                        }
                    }
                }
                //בדיקת הזזה שמאלה
                if (dest.getCol() < loc.get(loc.size() - 1).getCol())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() - 2) == false)
                        {
                            return false;
                        }
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() - 2) && board[loc.get(i).getRow()][loc.get(i).getCol() - 2] != 'e')
                        {
                            return false;
                        }
                    }
                }
            }

            //בדיקת אלכסון מקביל תחתון
            if (dest.getRow() > loc.get(loc.size() - 1).getRow())
            {
                for (int i = 0; i < loc.size(); i++)
                {
                    if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() + 1) == false)
                    {
                        return false;
                    }
                    if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() + 1) && board[loc.get(i).getRow() + 1][loc.get(i).getCol() + 1] != 'e')
                    {
                        return false;
                    }
                }
            }
            //בדיקת הזזה אלכסון מקביל עליון
            if (dest.getRow() < loc.get(loc.size() - 1).getRow())
            {
                for (int i = 0; i < loc.size(); i++)
                {
                    if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() - 1) == false)
                    {
                        return false;
                    }
                    if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() - 1) && board[loc.get(i).getRow() - 1][loc.get(i).getCol() - 1] != 'e')
                    {
                        return false;
                    }
                }
            }

        }
        if (upRightToLeftCross(loc) || downLeftToRightCross(loc))
        {
            //בדיקה האם המיקומים ריקים בהזזת האלכסון לאלכסון מקביל
            if (dest.getRow() == loc.get(loc.size() - 1).getRow())
            {
                //בדיקת הזזה ימינה
                if (dest.getCol() > loc.get(loc.size() - 1).getCol())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() + 2) == false)
                        {
                            return false;
                        }
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() + 2) && board[loc.get(i).getRow()][loc.get(i).getCol() + 2] != 'e')
                        {
                            return false;
                        }
                    }
                }
                //בדיקת הזזה שמאלה
                if (dest.getCol() < loc.get(loc.size() - 1).getCol())
                {
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() - 2) == false)
                        {
                            return false;
                        }
                        if (isLegalLoc(loc.get(i).getRow(), loc.get(i).getCol() - 2) && board[loc.get(i).getRow()][loc.get(i).getCol() - 2] != 'e')
                        {
                            return false;
                        }
                    }
                }
            }

            //בדיקת אלכסון מקביל תחתון
            if (dest.getRow() > loc.get(loc.size() - 1).getRow())
            {
                for (int i = 0; i < loc.size(); i++)
                {
                    if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() - 1) == false)
                    {
                        return false;
                    }
                    if (isLegalLoc(loc.get(i).getRow() + 1, loc.get(i).getCol() - 1) && board[loc.get(i).getRow() + 1][loc.get(i).getCol() - 1] != 'e')
                    {
                        return false;
                    }
                }
            }
            //בדיקת הזזה אלכסון מקביל עליון
            if (dest.getRow() < loc.get(loc.size() - 1).getRow())
            {
                for (int i = 0; i < loc.size(); i++)
                {
                    if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() + 1) == false)
                    {
                        return false;
                    }
                    if (isLegalLoc(loc.get(i).getRow() - 1, loc.get(i).getCol() + 1) && board[loc.get(i).getRow() - 1][loc.get(i).getCol() + 1] != 'e')
                    {
                        return false;
                    }
                }
            }

        }

        return flag;
    }

    /**
     * הפונקציה בודקת האם המיקומים שנבחרו הם שורה
     *
     * @param tmp
     * @return
     */
    public boolean isRow(ArrayList<Location> loc) {
        if (loc.size() == 1)
        {
            return true;
        }
        boolean flag = false;

        for (int i = 0; i < loc.size() - 1; i++)
        {
            //צריך למנוע לאחר בחירת שני נקודות באלכסון למנוע בחירת שורה
            //בדיקת רצף של שורה
            if (loc.get(i).getRow() == loc.get(i + 1).getRow())
            {
                if (Math.abs(loc.get(i).getCol() - loc.get(i + 1).getCol()) == 2)
                {
                    flag = true;
                } else
                {
                    return false;
                }
            } else
            {
                return false;
            }
            //מקרי קצה לחיצה שלישית
            if (i == 1)
            {
                if (loc.get(i).getRow() != loc.get(i + 1).getRow())
                {
                    return false;
                }
                if (loc.get(0).getRow() != loc.get(i + 1).getRow())
                {
                    return false;
                }

            }

        }

        if (flag)
        {
            return true;
        }
        return false;
    }
    /**
     * מחזירה את השחקן שלא תורו
     * @param player
     * @return 
     */
    public char getOpsitePlayer(char player) {
        if (player == 'w')
        {
            return 'b';
        } else
        {
            return 'w';
        }
    }
    /**
     * מעדכנת את לוח המודל לפי כיוון ההזזה המתאים
     * @param loc
     * @param dest
     * @param currentPlayer 
     */
    public void pushOpponetModelUpdate(ArrayList<Location> loc, Location dest, char currentPlayer) {
        ArrayList<Location> withDest = new ArrayList<>(loc);
        withDest.add(dest);
        if (downLeftToRightCross(withDest))
        {
            board[withDest.get(0).getRow()][withDest.get(0).getCol()] = 'e';
            if (withDest.size() == 3)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(2).getRow() + 1][withDest.get(2).getCol() + 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 1)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() + 1][withDest.get(3).getCol() + 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 2)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() + 1][withDest.get(3).getCol() + 1] = getOpsitePlayer(currentPlayer);
                board[withDest.get(3).getRow() + 2][withDest.get(3).getCol() + 2] = getOpsitePlayer(currentPlayer);
                return;
            }

        }

        if (downRightToLeftCross(withDest))
        {
            board[withDest.get(0).getRow()][withDest.get(0).getCol()] = 'e';
            if (withDest.size() == 3)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(2).getRow() + 1][withDest.get(2).getCol() - 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 1)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() + 1][withDest.get(3).getCol() - 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 2)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() + 1][withDest.get(3).getCol() - 1] = getOpsitePlayer(currentPlayer);
                board[withDest.get(3).getRow() + 2][withDest.get(3).getCol() - 2] = getOpsitePlayer(currentPlayer);
                return;
            }

        }

        if (upLeftToRightCross(withDest))
        {
            board[withDest.get(0).getRow()][withDest.get(0).getCol()] = 'e';
            if (withDest.size() == 3)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(2).getRow() - 1][withDest.get(2).getCol() + 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 1)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() - 1][withDest.get(3).getCol() + 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 2)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() - 1][withDest.get(3).getCol() + 1] = getOpsitePlayer(currentPlayer);
                board[withDest.get(3).getRow() - 2][withDest.get(3).getCol() + 2] = getOpsitePlayer(currentPlayer);
                return;
            }

        }

        if (upRightToLeftCross(withDest))
        {
            board[withDest.get(0).getRow()][withDest.get(0).getCol()] = 'e';
            if (withDest.size() == 3)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(2).getRow() - 1][withDest.get(2).getCol() - 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 1)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() - 1][withDest.get(3).getCol() - 1] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 2)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow() - 1][withDest.get(3).getCol() - 1] = getOpsitePlayer(currentPlayer);
                board[withDest.get(3).getRow() - 2][withDest.get(3).getCol() - 2] = getOpsitePlayer(currentPlayer);
                return;
            }

        }

        int min = withDest.get(0).getCol();
        //מציאת העמודה המקסימלתית שבה נשים את הערך ריק
        int max = withDest.get(0).getCol();
        for (int i = 0; i < withDest.size() - 1; i++)
        {
            if (min > withDest.get(i).getCol())
            {
                min = withDest.get(i).getCol();
            }
            if (max < withDest.get(i).getCol())
            {
                max = withDest.get(i).getCol();
            }
        }
        //הזזה שמאלה
        if (min > dest.getCol())
        {
            board[withDest.get(0).getRow()][max] = 'e';
            if (withDest.size() == 3)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol() - 2] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 1)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol() - 2] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 2)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol() - 2] = getOpsitePlayer(currentPlayer);
                board[withDest.get(3).getRow()][withDest.get(3).getCol() - 4] = getOpsitePlayer(currentPlayer);
                return;
            }

        }

        //הזזה ימינה
        if (min < dest.getCol())
        {
            board[withDest.get(0).getRow()][min] = 'e';
            if (withDest.size() == 3)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol() + 2] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 1)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol() + 2] = getOpsitePlayer(currentPlayer);
                return;
            }
            if (withDest.size() == 4 && opositePlayerPower == 2)
            {
                board[withDest.get(1).getRow()][withDest.get(1).getCol()] = currentPlayer;
                board[withDest.get(2).getRow()][withDest.get(2).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol()] = currentPlayer;
                board[withDest.get(3).getRow()][withDest.get(3).getCol() + 2] = getOpsitePlayer(currentPlayer);
                board[withDest.get(3).getRow()][withDest.get(3).getCol() + 4] = getOpsitePlayer(currentPlayer);
                return;
            }

        }

    }

    /**
     * *
     * הפונקציה בודקת האם אפשר לעשות ההזזה של שחקן יריב הפונקציה מחזירה אפס אם אי
     * אפשר להזיז את היריב הפונקציה מחזירה אחד אם יש אפשרות להזיז למקום ריק את
     * היריב הפונקציה מחזירה שתיים אם אפשר לדחוף את השחקן החוצה
     *
     * @param loc
     * @param dest
     * @param currentPlayer
     * @return מחזיר ערך האם אפשר לדחוף ואם כן איזה סוג הדיפה
     */
    public int isPossibleToPushOpponet(ArrayList<Location> loc, Location dest, char currentPlayer) {
        opositePlayerPower = 0;
        curentPlayerPower = loc.size();
        boolean flag = true;

        //המיקום של המשבצת אותה נרצה לדחוף וממה נספור את מספר ההופעות ברצף של שחקן
        int row = dest.getRow();
        int col = dest.getCol();

        //אם המקום האחרון ברשימה שהוא אמור להיות השחקן אותו נרצה לדחוף שונה מהשחקן היריב תצא
        if (board[row][col] != getOpsitePlayer(currentPlayer))
        {
            return 0;
        }

        //אלכסון יורד ימין לשמאל
        if (downRightToLeftCross(loc))
        {
            //ספירת ההופעות של שחקן הנגדי ברצף
            while (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
            {
                opositePlayerPower++;
                if (isBorder(row, col))
                {
                    //בדיקה האם כל המיקומים על הגבול
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (!isBorder(loc.get(i).getRow(), loc.get(i).getCol()))
                        {
                            flag = false;
                        }
                    }

                    if (flag == true)
                    {
                        //בדיקה האם האחרון מוצב בקודוד והוא אמור להדחף
                        if (isCorner(row, col) > 0)
                        {
                            return 2;
                        } else
                        {
                            return 1;
                        }
                    }

                    if (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                    {
                        if (curentPlayerPower > opositePlayerPower)
                        {
                            return 2;
                        }
                    }
                }
                col--;
                row++;
            }
            //אם לאחר שיצאנו מהלולאה המיקום שאחריו הוא ריק נבדוק את רמת החוזק אחד נגד השני
            if (curentPlayerPower > opositePlayerPower)
            {
                if (board[row][col] == 'e')
                {
                    return 1;
                }
            } else
            {
                return 0;
            }
        }

        //אלכסון יורד שמאל לימין
        if (downLeftToRightCross(loc))
        {
            flag = true;
            while (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
            {
                opositePlayerPower++;
                if (isBorder(row, col))
                {
                    //בדיקה האם כל המיקומים על הגבול
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (!isBorder(loc.get(i).getRow(), loc.get(i).getCol()))
                        {
                            flag = false;
                        }
                    }
                    //בדיקה האם האחרון מוצב בקודוד והוא אמור להדחף
                    if (flag == true)
                    {
                        if (isCorner(row, col) > 0)
                        {
                            return 2;
                        } else
                        {
                            return 1;
                        }
                    }

                    if (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                    {
                        if (curentPlayerPower > opositePlayerPower)
                        {
                            return 2;
                        }
                    }
                }
                col++;
                row++;
            }
            if (curentPlayerPower > opositePlayerPower)
            {
                if (isLegalLoc(row, col) && board[row][col] == 'e')
                {
                    return 1;
                }
            } else
            {
                return 0;
            }
        }
        //אלכסון עולה שמאל לימין
        if (upLeftToRightCross(loc))
        {
            flag = true;
            while (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
            {
                opositePlayerPower++;
                if (isBorder(row, col))
                {
                    //בדיקה האם כל המיקומים על הגבול
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (!isBorder(loc.get(i).getRow(), loc.get(i).getCol()))
                        {
                            flag = false;
                        }
                    }
                    //בדיקה האם האחרון מוצב בקודוד והוא אמור להדחף
                    if (flag == true)
                    {
                        if (isCorner(row, col) > 0)
                        {
                            return 2;
                        } else
                        {
                            return 1;
                        }
                    }
                    if (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                    {
                        if (curentPlayerPower > opositePlayerPower)
                        {
                            return 2;
                        }
                    }
                }
                col++;
                row--;

            }
            if (curentPlayerPower > opositePlayerPower)
            {
                if (isLegalLoc(row, col) && board[row][col] == 'e')
                {
                    return 1;
                }
            } else
            {
                return 0;
            }
        }
        //אלכסון עולה ימין לשמאל
        if (upRightToLeftCross(loc))
        {
            flag = true;
            while (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
            {
                opositePlayerPower++;
                if (isBorder(row, col))
                {
                    //בדיקה האם כל המיקומים על הגבול
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (!isBorder(loc.get(i).getRow(), loc.get(i).getCol()))
                        {
                            flag = false;
                        }
                    }
                    //בדיקה האם האחרון מוצב בקודוד והוא אמור להדחף
                    if (flag == true)
                    {
                        if (isCorner(row, col) > 0)
                        {
                            return 2;
                        } else
                        {
                            return 1;
                        }
                    }
                    if (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                    {
                        if (curentPlayerPower > opositePlayerPower)
                        {
                            return 2;
                        }
                    }
                }
                col--;
                row--;
            }
            if (curentPlayerPower > opositePlayerPower)
            {
                if (isLegalLoc(row, col) && board[row][col] == 'e')
                {
                    return 1;
                }
            } else
            {
                return 0;
            }
        }

        if (isRow(loc))
        {
            //ההזה ימינה
            if (loc.get(0).getCol() < loc.get(loc.size() - 1).getCol())
            {
                flag = true;
                while (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                {
                    opositePlayerPower++;
                    //בדיקה האם כל המיקומים על הגבול
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (!isBorder(loc.get(i).getRow(), loc.get(i).getCol()))
                        {
                            flag = false;
                        }
                    }
                    //בדיקה האם האחרון מוצב בקודוד והוא אמור להדחף
                    if (flag == true)
                    {
                        if (isCorner(row, col) > 0)
                        {
                            return 2;
                        } else
                        {
                            return 1;
                        }
                    }
                    if (isBorder(row, col))
                    {
                        if (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                        {
                            if (curentPlayerPower > opositePlayerPower)
                            {
                                return 2;
                            }
                        }
                    }
                    col += 2;
                }
                //אם המקום האחרון שאנו בודקים הוא ריק אז יש מקום להזיז
                if (curentPlayerPower > opositePlayerPower)
                {
                    if (isLegalLoc(row, col) && board[row][col] == 'e')
                    {
                        return 1;
                    }
                } else
                {
                    return 0;
                }
            }

            //ההזזה שמאלה
            if (loc.get(0).getCol() > loc.get(loc.size() - 1).getCol())
            {
                flag = true;
                while (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                {
                    opositePlayerPower++;
                    //בדיקה האם כל המיקומים על הגבול
                    for (int i = 0; i < loc.size(); i++)
                    {
                        if (!isBorder(loc.get(i).getRow(), loc.get(i).getCol()))
                        {
                            flag = false;
                        }
                    }
                    //בדיקה האם האחרון מוצב בקודוד והוא אמור להדחף
                    if (flag == true)
                    {
                        if (isCorner(row, col) > 0)
                        {
                            return 2;
                        } else
                        {
                            return 1;
                        }
                    }
                    if (isBorder(row, col))
                    {
                        if (isLegalLoc(row, col) && board[row][col] == getOpsitePlayer(currentPlayer))
                        {
                            if (curentPlayerPower > opositePlayerPower)
                            {
                                return 2;
                            }
                        }
                    }
                    col -= 2;
                }
                //אם המקום האחרון שאנו בודקים הוא ריק אז יש מקום להזיז
                if (curentPlayerPower > opositePlayerPower)
                {
                    if (isLegalLoc(row, col) && board[row][col] == 'e')
                    {
                        return 1;
                    }
                } else
                {
                    return 0;
                }
            }
        }
        return 0;
    }

    /**
     * מחזירה האם רצף של מיקומים הוא אלכסון או שורה
     * @param loc
     * @param dest
     * @return 
     */
    public boolean isRowOrCross(ArrayList<Location> loc, Location dest) {
        return isCross(loc) || isRow(loc);
    }

    /**
     * האם המיקום הוא על הגבול של לוח המשחק
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isBorder(int row, int col) {
        if ((row == 4 && col == 0))
        {
            return true;
        }
        if ((row == 4 && col == 16))
        {
            return true;
        }
        if ((row == 1 && col == 3))
        {
            return true;
        }
        if ((row == 2 && col == 2))
        {
            return true;
        }
        if ((row == 3 && col == 1))
        {
            return true;
        }
        if ((row == 1 && col == 13))
        {
            return true;
        }
        if ((row == 5 && col == 1))
        {
            return true;
        }
        if ((row == 6 && col == 2))
        {
            return true;
        }
        if ((row == 7 && col == 3))
        {
            return true;
        }
        if ((row == 7 && col == 13))
        {
            return true;
        }
        if ((row == 6 && col == 14))
        {
            return true;
        }
        if ((row == 5 && col == 15))
        {
            return true;
        }
        if ((row == 3 && col == 15))
        {
            return true;
        }
        if ((row == 2 && col == 14))
        {
            return true;
        }
        if (row == 0 && col >= 4 && col <= 12)
        {
            return true;
        }
        if (row == 8 && col >= 4 && col <= 12)
        {
            return true;
        }

        return false;
    }

    /**
     * עדכון לוח המשחק על דחיפת שחקן החוצה
     * @param loc
     * @param dest
     * @param currentPlayer 
     */
    public void popOutOpponetUpdate(ArrayList<Location> loc, Location dest, char currentPlayer) {
        for (int i = 1; i < loc.size(); i++)
        {
            board[loc.get(i).getRow()][loc.get(i).getCol()] = currentPlayer;
        }
        board[dest.getRow()][dest.getCol()] = currentPlayer;
        board[loc.get(0).getRow()][loc.get(0).getCol()] = 'e';

        if (currentPlayer == 'w')
        {
            blackPopOut++;
        } else
        {
            whitePopOut++;
        }

    }

    /**
     * האם המשחק נגמר
     * @return 
     */
    public boolean isGameOver() {
        if (blackPopOut == NUM_TO_POP || whitePopOut == NUM_TO_POP)
        {
            return true;
        } else
        {
            return false;
        }
    }

 
    /**
     * מדיפה את מטריצת התווים
     */
    public void printModelBoard() {
        for (int i = 0; i < BOARD_ROW; i++)
        {
            for (int j = 0; j < BOARD_COL; j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
/**
 * האם המיקום הוא קודוקוד הלוח
 * @param row
 * @param col
 * @return 
 */
    public int isCorner(int row, int col) {
        if (row == 0 && col == 4)
        {
            return 1;
        }
        if (row == 4 && col == 0)
        {
            return 2;
        }
        if (row == 0 && col == 12)
        {
            return 3;
        }
        if (row == 4 && col == 16)
        {
            return 4;
        }
        if (row == 8 && col == 4)
        {
            return 5;
        }
        if (row == 8 && col == 12)
        {
            return 6;
        }
        return 0;
    }

    /**
     * בדיקה האם המיקום הוא מסגרת של הלוח
     * @param row
     * @param col
     * @return 
     */
    public int isSide(int row, int col) {
        //צלע עליונה
        if ((row == 0 && col == 6) || (row == 0 && col == 8) || (row == 0 && col == 10))
        {
            return 1;
        }
        //צלע שמאלית עליונה
        if ((row == 1 && col == 3) || (row == 2 && col == 2) || (row == 3 && col == 1))
        {
            return 2;
        }
        //צלע שמאלית תחתונה
        if ((row == 5 && col == 1) || (row == 6 && col == 2) || (row == 7 && col == 3))
        {
            return 3;
        }
        //צלע תחתונה
        if ((row == 8 && col == 6) || (row == 8 && col == 8) || (row == 8 && col == 10))
        {
            return 4;
        }
        //צלע ימנית תחתונה
        if ((row == 7 && col == 13) || (row == 6 && col == 14) || (row == 5 && col == 15))
        {
            return 5;
        }
        //צלע ימנית עליונה
        if ((row == 1 && col == 13) || (row == 2 && col == 14) || (row == 3 && col == 15))
        {
            return 6;
        }
        return 0;
    }


    /**
     * האם המיקום חוקי ובתוך תחומי הלוח
     * @param row
     * @param col
     * @return 
     */
    private boolean isLegalLoc(int row, int col) {
        if ((row >= 0 && row <= 8) && (col >= 0 && col <= 16))
        {
            return true;
        }
        return false;
    }
    
/**
 * 
 * מקבלת רשימת מיקומים שנבחרו ומיקום היעד שנבחר להזיז והשחק הנוכחי 
 * בודקת איזה סוג של הזזה מדובר ומחזירה מספר שלם שמייצג הזזה
 * @param loc
 * @param row
 * @param col
 * @param currentPlayer
 * @return 
 */
    public int getMoveDirection(ArrayList<Location> loc, int row, int col, char currentPlayer) {
        Location dest = new Location(row, col);
        //שרשימה התכיל את מיקומי הבחירות ואת מיקום היעד
        ArrayList<Location> allLocs = new ArrayList<Location>();
        for (int i = 0; i < loc.size(); i++) {
            allLocs.add(new Location(loc.get(i).getRow(), loc.get(i).getCol()));
        }
        allLocs.add(dest);

        if (!loc.isEmpty()) {
            if (downLeftToRightCross(allLocs)) {
                return 1;
            }
            if (downRightToLeftCross(allLocs)) {
                return 2;
            }
            if (upRightToLeftCross(allLocs)) {
                return 3;
            }
            if (upLeftToRightCross(allLocs)) {
                return 4;
            }
            if (isRow(allLocs)) {
                return 5;
            }
            if (isCrossRow(loc, dest, currentPlayer)) {
                return 6;
            }
            if (isRowCross(loc, dest, currentPlayer)) {
                return 7;
            }
        }
        return 0;
    }
}
