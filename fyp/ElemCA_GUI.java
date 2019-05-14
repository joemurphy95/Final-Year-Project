package fyp;

/*
 * Joseph Murphy, 2019
 */

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ElemCA_GUI{
  private JFrame frame;
  private JPanel panel;
  private ElementaryCASimulator elemCA = new ElementaryCASimulator();
  private int cores = Runtime.getRuntime().availableProcessors();
  private int numGen = 60, numCells = 60, rule = 30;

  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          ElemCA_GUI window = new ElemCA_GUI();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public ElemCA_GUI(){
    initialize();
  }

  public void initialize(){
    frame = new JFrame();
    frame.setBounds(100, 100, 805, 740);
    frame.getContentPane().setLayout(null);
    frame.setResizable(false);

    panel = new JPanel();
    panel.setBackground(Color.WHITE);
    panel.setBounds(10, 42, 600, 600);
    frame.getContentPane().add(panel);

    JLabel titleLabel = new JLabel("Elementary CA Simulator");
    titleLabel.setVerticalAlignment(SwingConstants.TOP);
    titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
    titleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
    titleLabel.setBounds(12, 12, 492, 38);
    frame.getContentPane().add(titleLabel);

    JLabel ruleLabel = new JLabel("Rule:");
    ruleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    ruleLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    ruleLabel.setBounds(646, 45, 61, 20);
    frame.getContentPane().add(ruleLabel);

    JSpinner spRule = new JSpinner();
    spRule.setBounds(719, 45, 75, 20);
    spRule.setModel(new SpinnerNumberModel(rule, 0, 255, 1));
    frame.getContentPane().add(spRule);
    spRule.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        rule = (Integer)spRule.getValue(); //System.out.println(rule);
        compute();
      }
    });

    JLabel cellsLabel = new JLabel("Cells:");
    cellsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    cellsLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    cellsLabel.setBounds(646, 82, 61, 20);
    frame.getContentPane().add(cellsLabel);

    JSpinner spCells = new JSpinner();
    spCells.setBounds(719, 82, 75, 20);
    spCells.setModel(new SpinnerNumberModel(numCells, 1, null, 1));
    frame.getContentPane().add(spCells);
    spCells.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        numCells = (Integer)spCells.getValue(); //System.out.println(nCells);
        compute();
      }
    });

    JLabel genLabel = new JLabel("Gen:");
    genLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    genLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    genLabel.setBounds(646, 119, 61, 20);
    frame.getContentPane().add(genLabel);

    JSpinner spGen = new JSpinner();
    spGen.setBounds(719, 119, 75, 20);
    spGen.setModel(new SpinnerNumberModel(numGen, 1, null, 1));
    frame.getContentPane().add(spGen);
    spGen.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        numGen = (Integer)spGen.getValue(); //System.out.println(nGen);
        compute();
      }
    });

    JLabel changeRuleLabel = new JLabel("Change the rule!");
    changeRuleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    changeRuleLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    changeRuleLabel.setBounds(617, 151, 140, 24);
    changeRuleLabel.setToolTipText("Rules 18, 30, 57, 73, 161 or 225 ;)");
    frame.getContentPane().add(changeRuleLabel);

  }

  public void setVisible(){
    frame.setVisible(true);
  }

  public void compute(){
    elemCA.inizialize(numGen,numCells,rule);

    ExecutorService ex = Executors.newFixedThreadPool(cores);
    int window = numCells/cores, lower=0, upper=window;
    for(int i=0; i<cores; i++){
      ex.execute(new ElementaryCASimulator(upper,lower));
      lower=upper;
      upper+=window;
      if((i==cores-2)&&(window*cores!=numCells)){ upper+=(numCells-window*cores);}
    }
    ex.shutdown();
    while(!ex.isTerminated()){}

    try { paint(panel.getGraphics(), elemCA.get_cells());
    }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}
  }

  public void paint(Graphics g, int[][] m) throws InterruptedException{
    BufferedImage image=new BufferedImage(m[0].length, m.length, BufferedImage.TYPE_INT_RGB);
    for (int f=0;f<m.length ; ++f){
      for(int c=0; c<m[0].length; ++c){
        if(m[f][c]==0){
          Color color = Color.WHITE;
          image.setRGB(c,f,color.getRGB());
        }
        if(m[f][c]==1){
          Color color = Color.BLACK;
          image.setRGB(c,f,color.getRGB());
        }
      }
    }
    g.drawImage(image, 0, 0, panel.getWidth(),panel.getHeight(), panel);
  }

}
