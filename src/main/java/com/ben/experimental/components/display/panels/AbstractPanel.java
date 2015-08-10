package com.ben.experimental.components.display.panels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Ben on 8/6/2015.
 */
public abstract class AbstractPanel extends JPanel implements KeyListener {
    private Logger LOG = LogManager.getLogger(this.getClass());

    public AbstractPanel() {
        super(true);
    }

    public void initialize() {
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        setVisible(true);
    }

    public void cleanUp() {
        setVisible(false);
        setFocusable(false);
        removeKeyListener(this);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }
}
