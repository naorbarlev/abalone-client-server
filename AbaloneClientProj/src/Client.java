
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Document : Client
 * Created on : 17/10/2018 
 * Author : Naor Bar-Lev
 * המחלקה מייצגת לקוח מכילה את גרפיקת המשחק ומטפלת בפקודות מהשרת
 */
public class Client implements Serializable {

    private String serverIP; //כתובת השרת
    private int serverPort; //שקע השרת
    private Socket socket; //שקע שיווצר אצל הלקוח
    private ObjectInputStream objectInputStream; //ערוץ קבלת מידע
    private ObjectOutputStream objectOutputStream; // ערוץ הוצאת מידע
    private boolean isConected; // אם הלקוח מחובר

    //קבועים
    public static final int BOARD_ROW = 9;
    public static final int BOARD_COL = 17;
    public static final int BUTTON_SIZE = 38;
    public static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.BOLD, 30);
    public static String GAME_TITLE = "";

    public int numOfClicks = 0; //מספר בחירות הכדורים להזזה
    public Location firstLoc,secondLoc,thirdLoc,forthLoc; // מיקומי הבחירות של השחקן
    public ArrayList<Location> loc;
    public Location dest; // מיקום יעד ההזזה
    public int res = 0; //תשובה האם המהלך התבצע או לא

    private Model model; // לוח המשחק הלוגי
    private View view; //GUI

    public static final ImageIcon[] icons = new ImageIcon[5];  // collection of all images 

    /**
     * טוענת את כל מקורות המידע יוצרת את לוח המודל ואת הלוח המשחק הפיזי
     */
    public Client() {
        loadResources();
        model = new Model();
        view = new View();
        loc = new ArrayList();
        setGameEventHandlers();
    }

    /**
     * יוצרת את החיבור לשקעים עם השרת ומחכה להודעות מהשרת ומטפלת בהם
     */
    private void connect() 
    {
        getServerAddress();
        // יצירת חיבור שקע לשרת
        try
        {
            socket = new Socket(serverIP, serverPort);
        }
        catch(Exception ex)
        {
            jframeCountDown(" can't find sever exit in ",3);
            System.exit(0); // סגירת האפליקציה
        }
        
        try
        {
            // פתיחת ערוצי הזרמת נתונים מהלקוח לשרת וההפך
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            isConected = true;
            // שליחת נתון לשרת
            loginDialog();
            while (isConected)
            {
                String data = (String) readString();
                checkDataFromServer(data);
            }
        }
        catch(Exception ex)
        {
            printStackTrace(ex);
        }
        

    }
    /**
     * שליחת מצב לוח המחש לשרת
     * @param s 
     */
    public void sendState(State s) 
    {
        try
        {
            objectOutputStream.writeObject(s);
            objectOutputStream.flush();
        }
        catch(Exception ex)
        {
            printStackTrace(ex);
        }
        
    }
    /**
     * שליחת מחרוזת לשרת
     * @param s 
     */
    public void sendString(String s) 
    {
        try
        {
            objectOutputStream.writeObject(s);
            objectOutputStream.flush();
        }
        catch(Exception ex)
        {
            printStackTrace(ex);
        }
        
    }
    /**
     * קורא אובייקט מהשרת
     * @return 
     */
    public String readString()  
    {
        try
        {
           return objectInputStream.readUTF();
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
        return null;
    }
    /**
     * קורא מצב משחק מהשרת
     * @return 
     */
    public State readState()
    {
        try
        {
            return (State) objectInputStream.readObject();
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
        return null;
    }

    /**
     * מטפלת באירועי המשחק
     */
    private void setGameEventHandlers() {
        view.getWin().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int res = JOptionPane.showConfirmDialog(view.getWin(), "Do you want to exit?", "Exit App", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        sendString(Constants.EXIT);
                    } catch (Exception ex)
                    {
                        printStackTrace(ex);
                    }

                }

            }
        });

        view.getMenuBar().getMenu(0).getItem(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(view.getWin(), "- Each ball can only move one space.\n You can move your balls in any of the \n six directionsof the game hexagon and \n may move 1,2 or 3 balls as follows: \n  1 ball can be moved onto an \n an empty adjacent space ;\n  2 or 3 contigusus , aligned balls can be \n moved as a group. They must all move togther and in the same direction. \n\n - SUMITO  : Pushing your opponet:\n You can push your opponet's balls\n whenever you are in a position of numerical\n superioity. If you both have the same\n number of balls , a sumitu is not possible.\n\n - The first player to eject 6 opposing\n wins the game.  ", "How to play", 3);

            }
        });
        
        view.getMenuBar().getMenu(0).getItem(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(view.getWin(), "Copyright © 2018 By Naor Bar - Lev \n All Rights Reserved.", "About the programer", 3);
            }
        });

        view.getMenuBar().getMenu(0).getItem(2).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // טיפול באירוע לחיצה על כפתור סגירת החלון
                int res = JOptionPane.showConfirmDialog(view.getWin(), "Do you realy want to exit?", "Exit App", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        sendString(Constants.EXIT);
                    } 
                    catch (Exception ex)
                    {
                        printStackTrace(ex);
                    }
                }
            }
        });
        
        for (int row = 0; row < BOARD_ROW; row++)
        {
            for (int col = 0; col < BOARD_COL; col++)
            {
                view.getBoardButtons()[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int row = (int) ((JButton) e.getSource()).getClientProperty("rowIndex");
                        int col = (int) ((JButton) e.getSource()).getClientProperty("colIndex");
                        try
                        {
                            res = makeMove(row, col);
                        } 
                        catch (Exception ex)
                        {
                            printStackTrace(ex);
                        }
                        if (res == 1)
                        {
                            try
                            {
                                sendString(Constants.MOVE_COMPLETED);
                            } 
                            catch (Exception ex)
                            {
                                printStackTrace(ex);
                            }
                        }
                    }
                });
            }
        }

    }

    /**
     * מקבלת מיקום ובודקת האם הוא תקין מחזירה אחד אם המהלך התבצע ואפס אם לא בוצע עדיין
     * @param row
     * @param col
     */
    public Integer makeMove(int row, int col) 
    {
        try
        {
            char currentPlayer = model.getCurrentPlayer();
            if (checkLocs(row, col))
            {
                //dest = new Location(row, col);
                if (model.getCurrentState().getValueAt(row, col) == 'e' || (!loc.isEmpty() && model.getCurrentState().getValueAt(row, col) == model.getOtherPlayer(currentPlayer)))
                {
                    if (model.playerMove(loc, row, col))
                    {
                        view.updateViewBoard(model.getCurrentState().getModelBoard());
                        popedBallUpdate(currentPlayer);
                        loc.clear();
                        numOfClicks = 0;
                        return 1; // המהלך בוצע בהצלחה
                    } else
                    {
                        return 0; // המהלך לא בוצע עדיין
                    }
                } else
                {
                    return 0; // המהלך לא בוצע עדיין
                }

            }
            return 0; // המהלך לא בוצע עדיין
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
       return null;
    }
    /**
     *  מקבלת את כתובת השרת מהלקוח
     */
    private void getServerAddress() 
    {
        try
        {
            serverIP = getServerIpFromUser();//InetAddress.getLocalHost().getHostAddress();
            serverPort = 1234;   
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
        
    }
    
    
    
    

    /**
     * הפונקציה שומרת את מיקומי הלחיצות ברשימה ומסמנת אותם באדום ובודקת האם הם
     * חוקיים אם הם לא חוקיים הפונקציה מחזירה למצב הקודם את לוח הכפתורים וגם את
     * לוח התווים
     * @param row
     * @param col
     * מחזירה אמת אם הרצף נכון ושקר אם לא
     */
    public boolean checkLocs(int row, int col) {
        char player = model.getCurrentPlayer();
        if (model.getCurrentState().getValueAt(row, col) != 'e' && model.getCurrentState().getValueAt(row, col) == player)
        {

            numOfClicks++;
            if (numOfClicks == 1)
            {
                if (model.getCurrentState().getValueAt(row, col) == player)
                {
                    firstLoc = new Location (row,col);
                    loc.add(new Location(firstLoc.getRow(), firstLoc.getCol()));
                    model.getCurrentState().updateModelBoard(firstLoc.getRow(), firstLoc.getCol(), 's');
                    view.updateViewBoard(model.getCurrentState().getModelBoard());
                    return true;
                } else
                {
                    return false;
                }
            }

            if (numOfClicks == 2)
            {
                if (model.getCurrentState().getValueAt(row, col) == player)
                {
                    secondLoc = new Location(row, col);
                    loc.add(new Location(secondLoc.getRow(), secondLoc.getCol()));
                    //בדיקת אלכסון או שורה
                    if (model.getCurrentState().isRowOrCross(loc, dest))
                    {
                        model.getCurrentState().updateModelBoard(secondLoc.getRow(), secondLoc.getCol(), 's');
                        view.updateViewBoard(model.getCurrentState().getModelBoard());
                        return true;
                    } //לחיצה שלא יוצרת רצף מסויים מחזיר את הכפתור למצבו הקודם
                    else
                    {

                        model.getCurrentState().restoreLocs(loc, player);
                        view.updateViewBoard(model.getCurrentState().getModelBoard());
                        numOfClicks = 0;
                        loc.clear();
                        return true;

                    }

                } else
                {
                    return false;
                }
            }

            if (numOfClicks == 3)
            {
                if (model.getCurrentState().getValueAt(row, col) == player)
                {
                    thirdLoc = new Location(row, col);
                    loc.add(new Location(thirdLoc.getRow(), thirdLoc.getCol()));
                    if (model.getCurrentState().isRowOrCross(loc, dest))
                    {
                        model.getCurrentState().updateModelBoard(thirdLoc.getRow(), thirdLoc.getCol(), 's');
                        view.updateViewBoard(model.getCurrentState().getModelBoard());
                        return true;
                    } //לחיצה שלא יוצרת רצף מסויים מחזיר את הכפתור למצבו הקודם
                    else
                    {
                        model.getCurrentState().restoreLocs(loc, player);
                        view.updateViewBoard(model.getCurrentState().getModelBoard());
                        numOfClicks = 0;
                        loc.clear();
                        return true;
                    }

                } else
                {
                    return false;
                }

            }

            if (numOfClicks == 4)
            {
                if (model.getCurrentState().getValueAt(row, col) == 'b' || model.getCurrentState().getValueAt(row, col) == 'w')
                {
                    //לחיצה שלא יוצרת רצף מסויים מחזיר את הכפתור למצבו הקודם
                    model.getCurrentState().restoreLocs(loc, player);
                    view.updateViewBoard(model.getCurrentState().getModelBoard());
                    numOfClicks = 0;
                    loc.clear();
                    return true;

                } else
                {
                    return false;
                }
            }

        }
        return true;
    }
    /**
     *מנתקת לקוח משרת על ידי סגירת ערוצים ושקע
     */
    private void disconnect() 
    {
        try
        {
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        }catch(Exception e)
        {
            printStackTrace(e);
        }
        
    }
    /**
     * סוגר לקוח ויוצא מהאפליקציה
     */
    private void exit()  
    {
        try
        {
            disconnect();
            view.getWin().dispose();
            System.exit(0); // סגירת האפליקציה
        }
        catch (Exception e)
        {
            printStackTrace(e);
        }
        

    }
    /**
     * מאתחל את לוח המודל ואת לוח הכפתורים
     */
    public void newGame() {
        model.init();
        view.init();
        view.setVisible(true);
        popedBallUpdate(model.getCurrentPlayer());
        popedBallUpdate(model.getOtherPlayer(model.getCurrentPlayer()));
        numOfClicks = 0;
        loc.clear();

    }
    /**
     * מקבלת משחק ןמעדכת את הכדורים שנדחפו החוצה
     * @param currentPlayer 
     */
    public void popedBallUpdate(char currentPlayer) {
        if (currentPlayer == 'w')
        {
            view.popedBallUpdate(currentPlayer, model.getCurrentState().getBlackPop());
        } else
        {
            view.popedBallUpdate(currentPlayer, model.getCurrentState().getWhitePop());
        }
    }

    public static void main(String[] args) 
    {
        try
        {
            Client client = new Client();
            client.connect();
        }
        catch(Exception e)
        {
            jframeCountDown(" Server does not responed ", 7);
            printStackTrace(e);
            System.exit(0);
            
        }
       
    }
    /**
     * הפונקציה מבצעת את התהליך ההתחברות למשחק
     */
    public void loginDialog(){

        // שדות קלט עבור קליטת נתונים
        JLabel errLabel = new JLabel("");          // להצגת הודעת שגיאה
        errLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errLabel.setForeground(Color.RED);

        JTextField unField = new JTextField("naor");   // לקליטת שם המשתמש
        unField.setForeground(Color.BLUE);
        unField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        
        JTextField pwField = new JTextField("1111");   // לקליטת הסיסמה
        pwField.setForeground(Color.BLUE);
        pwField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        
        
        Object[] inputFields =
        {
            errLabel,
            " ",
            "Enter user name",
            unField,
            " ",
            "Enter password (4 digits)",
            pwField,
            " ",
            
        };

        boolean isLoginOK = false;
        int option;
        String un = "", pw = "";

        while (!isLoginOK)
        {
            UIManager.put("OptionPane.cancelButtonText", " Play as guest");
            UIManager.put("OptionPane.okButtonText", "Log in"); 
            option = JOptionPane.showConfirmDialog(null, inputFields, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option == JOptionPane.CANCEL_OPTION)//מקביל להתחברות כאורח
            {
                sendString(Constants.GUEST_PLAYER);
                String dataFromServer = readString();
                if (dataFromServer.equals(Constants.CLIENT_EXSIT))//בדיקה האם השרת החזיר תשובה חיובית
                {
                    newGame();
                    un = "Guest";
                    view.setTitle(un + "'s Abalone Board Game");
                    isLoginOK = true;
                    String isHavePartner = readString();
                    checkDataFromServer(isHavePartner);
                }
            }
            if (option == JOptionPane.OK_OPTION)//מקביל להתחברות כלקוח רשום
            {
                sendString(Constants.SIGNED_PLAYER);
                un = unField.getText();
                pw = pwField.getText();

                // send un & pw to server to check in DB
                sendString(un);
                sendString(pw);
                // קבלת נתון מהשרת
                String dataFromServer = readString();

                if (dataFromServer.equals(Constants.CLIENT_EXSIT))//בדיקה האם השרת החזיר תשובה חיובית
                {
                    newGame();
                    view.setTitle(un + "'s Abalone Board Game");
                    isLoginOK = true;
                    String isHavePartner = readString();
                    checkDataFromServer(isHavePartner);
                } 
                else 
                    if (dataFromServer.equals(Constants.NOT_EXIST))
                {
                    isLoginOK = false;
                    errLabel.setText("User Name or Password incorrct! try again...");
                }
                else 
                    if (dataFromServer.equals(Constants.ALREADY_EXIST))
                    {
                        isLoginOK = false;
                        errLabel.setText("User already exsit! try again...");
                    }

            }
            
        }
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.okButtonText", "Ok"); 
    }

    /**
     * הפונקציה מפענחת את הודעת השרת ומטפלת בהתאם
     * @param data
     */
    public void checkDataFromServer(String data)
    {
        try
        {
            switch (data)
            {
                case Constants.HAVE_PARTNER: //התקבל זוג ללקוח הראשון
                    view.setMessage(model.toString() + "You have a partner , let's start playing !");
                    Thread.sleep(1500);
                    view.setMessage(model.toString() + "You are starting , your color is white !");
                    break;
                
                case Constants.NO_PARTNER:
                    view.setMessage("Please wait for another player..");
                    view.setAllBtnDisable(false);
                    break;
                
                case Constants.BYE:
                    jframeCountDown(" Game will be closing in ", 3);
                    exit();
                    break;
                
                case Constants.NEW_PARTNER: //לקוח חדש התחבר
                    view.setMessage("You have a partner , let's start playing !");
                    Thread.sleep(1500);
                    view.setMessage("Please wait for your turn , your color is black !");
                    view.setAllBtnDisable(false);
                    break;
                
                case Constants.YOUR_TURN:
                    Thread.sleep(250);
                    view.setMessage(model.toString() + " It is your turn , please make a move!");
                    view.setAllBtnDisable(true);
                    break;
                
                case Constants.WAIT_FOR_TURN:
                    Thread.sleep(250);
                    view.setMessage(model.toString() + " You need to wait for your opponent move!");
                    view.setAllBtnDisable(false);
                    break;
                
                case Constants.CATCH_BOARD:
                    State updatedState = new State(readState());
                    model.setState(updatedState);
                    view.updateViewBoard(updatedState.getModelBoard());
                    popedBallUpdate(model.getOtherPlayer(model.getCurrentPlayer()));
                    break;
                
                case Constants.SEND_BOARD:
                    sendState(model.getCurrentState());
                    break;
                
                case Constants.YOUR_PARTNER_EXIT:
                    sendString("exit");
                    break;
                case Constants.GAME_OVER:
                    view.setAllBtnDisable(false);
                    newGame();
                    break;
                case Constants.YOU_WIN:
                    view.setMessage(" You win, game is over!");
                    jframeCountDown(" You win, new game starts in ", 7);
                    break;
                case Constants.YOU_LOST:
                    view.setMessage(" You lost, game is over!");
                    jframeCountDown(" You lost, new game starts in ", 7);
                    break;
                case Constants.BLACK_PLAYER:
                    model.setCurrentPlayer('b');
                    break;
                case Constants.WHITE_PLAYER:
                    model.setCurrentPlayer('w');
                    break;
                case Constants.SERVER_FALL:
                    jframeCountDown(" Server falls exit in ", 3);
                    exit();
                    break;
            }            
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
        
    }
    /**
     *  טעינת משאבי המשחק כגון תמונות קבצי קול ועוד
     */
    private void loadResources() {
        ImageIcon img = new ImageIcon(Client.class.getResource("resources/abaloneSplash.png"));
        icons[0] = new ImageIcon(img.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH));
        //שחור
        img = new ImageIcon(Client.class.getResource("resources/black.png"));
        icons[1] = new ImageIcon(img.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
        //לבן
        img = new ImageIcon(Client.class.getResource("resources/white.png"));
        icons[2] = new ImageIcon(img.getImage().getScaledInstance(BUTTON_SIZE , BUTTON_SIZE , Image.SCALE_SMOOTH));
        //בחירה
        img = new ImageIcon(Client.class.getResource("resources/selection.png"));
        icons[3] = new ImageIcon(img.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
        //מקום ריק
        img = new ImageIcon(Client.class.getResource("resources/grey.png"));
        icons[4] = new ImageIcon(img.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));

        // load more ressources...
    }
    /**
     * מקפיצה חלון שסופר שלוש שניות ומתחיל משחק חדש
     */
    public static void jframeCountDown(String s,int seconds)
    {
        try
        {
            JFrame GameOverFrame = new JFrame();
            GameOverFrame.setPreferredSize(new Dimension(425, 100));

            JLabel lbl = new JLabel(s);
            lbl.setForeground(Color.black);
            lbl.setFont(new Font(Font.MONOSPACED, Font.BOLD, 22));
            lbl.setVerticalAlignment(SwingConstants.CENTER);
            lbl.setBackground(Color.gray);
            lbl.setSize(new Dimension(150, 100));

            GameOverFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            GameOverFrame.add(lbl, BorderLayout.CENTER);
            GameOverFrame.setLocationRelativeTo(null);
            GameOverFrame.pack();
            GameOverFrame.setVisible(true);
            for (int i = seconds; i > 0; i--)
            {
                int x = 1000;
                lbl.setText(s + i * 1000 / x + "!");
                Thread.sleep(x);
            }
            GameOverFrame.dispose();
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
        
    }

    /**
     * מקפיצה חלון עם החריגה שנזרקה
     * @param ex 
     */
    public static void printStackTrace(Exception ex)
    {
        JOptionPane.showMessageDialog(null, "Client\n"+ex.toString() + "\n"+ ex.getCause().toString());
    }

    /**
     * חלון להזנת כתובת השרת
     * @return 
     */
    private String getServerIpFromUser() 
    {
        try
        {
            int option;
            // שדות קלט עבור קליטת נתונים
            JTextField ipField = new JTextField(InetAddress.getLocalHost().getHostAddress());
            ipField.setForeground(Color.BLUE);
            ipField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));

            Object[] inputFields =
            {
                "Enter Server IP",
                ipField,
            };

            option = JOptionPane.showConfirmDialog(null, inputFields, "Server's IP Adrress", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION)
            {
                return ipField.getText();
            }
            return null;
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
        return null;
    }
    
}
