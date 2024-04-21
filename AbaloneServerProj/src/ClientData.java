/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Document ClientData
 * @author Naor Bar Lev
 */
public class ClientData implements Serializable
{
    private String userName;//שם משתמש של הלקוח
    private Socket socket;//שקע התקשורת עם הלקוח
    private ClientData partner = null;//היריב שיהיה ללקוח
    private char player;//תו ההשחקן שייצג את צבעו
    private final ObjectOutputStream objectOutputStream;//ערוץ הוצאת נותנים
    private final ObjectInputStream objectInputStream;//ערוץ קבלת נתונים
    private Game clientGame;//משחק הלקוח יהיה שותף בו
    
    /**
     * פעולה בונה
     * @param userName
     * @param socket
     * @throws IOException 
     */
    public ClientData(String userName , Socket socket) throws IOException
    {
        this.userName = userName;
        this.socket = socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectOutputStream.flush();
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * מחזירה את המשחק שהלקוח נמצא בו
     * @return 
     */
    public Game getClientGame() {
        return clientGame;
    }
    /**
     * מציבה לשחקן מסויים משחק
     * @param clientGame 
     */
    public void setClientGame(Game clientGame) {
        this.clientGame = clientGame;
    }
    /**
     * מחזירה את התו שהשחקן מייצג
     * @return 
     */
    public char getPlayer() {
        return player;
    }
    /**
     * מציבה לשחקן תו המייצג את צבעו
     * @param player 
     */
    public void setPlayer(char player) {
        this.player = player;
    }
    /**
     * מחזירה את שם המשתמש של השחקן
     * @return 
     */
    public String getUserName() {
        return userName;
    }
    /**
     * מציבה שם משתמש 
     * @param nickName 
     */
    public void setUserName(String nickName) {
        this.userName = nickName;
    }
    /**
     * מחזירה את היריב של לקוח
     * @return 
     */
    public ClientData getPartner() 
    {
        return partner;
    }
    /**
     * מציבה יריב ללקוח
     * @param partner 
     */
    public void setPartner(ClientData partner)
    {
        this.partner = partner;
    }
    /**
     * שליחת מצב משחק ללקוח
     * @param s
     * @throws IOException 
     */
    public void sendState(State s) throws IOException
    {
        objectOutputStream.writeObject(s);
        objectOutputStream.flush();
    }
    /**
     * שליחת מחרוזת ללקוח
     * @param s
     * @throws IOException 
     */
     public void sendString(String s) throws IOException
    {
        objectOutputStream.writeUTF(s);
        objectOutputStream.flush();
    }
    /**
     * קריאה של אובייקט
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public Object readObj() throws IOException, ClassNotFoundException
    {
       return objectInputStream.readObject();
    }
    /**
     * נמצבעת סגירה של ערוצי התקשורת והשקע
     */
    public void disconnect() 
    {
        try
        {
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
            System.out.println("Client disconnect!");
        }
        catch(Exception e)
        {
            Server.printStackTrace(e);
        }
        
    }
}
