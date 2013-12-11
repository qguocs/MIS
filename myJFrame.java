/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.AWTException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.GroupLayout.*;
import javax.swing.text.BadLocationException;
import javax.swing.undo.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author qguo
 */
public class myJFrame extends javax.swing.JFrame implements DocumentListener {

  public boolean capLock;
  protected UndoManager undoManager;
  protected JFileChooser fileChooser;
  
  private static final String COMMIT_ACTION = "commit";
  public static enum Mode { INSERT, COMPLETION };
  private final List<String> words;
  public Mode mode = Mode.INSERT;
  private final int NUM_WORDS = 80368;
  private final String DICT_FILE = 
          "/home/developer/Dropbox/Emotiv/workspace/MIA/src/dict";
  
  private shortcutJDialog box = null;
  
  public void updateUndoRedoState() {
    //jButtonUndo.setText(undoManager.getUndoPresentationName());
    //jButtonRedo.setText(undoManager.getRedoPresentationName());
    jButtonUndo.setEnabled(undoManager.canUndo());
    jButtonRedo.setEnabled(undoManager.canRedo());
  }
  
  public void readFile(String fileName) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    try {
      String line = br.readLine();
      while (line != null) {
        words.add(line);
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }
  
  public void changedUpdate(DocumentEvent ev) {
  
  }
    
  public void removeUpdate(DocumentEvent ev) {
  
  }
    
  public void insertUpdate(DocumentEvent ev) {
    if (ev.getLength() != 1) {
      return;
    }
        
    int pos = ev.getOffset();
    String content = null;
    try {
      content = textArea.getText(0, pos + 1);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
        
    // Find where the word starts
    int w;
    for (w = pos; w >= 0; w--) {
      if (! Character.isLetter(content.charAt(w))) {
        break;
      }
    }
    if (pos - w < 2) {
      // Too few chars
      return;
    }
        
    String prefix = content.substring(w + 1).toLowerCase();
    int n = Collections.binarySearch(words, prefix);
    if (n < 0 && -n <= words.size()) {
      String match = words.get(-n - 1);
      if (match.startsWith(prefix)) {
        // A completion is found
        String completion = match.substring(pos - w);
        // We cannot modify Document from within notification,
        // so we submit a task that does the change later
        SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
      }
    } else {
      // Nothing found
      mode = Mode.INSERT;
      box.colorOff();
    }
  }
    
  private class CompletionTask implements Runnable {
    String completion;
    int position;
      
    CompletionTask(String completion, int position) {
      this.completion = completion;
      this.position = position;
    }
        
    public void run() {
      textArea.requestFocusInWindow();
      textArea.insert(completion, position);
      textArea.setCaretPosition(position + completion.length());
      textArea.moveCaretPosition(position);
      mode = Mode.COMPLETION;
      box.colorOn();
    }
  }
    
  private class CommitAction extends AbstractAction {
    public void actionPerformed(ActionEvent ev) {
      if (mode == Mode.COMPLETION) {
        int pos = textArea.getSelectionEnd();
        textArea.insert(" ", pos);
        textArea.setCaretPosition(pos + 1);
        mode = Mode.INSERT;
        box.colorOff();
      } else {
        textArea.replaceSelection("\n");
      }
    }
  }
  
  /**
   * Creates new form myJFrame
   */
  public myJFrame() throws AWTException {
    capLock = false;
    undoManager = new UndoManager();
    fileChooser = new JFileChooser();
    
    // Read dictionary into words
    words = new ArrayList<String>(NUM_WORDS);
    try {
      readFile(DICT_FILE);
    } catch (IOException e) {
      System.out.println("Error in readFile()!");
    }
    
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    
    initComponents();
    box = new shortcutJDialog(this, textArea, 
        jButtonA,
        jButtonB,
        jButtonC,
        jButtonD,
        jButtonE,
        jButtonF,
        jButtonG,
        jButtonH,
        jButtonI,
        jButtonJ,
        jButtonK,
        jButtonL,
        jButtonM,
        jButtonN,
        jButtonO,
        jButtonP,
        jButtonQ,
        jButtonR,
        jButtonS,
        jButtonT,
        jButtonU,
        jButtonV,
        jButtonW,
        jButtonX,
        jButtonY,
        jButtonZ);
    
    setVisible(true);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    ctrlLayerPane = new javax.swing.JLayeredPane();
    jButtonCopy = new javax.swing.JButton();
    jButtonCut = new javax.swing.JButton();
    jButtonPaste = new javax.swing.JButton();
    jButtonSave = new javax.swing.JButton();
    jButtonOpen = new javax.swing.JButton();
    jButtonUndo = new javax.swing.JButton();
    jButtonRedo = new javax.swing.JButton();
    jButtonKeyB = new javax.swing.JButton();
    textLayerPane = new javax.swing.JLayeredPane();
    textScrollPane = new javax.swing.JScrollPane();
    textArea = new javax.swing.JTextArea();
    keysLayeredPane = new javax.swing.JLayeredPane();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jButton3 = new javax.swing.JButton();
    jButton4 = new javax.swing.JButton();
    jButton5 = new javax.swing.JButton();
    jButton6 = new javax.swing.JButton();
    jButton7 = new javax.swing.JButton();
    jButton8 = new javax.swing.JButton();
    jButton9 = new javax.swing.JButton();
    jButton0 = new javax.swing.JButton();
    jButtonQ = new javax.swing.JButton();
    jButtonW = new javax.swing.JButton();
    jButtonE = new javax.swing.JButton();
    jButtonR = new javax.swing.JButton();
    jButtonT = new javax.swing.JButton();
    jButtonY = new javax.swing.JButton();
    jButtonU = new javax.swing.JButton();
    jButtonI = new javax.swing.JButton();
    jButtonO = new javax.swing.JButton();
    jButtonP = new javax.swing.JButton();
    jButtonA = new javax.swing.JButton();
    jButtonS = new javax.swing.JButton();
    jButtonD = new javax.swing.JButton();
    jButtonF = new javax.swing.JButton();
    jButtonG = new javax.swing.JButton();
    jButtonH = new javax.swing.JButton();
    jButtonJ = new javax.swing.JButton();
    jButtonK = new javax.swing.JButton();
    jButtonL = new javax.swing.JButton();
    jButtonDel = new javax.swing.JButton();
    jButtonZ = new javax.swing.JButton();
    jButtonX = new javax.swing.JButton();
    jButtonC = new javax.swing.JButton();
    jButtonV = new javax.swing.JButton();
    jButtonB = new javax.swing.JButton();
    jButtonN = new javax.swing.JButton();
    jButtonM = new javax.swing.JButton();
    jButtonCap = new javax.swing.JButton();
    jButtonComma = new javax.swing.JButton();
    jButtonPeriod = new javax.swing.JButton();
    jButtonQuestion = new javax.swing.JButton();
    jButtonDash = new javax.swing.JButton();
    jButtonAt = new javax.swing.JButton();
    jButtonDollar = new javax.swing.JButton();
    jButtonPercentage = new javax.swing.JButton();
    jButtonSpace = new javax.swing.JButton();
    jButtonEnter = new javax.swing.JButton();
    jButtonCtrl = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jLayeredPane1 = new javax.swing.JLayeredPane();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    sigJLabel = new javax.swing.JLabel();
    expJLabel = new javax.swing.JLabel();
    cogJLabel = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    movRJLabel = new javax.swing.JLabel();
    movDJLabel = new javax.swing.JLabel();
    jLabel11 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    clkJLabel = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jButtonEmail = new javax.swing.JButton();
    jButtonMsn = new javax.swing.JButton();
    jButtonFB = new javax.swing.JButton();
    jButtonTT = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Emotiv NotePad");
    setPreferredSize(new java.awt.Dimension(1200, 722));

    ctrlLayerPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Control Panel", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
    ctrlLayerPane.setToolTipText("Control Panel");

    jButtonCopy.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonCopy.setText("Copy");
    jButtonCopy.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonCopyMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonCopyMouseEntered(evt);
      }
    });
    jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCopyActionPerformed(evt);
      }
    });

    jButtonCut.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonCut.setText("Cut");
    jButtonCut.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonCutMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonCutMouseEntered(evt);
      }
    });
    jButtonCut.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCutActionPerformed(evt);
      }
    });

    jButtonPaste.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonPaste.setText("Paste");
    jButtonPaste.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonPasteMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonPasteMouseEntered(evt);
      }
    });
    jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonPasteActionPerformed(evt);
      }
    });

    jButtonSave.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonSave.setText("Save");
    jButtonSave.setToolTipText("");
    jButtonSave.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonSaveMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonSaveMouseEntered(evt);
      }
    });
    jButtonSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonSaveActionPerformed(evt);
      }
    });

    jButtonOpen.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonOpen.setText("Open");
    jButtonOpen.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonOpenMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonOpenMouseEntered(evt);
      }
    });
    jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonOpenActionPerformed(evt);
      }
    });

    jButtonUndo.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonUndo.setText("undo");
    jButtonUndo.setEnabled(false);
    jButtonUndo.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonUndoMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonUndoMouseEntered(evt);
      }
    });
    jButtonUndo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonUndoActionPerformed(evt);
      }
    });

    jButtonRedo.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonRedo.setText("redo");
    jButtonRedo.setEnabled(false);
    jButtonRedo.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonRedoMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonRedoMouseEntered(evt);
      }
    });
    jButtonRedo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonRedoActionPerformed(evt);
      }
    });

    jButtonKeyB.setBackground(java.awt.Color.orange);
    jButtonKeyB.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
    jButtonKeyB.setText("KeyB");
    jButtonKeyB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonKeyBMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonKeyBMouseEntered(evt);
      }
    });
    jButtonKeyB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonKeyBActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout ctrlLayerPaneLayout = new javax.swing.GroupLayout(ctrlLayerPane);
    ctrlLayerPane.setLayout(ctrlLayerPaneLayout);
    ctrlLayerPaneLayout.setHorizontalGroup(
      ctrlLayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(ctrlLayerPaneLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jButtonUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonCut, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButtonKeyB, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(12, 12, 12))
    );
    ctrlLayerPaneLayout.setVerticalGroup(
      ctrlLayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ctrlLayerPaneLayout.createSequentialGroup()
        .addGap(10, 10, 10)
        .addGroup(ctrlLayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonPaste, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonCut, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonUndo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonRedo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonKeyB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(10, 10, 10))
    );
    ctrlLayerPane.setLayer(jButtonCopy, javax.swing.JLayeredPane.DEFAULT_LAYER);
    ctrlLayerPane.setLayer(jButtonCut, javax.swing.JLayeredPane.DEFAULT_LAYER);
    ctrlLayerPane.setLayer(jButtonPaste, javax.swing.JLayeredPane.DEFAULT_LAYER);
    ctrlLayerPane.setLayer(jButtonSave, javax.swing.JLayeredPane.DEFAULT_LAYER);

    jButtonSave.getAccessibleContext().setAccessibleName("");
    ctrlLayerPane.setLayer(jButtonOpen, javax.swing.JLayeredPane.DEFAULT_LAYER);
    ctrlLayerPane.setLayer(jButtonUndo, javax.swing.JLayeredPane.DEFAULT_LAYER);
    ctrlLayerPane.setLayer(jButtonRedo, javax.swing.JLayeredPane.DEFAULT_LAYER);
    ctrlLayerPane.setLayer(jButtonKeyB, javax.swing.JLayeredPane.DEFAULT_LAYER);

    textLayerPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "NotePad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

    textArea.setColumns(20);
    textArea.setFont(new java.awt.Font("Liberation Serif", 0, 20)); // NOI18N
    textArea.setLineWrap(true);
    textArea.setRows(5);
    textArea.setTabSize(4);
    textArea.setToolTipText("Text Area");
    textArea.setWrapStyleWord(true);
    textScrollPane.setViewportView(textArea);
    textArea.getDocument().addUndoableEditListener(
      new UndoableEditListener() {
        public void undoableEditHappened(UndoableEditEvent e) {
          undoManager.addEdit(e.getEdit());
          updateUndoRedoState();
        }
      }
    );

    textArea.getDocument().addDocumentListener(this);
    InputMap im = textArea.getInputMap();
    ActionMap am = textArea.getActionMap();
    im.put(KeyStroke.getKeyStroke("ENTER"), COMMIT_ACTION);
    am.put(COMMIT_ACTION, new CommitAction());

    javax.swing.GroupLayout textLayerPaneLayout = new javax.swing.GroupLayout(textLayerPane);
    textLayerPane.setLayout(textLayerPaneLayout);
    textLayerPaneLayout.setHorizontalGroup(
      textLayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, textLayerPaneLayout.createSequentialGroup()
        .addGap(0, 0, Short.MAX_VALUE)
        .addComponent(textScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    textLayerPaneLayout.setVerticalGroup(
      textLayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(textLayerPaneLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(textScrollPane)
        .addContainerGap())
    );
    textLayerPane.setLayer(textScrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

    keysLayeredPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Keyboard", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

    jButton1.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton1.setText("1");
    jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton1MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton1MouseEntered(evt);
      }
    });
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jButton2.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton2.setText("2");
    jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton2MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton2MouseEntered(evt);
      }
    });
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    jButton3.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton3.setText("3");
    jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton3MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton3MouseEntered(evt);
      }
    });
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });

    jButton4.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton4.setText("4");
    jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton4MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton4MouseEntered(evt);
      }
    });
    jButton4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton4ActionPerformed(evt);
      }
    });

    jButton5.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton5.setText("5");
    jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton5MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton5MouseEntered(evt);
      }
    });
    jButton5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton5ActionPerformed(evt);
      }
    });

    jButton6.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton6.setText("6");
    jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton6MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton6MouseEntered(evt);
      }
    });
    jButton6.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton6ActionPerformed(evt);
      }
    });

    jButton7.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton7.setText("7");
    jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton7MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton7MouseEntered(evt);
      }
    });
    jButton7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton7ActionPerformed(evt);
      }
    });

    jButton8.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton8.setText("8");
    jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton8MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton8MouseEntered(evt);
      }
    });
    jButton8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton8ActionPerformed(evt);
      }
    });

    jButton9.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton9.setText("9");
    jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton9MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton9MouseEntered(evt);
      }
    });
    jButton9.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton9ActionPerformed(evt);
      }
    });

    jButton0.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButton0.setText("0");
    jButton0.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButton0MouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButton0MouseEntered(evt);
      }
    });
    jButton0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton0ActionPerformed(evt);
      }
    });

    jButtonQ.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonQ.setText("q");
    jButtonQ.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonQMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonQMouseExited(evt);
      }
    });
    jButtonQ.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonQActionPerformed(evt);
      }
    });

    jButtonW.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonW.setText("w");
    jButtonW.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonWMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonWMouseExited(evt);
      }
    });
    jButtonW.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonWActionPerformed(evt);
      }
    });

    jButtonE.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonE.setText("e");
    jButtonE.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonEMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonEMouseExited(evt);
      }
    });
    jButtonE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonEActionPerformed(evt);
      }
    });

    jButtonR.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonR.setText("r");
    jButtonR.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonRMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonRMouseExited(evt);
      }
    });
    jButtonR.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonRActionPerformed(evt);
      }
    });

    jButtonT.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonT.setText("t");
    jButtonT.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonTMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonTMouseExited(evt);
      }
    });
    jButtonT.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonTActionPerformed(evt);
      }
    });

    jButtonY.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonY.setText("y");
    jButtonY.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonYMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonYMouseEntered(evt);
      }
    });
    jButtonY.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonYActionPerformed(evt);
      }
    });

    jButtonU.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonU.setText("u");
    jButtonU.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonUMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonUMouseExited(evt);
      }
    });
    jButtonU.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonUActionPerformed(evt);
      }
    });

    jButtonI.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonI.setText("i");
    jButtonI.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonIMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonIMouseEntered(evt);
      }
    });
    jButtonI.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonIActionPerformed(evt);
      }
    });

    jButtonO.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonO.setText("o");
    jButtonO.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonOMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonOMouseEntered(evt);
      }
    });
    jButtonO.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonOActionPerformed(evt);
      }
    });

    jButtonP.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonP.setText("p");
    jButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonPMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonPMouseEntered(evt);
      }
    });
    jButtonP.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonPActionPerformed(evt);
      }
    });

    jButtonA.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonA.setText("a");
    jButtonA.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonAMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonAMouseEntered(evt);
      }
    });
    jButtonA.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonAActionPerformed(evt);
      }
    });

    jButtonS.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonS.setText("s");
    jButtonS.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonSMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonSMouseEntered(evt);
      }
    });
    jButtonS.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonSActionPerformed(evt);
      }
    });

    jButtonD.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonD.setText("d");
    jButtonD.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonDMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonDMouseEntered(evt);
      }
    });
    jButtonD.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDActionPerformed(evt);
      }
    });

    jButtonF.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonF.setText("f");
    jButtonF.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonFMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonFMouseEntered(evt);
      }
    });
    jButtonF.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonFActionPerformed(evt);
      }
    });

    jButtonG.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonG.setText("g");
    jButtonG.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonGMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonGMouseEntered(evt);
      }
    });
    jButtonG.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonGActionPerformed(evt);
      }
    });

    jButtonH.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonH.setText("h");
    jButtonH.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonHMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonHMouseEntered(evt);
      }
    });
    jButtonH.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonHActionPerformed(evt);
      }
    });

    jButtonJ.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonJ.setText("j");
    jButtonJ.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonJMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonJMouseEntered(evt);
      }
    });
    jButtonJ.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonJActionPerformed(evt);
      }
    });

    jButtonK.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonK.setText("k");
    jButtonK.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonKMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonKMouseEntered(evt);
      }
    });
    jButtonK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonKActionPerformed(evt);
      }
    });

    jButtonL.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonL.setText("l");
    jButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonLMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonLMouseEntered(evt);
      }
    });
    jButtonL.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonLActionPerformed(evt);
      }
    });

    jButtonDel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonDel.setText("del");
    jButtonDel.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonDelMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonDelMouseEntered(evt);
      }
    });
    jButtonDel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDelActionPerformed(evt);
      }
    });

    jButtonZ.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonZ.setText("z");
    jButtonZ.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonZMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonZMouseEntered(evt);
      }
    });
    jButtonZ.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonZActionPerformed(evt);
      }
    });

    jButtonX.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonX.setText("x");
    jButtonX.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonXMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonXMouseEntered(evt);
      }
    });
    jButtonX.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonXActionPerformed(evt);
      }
    });

    jButtonC.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonC.setText("c");
    jButtonC.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonCMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonCMouseEntered(evt);
      }
    });
    jButtonC.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCActionPerformed(evt);
      }
    });

    jButtonV.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonV.setText("v");
    jButtonV.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonVMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonVMouseEntered(evt);
      }
    });
    jButtonV.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonVActionPerformed(evt);
      }
    });

    jButtonB.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonB.setText("b");
    jButtonB.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonBMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonBMouseEntered(evt);
      }
    });
    jButtonB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonBActionPerformed(evt);
      }
    });

    jButtonN.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonN.setText("n");
    jButtonN.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonNMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonNMouseEntered(evt);
      }
    });
    jButtonN.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonNActionPerformed(evt);
      }
    });

    jButtonM.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonM.setText("m");
    jButtonM.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonMMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonMMouseEntered(evt);
      }
    });
    jButtonM.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonMActionPerformed(evt);
      }
    });

    jButtonCap.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonCap.setText("CAP");
    jButtonCap.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonCapMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonCapMouseEntered(evt);
      }
    });
    jButtonCap.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCapActionPerformed(evt);
      }
    });

    jButtonComma.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonComma.setText(",");
    jButtonComma.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonCommaMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonCommaMouseEntered(evt);
      }
    });
    jButtonComma.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCommaActionPerformed(evt);
      }
    });

    jButtonPeriod.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonPeriod.setText(".");
    jButtonPeriod.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonPeriodMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonPeriodMouseEntered(evt);
      }
    });
    jButtonPeriod.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonPeriodActionPerformed(evt);
      }
    });

    jButtonQuestion.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonQuestion.setText("?");
    jButtonQuestion.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonQuestionMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonQuestionMouseEntered(evt);
      }
    });
    jButtonQuestion.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonQuestionActionPerformed(evt);
      }
    });

    jButtonDash.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonDash.setText("-");
    jButtonDash.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonDashMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonDashMouseEntered(evt);
      }
    });
    jButtonDash.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDashActionPerformed(evt);
      }
    });

    jButtonAt.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonAt.setText("@");
    jButtonAt.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonAtMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonAtMouseEntered(evt);
      }
    });
    jButtonAt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonAtActionPerformed(evt);
      }
    });

    jButtonDollar.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonDollar.setText("$");
    jButtonDollar.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonDollarMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonDollarMouseEntered(evt);
      }
    });
    jButtonDollar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDollarActionPerformed(evt);
      }
    });

    jButtonPercentage.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonPercentage.setText("%");
    jButtonPercentage.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonPercentageMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonPercentageMouseEntered(evt);
      }
    });
    jButtonPercentage.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonPercentageActionPerformed(evt);
      }
    });

    jButtonSpace.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonSpace.setText("space");
    jButtonSpace.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonSpaceMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonSpaceMouseEntered(evt);
      }
    });
    jButtonSpace.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonSpaceActionPerformed(evt);
      }
    });

    jButtonEnter.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonEnter.setText("Enter");
    jButtonEnter.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonEnterMouseEntered(evt);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonEnterMouseExited(evt);
      }
    });
    jButtonEnter.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonEnterActionPerformed(evt);
      }
    });

    jButtonCtrl.setBackground(java.awt.Color.orange);
    jButtonCtrl.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jButtonCtrl.setText("Ctrl");
    jButtonCtrl.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseExited(java.awt.event.MouseEvent evt) {
        jButtonCtrlMouseExited(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        jButtonCtrlMouseEntered(evt);
      }
    });
    jButtonCtrl.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCtrlActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout keysLayeredPaneLayout = new javax.swing.GroupLayout(keysLayeredPane);
    keysLayeredPane.setLayout(keysLayeredPaneLayout);
    keysLayeredPaneLayout.setHorizontalGroup(
      keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(keysLayeredPaneLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(keysLayeredPaneLayout.createSequentialGroup()
            .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addGroup(keysLayeredPaneLayout.createSequentialGroup()
                .addComponent(jButtonCap, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonZ, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonX, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonC, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonV, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonB, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonN, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonM, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(keysLayeredPaneLayout.createSequentialGroup()
                .addComponent(jButtonComma, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDash, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAt, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDollar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jButtonEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jButtonCtrl, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(keysLayeredPaneLayout.createSequentialGroup()
            .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jButtonQ, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jButtonW, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(keysLayeredPaneLayout.createSequentialGroup()
                .addComponent(jButtonE, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonR, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonT, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonY, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonU, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonI, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonO, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(keysLayeredPaneLayout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
          .addGroup(keysLayeredPaneLayout.createSequentialGroup()
            .addComponent(jButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonS, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonD, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonF, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonG, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonH, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonJ, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonK, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonL, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jButtonDel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(12, 12, 12))
    );
    keysLayeredPaneLayout.setVerticalGroup(
      keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, keysLayeredPaneLayout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton0, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonQ, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonW, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonE, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonT, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonY, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonU, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonI, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonO, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonP, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonA, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonS, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonD, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonF, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonG, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonH, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonJ, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonK, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonL, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonDel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonCap, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonZ, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonX, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonC, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonV, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonB, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonN, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonM, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(keysLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonComma, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonQuestion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonDash, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonAt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonDollar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonPercentage, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jButtonCtrl, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(12, 12, 12))
    );
    keysLayeredPane.setLayer(jButton1, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton2, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton3, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton4, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton5, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton6, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton7, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton8, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton9, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButton0, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonQ, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonW, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonE, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonR, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonT, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonY, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonU, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonI, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonO, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonP, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonA, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonS, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonD, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonF, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonG, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonH, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonJ, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonK, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonL, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonDel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonZ, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonX, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonC, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonV, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonB, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonN, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonM, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonCap, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonComma, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonPeriod, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonQuestion, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonDash, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonAt, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonDollar, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonPercentage, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonSpace, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonEnter, javax.swing.JLayeredPane.DEFAULT_LAYER);
    keysLayeredPane.setLayer(jButtonCtrl, javax.swing.JLayeredPane.DEFAULT_LAYER);

    jLabel1.setFont(new java.awt.Font("Purisa", 3, 24)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(255, 178, 0));
    jLabel1.setText("Mind Input System");

    jLayeredPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Information Panel", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

    jLabel4.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jLabel4.setText("Signal Quality:");

    jLabel5.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jLabel5.setText("Expressiv Action:");

    jLabel6.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jLabel6.setText("Cognitiv Action:");

    sigJLabel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    sigJLabel.setText("N/A");

    expJLabel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    expJLabel.setText("N/A");

    cogJLabel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    cogJLabel.setText("N/A");

    jLabel10.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jLabel10.setText("Move Right:");

    movRJLabel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    movRJLabel.setText("N/A");

    movDJLabel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    movDJLabel.setText("N/A");

    jLabel11.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jLabel11.setText("Move Down:");

    jLabel12.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    jLabel12.setText("Click:");

    clkJLabel.setFont(new java.awt.Font("Liberation Sans", 1, 16)); // NOI18N
    clkJLabel.setText("N/A");

    javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
    jLayeredPane1.setLayout(jLayeredPane1Layout);
    jLayeredPane1Layout.setHorizontalGroup(
      jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jLayeredPane1Layout.createSequentialGroup()
        .addGap(25, 25, 25)
        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel6)
          .addComponent(jLabel5)
          .addComponent(jLabel4))
        .addGap(36, 36, 36)
        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(sigJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
          .addComponent(expJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(cogJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
        .addGap(36, 36, 36)
        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(clkJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(movDJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(movRJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    jLayeredPane1Layout.setVerticalGroup(
      jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jLayeredPane1Layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jLayeredPane1Layout.createSequentialGroup()
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(movRJLabel)
              .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(movDJLabel)
              .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel12)
              .addComponent(clkJLabel)))
          .addGroup(jLayeredPane1Layout.createSequentialGroup()
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel4)
              .addComponent(sigJLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel5)
              .addComponent(expJLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel6)
              .addComponent(cogJLabel))))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jLayeredPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(sigJLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(expJLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(cogJLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(jLabel10, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(movRJLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(movDJLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(jLabel11, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(jLabel12, javax.swing.JLayeredPane.DEFAULT_LAYER);
    jLayeredPane1.setLayer(clkJLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

    jLabel2.setFont(new java.awt.Font("Bitstream Charter", 3, 12)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(66, 139, 221));
    jLabel2.setText("Developed by Team M.I.A., University of Rochester");

    jLabel3.setFont(new java.awt.Font("Bitstream Charter", 3, 12)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(66, 139, 221));
    jLabel3.setText("Webpage: https://sites.google.com/site/rochciteammia");

    jButtonEmail.setIcon(new javax.swing.ImageIcon("/home/developer/Dropbox/Emotiv/workspace/MIA/src/email.png")); // NOI18N
    jButtonEmail.setBorder(null);
    jButtonEmail.setBorderPainted(false);
    jButtonEmail.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonEmailActionPerformed(evt);
      }
    });

    jButtonMsn.setIcon(new javax.swing.ImageIcon("/home/developer/Dropbox/Emotiv/workspace/MIA/src/sms.png")); // NOI18N
    jButtonMsn.setBorder(null);
    jButtonMsn.setBorderPainted(false);

    jButtonFB.setIcon(new javax.swing.ImageIcon("/home/developer/Dropbox/Emotiv/workspace/MIA/src/fb.png")); // NOI18N
    jButtonFB.setBorder(null);
    jButtonFB.setBorderPainted(false);

    jButtonTT.setIcon(new javax.swing.ImageIcon("/home/developer/Dropbox/Emotiv/workspace/MIA/src/tt.png")); // NOI18N
    jButtonTT.setBorder(null);
    jButtonTT.setBorderPainted(false);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(textLayerPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addGroup(layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(keysLayeredPane, javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(ctrlLayerPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.LEADING))
            .addGap(15, 15, 15))
          .addGroup(layout.createSequentialGroup()
            .addGap(29, 29, 29)
            .addComponent(jButtonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButtonMsn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButtonFB, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jButtonTT, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
            .addGap(61, 61, 61))))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
          .addGroup(layout.createSequentialGroup()
            .addGap(21, 21, 21)
            .addComponent(textLayerPane))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3))
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButtonEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonMsn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonFB, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonTT, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(ctrlLayerPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(keysLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(12, 12, 12))
    );

    jButtonMsn.getAccessibleContext().setAccessibleName("Message");
    jButtonFB.getAccessibleContext().setAccessibleName("Facebook");
    jButtonTT.getAccessibleContext().setAccessibleName("Twitter");

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("4");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("4");
    
    box.setVisible(false);
  }//GEN-LAST:event_jButton4ActionPerformed

  private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("7");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("7");
    
    box.setVisible(false);
  }//GEN-LAST:event_jButton7ActionPerformed

  private void jButtonEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEActionPerformed
    String sIn = (capLock)? "E": "e";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('e');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonEActionPerformed

  private void jButtonDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDActionPerformed
    String sIn = (capLock)? "D": "d";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('d');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonDActionPerformed

  private void jButtonVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVActionPerformed
    String sIn = (capLock)? "V": "v";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('v');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonVActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("1");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("1");
    box.setVisible(false);
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("2");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("2");
    box.setVisible(false);
  }//GEN-LAST:event_jButton2ActionPerformed

  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("3");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("3");
    box.setVisible(false);
  }//GEN-LAST:event_jButton3ActionPerformed

  private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("5");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("5");
    box.setVisible(false);
  }//GEN-LAST:event_jButton5ActionPerformed

  private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("6");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("6");
    box.setVisible(false);
  }//GEN-LAST:event_jButton6ActionPerformed

  private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("8");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("8");
    box.setVisible(false);
  }//GEN-LAST:event_jButton8ActionPerformed

  private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("9");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("9");
    box.setVisible(false);
  }//GEN-LAST:event_jButton9ActionPerformed

  private void jButton0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton0ActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("0");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("0");
    box.setVisible(false);
  }//GEN-LAST:event_jButton0ActionPerformed

  private void jButtonQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQActionPerformed
    String sIn = (capLock)? "Q": "q";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('q');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonQActionPerformed

  private void jButtonWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWActionPerformed
    String sIn = (capLock)? "W": "w";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('w');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonWActionPerformed

  private void jButtonRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRActionPerformed
    String sIn = (capLock)? "R": "r";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('r');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonRActionPerformed

  private void jButtonTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTActionPerformed
    String sIn = (capLock)? "T": "t";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('t');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonTActionPerformed

  private void jButtonYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonYActionPerformed
    String sIn = (capLock)? "Y": "y";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('y');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonYActionPerformed

  private void jButtonUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUActionPerformed
    String sIn = (capLock)? "U": "u";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('u');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonUActionPerformed

  private void jButtonIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIActionPerformed
    String sIn = (capLock)? "I": "i";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('i');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonIActionPerformed

  private void jButtonOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOActionPerformed
    String sIn = (capLock)? "O": "o";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('o');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonOActionPerformed

  private void jButtonPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPActionPerformed
    String sIn = (capLock)? "P": "p";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('p');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonPActionPerformed

  private void jButtonAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAActionPerformed
    String sIn = (capLock)? "A": "a";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('a');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonAActionPerformed

  private void jButtonSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSActionPerformed
    String sIn = (capLock)? "S": "s";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('s');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonSActionPerformed

  private void jButtonFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFActionPerformed
    String sIn = (capLock)? "F": "f";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('f');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonFActionPerformed

  private void jButtonGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGActionPerformed
    String sIn = (capLock)? "G": "g";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('g');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonGActionPerformed

  private void jButtonHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHActionPerformed
    String sIn = (capLock)? "H": "h";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));

    box.updateBox('h');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonHActionPerformed

  private void jButtonJActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJActionPerformed
    String sIn = (capLock)? "J": "j";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('j');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonJActionPerformed

  private void jButtonKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKActionPerformed
    String sIn = (capLock)? "K": "k";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('k');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonKActionPerformed

  private void jButtonLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLActionPerformed
    String sIn = (capLock)? "L": "l";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('l');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonLActionPerformed

  private void jButtonDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelActionPerformed
    String str = textArea.getText();
    int len = str.length();
    if (len > 0) {
      textArea.setText(str.substring(0, len-1));
    }
    else
      textArea.setText("");
    box.setVisible(false);
  }//GEN-LAST:event_jButtonDelActionPerformed

  private void jButtonCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCapActionPerformed
    capLock = (capLock)? false: true;
    if (capLock)
      //jButtonCap.setBorder(new LineBorder(Color.blue, 1, true));
      jButtonCap.setBackground(new Color(66, 139, 221));
    else
      //jButtonCap.setBorder(UIManager.getBorder("Button.border"));
      jButtonCap.setBackground(new Color(220, 220, 220));
    box.setVisible(false);
  }//GEN-LAST:event_jButtonCapActionPerformed

  private void jButtonZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZActionPerformed
    String sIn = (capLock)? "Z": "z";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('z');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonZActionPerformed

  private void jButtonXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXActionPerformed
    String sIn = (capLock)? "X": "x";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('x');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonXActionPerformed

  private void jButtonCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCActionPerformed
    String sIn = (capLock)? "C": "c";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('c');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonCActionPerformed

  private void jButtonBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBActionPerformed
    String sIn = (capLock)? "B": "b";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('b');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonBActionPerformed

  private void jButtonNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNActionPerformed
    String sIn = (capLock)? "N": "n";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('n');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonNActionPerformed

  private void jButtonMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMActionPerformed
    String sIn = (capLock)? "M": "m";
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(sIn);
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(sIn);
    
    capLock = false;
    jButtonCap.setBackground(new Color(220, 220, 220));
    
    box.updateBox('m');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonMActionPerformed

  private void jButtonCtrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCtrlActionPerformed
    box.setVisible(false);
    MIA.mouse.moveToCtrl();
  }//GEN-LAST:event_jButtonCtrlActionPerformed

  private void jButtonCommaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCommaActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(", ");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(", ");
    box.updateBox(' ');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonCommaActionPerformed

  private void jButtonPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPeriodActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(".");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(".");
    box.setVisible(false);
  }//GEN-LAST:event_jButtonPeriodActionPerformed

  private void jButtonQuestionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuestionActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("? ");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("? ");
    box.updateBox(' ');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonQuestionActionPerformed

  private void jButtonDashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDashActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("-");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("-");
    box.setVisible(false);
  }//GEN-LAST:event_jButtonDashActionPerformed

  private void jButtonAtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAtActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("@");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("@");
    box.setVisible(false);
  }//GEN-LAST:event_jButtonAtActionPerformed

  private void jButtonDollarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDollarActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("$");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("$");
    box.setVisible(false);
  }//GEN-LAST:event_jButtonDollarActionPerformed

  private void jButtonPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPercentageActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection("%");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append("%");
    box.setVisible(false);
  }//GEN-LAST:event_jButtonPercentageActionPerformed

  private void jButtonSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSpaceActionPerformed
    if(mode == Mode.COMPLETION) {
      textArea.replaceSelection(" ");
      mode = Mode.INSERT;
      box.colorOff();
    }
    else
      textArea.append(" ");
    box.updateBox(' ');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonSpaceActionPerformed

  private void jButtonEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEnterActionPerformed
    if (mode == Mode.COMPLETION) {
      int pos = textArea.getSelectionEnd();
      textArea.insert(" ", pos);
      textArea.setCaretPosition(pos + 1);
      mode = Mode.INSERT;
      box.colorOff();
    } else {
      textArea.replaceSelection("\n");
    }
    box.updateBox(' ');
    box.setVisible(true);
  }//GEN-LAST:event_jButtonEnterActionPerformed

  private void jButtonUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUndoActionPerformed
    try {
      undoManager.undo();
    } catch (CannotRedoException e) {
      e.printStackTrace();
    }
    updateUndoRedoState();
    box.setVisible(false);
  }//GEN-LAST:event_jButtonUndoActionPerformed

  private void jButtonRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRedoActionPerformed
    try {
      undoManager.redo();
    } catch (CannotRedoException e) {
      e.printStackTrace();
    }
    updateUndoRedoState();
    box.setVisible(false);
  }//GEN-LAST:event_jButtonRedoActionPerformed

  private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed
    textArea.selectAll();
    textArea.copy();
    box.setVisible(false);
  }//GEN-LAST:event_jButtonCopyActionPerformed

  private void jButtonCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCutActionPerformed
    textArea.selectAll();
    textArea.cut();
    box.setVisible(false);
  }//GEN-LAST:event_jButtonCutActionPerformed

  private void jButtonPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteActionPerformed
    textArea.selectAll();
    textArea.paste();
    box.setVisible(false);
  }//GEN-LAST:event_jButtonPasteActionPerformed

  private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed
    box.setVisible(false);
    fileChooser.setSelectedFile(new File("quote.txt"));
    if (fileChooser.showOpenDialog(myJFrame.this) == JFileChooser.APPROVE_OPTION) {
      File inFile = fileChooser.getSelectedFile();
      textArea.setText("");
      Scanner inScan = null;
      try {
        inScan = new Scanner(inFile);
        while(inScan.hasNext()) {
          String inLine = inScan.nextLine();
          textArea.append(inLine + "\n");
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        inScan.close();
      }
    }
  }//GEN-LAST:event_jButtonOpenActionPerformed

  private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
    box.setVisible(false);
    fileChooser.setSelectedFile(new File("text_121113.txt"));
    if (fileChooser.showSaveDialog(myJFrame.this) == JFileChooser.APPROVE_OPTION) {
      File outFile = fileChooser.getSelectedFile();
      PrintWriter out = null;
      try {
        out = new PrintWriter(outFile);
        String outBuf = textArea.getText();
        out.println(outBuf);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {out.flush();} catch(Exception ex1) {}
        try {out.close();} catch(Exception ex1) {}
      }
    }
  }//GEN-LAST:event_jButtonSaveActionPerformed

  private void jButtonQMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonQMouseEntered
    jButtonQ.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonQMouseEntered

  private void jButtonQMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonQMouseExited
    jButtonQ.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonQMouseExited

  private void jButtonEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEmailActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_jButtonEmailActionPerformed

  private void jButtonWMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonWMouseEntered
    jButtonW.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonWMouseEntered

  private void jButtonWMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonWMouseExited
    jButtonW.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonWMouseExited

  private void jButtonEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEMouseEntered
    jButtonE.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonEMouseEntered

  private void jButtonEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEMouseExited
    jButtonE.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonEMouseExited

  private void jButtonRMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRMouseEntered
    jButtonR.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonRMouseEntered

  private void jButtonRMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRMouseExited
    jButtonR.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonRMouseExited

  private void jButtonTMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonTMouseEntered
    jButtonT.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonTMouseEntered

  private void jButtonTMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonTMouseExited
    jButtonT.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonTMouseExited

  private void jButtonYMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonYMouseEntered
    jButtonY.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonYMouseEntered

  private void jButtonYMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonYMouseExited
    jButtonY.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonYMouseExited

  private void jButtonUMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUMouseEntered
    jButtonU.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonUMouseEntered

  private void jButtonUMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUMouseExited
    jButtonU.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonUMouseExited

  private void jButtonIMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonIMouseEntered
    jButtonI.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonIMouseEntered

  private void jButtonIMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonIMouseExited
    jButtonI.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonIMouseExited

  private void jButtonOMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOMouseEntered
    jButtonO.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonOMouseEntered

  private void jButtonOMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOMouseExited
    jButtonO.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonOMouseExited

  private void jButtonPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPMouseEntered
    jButtonP.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonPMouseEntered

  private void jButtonPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPMouseExited
    jButtonP.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonPMouseExited

  private void jButtonAMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAMouseEntered
    jButtonA.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonAMouseEntered

  private void jButtonAMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAMouseExited
    jButtonA.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonAMouseExited

  private void jButtonSMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSMouseEntered
    jButtonS.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonSMouseEntered

  private void jButtonSMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSMouseExited
    jButtonS.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonSMouseExited

  private void jButtonDMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDMouseEntered
    jButtonD.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonDMouseEntered

  private void jButtonDMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDMouseExited
    jButtonD.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonDMouseExited

  private void jButtonFMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFMouseEntered
    jButtonF.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonFMouseEntered

  private void jButtonFMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonFMouseExited
    jButtonF.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonFMouseExited

  private void jButtonGMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonGMouseEntered
    jButtonG.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonGMouseEntered

  private void jButtonGMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonGMouseExited
    jButtonG.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonGMouseExited

  private void jButtonHMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonHMouseEntered
    jButtonH.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonHMouseEntered

  private void jButtonHMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonHMouseExited
    jButtonH.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonHMouseExited

  private void jButtonJMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonJMouseEntered
    jButtonJ.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonJMouseEntered

  private void jButtonJMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonJMouseExited
    jButtonJ.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonJMouseExited

  private void jButtonKMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonKMouseEntered
    jButtonK.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonKMouseEntered

  private void jButtonKMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonKMouseExited
    jButtonK.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonKMouseExited

  private void jButtonLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonLMouseEntered
    jButtonL.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonLMouseEntered

  private void jButtonLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonLMouseExited
    jButtonL.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonLMouseExited

  private void jButtonDelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDelMouseEntered
    jButtonDel.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonDelMouseEntered

  private void jButtonDelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDelMouseExited
    jButtonDel.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonDelMouseExited

  private void jButtonZMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonZMouseEntered
    jButtonZ.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonZMouseEntered

  private void jButtonZMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonZMouseExited
    jButtonZ.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonZMouseExited

  private void jButtonXMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonXMouseEntered
    jButtonX.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonXMouseEntered

  private void jButtonXMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonXMouseExited
    jButtonX.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonXMouseExited

  private void jButtonCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCMouseEntered
    jButtonC.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonCMouseEntered

  private void jButtonCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCMouseExited
    jButtonC.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonCMouseExited

  private void jButtonVMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonVMouseEntered
    jButtonV.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonVMouseEntered

  private void jButtonVMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonVMouseExited
    jButtonV.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonVMouseExited

  private void jButtonBMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonBMouseEntered
    jButtonB.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonBMouseEntered

  private void jButtonBMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonBMouseExited
    jButtonB.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonBMouseExited

  private void jButtonNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNMouseEntered
    jButtonN.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonNMouseEntered

  private void jButtonNMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNMouseExited
    jButtonN.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonNMouseExited

  private void jButtonMMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMMouseEntered
    jButtonM.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonMMouseEntered

  private void jButtonMMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMMouseExited
    jButtonM.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonMMouseExited

  private void jButtonCapMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCapMouseEntered
    jButtonCap.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonCapMouseEntered

  private void jButtonCapMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCapMouseExited
    jButtonCap.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonCapMouseExited

  private void jButtonCommaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCommaMouseEntered
    jButtonComma.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonCommaMouseEntered

  private void jButtonCommaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCommaMouseExited
    jButtonComma.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonCommaMouseExited

  private void jButtonPeriodMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPeriodMouseEntered
    jButtonPeriod.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonPeriodMouseEntered

  private void jButtonPeriodMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPeriodMouseExited
    jButtonPeriod.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonPeriodMouseExited

  private void jButtonQuestionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonQuestionMouseEntered
    jButtonQuestion.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonQuestionMouseEntered

  private void jButtonQuestionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonQuestionMouseExited
    jButtonQuestion.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonQuestionMouseExited

  private void jButtonDashMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDashMouseEntered
    jButtonDash.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonDashMouseEntered

  private void jButtonDashMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDashMouseExited
    jButtonDash.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonDashMouseExited

  private void jButtonAtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAtMouseEntered
    jButtonAt.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonAtMouseEntered

  private void jButtonAtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAtMouseExited
    jButtonAt.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonAtMouseExited

  private void jButtonDollarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDollarMouseEntered
    jButtonDollar.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonDollarMouseEntered

  private void jButtonDollarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDollarMouseExited
    jButtonDollar.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonDollarMouseExited

  private void jButtonPercentageMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPercentageMouseEntered
    jButtonPercentage.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonPercentageMouseEntered

  private void jButtonPercentageMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPercentageMouseExited
    jButtonPercentage.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonPercentageMouseExited

  private void jButtonSpaceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSpaceMouseEntered
    jButtonSpace.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonSpaceMouseEntered

  private void jButtonSpaceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSpaceMouseExited
    jButtonSpace.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonSpaceMouseExited

  private void jButtonCtrlMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCtrlMouseEntered
    jButtonCtrl.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonCtrlMouseEntered

  private void jButtonCtrlMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCtrlMouseExited
    jButtonCtrl.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonCtrlMouseExited

  private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
    jButton1.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton1MouseEntered

  private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited
    jButton1.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton1MouseExited

  private void jButton2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseEntered
    jButton2.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton2MouseEntered

  private void jButton2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseExited
    jButton2.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton2MouseExited

  private void jButton3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseEntered
    jButton3.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton3MouseEntered

  private void jButton3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseExited
    jButton3.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton3MouseExited

  private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered
    jButton4.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton4MouseEntered

  private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
    jButton4.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton4MouseExited

  private void jButton5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseEntered
    jButton5.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton5MouseEntered

  private void jButton5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseExited
    jButton5.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton5MouseExited

  private void jButton6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseEntered
    jButton6.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton6MouseEntered

  private void jButton6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MouseExited
    jButton6.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton6MouseExited

  private void jButton7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseEntered
    jButton7.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton7MouseEntered

  private void jButton7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MouseExited
    jButton7.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton7MouseExited

  private void jButton8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseEntered
    jButton8.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton8MouseEntered

  private void jButton8MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MouseExited
    jButton8.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton8MouseExited

  private void jButton9MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseEntered
    jButton9.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton9MouseEntered

  private void jButton9MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseExited
    jButton9.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton9MouseExited

  private void jButton0MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton0MouseEntered
    jButton0.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButton0MouseEntered

  private void jButton0MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton0MouseExited
    jButton0.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButton0MouseExited

  private void jButtonUndoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUndoMouseEntered
    jButtonUndo.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonUndoMouseEntered

  private void jButtonUndoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonUndoMouseExited
    jButtonUndo.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonUndoMouseExited

  private void jButtonRedoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRedoMouseEntered
    jButtonRedo.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonRedoMouseEntered

  private void jButtonRedoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonRedoMouseExited
    jButtonRedo.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonRedoMouseExited

  private void jButtonCopyMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCopyMouseEntered
    jButtonCopy.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonCopyMouseEntered

  private void jButtonCopyMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCopyMouseExited
    jButtonCopy.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonCopyMouseExited

  private void jButtonCutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCutMouseEntered
    jButtonCut.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonCutMouseEntered

  private void jButtonCutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCutMouseExited
    jButtonCut.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonCutMouseExited

  private void jButtonPasteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPasteMouseEntered
    jButtonPaste.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonPasteMouseEntered

  private void jButtonPasteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPasteMouseExited
    jButtonPaste.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonPasteMouseExited

  private void jButtonSaveMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSaveMouseEntered
    jButtonSave.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonSaveMouseEntered

  private void jButtonSaveMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSaveMouseExited
    jButtonSave.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonSaveMouseExited

  private void jButtonOpenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOpenMouseEntered
    jButtonOpen.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonOpenMouseEntered

  private void jButtonOpenMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOpenMouseExited
    jButtonOpen.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonOpenMouseExited

  private void jButtonKeyBMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonKeyBMouseEntered
    jButtonKeyB.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonKeyBMouseEntered

  private void jButtonKeyBMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonKeyBMouseExited
    jButtonKeyB.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonKeyBMouseExited

  private void jButtonEnterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEnterMouseEntered
    jButtonEnter.setBorder(new LineBorder(new Color(66, 139, 221), 2, true));
  }//GEN-LAST:event_jButtonEnterMouseEntered

  private void jButtonEnterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonEnterMouseExited
    jButtonEnter.setBorder(UIManager.getBorder("Button.border"));
  }//GEN-LAST:event_jButtonEnterMouseExited

  private void jButtonKeyBActionPerformed(java.awt.event.ActionEvent evt) {                                          
    box.setVisible(false);
    MIA.mouse.moveToKeyB();
  }                                           

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(myJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
        //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
          try {
              new myJFrame().setVisible(true);
          } catch (AWTException ex) {
              Logger.getLogger(myJFrame.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JLabel clkJLabel;
  public javax.swing.JLabel cogJLabel;
  private javax.swing.JLayeredPane ctrlLayerPane;
  public javax.swing.JLabel expJLabel;
  private javax.swing.JButton jButton0;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  private javax.swing.JButton jButton4;
  private javax.swing.JButton jButton5;
  private javax.swing.JButton jButton6;
  private javax.swing.JButton jButton7;
  private javax.swing.JButton jButton8;
  private javax.swing.JButton jButton9;
  private javax.swing.JButton jButtonA;
  private javax.swing.JButton jButtonAt;
  private javax.swing.JButton jButtonB;
  private javax.swing.JButton jButtonC;
  private javax.swing.JButton jButtonCap;
  private javax.swing.JButton jButtonComma;
  private javax.swing.JButton jButtonCopy;
  private javax.swing.JButton jButtonCtrl;
  private javax.swing.JButton jButtonCut;
  private javax.swing.JButton jButtonD;
  private javax.swing.JButton jButtonDash;
  private javax.swing.JButton jButtonDel;
  private javax.swing.JButton jButtonDollar;
  private javax.swing.JButton jButtonE;
  private javax.swing.JButton jButtonEmail;
  private javax.swing.JButton jButtonEnter;
  private javax.swing.JButton jButtonF;
  private javax.swing.JButton jButtonFB;
  private javax.swing.JButton jButtonG;
  private javax.swing.JButton jButtonH;
  private javax.swing.JButton jButtonI;
  private javax.swing.JButton jButtonJ;
  private javax.swing.JButton jButtonK;
  private javax.swing.JButton jButtonKeyB;
  private javax.swing.JButton jButtonL;
  private javax.swing.JButton jButtonM;
  private javax.swing.JButton jButtonMsn;
  private javax.swing.JButton jButtonN;
  private javax.swing.JButton jButtonO;
  private javax.swing.JButton jButtonOpen;
  private javax.swing.JButton jButtonP;
  private javax.swing.JButton jButtonPaste;
  private javax.swing.JButton jButtonPercentage;
  private javax.swing.JButton jButtonPeriod;
  private javax.swing.JButton jButtonQ;
  private javax.swing.JButton jButtonQuestion;
  private javax.swing.JButton jButtonR;
  private javax.swing.JButton jButtonRedo;
  private javax.swing.JButton jButtonS;
  private javax.swing.JButton jButtonSave;
  private javax.swing.JButton jButtonSpace;
  private javax.swing.JButton jButtonT;
  private javax.swing.JButton jButtonTT;
  private javax.swing.JButton jButtonU;
  private javax.swing.JButton jButtonUndo;
  private javax.swing.JButton jButtonV;
  private javax.swing.JButton jButtonW;
  private javax.swing.JButton jButtonX;
  private javax.swing.JButton jButtonY;
  private javax.swing.JButton jButtonZ;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLayeredPane jLayeredPane1;
  private javax.swing.JLayeredPane keysLayeredPane;
  public javax.swing.JLabel movDJLabel;
  public javax.swing.JLabel movRJLabel;
  public javax.swing.JLabel sigJLabel;
  private javax.swing.JTextArea textArea;
  private javax.swing.JLayeredPane textLayerPane;
  private javax.swing.JScrollPane textScrollPane;
  // End of variables declaration//GEN-END:variables
}
