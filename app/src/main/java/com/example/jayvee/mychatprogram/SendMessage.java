package com.example.jayvee.mychatprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendMessage extends AppCompatActivity implements View.OnClickListener {

    EditText txtSender, txtMessage;
    Button btnSend;
    private String urlstring;
    private URL url;
    private HttpURLConnection conn;
    private String sender;
    private String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sendmessagelayout);

        this.txtSender = (EditText) this.findViewById(R.id.editText1);
        this.txtMessage = (EditText) this.findViewById(R.id.editText2);
        this.btnSend = (Button) this.findViewById(R.id.button1);

        this.btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sender = this.txtSender.getText().toString();
        message = this.txtMessage.getText().toString();

        if(!sender.equals("") && !message.equals("")) {
            urlstring = "http://192.168.15.2:1337/addmessage?sender=" +sender+ "&message=" +message;

            try {
                url = new URL(urlstring);
                conn = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String data = br.readLine();

                Log.d("Sender New Data", data);
                conn.disconnect();

                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();

            intent.putExtra("sender", sender);
            intent.putExtra("message", message);
            this.setResult(Activity.RESULT_OK, intent);

            this.finish();
        } else
            Toast.makeText(this, "Fill All Fields", Toast.LENGTH_SHORT).show();
    }
}
