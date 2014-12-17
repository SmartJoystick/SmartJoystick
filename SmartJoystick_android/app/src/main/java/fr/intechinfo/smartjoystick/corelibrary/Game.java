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
   // public URI url;
    public String id;

    public Game(String title, String description){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        //this.url = url;
    }
}
