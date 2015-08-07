package com.ben.experimental.components.display.frames;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Ben on 8/4/2015.
 */
public abstract class AbstractRootFrame extends JFrame implements WindowListener {

    public AbstractRootFrame(String title) {
        super(title);
        addWindowListener(this);
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
    }
}
