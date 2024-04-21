///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//
//import java.awt.*;
//import java.awt.geom.*;
//import javax.swing.*;
//import java.awt.event.*;
//
//public class RoundButton1 extends JButton {
//
//    private int row;
//    private int col;
//
//    public void setRow(int row) {
//        this.row = row;
//    }
//
//    public void setCol(int col) {
//        this.col = col;
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public int getCol() {
//        return col;
//    }
//
//    public RoundButton(String label) {
//        super(label);
//
//        setBackground(null);
//        setFocusable(true);
//
//        /*
//     These statements enlarge the button so that it 
//     becomes a circle rather than an oval.
//         */
//        Dimension size = getPreferredSize();
//        size.width = size.height = Math.max(size.width, size.height);
//        setPreferredSize(size);
//
//        /*
//     This call causes the JButton not to paint the background.
//     This allows us to paint a round background.
//         */
//        setContentAreaFilled(false);
//    }
//
//    protected void paintComponent(Graphics g) 
//    {
////        if (getModel().isArmed()) {
////            g.setColor(Color.gray);
////        } else {
////            g.setColor(getBackground());
////        }
////        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
//
//        super.paintComponent(g);
//    }
//
//    public void paintBorder(Graphics g) 
//    {
//        g.setColor(Color.darkGray);
//        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
//    }
//
//}
