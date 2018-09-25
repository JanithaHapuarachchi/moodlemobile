package mrt.lk.moodlemobile.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by janithamh on 8/12/18.
 */

public class RequestHandler {


    public static String sendGet(String methodname, Context context) throws Exception{
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        String responseString ="";
        Constants.MAIN_URL = Utility.getServerURL(context)+":"+Utility.getServerPORT(context)+"/fineract-provider/api/v1/";
        String completeurl = Constants.MAIN_URL+methodname;
        Log.e("URL GET",completeurl);
        URL obj = new URL(completeurl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setSSLSocketFactory(sc.getSocketFactory());
        con.setHostnameVerifier(new NullHostNameVerifier());
        con.setRequestMethod("GET");
        con.setRequestProperty("Fineract-Platform-TenantId",Utility.getServerTenant(context));
        if(!Utility.getAuthKey(context).equals("")){
            con.setRequestProperty ("Authorization", "Basic " + Utility.getAuthKey(context));
        }
        int responseCode = con.getResponseCode();
        Log.e("Code",Integer.toString(responseCode));
        /*if (responseCode == HttpURLConnection.HTTP_OK) {*/

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine= "";
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("String",responseString);
        in.close();

       /* }
        else {

        }
         */   return responseString;
    }


    public static String getLogin(String getparams, Context context) throws Exception{
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        String responseString ="";
        //Constants.MAIN_URL = Utility.getServerURL(context)+":"+Utility.getServerPORT(context)+"/fineract-provider/api/v1/";
        String completeurl = Constants.LOGIN_URL+getparams;
        Log.e("URL GET",completeurl);
        URL obj = new URL(completeurl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setSSLSocketFactory(sc.getSocketFactory());
        con.setHostnameVerifier(new NullHostNameVerifier());
        con.setRequestMethod("GET");
        con.setRequestProperty("Fineract-Platform-TenantId",Utility.getServerTenant(context));
        if(!Utility.getAuthKey(context).equals("")){
            con.setRequestProperty ("Authorization", "Basic " + Utility.getAuthKey(context));
        }
        int responseCode = con.getResponseCode();
        Log.e("Code",Integer.toString(responseCode));
        /*if (responseCode == HttpURLConnection.HTTP_OK) {*/

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine= "";
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("String",responseString);
        in.close();

       /* }
        else {

        }
         */   return responseString;
    }


    public static String sendPost(String request, String methodname,Context context) throws Exception{
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        String responseString ="";
        Constants.MAIN_URL = Utility.getServerURL(context)+":"+Utility.getServerPORT(context)+"/fineract-provider/api/v1/";
        String completeurl;
        completeurl = Constants.MAIN_URL+methodname;
//        if(methodname.equals(Constants.AUTHENTICATION_URL)){
//            completeurl = Constants.MAIN_URL+methodname+"?username="+postobject.getString("username")+"&password="+postobject.getString("password");
//        }
//        else{
//             completeurl = Constants.MAIN_URL+methodname;
//        }

        Log.e("URL POST",completeurl);
        URL obj = new URL(completeurl);

        sc.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setSSLSocketFactory(sc.getSocketFactory());
        con.setHostnameVerifier(new NullHostNameVerifier());

        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        con.connect();

        DataOutputStream printout = new DataOutputStream(con.getOutputStream ());
        printout.writeBytes(request);
        printout.flush ();
        printout.close ();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("FLI String",responseString);
        in.close();

        return responseString;
    }

    public static String sendServicePost(JSONObject postobject, String methodname,Context context) throws Exception{
        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        String responseString ="";
        Constants.MAIN_URL = Utility.getServerURL(context)+":"+Utility.getServerPORT(context)+"/fineract-provider/api/v1/";
        String completeurl = Constants.MAIN_URL+methodname;
        Log.e("URL POST",completeurl);
        URL obj = new URL(completeurl);

        sc.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setSSLSocketFactory(sc.getSocketFactory());
        con.setHostnameVerifier(new NullHostNameVerifier());

        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty("Fineract-Platform-TenantId",Utility.getServerTenant(context));
        if(!Utility.getAuthKey(context).equals("")){
            con.setRequestProperty  ("Authorization", "Basic " + Utility.getAuthKey(context));
        }
        con.connect();

        DataOutputStream printout = new DataOutputStream(con.getOutputStream ());
        printout.writeBytes(postobject.toString());
        printout.flush ();
        printout.close ();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("FLI Service String",responseString);
        in.close();

        return responseString;
    }



    public static void inititateSSL(Context cont) throws Exception {

// Create a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
     //   keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);


// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
      //context.init(null, tmf.getTrustManagers(), null);
     //   context.init(null,new TrustManager[]{tm},null);
        context.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
//https://52.74.229.37:443/fineract-provider/api/v1/
// Tell the URLConnection to use a SocketFactory from our SSLContext
        Constants.MAIN_URL = Utility.getServerURL(cont)+":"+Utility.getServerPORT(cont)+"/fineract-provider/api/v1/";
        URL url = new URL(Constants.MAIN_URL+"clients");
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setHostnameVerifier(new NullHostNameVerifier());
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Fineract-Platform-TenantId",Utility.getServerTenant(cont));
        urlConnection.setRequestProperty  ("Authorization", "Basic " + "amFuaXRoYTphYmNAMTIzNA==");
       // InputStream in = urlConnection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        String responseString = response.toString();
        Log.e("String",responseString);
        in.close();;
    }



    static class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }
    static class NullX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
