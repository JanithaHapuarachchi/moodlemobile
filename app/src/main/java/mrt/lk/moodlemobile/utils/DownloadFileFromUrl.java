package mrt.lk.moodlemobile.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by janithah on 9/29/2018.
 */

public class DownloadFileFromUrl extends AsyncTask <String,Void,String> {

    ProgressBarController progressBarController;
    Context context;
    Button clickedBtn;

    public DownloadFileFromUrl(ProgressBarController progressBarController,Button clickedBtn,Context context){
        this.context = context;
        this.progressBarController = progressBarController;
        this.clickedBtn =clickedBtn;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        clickedBtn.setText("Downloading...");
        clickedBtn.setEnabled(false);
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        clickedBtn.setText("Download");
        clickedBtn.setEnabled(true);
        Utility.showMessage(res,context);
    }

    @Override
    protected String doInBackground(String... params) {
        int count;
        try {
            URL url = new URL(params[0]);
            String[] items= params[0].split("/");
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            OutputStream output = new FileOutputStream(Environment
                    .getExternalStorageDirectory().toString() +items[items.length-1]  );

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
              //  publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
           // Utility.showMessage("Downloaded",context);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return e.getMessage();
        }




        return "Downloaded";
    }
}
