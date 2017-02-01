package com.ivo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {

    final Context context = this;
    AlertDialog.Builder myDialog;
    WebView webview;
    private Object myJSInterface;
    MyUtils mut = new MyUtils();
    JSONObject jsonObj = null;
    String tagt = "";
    String idz = "";
    ProgressDialog progress;
    private final static String CLIENT_ID = "d2e8c62e82a24439868579320bcb06a9";
    private final static String CLIENT_SECRET = "dccb69d503c44772886cabcc075e740a";
    private final static String CALLBACK_URL = "com.example.runkeeperapi://RunKeeperIsCallingBack";

//    private static final String targetURL = "http://192.168.1.108:5700/sdelab/control";
	private static final String targetURL = "https://hidden-basin-54030.herokuapp.com/sdelab/control";
//	private static final String targetURL = "https://hidden-savannah-56220.herokuapp.com/sdelab/flicka";
//	private static final String targetURL = "https://quiet-taiga-62056.herokuapp.com/sdelab/runkip";

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	CookieManager cookieManager = CookieManager.getInstance();
	cookieManager.removeAllCookie();
	webview = (WebView) findViewById(R.id.webView1);
	webview.getSettings().setBuiltInZoomControls(true);
	webview.addJavascriptInterface(myJSInterface, "JSInterface");
	WebSettings settings = webview.getSettings();
	settings.setJavaScriptEnabled(true);
//        webview.setWebViewClient(new WebViewClient());
//        webview.setWebChromeClient(new WebChromeClient());
	webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	webview.setBackgroundColor(0);
	webview.setBackgroundResource(color.background_light);
	webview.addJavascriptInterface(this, "Android");

	if (String.valueOf(isConnectingToInternet()).equals("false")) {
	    not_connected();
	} else {
	    loadPage();
//		    String sd = "1";
//		    getAuthorizationCode(sd);

	}
    }

    //send user data
    public void posst(String dat) {
	try {
	    lodd();
	    String per = mut.sendurl(targetURL + "/user", "POST", dat);
	    System.out.println("bk " + per);
	    unlodd();
	    webview.loadUrl("javascript:cloz_reg()");
	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}

    }

    //Edit a user
    public void eddt(String dat) {
	try {
	    lodd();
	    String per = mut.sendurl(targetURL + "/user", "PUT", dat);
	    System.out.println("bk " + per);
	    unlodd();
	    //webview.loadUrl("javascript:cloz_reg()");
	    gett();
	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}

    }

    //get all registered users
    public void gett() {
	try {
	    //lodd();
	    System.out.println("inside gett");
	    String bk = mut.makeGetRequest(targetURL + "/user_all");
	    System.out.println("pips " + bk);
  //unlodd();
	    if (!bk.contains("{")) {
		enpty();
	    } else {
		webview.loadUrl("javascript:reg_users(" + bk + ")");
	    }

//
	} catch (Exception ex) {
	    ex.printStackTrace();
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
//////progress.dismiss();
	}

	//return "haha";
    }

    //get a person's h.profile
    public void getHp(String id) {
	try {
	    lodd();
	    System.out.println("hp " + id);
	    String lnk = targetURL + "/user_hp/" + id;
	    System.out.println("link " + lnk);
	    String bk = mut.makeGetRequest(lnk);
	    unlodd();
	    System.out.println("pips " + bk);
	    if (!bk.contains("{")) {
		enpty();
	    } else {
		webview.loadUrl("javascript:hp(" + bk + ")");
	    }
	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
//////progress.dismiss();
	}

    }

	//get a person's h.profile history
    public void gethp_hist(String id) {
	try {

	    lodd();
	    System.out.println("hp " + id);
	    String lnk = targetURL + "/hp_hist/" + id;
	    System.out.println("link " + lnk);
	    String bk = mut.makeGetRequest(lnk);
	    System.out.println("pips " + bk);
	    unlodd();
	    if (!bk.contains("{")) {
		enpty();
	    } else {
		ArrayList<String> items_ht = new ArrayList<String>();
		JSONArray array = new JSONArray(bk);
		for (int i = 0; i < array.length(); i++) {
		    JSONObject row = array.getJSONObject(i);
		    if (!"".equals(row.getString("ht").trim())) {
			items_ht.add("ht-" + row.getString("hid") + " " + row.getString("date") + " " + row.getString("ht"));
		    }
		    if (!"".equals(row.getString("wt").trim())) {
			items_ht.add("wt-" + row.getString("hid") + " " + row.getString("date") + " " + row.getString("wt"));
		    }
		}
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		builderSingle.setTitle("Height and weight");

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
			context,
			android.R.layout.select_dialog_singlechoice, items_ht);

		builderSingle.setNegativeButton(
			"cancel",
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			    }
			});

		builderSingle.setAdapter(
			arrayAdapter,
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
				String strName = arrayAdapter.getItem(which);
				System.out.println("split " + strName.substring(strName.indexOf("-") + 1, strName.indexOf(" ")));
				delHp(strName.substring(strName.indexOf("-") + 1, strName.indexOf(" ")));
			    }
			});
		builderSingle.show();

	    }
	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//delete a person
    public void delPsn(String id) {
	try {
	    lodd();
	    System.out.println("del " + id);
	    String lnk = targetURL + "/" + id;
	    System.out.println("link " + lnk);
	    String bk = mut.sendurl(lnk, "DELETE", "");
	    System.out.println("pips " + bk);
	    unlodd();
	    if (!bk.contains("{")) {
		enpty();
	    } else //webview.loadUrl("javascript:reg_users(" + bk + ")");
	    {
		gett();
	    }
	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//reset the user page
    public void resett() {
	try {

	    webview.loadUrl("file:///android_asset/advanced.html");

	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//delete a h.profile
    public void delHp(final String idd) {
	try {

	    AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    alert.setTitle("Delete HP?");

	    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
		    dialog.dismiss();
		    System.out.println("del " + idd);
		    lodd();
		    String lnk = targetURL + "/hp/" + idd;
		    System.out.println("link " + lnk);
		    String bk = mut.sendurl(lnk, "DELETE", "");
		    unlodd();
		    System.out.println("pips " + bk);
		    if (!bk.contains("{")) {
			enpty();
		    } else {
			webview.loadUrl("javascript:reg_users(" + bk + ")");
		    }
		}
	    });
	    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
		    dialog.dismiss();
		}
	    });
	    alert.show();

	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//Save a person's goal
    public void saveGoal(String gdt) {
	try {
	    lodd();
	    System.out.println("hp " + gdt);
	    String lnk = targetURL + "/goal";
	    System.out.println("link " + lnk);
	    String bk = mut.sendurl(lnk, "POST", gdt);
	    System.out.println("pips " + bk);
	    unlodd();
//webview.loadUrl("javascript:hp_hst(" + bk + ")");

	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//get a person's goal
    public void getGoal(String pid) {
	try {
	    lodd();
	    System.out.println("hp " + pid);
	    String lnk = targetURL + "/goal/" + pid;
	    System.out.println("link " + lnk);
	    String bk = mut.makeGetRequest(lnk);
	    System.out.println("pips " + bk);
	    unlodd();
	    if (!bk.contains("{")) {
		enpty();
	    } else {
		ArrayList<String> items_ht = new ArrayList<String>();
		JSONArray array = new JSONArray(bk);
		for (int i = 0; i < array.length(); i++) {
		    JSONObject row = array.getJSONObject(i);
		    items_ht.add("G-" + row.getString("gid") + " " + row.getString("date") + " " + row.getString("val"));

		}
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		builderSingle.setTitle("Gaols History");

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
			context,
			android.R.layout.select_dialog_singlechoice, items_ht);

		builderSingle.setNegativeButton(
			"cancel",
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			    }
			});

		builderSingle.setAdapter(
			arrayAdapter,
			new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
				String strName = arrayAdapter.getItem(which);
				System.out.println("split " + strName.substring(strName.indexOf("-") + 1, strName.indexOf(" ")));
				delGol(strName.substring(strName.indexOf("-") + 1, strName.indexOf(" ")));
			    }
			});
		builderSingle.show();

	    }
	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//delete a person's goal
    public void delGol(final String idd) {
	try {

	    AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    alert.setTitle("Delete Goal?");

	    alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
		    dialog.dismiss();
		    System.out.println("del " + idd);
		    lodd();
		    String lnk = targetURL + "/goal/" + idd;
		    System.out.println("link " + lnk);
		    String bk = mut.sendurl(lnk, "DELETE", "");
		    System.out.println("pips " + bk);
		    unlodd();
		    if (!bk.contains("{")) {
			enpty();
		    } else {
			webview.loadUrl("javascript:reg_users(" + bk + ")");
		    }
		}
	    });
	    alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
		    dialog.dismiss();
		}
	    });
	    alert.show();

	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//get measure types
    public void getMeasure() {
	try {
	    lodd();
	    String lnk = targetURL + "/measure";
	    System.out.println("link " + lnk);
	    String bk = mut.makeGetRequest(lnk);
	    System.out.println("pips " + bk);
	    unlodd();
	    if (!bk.contains("{")) {
		enpty();
	    } else {
		webview.loadUrl("javascript:measures(" + bk + ")");
	    }

	} catch (Exception ex) {
	    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
	}
    }

