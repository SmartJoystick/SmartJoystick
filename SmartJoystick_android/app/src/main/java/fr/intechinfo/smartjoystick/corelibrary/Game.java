package fr.intechinfo.smartjoystick.corelibrary;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

/**
 * Created by Spraden on 08/11/2014.
 */
public class Game implements Serializable {
    public String title;
    public String description;
    public String id;

    //for test only
    public int minPlayer = 2;
    public int maxPlayer = 5;

   // public URI url;


    public Game(String title, String description){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        //this.url = url;
    }
}
