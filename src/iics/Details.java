/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iics;

/**
 *
 * @author benhur
 */
/** Main class */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Details{
    public List<String> data = new ArrayList<String>();
   private InetAddress addr = null;
        String s,line = null;
        private static String currentHostIpAddress,nem,loggeduser,osName;
        int i=0;
	//System.("tutuuse "+pth[pth.length-1]);
	public void getdetails() {
		      if (currentHostIpAddress == null) {
            Enumeration<NetworkInterface> netInterfaces = null;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();

                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        InetAddress addr = address.nextElement();
  //                      log.debug("Inetaddress:" + addr.getHostAddress() + " loop? " + addr.isLoopbackAddress() + " local? "
    //                            + addr.isSiteLocalAddress());
                        if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
                                && !(addr.getHostAddress().indexOf(":") > -1)) {
                            currentHostIpAddress = addr.getHostAddress();
                            try {
                                nem=addr.getLocalHost().getCanonicalHostName();
                            } catch (UnknownHostException ex) {
                                System.out.println(ex);
                            }
;
                        }
                    }
                }
//                if (currentHostIpAddress == null) {
//                    currentHostIpAddress = "127.0.0.1";
//                }

            } catch (SocketException e) {
//                log.error("Somehow we have a socket error acquiring the host IP... Using loopback instead...");
                currentHostIpAddress = "127.0.0.1";
            }
        }
        //return currentHostIpAddress;
                s=currentHostIpAddress;
        try {
            getMacAddress();
        } catch (Exception ex) {
           System.out.println(ex);
        }
    // System.out.println(s);
	}
    
    public String getMacAddress() throws Exception {
    String macAddress = null;
    String command = "arp -a";
    loggeduser = System.getProperty("user.name"); 
    osName = System.getProperty("os.name");
    //System.out.println("Operating System is " + osName);

    if (osName.startsWith("Windows")) {
        command = "ipconfig /all";
    } else if (osName.startsWith("Linux") || osName.startsWith("Mac") || osName.startsWith("HP-UX")
            || osName.startsWith("NeXTStep") || osName.startsWith("Solaris") || osName.startsWith("SunOS")
            || osName.startsWith("FreeBSD") || osName.startsWith("NetBSD")) {
        command = "ifconfig -a";
    } else if (osName.startsWith("OpenBSD")) {
        command = "netstat -in";
    } else if (osName.startsWith("IRIX") || osName.startsWith("AIX") || osName.startsWith("Tru64")) {
        command = "netstat -ia";
    } else if (osName.startsWith("Caldera") || osName.startsWith("UnixWare") || osName.startsWith("OpenUNIX")) {
        command = "ndstat";
    } else {// Note: Unsupported system.
        throw new Exception("The current operating system '" + osName + "' is not supported.");
    }

    Process pid = Runtime.getRuntime().exec(command);
    BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
    Pattern p = Pattern.compile("([\\w]{1,2}(-|:)){5}[\\w]{1,2}");
    while (true) {
        line = in.readLine();
      // System.out.println("line " + line);
        if (line == null)
            break;

        Matcher m = p.matcher(line);
        if (m.find()) {
            macAddress = m.group();
            break;
        }
    }
    in.close();
   // System.out.println("dvdvdvdvdv "+s);
    s=macAddress+"~~~"+nem.replaceAll("\\s+","-")+"~~~"+osName.replaceAll("\\s+","-")+"~~~"+loggeduser.replaceAll("\\s+","-")+"~~~"+s;
  data.add(s);
    return macAddress;
}
	public List<String> getLHost() {
	    getdetails();
       try {
	   getMacAddress();
       } catch (Exception ex) {
	   ex.printStackTrace();
       }
		return data;
	}
}