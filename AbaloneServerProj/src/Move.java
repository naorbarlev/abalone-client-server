/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author naorbarlev
 */
public class Move implements Serializable{

    private ArrayList<Location> loc;
    private int row;
    private int col;

    public Move(ArrayList<Location> loc, int row, int col) {
        this.loc = new ArrayList<>(loc);
        this.row = row;
        this.col = col;
    }

    public Move(ArrayList<Location> loc) {
        this.loc = loc;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ArrayList<Location> getLocs() {
        return loc;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setLocs(ArrayList<Location> locs) {
        this.loc = locs;
    }

    @Override
    public String toString() {
        return "Move{" + "loc=" + loc + ", row=" + row + ", col=" + col + '}';
    }

}
