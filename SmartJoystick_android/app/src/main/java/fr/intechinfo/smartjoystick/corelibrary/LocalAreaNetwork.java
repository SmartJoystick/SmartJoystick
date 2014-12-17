package fr.intechinfo.smartjoystick.corelibrary;

import android.os.Handler;

import java.io.IOException;


/**
 * Created by Spraden on 17/11/2014.
 */
public interface LocalAreaNetwork {
    void Send(String msg) throws IOException;
    void InitializeLAN(String address, int port, Handler myHandler) throws IOException;
}
