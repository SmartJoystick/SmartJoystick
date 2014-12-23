package fr.intechinfo.smartjoystick.localareanetwork;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import fr.intechinfo.smartjoystick.corelibrary.LocalAreaNetwork;

/**
 * Created by Spraden on 18/11/2014.
 */
public class LAN implements LocalAreaNetwork, Serializable {

    private transient Socket socket = null;
    private transient PrintWriter out = null;
    private transient BufferedReader in = null;

    private String address;
    private int port;

    public void Send(final String msg) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                        socket = new Socket(address, port);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
    public void InitializeLAN(final String address, final int port, final android.os.Handler handler) throws IOException {

        this.address = address;
        this.port = port;

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket == null) {
                    try {
                        socket = new Socket(address, port);
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
                        System.out.println(msg);

                        Message msgObj = handler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putString("message", msg);
                        msgObj.setData(b);
                        handler.sendMessage(msgObj);
                    }
                }
            }
        }).start();
    }
}
