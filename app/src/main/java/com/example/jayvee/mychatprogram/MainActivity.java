package com.example.jayvee.mychatprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    ArrayList<ChatMessage> list = new ArrayList<>();
    ChatAdapter adapter;
    private URL url;
    private HttpURLConnection conn;
    private JSONObject json;
    private JSONArray jarry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // allow this app to run along size another process/thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.lv = (ListView) this.findViewById(R.id.listView1);
        this.adapter = new ChatAdapter(this, list);
        this.lv.setAdapter(adapter);
        
        // connect to the remote server
        try {
            url = new URL("http://192.168.15.2:1337");
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String data = br.readLine();

            // check if we got the data
            Log.d("Data from Server", data);
            conn.disconnect();

            // parse the JSON data
            try {
                json = new JSONObject(data);
                jarry = json.getJSONArray("messages");

                // get each data from array
                for(int i=0; i<jarry.length(); i++) {
                    String sender = jarry.getJSONObject(i).getString("sender");
                    String message = jarry.getJSONObject(i).getString("message");

                    list.add(new ChatMessage(sender, message));
                }

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, SendMessage.class);
        this.startActivityForResult(intent, 0);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            Bundle b = data.getExtras();

            String s = b.getString("sender");
            String m = b.getString("message");

            list.add(new ChatMessage(s, m));
            adapter.notifyDataSetChanged();
        }
    }
}
