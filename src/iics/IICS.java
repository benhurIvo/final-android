/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iics;

import javax.swing.*;

/**
 *
 * @author benhur
 */
public class IICS {

    /**
     * @param args the command line arguments
     */
    static Connection cn = new Connection();

    public static void main(String[] args) throws Exception {
	try {
	    cn.setinfo();

	} catch (Exception ex) {
	    cn.close_loda();
	    JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);

	}

    }

}
