package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import fr.intechinfo.smartjoystick.R;

/**
 * Created by Spraden on 24/11/2014.
 */
@EActivity(R.layout.testserver)
public class server extends Activity {

    private Handler handler = new Handler();

    @Extra
    List<String> data;

    @ViewById(R.id.editText)
    EditText editext;

    @ViewById(R.id.textView2)
    TextView textView;

 //   private String host = data.get(0);
    // private int port = Integer.parseInt(data.get(1));
   // private String port = data.get(1);

    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Connected to "+data.get(0)+" on PORT "+data.get(1), Toast.LENGTH_SHORT).show();
        receiveMsg();
       // sendMessageToServer(data.get(3));
    }

    @Click
    void sendButton() {
        displayMsg(editext.getText().toString());
        sendMessageToServer(editext.getText().toString());
        editext.setText("");
        hideKeyboard();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void sendMessageToServer(String str) {
        final String str1 = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                       socket = new Socket(data.get(0), Integer.parseInt(data.get(1)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    out = new PrintWriter(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println(str1);
                out.flush();
            }
        }).start();
    }

    public void receiveMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                        socket = new Socket(data.get(0), Integer.parseInt(data.get(1)));
                    } catch (UnknownHostException e) {

                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    String msg = null;
                    try {
                        msg = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (msg == null) {
                        break;
                    } else {
                        displayMsg(msg);
                    }
                }
            }
        }).start();
    }
    public void displayMsg(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(textView.getText().toString() + "\r\n" + msg);
            }
        });
    }

}

