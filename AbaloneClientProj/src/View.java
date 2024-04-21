

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Document : View 
 * Created on : 07/02/2018
 * Author : Naor Bar-Lev
 */
public class View {

    //חלון המשחק
    private JFrame win; 
    // מטריצת כפתורים
    private JButton[][] viewBoard; 
    //איזורי מידע בחלון המשחק
    private JLabel lblMsg, lblBlackCounter, lblWhiteCounter  ,emptyLbl;
    //איזור הכפתורים
    private JPanel pnlButtons;
    //תפריט ראשי
    private JMenuBar menuBar;
    //כתפור בלוח המשחק
    private JButton btn;
    //מטירצת אייקונים
    public ImageIcon[][] boardPic = new ImageIcon[Client.BOARD_ROW][Client.BOARD_COL];
    
    /**
     * פעולה בונה היוצרת את הכתורים ואת לוח המשחק
     */
    public View() {
        viewBoard = new JButton[Client.BOARD_ROW][Client.BOARD_COL];
        
        pnlButtons = createButtonsPanel();
        emptyLbl = createEmptyLable();
        lblMsg = createMessageLable();
        menuBar = createMenuBar();
        lblBlackCounter = createCounterLable("Black:0 ");
        lblWhiteCounter = createCounterLable(" White:0");
        

        win = new JFrame(Client.GAME_TITLE);
        win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        win.setJMenuBar(menuBar);
        win.add(pnlButtons, BorderLayout.CENTER);
        win.add(lblMsg, BorderLayout.SOUTH);
        
        win.add(emptyLbl,BorderLayout.NORTH);
        
        emptyLbl.setOpaque(true);
        lblMsg.setOpaque(true);
        lblMsg.setBackground(new Color(200, 200, 200));
        win.setResizable(false);

        //הצגת הכדורים שמחוץ למשחק
        lblBlackCounter.setVerticalAlignment(SwingConstants.NORTH);
        lblWhiteCounter.setVerticalAlignment(SwingConstants.NORTH);
        lblBlackCounter.setOpaque(true);
        lblWhiteCounter.setOpaque(true);
        lblBlackCounter.setBackground(new Color(200, 200, 200));
        lblWhiteCounter.setBackground(new Color(200, 200, 200));
        win.add(lblBlackCounter, BorderLayout.EAST);
        win.add(lblWhiteCounter, BorderLayout.WEST);

        win.pack();
        win.setLocationRelativeTo(null);
    }
    
    /**
     * עדכון כותרת החלון
     * @param s 
     */
    public void setTitle(String s)
    {
        win.setTitle(s);
    }

   /**
    *  יצירת כפתורות ותכונותיו והשמה בלוח הכפתורים
    * @return 
    */
    private JPanel createButtonsPanel() {

        JPanel pnl = new JPanel(new GridLayout(Client.BOARD_ROW, Client.BOARD_COL));
        pnl.setBackground(new Color(200, 200, 200));

        for (int row = 0; row < viewBoard.length; row++)
        {
            for (int col = 0; col < viewBoard[row].length; col++)
            {

                btn = new JButton("");
                btn.setBackground(null);
                btn.setBorder(null);
                btn.setPreferredSize(new Dimension(Client.BUTTON_SIZE, Client.BUTTON_SIZE));
                btn.setFocusable(false);
                btn.putClientProperty("rowIndex", row);
                btn.putClientProperty("colIndex", col);
                viewBoard[row][col] = btn;
                pnl.add(btn);

            }

        }

        return pnl;
    }

    /**
     * אתחול לוח המשחק למצב התחלתי
     */
    public void init() {
        win.setIconImage(new ImageIcon(getClass().getResource("resources/abaloneSplash.png")).getImage());
        createBoardGame();
        hideButtons();
    }

    /**
     * יצירת תפריט ראשי
     * @return 
     */
    private JMenuBar createMenuBar() {
        //יצירת בר התפריטים אותו נצרף לחלון המשחק 
        JMenuBar menuBar = new JMenuBar();

        // יצירת תפריט ראשי ראשון אותו נוסיף לבר התפריטים
        JMenu menu1 = new JMenu("Options"); // יצירת 
        menuBar.add(menu1);

        // יצירת סדרת תת תפריטים אותם נוסיף לתפריט הראשי הראשון
        JMenuItem menu1_1 = new JMenuItem("How To Play");
        JMenuItem menu1_2 = new JMenuItem("About");
        JMenuItem menu1_3 = new JMenuItem("Exit");
        menu1.add(menu1_1);
        menu1.add(menu1_2);
        menu1.add(menu1_3);
        // sub menu

        return menuBar;
    }
    
