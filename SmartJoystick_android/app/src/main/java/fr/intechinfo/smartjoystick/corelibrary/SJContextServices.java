package fr.intechinfo.smartjoystick.corelibrary;

/**
 * Created by Spraden on 08/11/2014.
 */
public class SJContextServices {

    public LocalAreaNetwork _lan;
    public Repository _repo;

    public SJContextServices(Repository repo, LocalAreaNetwork lan)
    {
        if (repo == null || lan == null) throw new IllegalArgumentException("repo");
        _repo = repo;
        _lan = lan;
    }

}
