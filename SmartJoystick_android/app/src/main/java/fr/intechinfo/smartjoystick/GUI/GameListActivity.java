package fr.intechinfo.smartjoystick.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Set;

import fr.intechinfo.smartjoystick.R;
import fr.intechinfo.smartjoystick.corelibrary.Helper;
import fr.intechinfo.smartjoystick.corelibrary.SJContext;
import fr.intechinfo.smartjoystick.localareanetwork.LAN;


/**
 * Created by Spraden on 05/11/2014.
 */
@EActivity
public class GameListActivity extends Activity {

    @Extra
    SJContext sjc;

    PopupWindow popupWindow;

    TextView players;

    Set currentPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        LAN.getInstance().handler = myHandler;

        popupWindow = null;

        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this, sjc));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageAdapter.Item item = (ImageAdapter.Item) gridView.getAdapter().getItem(position);

                if (popupWindow == null) {

                    //set the current game
                    sjc.currentGame = sjc.getCurrentGame(item.name);

                    //subscribe player to the current game
                    sjc.gamePlayers.put(sjc.currentUser.username, sjc.currentGame.title);

                    //confirm the subscription by sending data to the server
                    try {
                        LAN.getInstance().Send("{\"game\":\"" + sjc.currentGame.title + "\",\"registerPlayer\":\"" + sjc.currentUser.username + "\"}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    showPopup(v);
                }
            }
        });
    }

    private void showPopup(View v) {

        //create the popup's layout
        LayoutInflater layoutInflater
                = (LayoutInflater) GameListActivity.this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_layout, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(v, 45, -475);

        Button btnDismiss = (Button) popupView.findViewById(R.id.cancelButton);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //unsubscribe the player of the current game
                sjc.gamePlayers.remove(sjc.currentUser.username);

                //confirm by sending data to the server
                try {
                    LAN.getInstance().Send("{\"unregisterPlayer\":\"" + sjc.currentUser.username + "\"}");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //close the popup
                popupWindow.dismiss();
                popupWindow = null;
            }
        });
        //get all players of the current game
        currentPlayers = Helper.getKeysByValue(sjc.gamePlayers, sjc.currentGame.title);

        //display the players
        players = (TextView) popupView.findViewById(R.id.playersTv);
        players.setText("Joueurs : " + currentPlayers.size() + "/" + sjc.currentGame.maxPlayer);

    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("data received", msg.getData().getString("message"));

            try {
                //parse received data
                JSONObject obj = new JSONObject(msg.getData().getString("message"));
                String game = obj.getString("game");
                String user = obj.getString("registerPlayer");

                //add player to a game
                sjc.gamePlayers.put(user, game);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //get all players of the current game
            currentPlayers = Helper.getKeysByValue(sjc.gamePlayers, sjc.currentGame.title);

            //start the game if there are enough players
            if (currentPlayers.size() == sjc.currentGame.minPlayer) {
                GameActivity_.intent(GameListActivity.this).sjc(sjc).start();
            }
        }
    };
}
