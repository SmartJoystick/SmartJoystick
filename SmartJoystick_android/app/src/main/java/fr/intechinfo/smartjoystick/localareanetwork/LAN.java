package fr.intechinfo.smartjoystick.localareanetwork;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by StephaneTruong on 24/12/2014.
 */
public class LAN {
    private static LAN mInstance = null;

    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public String address;
    public int port;
    public Handler handler;

    private LAN() {

    }

    //it's a singleton class
    public static LAN getInstance() {
        if (mInstance == null) {
            mInstance = new LAN();
        }
        return mInstance;
    }

    public void Send(final String msg) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //create a new socket if not already created
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
                //send data to the the server
                out.println(msg);
                out.flush();
            }
        }).start();
    }

    public void SetReceiver() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //create a new socket if not already created
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
                    //receive data sent by the server
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    String msg = null;
                    try {
                        //convert bufferedreader to string
                        msg = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (msg == null) {
                        break;
                    } else {
                        //set received data to the handler msg
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

    public void Close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
