package digitalLabManagementSystem;

import java.awt.Font;
import java.awt.Graphics;

public interface IDraw {
  public void setGraphics(Graphics g);
  public void moveTo(float x,float y);
  public void lineTo(float x,float y);
  public void curveTo(float x1,float y1,float x2, float y2);
  public void renderScene();
  public void createPath(int rule);
  public void setFont(Font f);
  public void drawText(String str,int x, int y);
  public void drawText(String str,float x,float y);

}
