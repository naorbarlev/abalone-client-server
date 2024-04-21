import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *  Document   : Server
 *  Created on : 17/10/2018
 *  Author     : Naor Bar-Lev
 */
public class Server implements Serializable
{
    private static JFrame win;
    static int NUM_TO_POP = 1; //מספר הכדורים שנדרש להוציא כדי לנצח
    static String SERVER_IP; //כתובת השרת
    static int SERVER_PORT; // הפורט שהשרת ישב בו
    static JTextArea logArea; //איזור הודעות השרת
    static ArrayList<ClientData> clientsList; //רשימת כל הלקוחות המחוברים
    static ArrayList<Game> gamesList; //רשימת המשחקים המשוחקים
    static State basicState; // מצב משחק ראשוני
    
    public static void main(String[] args) 
    {
        try
        {
            SERVER_IP = InetAddress.getLocalHost().getHostAddress(); // get thelocal computer IP
        } 
        catch (Exception ex)
        {
            printStackTrace(ex);
        }
        SERVER_PORT = 1234;
        createServerGUI();
        runTheServer();
    }
   
    /**
     * הפונקציה מאיזנה ללקוחות שמתחברים לשרת ויוצרת שקע מולם
     */
    private static void runTheServer() 
    {
        try
        {
            clientsList = new ArrayList<>();
            gamesList = new ArrayList<>();
            basicState = new State();
            // יצירת שקע שרת להאזנה וקבלת לקוחות
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            log("SERVER runs on [ " + SERVER_IP + " : " + SERVER_PORT + " ]\n");
        
            // לולאה לקבלת לקוחות
            while (true)
            {
                handleClient(serverSocket.accept());
            }
        }
        catch(Exception e)
        {
            printStackTrace(e);
        }
    }
      
