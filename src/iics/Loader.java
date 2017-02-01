/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.LayerUI;

/**
 *
 * @author benhur
 */
public class Loader {

    JFrame frame = new JFrame("IICS");

    public void createUI() {

	final WaitLayerUI layerUI = new WaitLayerUI();
	JPanel panel = createPanel();
	JLayer<JPanel> jlayer = new JLayer<JPanel>(panel, layerUI);

	final Timer stopper = new Timer(4000, new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		// layerUI.stop();
	    }
	});
	stopper.setRepeats(false);
	layerUI.start();
//    mOrderButton.addActionListener(
//      new ActionListener() {
//        public void actionPerformed(ActionEvent ae) {
//          layerUI.start();
//          if (!stopper.isRunning()) {
//            stopper.start();
//          }
//        }
//      }
//    );
	frame.setUndecorated(true);
	frame.add(jlayer);

	frame.setSize(450, 150);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    private JPanel createPanel() {
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

	JPanel p1 = new JPanel();
	p1 = new JPanel() {
	    public void paintComponent(Graphics g) {
		Image img = Toolkit.getDefaultToolkit().getImage(
			Loader.class.getResource("iics.png"));
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    }
	};

	p1.setMaximumSize(new Dimension(500 * 35, 150));
	JPanel p2 = new JPanel();
	p2 = new JPanel() {
	    public void paintComponent(Graphics g) {
		Image img = Toolkit.getDefaultToolkit().getImage(
			Loader.class.getResource("logo.png"));
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    }
	};
	p.add(p1);
	p.add(p2);
	return p;
    }
}

class WaitLayerUI extends LayerUI<JPanel> implements ActionListener {

    private boolean mIsRunning;
    private boolean mIsFadingOut;
    private Timer mTimer;

    private int mAngle;
    private int mFadeCount;
    private int mFadeLimit = 15;

    @Override
    public void paint(Graphics g, JComponent c) {
	int w = c.getWidth();
	int h = c.getHeight();

	// Paint the view.
	super.paint(g, c);

	if (!mIsRunning) {
	    return;
	}

	Graphics2D g2 = (Graphics2D) g.create();

	float fade = (float) mFadeCount / (float) mFadeLimit;
	// Gray it out.
	Composite urComposite = g2.getComposite();
	g2.setComposite(AlphaComposite.getInstance(
		AlphaComposite.SRC_OVER, .0f * fade));
	g2.fillRect(0, 0, w, h);
	g2.setComposite(urComposite);

	// Paint the wait indicator.
	int s = Math.min(w, h) / 5;
	int cx = w / 6;
	int cy = h / 2;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setStroke(
		new BasicStroke(s / 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	g2.setPaint(Color.white);
	g2.rotate(Math.PI * mAngle / 180, cx, cy);
	for (int i = 0; i < 12; i++) {
	    float scale = (11.0f - (float) i) / 11.0f;
	    g2.drawLine(cx + s, cy, cx + s * 2, cy);
	    g2.rotate(-Math.PI / 6, cx, cy);
	    g2.setComposite(AlphaComposite.getInstance(
		    AlphaComposite.SRC_OVER, scale * fade));
	}

	g2.dispose();
    }

    public void actionPerformed(ActionEvent e) {
	if (mIsRunning) {
	    firePropertyChange("tick", 0, 1);
	    mAngle += 3;
	    if (mAngle >= 360) {
		mAngle = 0;
	    }
	    if (mIsFadingOut) {
		if (--mFadeCount == 0) {
		    mIsRunning = false;
		    mTimer.stop();
		}
	    } else if (mFadeCount < mFadeLimit) {
		mFadeCount++;
	    }
	}
    }

    public void start() {
	if (mIsRunning) {
	    return;
	}

	// Run a thread for animation.
	mIsRunning = true;
	mIsFadingOut = false;
	mFadeCount = 0;
	int fps = 24;
	int tick = 1000 / fps;
	mTimer = new Timer(tick, this);
	mTimer.start();
    }

    public void stop() {
	mIsFadingOut = true;
    }

    @Override
    public void applyPropertyChange(PropertyChangeEvent pce, JLayer l) {
	if ("tick".equals(pce.getPropertyName())) {
	    l.repaint();
	}
    }
}
