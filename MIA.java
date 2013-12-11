import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;
import java.util.Timer;
import java.util.TimerTask;


public class MIA {

  /** Thresholds */
  public static final double TH_COG_POWER = 0.0;
  public static final double TH_EXP_POWERL = 0.0;
  public static final double TH_EXP_POWERU = 0.0;
  public static final int TH_GYRO_X = 1000;
  public static final int TH_GYRO_Y = 400;
  public static final int TH_CTRL_DELAY = 500;    //ms
  public static final boolean GYRO_EN = true;
  
  
  /** Components */
  public static myJFrame keyBoard;
  public static Mouse mouse;
  
  private static Pointer eEvent;
  private static Pointer eState;
  private static IntByReference userID;
  private static IntByReference gyroX;
  private static IntByReference gyroY;
  
  public static boolean taskStop;
  
  
  public static int absInt(int in) {
    return (in > 0)? in: (-in);
  }
  
  /** Control mouse using states */
  public static void mouseCtrl() {
    // Movement
    if (mouse.curMove == Mouse.MouseMov.UP)
      mouse.moveUp();
    else if (mouse.curMove == Mouse.MouseMov.DOWN)
      mouse.moveDown();
    else if (mouse.curMove == Mouse.MouseMov.LEFT)
      mouse.moveLeft();
    else if (mouse.curMove == Mouse.MouseMov.RIGHT)
      mouse.moveRight();
    if (!GYRO_EN)
      mouse.curMove = Mouse.MouseMov.NONE;
    
    // Click
    if (mouse.curClick == Mouse.MouseClk.LEFT)
      mouse.clickLeft();
    else if (mouse.curClick == Mouse.MouseClk.RIGHT)
      mouse.clickRight();
    else if (mouse.curClick == Mouse.MouseClk.MIDDLE)
      mouse.clickMiddle();
    if (!GYRO_EN)
      mouse.curClick = Mouse.MouseClk.NONE;
  }

  
  /** Signal processing */
  public static boolean eventHandler() throws Exception {
    int state = 0;
    float cogPower = 0, expPowerL = 0, expPowerU = 0;
    int cogAct, expAct;
    boolean hasCog = false, hasExp = false;

    state = Edk.INSTANCE.EE_EngineGetNextEvent(eEvent);
    if (state == EdkErrorCode.EDK_OK.ToInt()) {
      int eventType = Edk.INSTANCE.EE_EmoEngineEventGetType(eEvent);
      Edk.INSTANCE.EE_EmoEngineEventGetUserId(eEvent, userID);

      if (eventType == Edk.EE_Event_t.EE_EmoStateUpdated.ToInt()) {
        Edk.INSTANCE.EE_EmoEngineEventGetEmoState(eEvent, eState);
        //float timestamp = EmoState.INSTANCE.ES_GetTimeFromStart(eState);
        //System.out.println(timestamp + " : New EmoState from user " + userID.getValue());

        /** Signal quality */
        int signal = EmoState.INSTANCE.ES_GetWirelessSignalStatus(eState);
        if (signal == 2) {
          keyBoard.sigJLabel.setText("Good");
          keyBoard.sigJLabel.setForeground(new java.awt.Color(0, 111, 0));
        }
        else if (signal == 1) {
          keyBoard.sigJLabel.setText("Bad");
          keyBoard.sigJLabel.setForeground(java.awt.Color.red);
        }
        else {
          keyBoard.sigJLabel.setText("No");
          keyBoard.sigJLabel.setForeground(java.awt.Color.black);
          keyBoard.cogJLabel.setText("N/A");
          keyBoard.cogJLabel.setForeground(java.awt.Color.black);
          keyBoard.expJLabel.setText("N/A");
          keyBoard.expJLabel.setForeground(java.awt.Color.black);
          return false;
        }

        /** Cognitv */
        /*
        int cogActive = EmoState.INSTANCE.ES_CognitivIsActive(eState);
        if (cogActive == 0) {
          hasCog = false;
          keyBoard.cogJLabel.setText("Too Noisy");
          keyBoard.cogJLabel.setForeground(java.awt.Color.red);
        }
        else {
        */
          cogAct = EmoState.INSTANCE.ES_CognitivGetCurrentAction(eState);
          cogPower = EmoState.INSTANCE.ES_CognitivGetCurrentActionPower(eState);

          if (cogAct > 1 && cogPower > TH_COG_POWER) {
            hasCog = true;
            if (cogAct == EmoState.EE_CognitivAction_t.COG_PUSH.ToInt()) {
              keyBoard.cogJLabel.setText("Push");
              //mouse.curClick = Mouse.MouseClk.LEFT;
            }
            
            if (cogAct == EmoState.EE_CognitivAction_t.COG_PULL.ToInt())
              keyBoard.cogJLabel.setText("Pull");
            
            /*
            if (cogAct == EmoState.EE_CognitivAction_t.COG_LIFT.ToInt())
              keyBoard.cogJLabel.setText("Lift");
            */
            /*
            if (cogAct == EmoState.EE_CognitivAction_t.COG_DROP.ToInt())
              keyBoard.cogJLabel.setText("Drop");
            */
            /*
            if (cogAct == EmoState.EE_CognitivAction_t.COG_LEFT.ToInt())
              keyBoard.cogJLabel.setText("Left");
            */
            /*
            if (cogAct == EmoState.EE_CognitivAction_t.COG_RIGHT.ToInt())
              keyBoard.cogJLabel.setText("Right");
            */
            // keyBoard.cogJLabel.setText("Others");

            keyBoard.cogJLabel.setForeground(new java.awt.Color(0, 111, 0));
          }
          else {
            hasCog = false;
            keyBoard.cogJLabel.setText("N/A");
            keyBoard.cogJLabel.setForeground(java.awt.Color.black);
          }
        //}

        /** Expressiv */
        expAct = EmoState.INSTANCE.ES_ExpressivGetLowerFaceAction(eState);
        expPowerL = EmoState.INSTANCE.ES_ExpressivGetLowerFaceActionPower(eState);
        
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_SMIRK_LEFT.ToInt() && expPowerL > TH_EXP_POWERL) {
          hasExp = true;
          keyBoard.expJLabel.setText("Smirk Left");
          //mouse.curMove = Mouse.MouseMov.DOWN;
        }
        
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_SMIRK_RIGHT.ToInt() && expPowerL > TH_EXP_POWERL) {
          hasExp = true;
          keyBoard.expJLabel.setText("Smirk Right");
          //mouse.curMove = Mouse.MouseMov.RIGHT;
        }
        
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_LAUGH.ToInt() && expPowerL > TH_EXP_POWERL)
          keyBoard.expJLabel.setText("Laugh");
        
        
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_SMILE.ToInt() && expPowerL > TH_EXP_POWERL)
          keyBoard.expJLabel.setText("Smile");
        
        
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_CLENCH.ToInt() && expPowerL > TH_EXP_POWERL)
          keyBoard.expJLabel.setText("Clench");
        

        /*
        expPowerU = EmoState.INSTANCE.ES_ExpressivGetUpperFaceActionPower(eState);
        expAct = EmoState.INSTANCE.ES_ExpressivGetUpperFaceAction(eState);
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_EYEBROW.ToInt() && expPowerU > TH_EXP_POWERU) {
          hasExp = true;
          keyBoard.expJLabel.setText("Raise Brow");
        }
        if (expAct == EmoState.EE_ExpressivAlgo_t.EXP_FURROW.ToInt() && expPowerU > TH_EXP_POWERU) {
          hasExp = true;
          keyBoard.expJLabel.setText("Furrow Brow");
        }
        */

        if (EmoState.INSTANCE.ES_ExpressivIsBlink(eState) == 1) {
          hasExp = true;
          keyBoard.expJLabel.setText("Blink");
        }
        
        if (EmoState.INSTANCE.ES_ExpressivIsLeftWink(eState) == 1) {
          hasExp = true;
          keyBoard.expJLabel.setText("Wink Left");
          //mouse.curClick = Mouse.MouseClk.LEFT;
        }
        
        if (EmoState.INSTANCE.ES_ExpressivIsRightWink(eState) == 1) {
          hasExp = true;
          keyBoard.expJLabel.setText("Wink Right");
          //mouse.curClick = Mouse.MouseClk.LEFT;
        }
        
        if (EmoState.INSTANCE.ES_ExpressivIsLookingLeft(eState) == 1) {
          hasExp = true;
          keyBoard.expJLabel.setText("Look Left");
        }
        
        if (EmoState.INSTANCE.ES_ExpressivIsLookingRight(eState) == 1) {
          hasExp = true;
          keyBoard.expJLabel.setText("Look Right");
        }

        if (hasExp)
          keyBoard.expJLabel.setForeground(new java.awt.Color(0, 111, 0));
        else {
          keyBoard.expJLabel.setText("N/A");
          keyBoard.expJLabel.setForeground(java.awt.Color.black);
        }
        
        if (!hasCog && !hasExp)
          mouse.curClick = Mouse.MouseClk.NONE;

        /** Gyro */
        if (GYRO_EN) {
          Edk.INSTANCE.EE_HeadsetGetGyroDelta(userID.getValue(), gyroX, gyroY);
          System.out.println("gyroX = " + gyroX.getValue() + " gyroY = " + gyroY.getValue());
          if ((absInt(gyroX.getValue()) > absInt(gyroY.getValue())) && (absInt(gyroX.getValue()) > TH_GYRO_X)) {
            //move right
            if (gyroX.getValue() > 0) {
              if (mouse.curMove == Mouse.MouseMov.NONE)
                mouse.curMove = Mouse.MouseMov.RIGHT;
              else if (mouse.curMove == Mouse.MouseMov.LEFT)
                mouse.curMove = Mouse.MouseMov.NONE;
            }
            //move left
            else {
              if (mouse.curMove == Mouse.MouseMov.NONE)
                mouse.curMove = Mouse.MouseMov.LEFT;
              else if (mouse.curMove == Mouse.MouseMov.RIGHT)
                mouse.curMove = Mouse.MouseMov.NONE;
            }
          }
          else if ((absInt(gyroY.getValue()) > absInt(gyroX.getValue())) && (absInt(gyroY.getValue()) > TH_GYRO_Y)) {
            //move down
            if (gyroY.getValue() > 0) {
              if (mouse.curMove == Mouse.MouseMov.NONE)
                mouse.curMove = Mouse.MouseMov.DOWN;
              else if (mouse.curMove == Mouse.MouseMov.UP)
                mouse.curMove = Mouse.MouseMov.NONE;
            }
            //move up
            else {
              if (mouse.curMove == Mouse.MouseMov.NONE)
                mouse.curMove = Mouse.MouseMov.UP;
              else if (mouse.curMove == Mouse.MouseMov.DOWN)
                mouse.curMove = Mouse.MouseMov.NONE;
            }
          }
        }
      }
    }
    else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
      System.out.println("Internal error in Emotiv Engine!");
      return true;
    }
    return false;
  }//end of eventHandler

  
  public static void main(String[] args) throws Exception {

    eEvent = Edk.INSTANCE.EE_EmoEngineEventCreate();
    eState = Edk.INSTANCE.EE_EmoStateCreate();
    userID = new IntByReference(0);
    gyroX = new IntByReference(0);
    gyroY = new IntByReference(0);
    short composerPort = 1726;      //EmoComposer port #
    short controlPort = 3008;       //Control panel port #
    int option = 3;                 //1: Headset, 2: EmoComposer

    /** Create connection */
    switch (option) {
    case 1: {
      if (Edk.INSTANCE.EE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
        System.out.println("Emotiv Engine start up failed.");
        return;
      }
      System.out.println("Emotiv Engine started!");
      break;
    }
    case 2: {
      if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", composerPort, "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
        System.out.println("Cannot connect to EmoComposer on [127.0.0.1]");
        return;
      }
      System.out.println("Connected to EmoComposer on [127.0.0.1]");
      break;
    }
    case 3: {
      if (Edk.INSTANCE.EE_EngineRemoteConnect("127.0.0.1", controlPort, "Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
        System.out.println("Cannot connect to Control Panel on [127.0.0.1]");
        return;
      }
      System.out.println("Connected to Control Panel on [127.0.0.1]");
      break;
    }
    default:
      System.out.println("Invalid option...");
      return;
    }

    /** Initialize keyboard and mouse */
    keyBoard = new myJFrame();
    mouse = new Mouse();
    if (Edk.INSTANCE.EE_DataAcquisitionEnable(userID.getValue(), true) != EdkErrorCode.EDK_OK.ToInt())
      System.out.println("Data Acquisition Enable failed!");

    /** Initialize information panel */
    keyBoard.movRJLabel.setText("Head Right");
    keyBoard.movRJLabel.setForeground(new java.awt.Color(0, 111, 0));
    keyBoard.movDJLabel.setText("Head Down");
    keyBoard.movDJLabel.setForeground(new java.awt.Color(0, 111, 0));
    keyBoard.clkJLabel.setText("Wink");
    keyBoard.clkJLabel.setForeground(new java.awt.Color(0, 111, 0));

    /** Initialize Gyro */
    Thread.sleep(5000);
    Edk.INSTANCE.EE_HeadsetGyroRezero(userID.getValue());
    
    /** Timer task for mouse actions */
    Timer myTimer = new Timer();
    myTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        try {
          mouseCtrl();
        } catch (Exception e) {
        }
      }
    }, 1000, TH_CTRL_DELAY);
    
    /** Sample EmoEngine data */
    taskStop = false;
    while (!taskStop)
      taskStop = eventHandler();
    
    myTimer.cancel();
    
    Edk.INSTANCE.EE_EngineDisconnect();
    System.out.println("Disconnected!");
  }//end of main
}
