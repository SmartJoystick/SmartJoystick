package fr.intechinfo.smartjoystick.corelibrary;

import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Spraden on 08/11/2014.
 */
public class SJContext implements Serializable {

    public User currentUser;
    public Game currentGame;
    public String currentCategory;

    public List<String> categories;
    public Map<String, List<String>> categoryList;
    public Map<String, List<String>> gameInfo;
    public List<Game> gameList;

    public transient SJContextServices services;

    public SJContext()
    {
        gameList = new ArrayList<Game>();
        categories = new ArrayList<String>();
        categoryList = new HashMap<String, List<String>>();
        gameInfo = new HashMap<String, List<String>>();
    }

    public User CreateUser(String username )
    {
        if (username == null) throw new IllegalArgumentException( "empty username" );
        return new User(username);
    }

    public static SJContext Load( SJContextServices services, Context c ) throws IOException, ClassNotFoundException {
        SJContext sjc = services._repo.LoadUnitializedContext(c);
        sjc.Initialize( services );
        return sjc;
    }
    public void Initialize( SJContextServices services )
    {
        if (services == null) throw new IllegalArgumentException( "services" );
        this.services = services;
    }
    public void Save(Context c) throws IOException {
        services._repo.Save(c, this);
    }
    public void InitializeLAN(String address, int port, Handler myHandler) throws IOException {
        services._lan.InitializeLAN(address, port, myHandler);
    }
    public void Send(String msg) throws IOException {
        services._lan.Send(msg);
    }
    public Game CreateGame(String title, String description){
        return new Game(title,description);
    }
    public Game getCurrentGame(String name){
        Game tmp = null;
        for (int i = 0; i<gameList.size();i++){
            if (name.equalsIgnoreCase(gameList.get(i).title)){
                tmp = gameList.get(i);
                break;
            }
        }
        return tmp;
    }
}