    /**
     * הפונקציה מנהלת את הלקוחות שהתחברו 
     * מנהלת את המשחק בין הזוגות השונים 
     *  מטפלת ביציאה של שחקן מהמשחק
     * @param clientSocket 
     */
    private static void handleClient(Socket clientSocket) 
    {
        log("New client connected !");
        eventHandler();
        Thread t1 = new Thread(new Runnable()
        {
            ClientData client = null;
            Game game = null;
            boolean guest = false;
            @Override
            public void run() 
            {
                try
                {
                    eventHandler();
                    client = new ClientData(null, clientSocket);
                    boolean isHavePartner, isLoginOK = false , isRightPlayer = false;
                    
                    String un = "guest" , pw = "";
                    
                    while (!isLoginOK)
                    {
                        
                        while(!isRightPlayer)
                        {
                            String data = (String)client.readObj(); 
                            if (data.equals(Constants.GUEST_PLAYER))
                            {
                                //אם היה נסיון כושל להתחברות כשחקן רשום שם המשתמש יוחזר לאורח
                                un = "guest";
                                guest = true;
                                isRightPlayer = true;
                            }

                            if (data.equals(Constants.SIGNED_PLAYER))
                            {
                                //קריאת נתונים מהלקוח
                                un = (String) client.readObj();
                                pw = (String) client.readObj();
                                //האם יש נסיון להתחבר עם משתמש מחובר כבר
                                if (DB.isUserInDB(un, pw) && isAlreadyConnected(un))
                                {
                                    isRightPlayer = false;
                                    client.sendString(Constants.ALREADY_EXIST);
                                }
                                else
                                    isRightPlayer = true;
                            } 
                        }
                        
                        //בדיקה האם המשתמש קיים במסד הנתנים ואם הוא לא מחובר כבר
                        if ( guest || (DB.isUserInDB(un, pw) && isAlreadyConnected(un)) == false )
                        {
                            client.setUserName(un);
                            isLoginOK = true;
                            client.sendString(Constants.CLIENT_EXSIT);//שליחת תשובה ללקוח
                            clientsList.add(client);
                            log("Welcome " + un + " you can access..\n");
                            isHavePartner = isHavePartner(client); // בדיקה האם יש למשתמש פרטנר
                            if (isHavePartner)
                            {
                                log("We are ready to play!\n");
                                client.getPartner().sendString(Constants.HAVE_PARTNER);
                                client.sendString(Constants.NEW_PARTNER);

                                client.getPartner().sendString(Constants.WHITE_PLAYER);
                                client.sendString(Constants.BLACK_PLAYER);

                                client.getPartner().setPlayer('w');
                                client.setPlayer('b');

                                //הלקוח שהתחבר ראשון הוא זה שיתחיל את המשחק 
                                client.getPartner().sendString(Constants.YOUR_TURN);
                                client.sendString(Constants.WAIT_FOR_TURN);
                                
                                
                                //יצירת משחק והוספה לרשימת המשחקים המשוחקים
                                game = new Game(client, client.getPartner());
                                gamesList.add(game);
                                client.setClientGame(game);
                                client.getPartner().setClientGame(game);
                            }
                            else
                            {
                                log(un + " you don't have a partner please wait for \n another player to connect..\n");
                                // עדכון הלקוח הראשון שאין לו פרטנר
                                client.sendString(Constants.NO_PARTNER); 
                            }
                        }
                        else 
                        {
                            //תשובה ללקוח שיש כבר לקוח מחובר בשם המשתמש
                            if (isAlreadyConnected(un)) 
                            {
                                isLoginOK = false;
                                log("This client is already online...\n");
                                client.sendString(Constants.ALREADY_EXIST);
                            } 
                            else
                            {
                                isLoginOK = false;
                                client.sendString(Constants.NOT_EXIST);
                            }
                        }
                    }
                    
                    while(true)
                    {
                        String clientMsg = (String)client.readObj();
                        if(client.getPartner() != null)
                        {
                            //קבלה מהלקוח שהוא בצע מהלך
                            if (clientMsg.equals(Constants.MOVE_COMPLETED))
                            {
                                //שליחה לשחקן שתורו שישלח את הלוח המעודכן
                                if (client.getPlayer() == client.getClientGame().getPlayerTurn())
                                {
                                    client.sendString(Constants.SEND_BOARD);
                                } 
                                else
                                {
                                    client.getPartner().sendString(Constants.SEND_BOARD);
                                }
                                //קריאת לוח מהלקוח
                                client.getClientGame().setCurrentState(new State((State)client.readObj()));
                                log("State arrived from "+client.getUserName());
                                log(" ");
                                
                                //שליחת הלוח המעודכן לפרטנר
                                if (client.getPlayer() == client.getClientGame().getPlayerTurn())
                                {
                                    log("update state sent to "+ client.getPartner().getUserName());
                                    log(" ");
                                    client.getPartner().sendString(Constants.CATCH_BOARD);
                                    client.getPartner().sendState(client.getClientGame().getCurrentState());
                                }
                                else
                                {
                                    log("update state sent to "+client.getUserName());
                                    log(" ");
                                    client.sendString(Constants.CATCH_BOARD);
                                    client.sendState(client.getClientGame().getCurrentState());
                                }

                                //החלפת תור השחקן
                                if (client.getClientGame().getPlayerTurn() == 'w')
                                    client.getClientGame().setPlayerTurn('b');
                                else
                                    client.getClientGame().setPlayerTurn('w');
                                
                                //שליחה לשני השחקנים מי תורו ומי לא
                                if (client.getPlayer() == client.getClientGame().getPlayerTurn())
                                {
                                    client.sendString(Constants.YOUR_TURN);
                                    client.getPartner().sendString(Constants.WAIT_FOR_TURN);
                                } 
                                else
                                {
                                    client.getPartner().sendString(Constants.YOUR_TURN);
                                    client.sendString(Constants.WAIT_FOR_TURN);
                                }
                            }
                                //בדיקה האם הסתיים המשחק
                                if(client.getClientGame().getCurrentState().isGameOver())
                                {
                                    if(client.getClientGame().getCurrentState().getBlackPop() == NUM_TO_POP)
                                    {
                                        if(client.getPlayer() == 'w')
                                        {
                                            DB.updateNumOfWins(client.getUserName());
                                            client.sendString(Constants.YOU_WIN);
                                            client.getPartner().sendString(Constants.YOU_LOST);
                                        }
                                            
                                        if(client.getPlayer() == 'b')
                                        {
                                            DB.updateNumOfWins(client.getPartner().getUserName());
                                            client.sendString(Constants.YOU_LOST);
                                            client.getPartner().sendString(Constants.YOU_WIN);
                                        }
                                        
                                    }
                                    if(client.getClientGame().getCurrentState().getWhitePop()== NUM_TO_POP)
                                    {
                                        if(client.getPlayer() == 'w')
                                        {
                                            DB.updateNumOfWins(client.getPartner().getUserName());
                                            client.sendString(Constants.YOU_LOST);
                                            client.getPartner().sendString(Constants.YOU_WIN);
                                        }
                                            
                                        if(client.getPlayer() == 'b')
                                        {
                                            DB.updateNumOfWins(client.getUserName());
                                            client.sendString(Constants.YOU_WIN);
                                            client.getPartner().sendString(Constants.YOU_LOST);
                                        }
                                    }
                                    client.sendString(Constants.GAME_OVER);
                                    client.getPartner().sendString(Constants.GAME_OVER);
                                    log("game over - ("+client.getUserName()+","+client.getPartner().getUserName()+")");
                                    log(" ");
                                    client.getClientGame().setPlayerTurn('w');
                                    client.getClientGame().setCurrentState(basicState);
                                    //התחלה של משחק חדש
                                    if (client.getPlayer() == client.getClientGame().getPlayerTurn())
                                    {
                                        client.sendString(Constants.YOUR_TURN);
                                        client.getPartner().sendString(Constants.WAIT_FOR_TURN);
                                    } 
                                    else
                                    {
                                        client.getPartner().sendString(Constants.YOUR_TURN);
                                        client.sendString(Constants.WAIT_FOR_TURN);
                                    }
                                }
                        }
                        
                        //טיפול בבקשה לסגירת המשחק
                        if (clientMsg.equals(Constants.EXIT))
                        {
                            if (client.getPartner() != null)
                            {
                                client.getPartner().sendString(Constants.YOUR_PARTNER_EXIT);
                                // כדי שלא נכנס שוב פעם לתנאי ונשלח שוב לשרת
                                client.getPartner().setPartner(null); 
                            }
                            log(client.getUserName() + " has left the game\n");
                            client.sendString(Constants.BYE);
                            gamesList.remove(client.getClientGame());
                            clientsList.remove(client);
                            return;
                        }
                    }
                }
                catch (Exception ex)
                {
                    printStackTrace(ex);
                } 
            }
        });
        t1.start();
    }
    /**
     * בודקת האם יש לקוח כבר שמחובר בשם המשתמש שהוזן
     * @param un
     * @return 
     */
    public static boolean isAlreadyConnected(String un)
    {
        for (int i = 0; i < clientsList.size(); i++)
        {
            if(clientsList.get(i).getUserName().equals(un))
                return true;
        }
        return false;
    }
    
