package digitalLabManagementSystem;

/*
  This class provides the basis for a menu system that can be built through method calls
  This class wraps a JMenu and allows multiple menus to be stored in an ArrayList
  Plugin's will be using this to go connect themselves to the gui.

*/
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * <p>Title: Menu Engine</p>
 *
 * <p>Description:
 *  This class provides the basis for a menu system that can be built through method calls
 *  This class wraps a JMenu and allows multiple menus to be stored in an ArrayList
 *  Plugin's will be using this to go connect themselves to the gui.
 * </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * @author Chris Holcombe
 * @version 1.0
 */
public class MenuEngine {
  private ArrayList menus = new ArrayList();
  private ArrayList listeners = new ArrayList();
  private ReferenceEngine re;
  private int menuID = 0;

  public MenuEngine(ReferenceEngine re){
    this.re = re;
    re.putReference("MenuEngine",this);
  }
  /**
   * create a new menu and return the id
   * @return int
   */
  public int createNewMenu(){
    menus.add(new JMenu());
    return menuID++;
  }
  /**
   * create a new menu with specified action and return the id
   * @param a Action
   * @return int
   */
  public int createNewMenu(Action a){
    menus.add(new JMenu(a));
    return menuID++;
  }
  /**
   * create a new menu with specified string and return the id
   * @param s String
   * @return int
   */
  public int createNewMenu(String s){
    menus.add(new JMenu(s));
    return menuID++;
  }
  /**
   * create a new menu with specified string and tear off menu and return the id
   * @param s String
   * @param b boolean
   * @return int
   */
  public int createNewMenu(String s,boolean b){
    menus.add(new JMenu(s,b));
    return menuID++;
  }
  /**
   * unregister's the menu with the Engine but not from the GUI.
   * @param menuID int
   */
  public void removeMenu(int menuID){
    //Fire an event message to remove the menu
//    GlobalEvent e = new GlobalEvent(GlobalEvent.removeMenu);
//    e.setMenu((JMenu)menus.get(menuID));
//    fireGlobalActionEvent(e);
    menus.remove(menuID);
  }
  /**
   * add an action to the menu with given id
   * @param a Action
   * @param menuID int
   */
  public void addAction(Action a,int menuID){
    ((JMenu)menus.get(menuID)).add(a);
  }
  /**
   * add a component to the menu with given id
   * @param c Component
   * @param menuID int
   */
  public void addComponent(Component c,int menuID){
    ((JMenu)menus.get(menuID)).add(c);
  }
  /**
   * add a component to the menu with the given id at a given index
   * @param c Component
   * @param index int
   * @param menuID int
   */
  public void addComponent(Component c, int index,int menuID){
    ((JMenu)menus.get(menuID)).add(c,index);
  }
  /**
   * add a menu item to the menu with the given id
   * @param item JMenuItem
   * @param menuID int
   */
  public void addMenuItem(JMenuItem item,int menuID){
    ((JMenu)menus.get(menuID)).add(item);
  }
  /**
   * add a menu with the given string text with given id
   * @param s String
   * @param menuID int
   */
  public void addString(String s,int menuID){
    ((JMenu)menus.get(menuID)).add(s);
  }
  /**
   * add a listener to the menu with the given id
   * @param l MenuListener
   * @param menuID int
   */
  public void addMenuListener(MenuListener l,int menuID){
    ((JMenu)menus.get(menuID)).addMenuListener(l);
  }
  /**
   * add a seperator to the menu at given id
   * @param menuID int
   */
  public void addSeperator(int menuID){
    ((JMenu)menus.get(menuID)).addSeparator();
  }
  /**
   *
   * @param o ComponentOrientation
   * @param menuID int
   */
  public void applyComponentOrientation(ComponentOrientation o,int menuID){
    ((JMenu)menus.get(menuID)).applyComponentOrientation(o);
  }
  /**
   * programmatically click the given menu with id
   * @param menuID int
   */
  public void doClick(int menuID){
    ((JMenu)menus.get(menuID)).doClick();
  }
  /**
   * returns the delay that is set for the given menu with id
   * @param menuID int
   * @return int
   */
  public int getDelay(int menuID){
    return ((JMenu)menus.get(menuID)).getDelay();
  }
  /**
   * return the component count of the menu with given id
   * @param menuID int
   * @return int
   */
  public int getMenuComponentCount(int menuID){
    return ((JMenu)menus.get(menuID)).getMenuComponentCount();
  }
  /**
   * return the name of the look and feel class that renders this menu with given id
   * @param menuID int
   * @return String
   */
  public String getUIClassID(int menuID){
    return ((JMenu)menus.get(menuID)).getUIClassID();
  }
  /**
   * inserts a menu
   * @param a Action
   * @param pos int
   * @param menuID int
   */
  public void insert(Action a, int pos, int menuID){
    ((JMenu)menus.get(menuID)).insert(a,pos);
  }
  /**
   *
   * @param mi JMenuItem
   * @param pos int
   * @param menuID int
   */
  public void insert(JMenuItem mi,int pos, int menuID){
    ((JMenu)menus.get(menuID)).insert(mi,pos);
  }
  /**
   *
   * @param s String
   * @param pos int
   * @param menuID int
   */
  public void insert(String s,int pos, int menuID){
    ((JMenu)menus.get(menuID)).insert(s,pos);
  }
  /**
   *
   * @param index int
   * @param menuID int
   */
  public void insertSeperator(int index, int menuID){
    ((JMenu)menus.get(menuID)).insertSeparator(index);
  }
  /**
   *
   * @param c Component
   * @param menuID int
   * @return boolean
   */
  public boolean isMenuComponent(Component c, int menuID){
    return ((JMenu)menus.get(menuID)).isMenuComponent(c);
  }
  /**
   *
   * @param menuID int
   * @return boolean
   */
  public boolean isPopupMenuVisible(int menuID){
    return ((JMenu)menus.get(menuID)).isPopupMenuVisible();
  }
  /**
   *
   * @param menuID int
   * @return boolean
   */
  public boolean isSelected(int menuID){
    return ((JMenu)menus.get(menuID)).isSelected();
  }
  /**
   *
   * @param menuID int
   * @return boolean
   */
  public boolean isTearOff(int menuID){
    return ((JMenu)menus.get(menuID)).isTearOff();
  }
  /**
   *
   * @param menuID int
   * @return boolean
   */
  public boolean isTopLevelMenu(int menuID){
    return ((JMenu)menus.get(menuID)).isTopLevelMenu();
  }
  /**
   *
   * @param c Component
   * @param menuID int
   */
  public void remove(Component c,int menuID){
    ((JMenu)menus.get(menuID)).remove(c);
  }
  /**
   *
   * @param pos int
   * @param menuID int
   */
  public void remove(int pos, int menuID){
    ((JMenu)menus.get(menuID)).remove(pos);
  }
  /**
   *
   * @param item JMenuItem
   * @param menuID int
   */
  public void remove(JMenuItem item, int menuID){
    ((JMenu)menus.get(menuID)).remove(item);
  }
  /**
   *
   * @param menuID int
   */
  public void removeAll(int menuID){
    ((JMenu)menus.get(menuID)).removeAll();
  }
  /**
   *
   * @param l MenuListener
   * @param menuID int
   */
  public void removeMenuListener(MenuListener l, int menuID){
    ((JMenu)menus.get(menuID)).removeMenuListener(l);
  }
  /**
   *
   * @param keyStroke KeyStroke
   * @param menuID int
   */
  public void setAccelerator(KeyStroke keyStroke, int menuID){
    ((JMenu)menus.get(menuID)).setAccelerator(keyStroke);
  }
  /**
   *
   * @param o ComponentOrientation
   * @param menuID int
   */
  public void setComponentOrientation(ComponentOrientation o, int menuID){
    ((JMenu)menus.get(menuID)).setComponentOrientation(o);
  }
  /**
   *
   * @param d int
   * @param menuID int
   */
  public void setDelay(int d, int menuID){
    ((JMenu)menus.get(menuID)).setDelay(d);
  }
  /**
   *
   * @param x int
   * @param y int
   * @param menuID int
   */
  public void setMenuLocation(int x, int y, int menuID){
    ((JMenu)menus.get(menuID)).setMenuLocation(x,y);
  }
  /**
   *
   * @param newModel ButtonModel
   * @param menuID int
   */
  public void setModel(ButtonModel newModel,int menuID){
    ((JMenu)menus.get(menuID)).setModel(newModel);
  }
  /**
   *
   * @param b boolean
   * @param menuID int
   */
  public void setPopupMenuVisible(boolean b, int menuID){
    ((JMenu)menus.get(menuID)).setPopupMenuVisible(b);
  }
  /**
   *
   * @param b boolean
   * @param menuID int
   */
  public void setSelected(boolean b, int menuID){
    ((JMenu)menus.get(menuID)).setSelected(b);
  }
  /**
   *
   * @param menuID int
   */
  public void updateUI(int menuID){
    ((JMenu)menus.get(menuID)).updateUI();
  }

  /**
   * This is used by the main GUI to get all menus to render
   * @return JMenu[]
   */
  public JMenu[] getAllMenus(){
    return (JMenu[])menus.toArray(new JMenu[0]);
  }
//  public void registerGlobalListener(GlobalListener l){
//    listeners.add(l);
//  }
//unregister to stop listening for events issue from WindowEngine
//  public void unregisterGlobalListener(GlobalListener l){
//    listeners.remove(l);
//  }
//handle events
//  public void globalActionPerformed(GlobalEvent e){

//  }
  //fire an event off to all registered listeners
//  private void fireGlobalActionEvent(GlobalEvent e){
//    int size = listeners.size();
//  for(int i=0;i<size;i++){
//      ((GlobalListener)listeners.get(i)).globalActionPerformed(e);
//    }
//  }
}