//alert when empty
    public void enpty() {
	AlertDialog.Builder alert = new AlertDialog.Builder(this);
	alert.setTitle("No items found");

	alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int id) {
		dialog.dismiss();

	    }
	});
	alert.show();
    }

//load image in webview
    public void loadGift(String imgg, String txt) {
	unlodd();
	AlertDialog.Builder alert = new AlertDialog.Builder(this);
	alert.setTitle("Message!!");

	RelativeLayout rl = new RelativeLayout(context);
	WebView wv = new WebView(this);
	ImageView img = new ImageView(context);
	try {
	    URL url = new URL("http://static.flickr.com/" + imgg);
	    URLConnection conn = url.openConnection();
	    HttpURLConnection httpConn = (HttpURLConnection) conn;
	    httpConn.setRequestMethod("GET");
	    httpConn.connect();
	    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		InputStream inputStream = httpConn.getInputStream();
		Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
		inputStream.close();
		img.setImageBitmap(bitmap);
	    }
	} catch (Exception e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	TextView tv = new TextView(context);
	tv.setText(txt);
	rl.addView(img);
	rl.addView(tv);
	alert.setView(rl);

	alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int id) {
		dialog.dismiss();
		loadPage();
	    }
	});
	alert.show();
//Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();

    }

    /**
     * ****************************** Check if connected to internet ******************
     */
    public boolean isConnectingToInternet() {
	ConnectivityManager connectivity = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (connectivity != null) {
	    NetworkInfo[] info = connectivity.getAllNetworkInfo();
	    if (info != null) {
		for (int i = 0; i < info.length; i++) {
		    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
			return true;
		    }
		}
	    }

	}
	return false;
    }

    /**
     * ******************** if not connected, alert ************************
     */
    private void not_connected() {

	AlertDialog alertDialog = new AlertDialog.Builder(context).create();

	// Setting Dialog Title
	alertDialog.setTitle(" ");
		//alertDialog.setIcon(R.drawable.i);

	// Setting Dialog Message
	alertDialog.setMessage("No Internet Connection!");

	// Setting OK Button
	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	    }
	});

	// Showing Alert Message
	alertDialog.show();
    }

    //Connecting to server
    public void loadPage() {

	webview.loadUrl("file:///android_asset/advanced.html");

	/**
	 * @return
			 *****************************************************************
	 */
    }

