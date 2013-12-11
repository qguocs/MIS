import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.Point;
import java.awt.MouseInfo;


public class Mouse {
  
  /** Corner keys coordinates */
  public static final int CTRL_LEFT_X = 675;
  public static final int CTRL_RIGHT_X = 1200;
  public static final int CTRL_TOP_Y = 295;
  public static final int CTRL_BOTTOM_Y = 295;
  public static final int CTRL_DELTA_X = 75;
  public static final int CTRL_DELTA_Y = 0;
  
  public static final int KEYB_LEFT_X = 660;
  public static final int KEYB_RIGHT_X = 1200;
  public static final int KEYB_TOP_Y = 405;
  public static final int KEYB_BOTTOM_Y = 605;
  public static final int KEYB_DELTA_X = 60;
  public static final int KEYB_DELTA_Y = 50;
  
  /** Mouse states */
  public static enum MouseMov {NONE, UP, DOWN, LEFT, RIGHT};
  public static enum MouseClk {NONE, LEFT, MIDDLE, RIGHT};
  public static enum Panel {KEYBOARD, CONTROL, HOTKEY};
  
  /** Components */
  Robot robot;
  MouseMov curMove;
  MouseClk curClick;
  Panel curPanel;
  Point curPosition;

  /** Construction */
  Mouse() throws Exception {
    robot = new Robot();
    curMove = MouseMov.NONE;
    curClick = MouseClk.NONE;
    curPanel = Panel.KEYBOARD;
    robot.mouseMove(KEYB_RIGHT_X, KEYB_BOTTOM_Y);
    curPosition = MouseInfo.getPointerInfo().getLocation();
  }
  
  /** Move left one hop */
  public void moveLeft() {
    int max_x, min_x, delta_x;
    curPosition = MouseInfo.getPointerInfo().getLocation();
    if (curPanel == Panel.KEYBOARD) {
      max_x = KEYB_RIGHT_X;
      min_x =  KEYB_LEFT_X;
      delta_x = KEYB_DELTA_X;
    }
    else {
      max_x = CTRL_RIGHT_X;
      min_x = CTRL_LEFT_X;
      delta_x = CTRL_DELTA_X;
    }
    
    if (curPosition.x <= min_x)
      robot.mouseMove(max_x, curPosition.y);
    else
      robot.mouseMove(curPosition.x - delta_x, curPosition.y);
  }
  
  /** Move right one hop */
  public void moveRight() {
    int max_x, min_x, delta_x;
    curPosition = MouseInfo.getPointerInfo().getLocation();
    if (curPanel == Panel.KEYBOARD) {
      max_x = KEYB_RIGHT_X;
      min_x =  KEYB_LEFT_X;
      delta_x = KEYB_DELTA_X;
    }
    else {
      max_x = CTRL_RIGHT_X;
      min_x = CTRL_LEFT_X;
      delta_x = CTRL_DELTA_X;
    }
    
    if (curPosition.x >= max_x)
      robot.mouseMove(min_x, curPosition.y);
    else
      robot.mouseMove(curPosition.x + delta_x, curPosition.y);
  }
  
  /** Move up one hop */
  public void moveUp() {
    int max_y, min_y, delta_y;
    curPosition = MouseInfo.getPointerInfo().getLocation();
    if (curPanel == Panel.KEYBOARD) {
      max_y = KEYB_BOTTOM_Y;
      min_y =  KEYB_TOP_Y;
      delta_y = KEYB_DELTA_Y;
    }
    else {
      max_y = CTRL_BOTTOM_Y;
      min_y = CTRL_TOP_Y;
      delta_y = CTRL_DELTA_Y;
    }
    
    if (curPosition.y <= min_y)
      robot.mouseMove(curPosition.x, max_y);
    else
      robot.mouseMove(curPosition.x, curPosition.y - delta_y);
  }
  
  /** Move down one hop */
  public void moveDown() {
    int max_y, min_y, delta_y;
    curPosition = MouseInfo.getPointerInfo().getLocation();
    if (curPanel == Panel.KEYBOARD) {
      max_y = KEYB_BOTTOM_Y;
      min_y =  KEYB_TOP_Y;
      delta_y = KEYB_DELTA_Y;
    }
    else {
      max_y = CTRL_BOTTOM_Y;
      min_y = CTRL_TOP_Y;
      delta_y = CTRL_DELTA_Y;
    }
    
    if (curPosition.y >= max_y)
      robot.mouseMove(curPosition.x, min_y);
    else
      robot.mouseMove(curPosition.x, curPosition.y + delta_y);
  }
  
  /** Click the left button */
  public void clickLeft() {
    robot.mousePress(InputEvent.BUTTON1_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_MASK);
    Edk.INSTANCE.EE_HeadsetGyroRezero(0);
    curMove = MouseMov.NONE;
    curClick = MouseClk.NONE;
  }
  
  /** Click the middle button */
  public void clickMiddle() {
    robot.mousePress(InputEvent.BUTTON2_MASK);
    robot.mouseRelease(InputEvent.BUTTON2_MASK);
    Edk.INSTANCE.EE_HeadsetGyroRezero(0);
    curMove = MouseMov.NONE;
    curClick = MouseClk.NONE;
  }
  
  /** Click the right button */
  public void clickRight() {
    robot.mousePress(InputEvent.BUTTON3_MASK);
    robot.mouseRelease(InputEvent.BUTTON3_MASK);
    Edk.INSTANCE.EE_HeadsetGyroRezero(0);
    curMove = MouseMov.NONE;
    curClick = MouseClk.NONE;
  }
  
  /** Move cursor to keyboard panel */
  public void moveToKeyB() {
    curPanel = Panel.KEYBOARD;
    robot.mouseMove(KEYB_RIGHT_X, KEYB_BOTTOM_Y);
    curPosition = MouseInfo.getPointerInfo().getLocation();
    Edk.INSTANCE.EE_HeadsetGyroRezero(0);
    curMove = MouseMov.NONE;
    curClick = MouseClk.NONE;
  }
  
  /** Move cursor to control panel */
  public void moveToCtrl() {
    curPanel = Panel.CONTROL;
    robot.mouseMove(CTRL_RIGHT_X, CTRL_BOTTOM_Y);
    curPosition = MouseInfo.getPointerInfo().getLocation();
    Edk.INSTANCE.EE_HeadsetGyroRezero(0);
    curMove = MouseMov.NONE;
    curClick = MouseClk.NONE;
  }
}

