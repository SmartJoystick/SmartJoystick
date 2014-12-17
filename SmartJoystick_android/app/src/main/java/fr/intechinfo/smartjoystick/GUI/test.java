package fr.intechinfo.smartjoystick.GUI;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by StephaneTruong on 16/12/2014.
 */
public class test {

    final static int mapSize = 80;
    final static int BLACK = 0;
    final static int WHITE = 1;

    public static void main(int args[]) {
        final  int mapSize = 80;
        final  int BLACK = 0;
        final  int WHITE = 1;
        HashMap<String[][], Integer> colorCase = new HashMap<String[][], Integer>();

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                Random rand = new Random();
                int x = rand.nextInt(1);
                colorCase.put(new String[i][j],x);
            }
        }

    }
    public void createBoard(int mapSize){

        final int WHITE = 0;
        final int BLACK = 1;

        boolean color = false;
        int x = 0;
        int[][] board = new int[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                if(color){
                    x = BLACK;
                    color = false;
                }
                else {
                    x = WHITE;
                    color = true;
                }
                board[i][j] = x;
            }
        }
    }

}
