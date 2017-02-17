import java.awt.*;
import javax.swing.*;
public  class DisplayWindow extends JFrame{

  private Container c;

  public DisplayWindow(){
    super("Chase's Super Awsome Snake Game");
    c = this.getContentPane();
  }

  public void addPanel(JPanel p){
    p.setPreferredSize(new Dimension(810,610)); // changed the size of the JPanel
    c.add(p);
  }

  public void showFrame(){
    this.pack();
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

