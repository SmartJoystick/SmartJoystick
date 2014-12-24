package fr.intechinfo.smartjoystick.corelibrary;

import java.io.Serializable;

/**
 * Created by Spraden on 08/11/2014.
 */
public class SJContextServices implements Serializable {

    public Repository repo;

    public SJContextServices(Repository repo)
    {
        if (repo == null) throw new IllegalArgumentException("repo");
        this.repo = repo;
    }

}
