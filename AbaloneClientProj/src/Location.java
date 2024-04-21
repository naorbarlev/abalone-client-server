
import java.io.Serializable;
/**
 * Document : Location
 * Created on : 21/02/2018
 * Author : Naor Bar-Lev
 */
public class Location implements Serializable {
    // תכונות
    private int row;    //מייצג שורה
    private int col;    //מייצג עמודה

    /**
     * פעולה בונה
     * @param row
     * @param col 
     */
    public Location(int row, int col) // פרמטרים
    {
        this.row = row;
        this.col = col;
    }

    /**
     * copy constrctor פעולה בונה מעתיקה
     * @param l 
     */
    public Location(Location l) // פרמטרים
    {
        this.row = l.row;
        this.col = l.col;
    }

    /**
     * מחזירה  שורה
     * @return 
     */
    public int getRow() {
        return row;
    }

    /**
     * מחזירה עמודה
     * @return 
     */
    public int getCol() {
        return col;
    }

    /**
     * מדפיסה את המיקום
     * @return 
     */
    public String toString() {
        return "Location{" + "row=" + row + ", col=" + col + '}';
    }


}