    /**
     * חלון הודעות השרת
     */
    private static void createServerGUI()
    {
        
        win = new JFrame("Server Log");
        win.setSize(360, 400);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setAlwaysOnTop(true);
        win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);
        win.add(new JScrollPane(logArea), BorderLayout.CENTER);
        win.setLocationRelativeTo(null);
        win.setVisible(true);
    }

    /**
     * הדפסת מחרוזת ללוג השרת
     * @param str 
     */
    public static void log(String str)
    {
        logArea.append(" " + str);
        logArea.append("\n");
        logArea.setCaretPosition(logArea.getText().length());
    }
    
    /**
     * הפונקציה מקבלת לקוח ומעדכנת את השדה של הפרטנר שחו אם יש לו זוג 
     * מחזירה אמת אם כן ושקר אם לא
     * @param client
     * @return 
     */
    public static boolean isHavePartner(ClientData client)
    {
        int index = clientsList.indexOf(client);
         
        //אם האינדקס הוא אי זוגי בהכרח שיש לו פרטנר
        if (index % 2 != 0)
        {
            client.setPartner(clientsList.get(index - 1));
            clientsList.get(index - 1).setPartner(client);
            return true;
        }
        //אם האינקס זוגי והוא לא האחרון ברשימה בהכרח שיש לו פרטנר
        if (index % 2 == 0 && index != clientsList.size() - 1)
        {
            client.setPartner(clientsList.get(index + 1));
            clientsList.get(index + 1).setPartner(client);
            return true;
        }
        return false;
    }
    
     /**
      * מקפיצה חלון עם חריגות שנזרקו
      * @param ex 
      */
    public static void printStackTrace(Exception ex)
    {
        JOptionPane.showMessageDialog(null, "Server\n"+ex.toString() + "\n"+ ex.getCause().toString());
    }

    /**
     * מטפלת באירוע סגירת שרת
     */
    private static void eventHandler() 
    {
        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) 
            {
                for (int i = 0; i < clientsList.size(); i++)
                {
                    try
                    {
                        clientsList.get(i).sendString(Constants.SERVER_FALL);
                    } 
                    catch (IOException ex)
                    {
                        printStackTrace(ex);
                    }
                }
                
                win.dispose();
                System.exit(0);
            }
        });
    }

    
}