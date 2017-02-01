/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.RandomStringUtils;
import static org.apache.commons.lang3.StringUtils.rightPad;

/**
 *
 * @author benhur
 */
public class Connection {

    private static boolean success;
    static String IV = RandomStringUtils.randomAlphabetic(16);
    static String plaintext = "";//rightPad("benhur", 128, "\0"); /*Note null padding*/
    static String encryptionKey = RandomStringUtils.randomAlphabetic(16);
    private static final String dir = System.getProperty("user.home") + "/.httpfile";
    public static final File f = new File(dir + File.separator + "file.html");
    private static final File f1 = new File(dir + File.separator + "file.txt");
    private static String content = "";
    static FileWriter fw = null;
    static byte[] cipher;
    static String path = null;
    static Details dt = new Details();
    static Loader loda = new Loader();
    static String det = "det";     
    static Timer timer;

    

    void setinfo() throws Exception {
	loda.createUI();
	//Get the computer details

	plaintext = rightPad((dt.getLHost().get(1)).trim(), 256, "\0");
	//System.out.println("data " + IV + "\n" + encryptionKey);

	//bytes to enctypt
	cipher = Enc.encrypt(plaintext, encryptionKey);

	String decoded = new String(cipher, "ISO-8859-1");

	byte[] encoded = decoded.getBytes("ISO-8859-1");

	String urlParameters
		= "&s=" + URLEncoder.encode(IV.toString(), "ISO-8859-1")
		+ "&s1=" + URLEncoder.encode(encryptionKey.toString(), "ISO-8859-1")
		+ "&b=" + URLEncoder.encode(new String(cipher, "ISO-8859-1"), "ISO-8859-1");
	Base64Dec bs64dec = new Base64Dec();
	path = bs64dec.decode(readfile());
	//System.out.println("wowowow "+decoded);
	excutePost("http://" + path + "/IICS/setinfo.htm", urlParameters);
	//create_dir();
    }

    //Read the encrypted path from file
    String readfile() {

	String link = "";
	try {
	    try (BufferedReader br = new BufferedReader(new FileReader(f1.getAbsoluteFile()))) {
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
		    sb.append(line);
		    sb.append(System.lineSeparator());
		    line = br.readLine();
		}
		link = sb.toString();
		// System.out.println("files "+link);
	    }
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
	    close_loda();
	}
	return link;
    }

    //Posting the data to server (its encrypted)
    public String excutePost(String targetURL, String urlParameters) {

	URL url;
	HttpURLConnection connection = null;
	try {
	    //Create connection
	    url = new URL(targetURL);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type",
		    "application/x-www-form-urlencoded");

	    connection.setRequestProperty("Content-Length", ""
		    + Integer.toString(urlParameters.getBytes().length));
	    connection.setRequestProperty("Content-Language", "en-US");

	    connection.setUseCaches(false);
	    connection.setDoInput(true);
	    connection.setDoOutput(true);

	    //Send request
	    DataOutputStream wr = new DataOutputStream(
		    connection.getOutputStream());
	    wr.writeBytes(urlParameters);
	    wr.flush();
	    wr.close();

	    //Get Response	
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    String line;
	    StringBuffer response = new StringBuffer();
	    while ((line = rd.readLine()) != null) {
		if (!line.equals("")) {
		    response.append(line);
		}
		//response.append('\r');
	    }
	    rd.close();

	    // System.out.println("gigig " + response.toString());
	    if (response.toString().substring(0, 8).equals("Response")) {
		String []dett = response.toString().split("~~~");
		det = dett[1];
		create_dir();
		openweb(f.toString());
	    } else {
		//System.out.println("err "+ex.getMessage());
		JOptionPane.showMessageDialog(null, "                 An error occured!! \n Try again later or contact your system admin for help.");
		close_loda();
	    }
	    return response.toString();

	} catch (Exception e) {

	   JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
		close_loda();
	    return null;

	} finally {

	    if (connection != null) {
		connection.disconnect();
	    }
	}
    }

    //Open the browser
    static void openweb(String url) {
	String os = System.getProperty("os.name").toLowerCase();
	Runtime rt = Runtime.getRuntime();
changefile();
	try {

	    if (os.indexOf("win") >= 0) {

		// this doesn't support showing urls in the form of "page.html#nameLink" 
		rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

	    } else if (os.indexOf("mac") >= 0) {

		rt.exec("open " + url);

	    } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {

		// Do a best guess on unix until we get a platform independent way
		// Build a list of browsers to try, in this order.
		String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
		    "netscape", "opera", "links", "lynx"};

		// Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
		StringBuffer cmd = new StringBuffer();
		for (int i = 0; i < browsers.length; i++) {
		    cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");
		}

		rt.exec(new String[]{"sh", "-c", cmd.toString()});

	    } else {
		return;
	    }
	} catch (Exception e) {
	    return;
	}
	changefile();
    }

    static void create_dir() {

	try {
	    File directory = new File(dir);
	    if (directory.exists()) {
		create_file();

	    } else {
		System.out.println("Directory not exists, creating now");

		success = directory.mkdir();
		if (success) {
		    create_file();
		} else {
		    System.out.printf("Failed to create new directory: %s%n", dir);
		    JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
		    close_loda();
		}
	    }
	    fw = new FileWriter(f.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    bw.write(content);
	    bw.close();
	} catch (IOException ex) {
	    //   Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
	    JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
	    close_loda();
	} finally {
	    try {
		fw.close();
	    } catch (IOException ex) {
		//	Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
		JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
		close_loda();
	    }
	}
    }

    static void create_file() {
	try {
	    if (f.exists()) {
		content = "<html><head>"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\"> "
			+ "</head><body onload='document.form1.submit()'>\n"
			+ "<form id='form1' name='form1' method='post' action='" + "http://" + path + "/IICS/" + "/'>\n"
			+ "<input type='type' name='s' value='" + IV.toString() + "' />\n"
			+ "<input type='type' name='s1' value='" + encryptionKey.toString() + "' />\n"
			+ "<input type='type' name='b' value='" + Base64Enc.encode((IV.toString() + "~~~" + dt.getLHost().get(1) + "~~~"+det+"~~~" + encryptionKey.toString()).trim()) + "' />\n";

		content = content + "</form>\n"
			+ "</body></html>";
		fw = new FileWriter(f.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();

	    } else {

		System.out.println("No such file exists, creating now");
		success = f.createNewFile();
		if (success) {
		    System.out.printf("Successfully created new file: %s%n", f);
		} else {
		    //  System.out.printf("Failed to create new file: %s%n", f);
		    JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
		    close_loda();
		}

	    }
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(null, "                 An error occured!! \n Contact your system admin for help.", null, JOptionPane.WARNING_MESSAGE);
	    close_loda();
	}
    }

    static void close_loda() {
	loda.frame.setVisible(false); //you can't see me!
	loda.frame.dispose(); //Destroy the JFrame object
	Runtime.getRuntime().exit(0);
    }
    
    static void changefile(){
    
	timer = new java.util.Timer();
	timer.schedule(new RemindTask(), 5 * 1000);
	
    }
}
