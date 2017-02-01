/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimerTask;

/**
 *
 * @author benhur
 */
class RemindTask extends TimerTask {

	@Override
	public void run() {
	    
	    System.out.println("Time's up!");
	    try {
		FileWriter fw = new FileWriter(Connection.f.getAbsoluteFile());
	BufferedWriter bw = new BufferedWriter(fw);
		    bw.write("<html></script><head></head><body>\n"
			    + "<form id='form1' name='form1'>\n"
			    + "</form>\n"
			    + "</body></html>");
		
		//timer.cancel(); //Not necessary because we call System.exit
		System.exit(0); //Stops the AWT thread (and everything else)

	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
    }
