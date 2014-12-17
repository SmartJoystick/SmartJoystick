package fr.intechinfo.smartjoystick.corelibrary;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Spraden on 08/11/2014.
 */
public class User implements Serializable{

    public String username;
    public String ID;

    protected User(String username) {

        this.username = username;
        ID = UUID.randomUUID().toString();
    }
}
