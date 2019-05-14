package fyp;

/*
 * Joseph Murphy, 2019
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class GUI{
  private JFrame frame;

  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUI window = new GUI();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUI(){
    initialize();
  }

  public void initialize(){
    frame = new JFrame();
    frame.setBounds(100, 100, 204, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    frame.setResizable(false);

    JButton elemCA_Button = new JButton("Elementary CA Simulator");
    JButton twoDimCA_Button = new JButton("2-D CA Simulator");
    JButton gol_Button = new JButton("Game Of Life");
    JButton backButton = new JButton("< Back");
    //JButton trafficCA_Button = new JButton("Traffic Flow Model");
    JLabel twoDimCA_Label = new JLabel("2-D CA Simulator");

    elemCA_Button.setBounds(10, 31, 184, 43);
    frame.getContentPane().add(elemCA_Button);
    elemCA_Button.setVisible(true);
    elemCA_Button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        ElemCA_GUI g = new ElemCA_GUI();
        g.setVisible();
      }
    });
    
    //trafficCA_Button.setBounds(10, 66, 184, 43);
    //frame.getContentPane().add(trafficCA_Button);
    //trafficCA_Button.setVisible(true);

    twoDimCA_Button.setBounds(10, 104, 184, 43);
    frame.getContentPane().add(twoDimCA_Button);
    twoDimCA_Button.setVisible(true);
    twoDimCA_Button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        elemCA_Button.setVisible(false);
        twoDimCA_Button.setVisible(false);
        gol_Button.setVisible(true);
        backButton.setVisible(true);
        twoDimCA_Label.setVisible(true);
      }
    });

    twoDimCA_Label.setVerticalAlignment(SwingConstants.TOP);
    twoDimCA_Label.setHorizontalAlignment(SwingConstants.LEFT);
    twoDimCA_Label.setFont(new Font("Dialog", Font.BOLD, 10));
    twoDimCA_Label.setBounds(10, 10, 184, 43);
    frame.getContentPane().add(twoDimCA_Label);
    twoDimCA_Label.setVisible(false);

    gol_Button.setBounds(10, 31, 184, 43);
    frame.getContentPane().add(gol_Button);
    gol_Button.setVisible(false);
    gol_Button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        GameOfLife_GUI g = new GameOfLife_GUI();
        g.setVisible();
      }
    });

    backButton.setBounds(10, 240, 184, 20);
    frame.getContentPane().add(backButton);
    backButton.setVisible(false);
    backButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        elemCA_Button.setVisible(true);
        twoDimCA_Button.setVisible(true);
        gol_Button.setVisible(false);
        backButton.setVisible(false);
        twoDimCA_Label.setVisible(false);
      }
    });

  }
}