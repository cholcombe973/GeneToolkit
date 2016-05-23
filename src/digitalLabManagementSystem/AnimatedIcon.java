package digitalLabManagementSystem;

/**
 * <p>Title: AnimatedIcon</p>
 *
 * <p>Description: This class will extend JLabel to create an animated JLabel</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 *
 * @author Chris Holcombe
 * @version 1.0
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;
import javax.swing.*;
import static digitalLabManagementSystem.Constants.*;

public class AnimatedIcon implements Icon{
  private Thread t;
  private boolean done = false;
  private int width;
  private int height;
  private Graphics graphics;
  private Timer timer;
  private Rectangle r;

  public AnimatedIcon(int width, int height) {
    this.width = width;
    this.height = height;
  }
  public int getIconHeight(){
    return height;
  }
  public int getIconWidth(){
    return width;
  }
  public void setGraphics(Graphics graphics){
    this.graphics = graphics;
  }
  public void start(){
    animate();
  }
  public void stop(){
    done = true;
  }

  public void paintIcon(Component c, Graphics g, int x, int y){
      //make a cool little sine wave
//    super.paint(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      double x1,y1;
      for(int i=0;i<=360;i++){
        //sin(i) is the y value
        //i is the x value
        x1 = i;
        y1 = Math.sin(deg2rad(i));

//      g2.setColor(Color.lightGray);
//      g2.fillRect(r.x,r.y,r.width,r.height);
        g2.setColor(Color.black);
//      g2.drdrawLine(i,y,);
//      System.out.println("x:"+i + " y:"+((int)(y*20)));
        g2.drawOval(i,(int)(y1*20),1,1);
      }
  }

  public void paintComponent(Graphics g){
    //make a cool little sine wave
//    super.paint(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    double x,y;
    for(int i=0;i<=360;i++){
      //sin(i) is the y value
      //i is the x value
      x = i;
      y = Math.sin(deg2rad(i));

//      g2.setColor(Color.lightGray);
//      g2.fillRect(r.x,r.y,r.width,r.height);
      g2.setColor(Color.black);
//      g2.drdrawLine(i,y,);
//      System.out.println("x:"+i + " y:"+((int)(y*20)));
      g2.drawOval(i,(int)(y*20),1,1);
    }
  }
  public void setBounds(Rectangle r){
    this.r = r;
  }
  public double deg2rad(int i ) {
    return (i * (Math.PI / 180));
  }
  public void animate(){
    t = new Thread(new Runnable(){
      public void run(){
        while(!done){
          paintComponent(graphics);
        }
      }
    });
    t.start();
  }
  static class SineCurve extends JComponent {
    float frequency=1.0f;
    public void setFrequency(float f) {
      frequency=f;
      repaint();
    }
    public Dimension getPreferredSize() { return new Dimension(100,50); }
    public void paintComponent(Graphics g) {
      Rectangle b=getBounds();
      float lasty = 0;
      for (int x=0; x<b.width/4; x++) {
        float f=frequency*((float)x)/b.width*5;
        int y=(int) (Math.sin(f)*b.height/2 + b.height/2);
        if (x>0)
          g.drawLine(x-1, (int)lasty, x, y);
        lasty=y;
      }
    }
  }

  public static void main(String[] args) {
    AnimatedIcon aicon = new AnimatedIcon(100,16);
    //SineCurve sc = new SineCurve();
    final JLabel label = new JLabel(aicon, SwingConstants.CENTER);
    label.setHorizontalTextPosition(SwingConstants.CENTER);
    label.setOpaque(true);
    label.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

    JFrame f = new JFrame("Example");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(new SineCurve());
    f.setSize(800,800);
    f.validate();
    f.setLocationRelativeTo(null);
    f.setVisible(true);

    Rectangle frameBounds = f.getBounds();
    aicon.setBounds(frameBounds);
    aicon.setGraphics(f.getGraphics());
    aicon.animate();
  }

}
