//package com.ivo;
//
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.R.color;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.util.Log;
//import android.view.Menu;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class MainActivity extends Activity {
//	final Context context = this;
//	AlertDialog.Builder myDialog;
//	WebView webview;
//	private static final String TAG = "Main";
//	private ProgressDialog progressBar;
//	private Object myJSInterface;
//	MyUtils mut = new MyUtils();
//	JSONObject jsonObj = null;
//	String tagt = "";
//	
//	private static final String targetURL = "http://192.168.1.105:5700/sdelab/person";
//	
//	
//	@SuppressLint("JavascriptInterface")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		webview = (WebView) findViewById(R.id.webView1);
//		webview.getSettings().setBuiltInZoomControls(true);
//		webview.addJavascriptInterface(myJSInterface, "JSInterface");
//		WebSettings settings = webview.getSettings();
//		settings.setJavaScriptEnabled(true);
//        webview.setWebViewClient(new WebViewClient());
//        webview.setWebChromeClient(new WebChromeClient());
//		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//		webview.setBackgroundColor(0);
//		webview.setBackgroundResource(color.background_light);
//		webview.addJavascriptInterface(this, "Android");
//
//		if (String.valueOf(isConnectingToInternet()).equals("false"))
//			not_connected();
//		else {
//				loadPage();
//			
//
//		}
//	}
//
//	public String posst(String dat){
//	    try {
//	  
//		String [] dt = dat.split("~~");
//		String per = mut.sendurl(targetURL, "POST", dt[0]);
//		System.out.println("bk "+per);
//		//Toast.makeText(context, bk, Toast.LENGTH_SHORT).show();
////		Log.e("aww ", "here we r");
//			
////	  
//		tagt = targetURL+"/typ/weight";
//	   String wtt =  mut.sendurl(tagt, "GET", "");
//	   if(wtt.equals("[]")||wtt.equals(null)||wtt.equals("")){
//	    JSONObject wt = new JSONObject();
//		wt.put("type","weight");
//		wt.put("measure","kg");
//		tagt = targetURL+"/typ";
//	   wtt =  mut.sendurl(tagt, "POST", wt.toString());
//	   }
//	   
//	   	tagt = targetURL+"/typ/height";
//	   String htt =  mut.sendurl(tagt, "GET", "");
//	   if(htt.equals("[]")||htt.equals(null)||htt.equals("")){
//	    JSONObject ht = new JSONObject();
//		ht.put("type","height");
//		ht.put("measure","m");
//		tagt = targetURL+"/typ";
//	  htt =  mut.sendurl(tagt, "POST", ht.toString());
//	   }
//	   
//			
//	JSONObject wtobj = new JSONObject(dt[1]);
//	JSONObject htobj = new JSONObject(dt[2]);
//	JSONObject wttobj = new JSONObject(wtt.substring(1, wtt.length()-1));
//	JSONObject httobj = new JSONObject(htt.substring(1, htt.length()-1));
//	JSONObject perobj = new JSONObject(per);
//	System.out.println("wt "+wtobj.getString("wt"));
//	System.out.println("ht "+htobj.getString("ht"));
//	System.out.println("httobj "+httobj.getString("tid"));
//	System.out.println("wttobj "+wttobj.getString("tid"));
//		
//	
//	JSONObject hph = new JSONObject();
//		hph.put("pid",perobj.getString("pid"));
//		Date det = new Date();
//	    hph.put("datecreated",new SimpleDateFormat("yyyy-MM-dd").format(det));		   
//		hph.put("tid",httobj.getString("tid"));
//		
//	    tagt = targetURL+"/hp";
//	    String hp_ht = mut.sendurl(tagt, "POST", hph.toString());
//	    System.out.println("hp_ht "+hp_ht);
//	    
//	    JSONObject hpw = new JSONObject();
//		hpw.put("pid",perobj.getString("pid"));
//	    hpw.put("datecreated",new SimpleDateFormat("yyyy-MM-dd").format(det));		   
//		hpw.put("tid",wttobj.getString("tid"));
//		
//	    tagt = targetURL+"/hp";
//	    String hp_hw = mut.sendurl(tagt, "POST", hpw.toString());
//	    System.out.println("hp_wt "+hp_hw);
//	
//	    webview.loadUrl("javascript:cloz_reg()");
//	    } catch (Exception ex) {
//		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//	    }
//	    
//		return "haha";
//	}
//	
//	
//	public String gett(){
//	    try {
//	String bk = mut.sendurl(targetURL, "GET", "");
//  System.out.println("pips "+ bk);
//  String bk1= "";
//  if(bk.contains("["))
//      bk1= bk.substring(1, bk.length()-1);
//  else
//      bk1=bk;
//
//webview.loadUrl("javascript:reg_users(" + bk + ")");
//
//
////
//	    } catch (Exception ex) {
//		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//	    }
//	    
//		return "haha";
//	}
//	
//	/******************************** Check if connected to internet *******************/
//	public boolean isConnectingToInternet() {
//		ConnectivityManager connectivity = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if (connectivity != null) {
//			NetworkInfo[] info = connectivity.getAllNetworkInfo();
//			if (info != null)
//				for (int i = 0; i < info.length; i++)
//					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//						return true;
//					}
//
//		}
//		return false;
//	}
//
//	/********************** if not connected, alert *************************/
//	private void not_connected() {
//
//		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//		// Setting Dialog Title
//		alertDialog.setTitle(" ");
//		//alertDialog.setIcon(R.drawable.i);
//
//		// Setting Dialog Message
//		alertDialog.setMessage("No Internet Connection!");
//
//		// Setting OK Button
//		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
//
//		// Showing Alert Message
//		alertDialog.show();
//	}
//	
//	//Connecting to server
//		public void loadPage() {
//			//read server ip from internal memory
//			/************************ Loading page in webview *******************************/
//			final AlertDialog alertDialog = new AlertDialog.Builder(context)
//					.create();
//
//			progressBar = ProgressDialog.show(context, "...", "Loading...");
//
//			webview.setWebViewClient(new WebViewClient() {
//				public boolean shouldOverrideUrlLoading(WebView view, String url) {
//					Log.i(TAG, "Processing webview url click...");
//					view.loadUrl(url);
//					return true;
//				}
//
//				public void onPageFinished(WebView view, String url) {
//					Log.i(TAG, "Finished loading URL: " + url);
//					//if (progressBar.isShowing()) {
//						progressBar.dismiss();
//					//}
//				}
//
//				public void onReceivedError(WebView view, int errorCode,
//						String description, String failingUrl) {
//					webview.loadUrl("file:///android_asset/err.html");
//	                alertDialog.setTitle("ERROR!!");
//	                alertDialog.setMessage("Unable to Connect!!");
//					alertDialog.setButton("OK",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int which) {
//									return;
//								}
//							});
//					alertDialog.show();
//				}
//			});
//			
//
//			
//			webview.loadUrl("file:///android_asset/advanced.html");
//
//			/**
//			 * @return
//			 ******************************************************************/
//		}
//
//	
//	//Exit when button pressed on webview
//	 public void exitApp()
//	    {
//	        finish();
//	    }
//
//}
