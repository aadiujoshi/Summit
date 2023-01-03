package summit.testing;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import summit.util.Time;

public class ignore extends javax.swing.JComponent{
    
    private boolean terminate;

    ignore(){
        
        super.setEnabled(true);
        super.setFocusable(true);

        super.setInputMap(UNDEFINED_CONDITION, new InputMap());
        super.getInputMap().put(KeyStroke.getKeyStroke('p'), "terminate");

		super.getActionMap().put("terminate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminate = true;
            }
        });

        java.awt.Dimension s = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        
        while(!terminate){
            System.out.println(terminate);

            Time.nanoDelay(Time.NS_IN_S*3);
            
            r.mouseMove(1210, 650);
            // r.mousePress(ABORT);
        }
    }

    public static void main0(String[] args) {
        new ignore();
    }

}
