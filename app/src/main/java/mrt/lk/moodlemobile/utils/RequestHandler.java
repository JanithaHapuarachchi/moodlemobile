package mrt.lk.moodlemobile.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import mrt.lk.moodlemobile.data.ResObject;

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


    public static String upload_report(String method,boolean isProject,String participant_id, String project_id, String project_name, File report, String time, String is_final, String is_preview ) throws Exception {
        String responseString ="";
        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int serverResponseCode = 0;
        String selectedFilePath = report.getAbsolutePath();
        FileInputStream fileInputStream = new FileInputStream(report);
        URL url = new URL(Constants.MAIN_URL+method);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);//Allow Inputs
        connection.setDoOutput(true);//Allow Outputs
        connection.setUseCaches(false);//Don't use a cached Copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("ENCTYPE", "multipart/form-data");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        connection.setRequestProperty("report",selectedFilePath);

        //creating new dataoutputstream
        dataOutputStream = new DataOutputStream(connection.getOutputStream());

        //writing bytes to data outputstream
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"participant_id\""+lineEnd+lineEnd+participant_id+lineEnd);
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        if(isProject) {
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"project_id\"" + lineEnd + lineEnd + project_id + lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"project_name\"" + lineEnd + lineEnd + project_name + lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        }
        else{
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"subproject_id\"" + lineEnd + lineEnd + project_id + lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"subproject_name\"" + lineEnd + lineEnd + project_name + lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        }
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"time\""+lineEnd+lineEnd+time+lineEnd);
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"is_final\""+lineEnd+lineEnd+is_final+lineEnd);
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"is_preview\""+lineEnd+lineEnd+is_preview+lineEnd);
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"report\";filename=\""
                + report.getName() + "\"" + lineEnd);

        dataOutputStream.writeBytes(lineEnd);

        //returns no. of bytes present in fileInputStream
        bytesAvailable = fileInputStream.available();
        //selecting the buffer size as minimum of available bytes or 1 MB
        bufferSize = Math.min(bytesAvailable,maxBufferSize);
        //setting the buffer as byte array of size of bufferSize
        buffer = new byte[bufferSize];

        //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
        bytesRead = fileInputStream.read(buffer,0,bufferSize);

        //loop repeats till bytesRead = -1, i.e., no bytes are left to read
        while (bytesRead > 0){
            //write the bytes read from inputstream
            dataOutputStream.write(buffer,0,bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable,maxBufferSize);
            bytesRead = fileInputStream.read(buffer,0,bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        serverResponseCode = connection.getResponseCode();
        String serverResponseMessage = connection.getResponseMessage();

        Log.i("MoodleMobile", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

        //response code of 200 indicates the server status OK

        //closing the input and output streams
        fileInputStream.close();
        dataOutputStream.flush();
        dataOutputStream.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
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
