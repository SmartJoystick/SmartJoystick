package fr.intechinfo.smartjoystick.localareanetwork;

import android.os.AsyncTask;
import android.os.Message;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Handler;

import fr.intechinfo.smartjoystick.corelibrary.LocalAreaNetwork;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;

/**
 * Created by Spraden on 18/11/2014.
 */
public class LAN implements LocalAreaNetwork {

    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public void SetReceiver(final android.os.Handler myHandler) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        //DO SOMETHING;
                        Message message = myHandler.obtainMessage();
                        message.obj = msg;
                        myHandler.sendMessage(message);
                    }
                }
            }
        }).start();

    }

    public void Send(final String msg) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out = new PrintWriter(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println(msg);
                out.flush();
            }
        }).start();
    }

    @Override
    public void InitializeLAN(String address, int port, android.os.Handler myHandler) throws IOException {
        if (socket == null) {
            try {
                socket = new Socket(address, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SetReceiver(myHandler);
    }
}
