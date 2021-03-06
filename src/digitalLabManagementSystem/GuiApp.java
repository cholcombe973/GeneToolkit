package digitalLabManagementSystem;

import javax.swing.UIManager;
import java.awt.Toolkit;
import java.awt.Dimension;
import static digitalLabManagementSystem.Constants.*;
/**
 *
 * <p>Title: GuiApp</p>
 *
 * <p>Description: Called to startup the gui portion of the program.  Used as a convience
 * class.</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 *
 * @author Chris Holcombe
 * @version 1.0
 */
public class GuiApp {
  private boolean packFrame = false;
  private ReferenceEngine re;

  public GuiApp(ReferenceEngine re) {
      //Validate frames that have preset sizes
      //Pack frames that have useful preferred size info, e.g. from their layout
      this.re = re;
      re.putReference("GuiApp",this);

      GUI frame = new GUI(re);

      if (packFrame)
        frame.pack();
      else
        frame.validate();

      //Center the window
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = frame.getSize();
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
      frame.setVisible(true);
    }
  }
