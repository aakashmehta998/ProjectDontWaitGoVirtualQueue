package com.example.android.dontwait_govirtualqueue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String firstname="";
    String lastname="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.signup_button);
        loginButton.setOnClickListener(loginOnClickListener);
        signUpButton.setOnClickListener(signUpOnClickListener);


    }

    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView loginsuccess = findViewById(R.id.login_successful_text);
            loginsuccess.setVisibility(View.INVISIBLE);

        EditText email = findViewById(R.id.email);
        EditText pass = findViewById(R.id.password);
        String result ="";
        String[] userpass = {email.getText().toString(),pass.getText().toString()};
        LoginValidator loginValidator = new LoginValidator();
            try {
                result = loginValidator.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userpass).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(result.equals("valid"))
            {
                Log.v("harshdemo","valid");
                TextView textView = findViewById(R.id.login_successful_text);
                textView.setText("Login is successful!");
                textView.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getApplicationContext(),Login_Successful.class);
                intent.putExtra("firstname",firstname);
                intent.putExtra("lastname",lastname);
                intent.putExtra("email",email.getText());
                startActivity(intent);
            }
            else
            {
                Log.v("harshdemo","invalid");
                TextView textView = findViewById(R.id.login_successful_text);
                textView.setText("Sorry! Credentials are not valid!");
                textView.setVisibility(View.VISIBLE);
            }


        }
    };

    public class LoginValidator extends AsyncTask<String,Void,String>
    {


        @Override
        protected String doInBackground(String... strings) {
            try {
                Socket s = new Socket("45.115.18.156",5982);
                Log.v("harshdemo","Socket ");
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                DataInputStream dis = new DataInputStream(s.getInputStream());
                dos.writeUTF("validate");
                dos.writeUTF(strings[0]);
                dos.writeUTF(strings[1]);
                String result = dis.readUTF();
                 firstname = dis.readUTF();
                 lastname = dis.readUTF();
                dos.close();
                dis.close();
                s.close();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
    }

    private View.OnClickListener signUpOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),SignUp.class);
            startActivity(intent);


        }
    };


}
