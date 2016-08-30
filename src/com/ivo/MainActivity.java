package com.ivo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
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
	private final static String CLIENT_ID = "d2e9c62e82a247439868579320bcab06a9";
        private final static String CLIENT_SECRET = "dcab69d503c64772886cabcc025e740a";
        private final static String CALLBACK_URL = "com.example.runkeeperapi://RunKeeperIsCallingBack";
	
	private static final String targetURL = "https://hidden-basin-54030.herokuapp.com/sdelab/control";
	
	
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
        webview.setWebViewClient(new WebViewClient());
        webview.setWebChromeClient(new WebChromeClient());
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webview.setBackgroundColor(0);
		webview.setBackgroundResource(color.background_light);
		webview.addJavascriptInterface(this, "Android");

		if (String.valueOf(isConnectingToInternet()).equals("false"))
			not_connected();
		else {
				loadPage();
			

		}
	}

	//send user data
	public void posst(String dat){
	    try {
	  
		String per = mut.sendurl(targetURL+"/user", "POST", dat);
		System.out.println("bk "+per);
			
	     webview.loadUrl("javascript:cloz_reg()");
	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
	}
	
	//Edit a user
	public void eddt(String dat){
	    try {
	  
		String per = mut.sendurl(targetURL+"/user", "PUT", dat);
		System.out.println("bk "+per);
			
	    // webview.loadUrl("javascript:cloz_reg()");
	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
	}
	
	//get all registered users
	public void gett(){
	    try {
	String bk = mut.sendurl(targetURL+"/user_all", "GET", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:reg_users(" + bk + ")");


//
	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
		//return "haha";
	}
	
	//get a person's h.profile
	public void getHp(String id){
	    try {
		System.out.println("hp "+id);
		String lnk = targetURL+"/user_hp/"+id;
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "GET", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:hp(" + bk + ")");
    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
	}

	//get a person's h.profile history
public void gethp_hist(String id){
	    try {
		System.out.println("hp "+id);
		String lnk = targetURL+"/hp_hist/"+id;
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "GET", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:hp_hst(" + bk + ")");

	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

//delete a person
public void delPsn(String id){
	    try {
		System.out.println("del "+id);
		String lnk = targetURL+"/"+id;
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "DELETE", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:reg_users(" + bk + ")");

	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

//reset the user page
public void resett(){
	    try {
	
webview.loadUrl("file:///android_asset/advanced.html");


	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

//delete a h.profile
public void delHp(final String idd){
	    try {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this); 
alert.setTitle("Delete HP?");

alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
	System.out.println("del "+idd);
		String lnk = targetURL+"/hp/"+idd;
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "DELETE", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:reg_users(" + bk + ")");
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
	    }
	}

//Save a person's goal
public void saveGoal(String gdt){
	    try {
		System.out.println("hp "+gdt);
		String lnk = targetURL+"/goal";
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "POST", gdt);
  System.out.println("pips "+ bk);

//webview.loadUrl("javascript:hp_hst(" + bk + ")");

	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

//get a person's goal
public void getGoal(String pid){
	    try {
		System.out.println("hp "+pid);
		String lnk = targetURL+"/goal/"+pid;
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "GET", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:goalzz(" + bk + ")");

	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}


//delete a person's goal
public void delGol(final String idd){
	    try {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this); 
alert.setTitle("Delete Goal?");

alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
	System.out.println("del "+idd);
		String lnk = targetURL+"/goal/"+idd;
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "DELETE", "");
  System.out.println("pips "+ bk);
if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:reg_users(" + bk + ")");
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
	    }
	}


//get measure types
public void getMeasure(){
	    try {
		String lnk = targetURL+"/measure";
		System.out.println("link "+lnk);
	String bk = mut.sendurl(lnk, "GET", "");
  System.out.println("pips "+ bk);
  if(!bk.contains("{"))
      enpty();
  else
webview.loadUrl("javascript:measures(" + bk + ")");

	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

//alert when empty
public void enpty(){
webview.loadUrl("javascript:enpty()");
}

//load image in webview
public void loadGift(String imgg,String txt){
    System.out.println("oh oh "+imgg);
AlertDialog.Builder alert = new AlertDialog.Builder(this); 
alert.setTitle("Message!!");

RelativeLayout rl = new RelativeLayout(context);
WebView wv = new WebView(this);
wv.getSettings().setLoadWithOverviewMode(true);
wv.getSettings().setUseWideViewPort(true);
wv.loadUrl("http://static.flickr.com/"+imgg);
wv.setWebViewClient(new WebViewClient() {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
});
TextView tv = new TextView(context);
tv.setText(txt);
rl.addView(wv);
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
Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_LONG).show();
            
}

	/******************************** Check if connected to internet *******************/
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	/********************** if not connected, alert *************************/
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
			 ******************************************************************/
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
                    //webview.setVisibility(View.GONE);
		    System.out.println("authcode "+authCode);
                    getAccessToken(authCode);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webview.loadUrl(authorizationUrl);
	
    }

//Get access token from runkeeper
    public void getAccessToken(String authCode) {
	System.out.println("in getAccessToken");
        String accessTokenUrl = "https://runkeeper.com/apps/token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=%s";
        final String finalUrl = String.format(accessTokenUrl, authCode, CLIENT_ID, CLIENT_SECRET, CALLBACK_URL);

        Thread networkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(finalUrl);

                    HttpResponse response = client.execute(post);

                    String jsonString = EntityUtils.toString(response.getEntity());
                    final JSONObject json = new JSONObject(jsonString);

                    String accessToken = json.getString("access_token");
		    System.out.println("accessToken "+accessToken);
                    getTotalDistance(accessToken);

                } catch (Exception e) {
                    displayToast("Exception occured:(");
                    e.printStackTrace();
                    loadPage();
                }

            }
        });

        networkThread.start();
    }

    //Runkeeper's total distance, returns a json array
    public void getTotalDistance(String accessToken) { 
	System.out.println("in getTotalDistance");
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://api.runkeeper.com/records");
            
            get.addHeader("Authorization", "Bearer " + accessToken);
            get.addHeader("Accept", "*/*");
            
            HttpResponse response = client.execute(get);
            
            String jsonString = EntityUtils.toString(response.getEntity());
	    System.out.println("jsnnn "+jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);
            findTotalWalkingDistance(jsonArray);

        } catch (Exception e) {
            displayToast("Exception occured:(");
            e.printStackTrace();
           loadPage();
        }
    }

    //calculating the distance run, in kilometers
    public void findTotalWalkingDistance(JSONArray arrayOfRecords) {
        try {
            //Each record has activity_type and array of statistics. Traverse to  activity_type = Walking
            for (int ii = 0; ii < arrayOfRecords.length(); ii++) {
                JSONObject statObject = (JSONObject) arrayOfRecords.get(ii);
                if ("Running".equalsIgnoreCase(statObject.getString("activity_type"))) {
                    //Each activity_type has array of stats, navigate to "Overall" statistic to find the total distance walked.
                    JSONArray walkingStats = statObject.getJSONArray("stats");
                    for (int jj = 0; jj < walkingStats.length(); jj++) {
                        JSONObject iWalkingStat = (JSONObject) walkingStats.get(jj);
                        if ("Overall".equalsIgnoreCase(iWalkingStat.getString("stat_type"))) {
                            long totalWalkingDistanceMeters = iWalkingStat.getLong("value");
                            double totalWalkingDistanceKm = totalWalkingDistanceMeters * 0.001;
                           try {
		String lnk = targetURL+"/miles/"+idz+"~~"+totalWalkingDistanceKm;
		System.out.println("link "+lnk);
	final String bk = mut.sendurl(lnk, "GET", "");
	final String [] bk1 = bk.split("~~");
  System.out.println("pips "+ bk);
//  if(!bk.contains("jpg"))
//      enpty();
//  else{
       runOnUiThread(new Runnable() {

            @Override
            public void run() {
            
   //loadPage();
   loadGift(bk1[0],bk1[1]);    
	    }
        });
  

	    } catch (Exception ex) {
		Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
	    }
			    
                            return;
                        }
                    }
                }
            }
            displayToast("Something went wrong!!!");
        } catch (JSONException e) {
            displayToast("Exception occured:(");            
            e.printStackTrace();
            loadPage();
        }
    }
    
//Toast to show messages
    public void displayToast(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
               Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
             }
        });
    }
		
		
		
		
		
	//Exit when button pressed on webview
	 public void exitApp()
	    {
	        finish();
	    }

}

