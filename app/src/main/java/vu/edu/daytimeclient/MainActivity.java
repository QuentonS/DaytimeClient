package vu.edu.daytimeclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) this.findViewById(R.id.button);
        final TextView textView = (TextView) this.findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Requesting time from time server", Toast.LENGTH_SHORT).show();

                AsyncTask<Void, String, String> asyncTask = new AsyncTask<Void, String, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        // contact server and get the time
                        Socket socket = null;
                        InputStream in = null;
                        OutputStream out = null;
                        try {
                            socket = new Socket("nist1-macon.macon.ga.us", 13);

                            in = socket.getInputStream();
                            out = socket.getOutputStream();

                            int b;
                            StringBuffer sb = new StringBuffer();
                            while ((b = in.read()) != -1) {
                                sb.append((char) b);
                            }

                            out.close();

                            return sb.toString().trim();

                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        } finally {
                            // make sure we close the socket by closing the output stream
                            if (out != null )
                                try { out.close(); } catch (IOException ignored) {}
                        }
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        // this is called when publishProgress is invoked from withing doInBackground
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        textView.setText("Server returned: "+ s);
                    }
                };
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

    }
}