    /**
     * יצירת איזור ההודעות
     * @return 
     */
    private JLabel createMessageLable() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(Color.BLACK);
        lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
        return lbl;
    }
    
    /**
     * יצירת איזור ריק
     * @return 
     */
     private JLabel createEmptyLable() 
     {
        JLabel l = new JLabel(" ");
        l.setBackground(new Color(200, 200, 200));
        return l;
    }
    
     /**
      * יצירת איזור מספר הכדורים שיצאו לכל שחקן
      * @param s
      * @return 
      */
    private JLabel createCounterLable(String s) {
        JLabel lbl = new JLabel(s);
        lbl.setForeground(Color.black);
        lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        return lbl;
    }

    /**
     * עדכון איזור ההודעות
     * @param msg 
     */
    public void setMessage(String msg) 
    {
        lblMsg.setText(msg);
    }
    
    /**
     * מציג או מסתיר את חלון המשחק
     * @param status 
     */
    public void setVisible(boolean status)
    {
        win.setVisible(true);
    }

    
    public JFrame getWin() {
        return win;
    }
    
    /**
     *  נועל את כפתורי המשחק
     * @param b 
     */
    public void setAllBtnDisable(Boolean b) {
        for (int row = 0; row < Client.BOARD_ROW; row++)
        {
            for (int col = 0; col < Client.BOARD_COL; col++)
            {
                viewBoard[row][col].setEnabled(b);
            }
        }
    }

    /**
     *  השמת תמונות על הפתורים
     */
    public void createBoardGame() {
        //שורה ראשונה ואחרונה
        for (int i = 4; i < 13; i++)
        {
            if (i % 2 == 0)
            {
                viewBoard[0][i].setIcon(Client.icons[4]);
                viewBoard[0][i].setDisabledIcon(Client.icons[4]);
                viewBoard[8][i].setIcon(Client.icons[4]);
                viewBoard[8][i].setDisabledIcon(Client.icons[4]);
                viewBoard[0][i].setIcon(Client.icons[2]);
                viewBoard[0][i].setDisabledIcon(Client.icons[2]);
                viewBoard[8][i].setIcon(Client.icons[1]);
                viewBoard[8][i].setDisabledIcon(Client.icons[1]);
            } 
            else
            {

                viewBoard[0][i].setEnabled(false);
                viewBoard[8][i].setEnabled(false);
                viewBoard[0][i].setVisible(false);
                viewBoard[8][i].setVisible(false);
            }
        }
        //שורה שנייה ושישית
        for (int i = 3; i < 14; i++)
        {
            if (i % 2 == 0)
            {
                viewBoard[1][i].setEnabled(false);
                viewBoard[7][i].setEnabled(false);
                viewBoard[1][i].setVisible(false);
                viewBoard[7][i].setVisible(false);
                continue;
            }
            viewBoard[1][i].setIcon(Client.icons[4]);
            viewBoard[1][i].setDisabledIcon(Client.icons[4]);
            viewBoard[7][i].setIcon(Client.icons[4]);
            viewBoard[7][i].setDisabledIcon(Client.icons[4]);
            viewBoard[1][i].setIcon(Client.icons[2]);
            viewBoard[1][i].setDisabledIcon(Client.icons[2]);
            viewBoard[7][i].setIcon(Client.icons[1]);
            viewBoard[7][i].setDisabledIcon(Client.icons[1]);

        }

        //שורה שלישית וחמישית
        for (int i = 2; i < 15; i++)
        {
            if (i % 2 == 0)
            {
                viewBoard[2][i].setIcon(Client.icons[4]);
                viewBoard[2][i].setDisabledIcon(Client.icons[4]);
                viewBoard[6][i].setIcon(Client.icons[4]);
                viewBoard[6][i].setDisabledIcon(Client.icons[4]);

                if (i == 6 || i == 8 || i == 10)
                {
                    viewBoard[2][i].setIcon(Client.icons[2]);
                    viewBoard[2][i].setDisabledIcon(Client.icons[2]);
                    viewBoard[6][i].setIcon(Client.icons[1]);
                    viewBoard[6][i].setDisabledIcon(Client.icons[1]);
                }
            } else
            {
                viewBoard[2][i].setEnabled(false);
                viewBoard[6][i].setEnabled(false);
                viewBoard[2][i].setVisible(false);
                viewBoard[6][i].setVisible(false);
            }
        }

        //השורה הרביעית והשישית
        for (int i = 1; i < 16; i++)
        {
            if (i % 2 == 0)
            {
                viewBoard[3][i].setEnabled(false);
                viewBoard[5][i].setEnabled(false);
                viewBoard[3][i].setVisible(false);
                viewBoard[5][i].setVisible(false);
                continue;
            }

            viewBoard[3][i].setIcon(Client.icons[4]);
            viewBoard[3][i].setDisabledIcon(Client.icons[4]);
            viewBoard[5][i].setIcon(Client.icons[4]);
            viewBoard[5][i].setDisabledIcon(Client.icons[4]);

        }

        //השורה האמצעית
        for (int i = 0; i < Client.BOARD_COL; i++)
        {
            if (i % 2 == 0)
            {
                viewBoard[4][i].setIcon(Client.icons[4]);
                viewBoard[4][i].setDisabledIcon(Client.icons[4]);
            } else
            {
                viewBoard[4][i].setEnabled(false);
                viewBoard[4][i].setVisible(false);

            }

        }
    }

    /**
     * הפעולה מסתירה את הכפתורים שלא הולכים להיות בשימוש
     */
    public void hideButtons() {

        for (int i = 0; i < Client.BOARD_COL; i++)
        {
            if (i > 3 && i < 13)
            {
                continue;
            } else
            {
                viewBoard[0][i].setEnabled(false);
                viewBoard[8][i].setEnabled(false);
                viewBoard[0][i].setVisible(false);
                viewBoard[8][i].setVisible(false);
            }

        }

        for (int i = 0; i < Client.BOARD_COL; i++)
        {
            if (i > 2 && i < 14)
            {
                continue;
            } else
            {
                viewBoard[1][i].setEnabled(false);
                viewBoard[7][i].setEnabled(false);
                viewBoard[1][i].setVisible(false);
                viewBoard[7][i].setVisible(false);
            }

        }

        for (int i = 0; i < Client.BOARD_COL; i++)
        {
            if (i > 1 && i < 15)
            {
                continue;
            } else
            {
                viewBoard[2][i].setEnabled(false);
                viewBoard[6][i].setEnabled(false);
                viewBoard[2][i].setVisible(false);
                viewBoard[6][i].setVisible(false);

            }

        }

        for (int i = 0; i < Client.BOARD_COL; i++)
        {
            if (i > 0 && i < 16)
            {
                continue;
            } else
            {
                viewBoard[3][i].setEnabled(false);
                viewBoard[5][i].setEnabled(false);
                viewBoard[3][i].setVisible(false);
                viewBoard[5][i].setVisible(false);
            }

        }

        for (int i = 0; i < Client.BOARD_COL; i++)
        {
            if (i % 2 != 0)
            {
                viewBoard[4][i].setEnabled(false);
                viewBoard[4][i].setVisible(false);
            }

        }

    }

    public JButton[][] getBoardButtons() {
        return viewBoard;
    }
    
    /**
     *  מחזיר כפתור במיקום מסויים
     * @param row
     * @param col
     * @return 
     */
    public JButton getButton(int row, int col) {
        return viewBoard[row][col];
    }
    
    /**
     * מקבלת את מטריצת התווים ומעדכנת בהתאם את הכפתורים
     * @param mat 
     */
    public void updateViewBoard(char[][] mat) 
    {
        for (int row = 0; row < Client.BOARD_ROW; row++)
        {
            for (int col = 0; col < Client.BOARD_COL; col++)
            {
                if (mat[row][col] == 'b')
                {
                    viewBoard[row][col].setIcon(Client.icons[1]);
                    viewBoard[row][col].setDisabledIcon(Client.icons[1]);
                }
                if (mat[row][col] == 'w')
                {
                    viewBoard[row][col].setIcon(Client.icons[2]);
                    viewBoard[row][col].setDisabledIcon(Client.icons[2]);
                }
                if (mat[row][col] == 's')
                {
                    viewBoard[row][col].setIcon(Client.icons[3]);
                    viewBoard[row][col].setDisabledIcon(Client.icons[3]);
                }
                if (mat[row][col] == 'e')
                {
                    viewBoard[row][col].setIcon(Client.icons[4]);
                    viewBoard[row][col].setDisabledIcon(Client.icons[4]);
                }
            }
        }
    }

    /**
     *  עדכון מספר הכדורים שנהדפו החוצה
     * @param currentPlayer
     * @param number 
     */
    public void popedBallUpdate(char currentPlayer, int number) {
        if (currentPlayer == 'w')
        {
            lblBlackCounter.setText("Black:" + number + " ");
        } else
        {
            lblWhiteCounter.setText(" White:" + number);
        }
    }
    
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