//Get auth code from runkeeper after logging in
    public void getAuthorizationCode(String str) {
	idz = str;
	System.out.println("in auth");
	String authorizationUrl = "https://runkeeper.com/apps/authorize?response_type=code&client_id=%s&redirect_uri=%s";
	authorizationUrl = String.format(authorizationUrl, CLIENT_ID, CALLBACK_URL);

	webview.setWebViewClient(new WebViewClient() {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (url.startsWith(CALLBACK_URL)) {

		    final String authCode = Uri.parse(url).getQueryParameter("code");

		    String lnk = targetURL + "/miles/" + authCode;
		    String bk = mut.makeGetRequest(lnk);
		    try {
			String lnk1 = targetURL + "/pic/" + idz + "~~" + bk;
			System.out.println("link " + lnk1);
			final String bkz = mut.makeGetRequest(lnk1);
			final String[] bk1 = bkz.split("~~");
			runOnUiThread(new Runnable() {

			    @Override
			    public void run() {
				loadGift(bk1[0], bk1[1]);
			    }
			});

		    } catch (Exception ex) {
			Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
//Toast.makeText(context, "An Error occured!", Toast.LENGTH_LONG).show(); 
////progress.dismiss();
		    }
		    return true;
		}

		return super.shouldOverrideUrlLoading(view, url);
	    }
	});

	webview.loadUrl(authorizationUrl);

    }

//Toast to show messages
    public void displayToast(final String message) {
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		// Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	    }
	});
    }

    //Exit when button pressed on webview
    public void exitApp() {
	finish();
    }

    public void lodd() {
   //  Toast.makeText(this, "Processing...", Toast.LENGTH_LONG).show();
	//final AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Leaving launcher").setMessage("processing...");
//  progress = new ProgressDialog(this);
//	progress.setTitle("Loading");
//progress.setMessage("Pse loading...");
//progress.show();   
// alat = dialog.create();
//alat.show();
    }

    public void unlodd() {
//	runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
	//       Toast.makeText(context, "Done processing", Toast.LENGTH_LONG).show();
//////progress.dismiss();
//    alat.dismiss();
//      }
//        });
    }
}
