package fr.intechinfo.smartjoystick.corelibrary;

import java.io.Serializable;

/**
 * Created by Spraden on 08/11/2014.
 */
public class SJContextServices implements Serializable {

    public LocalAreaNetwork lan;
    public Repository repo;

    public SJContextServices(Repository repo, LocalAreaNetwork lan)
    {
        if (repo == null || lan == null) throw new IllegalArgumentException("repo");
        this.repo = repo;
        this.lan = lan;
    }

}
