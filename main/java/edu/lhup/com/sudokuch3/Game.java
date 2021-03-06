package edu.lhup.com.sudokuch3;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends AppCompatActivity {

    public static final String KEY_DIFFICULTY = "comp255.difficulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;

    private static final String TAG = "comp255";

    public int[][][] usedTiles = new int[9][9][];

    private final String easyPuzzle =
            "360000000004230800000004200" +
                    "070460003820000014500013020" +
                    "001900000007048300000000045";

    private final String mediumPuzzle =
            "650000070000506000014000005" +
                    "007009000002314700000700800" +
                    "500000630000201000030000097";

    private final String hardPuzzle =
            "009000000080605020501078000" +
                    "000000700706040102004000000" +
                    "000720903090301080000000600";


    public int puzzle[] = new int[9 * 9]; //single dimensional representation

    PuzzleView puzzleview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
        puzzle = getPuzzle(diff);

        calculateUsedTiles();

        puzzleview = new PuzzleView(this);
        setContentView(puzzleview);
        puzzleview.requestFocus();

        Log.d(TAG, "Game:onCreate() - Created!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this, R.raw.game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    private int[] getPuzzle(int diff) {
        String str = "";

        switch (diff) {
            case DIFFICULTY_EASY:
                str = easyPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                str = mediumPuzzle;
                break;
            case DIFFICULTY_HARD:
                str = hardPuzzle;
                break;
            default:
                str = easyPuzzle;
                break;
        }
        return fromPuzzleString(str);
    }


    static private String toPuzzleString(int[] puz) {
        StringBuilder buf = new StringBuilder();
        for (int element : puz) {
            buf.append(element);
        }
        return buf.toString();
    }

//    private String toPuzzleString(int[] puz){
//        StringBuilder builder = new StringBuilder();
//
//        for (int i : puz){
//            builder.append(i);
//        }
//        return builder.toString();
//    }

    static protected int[] fromPuzzleString(String string) {
        int[] puz = new int[string.length()];
        for (int i = 0; i < puz.length; i++) {
            puz[i] = string.charAt(i) - '0';
        }
        return puz;
    }

    private int getTile(int x, int y) {

        return puzzle[y * 9 + x];
    }

    private void setTile(int x, int y, int value) {

        puzzle[y * 9 + x] = value;
    }

    protected String getTileString(int x, int y) {
        int v = getTile(x, y);
        if (v == 0)
            return "";
        else
            return String.valueOf(v);
    }

    protected boolean setTileIfValid(int x, int y, int value) {
        int tiles[] = getUsedTiles(x, y);
        if (value != 0) {
            for (int tile : tiles) {
                if (tile == value)
                    return false;
            }
        }
        setTile(x, y, value);
        calculateUsedTiles();
        return true;
    }

    protected void showKeypadOrError(int x, int y) {
        int tiles[] = getUsedTiles(x, y);
        if (tiles.length == 9) {
            Toast toast = Toast.makeText(this,
                    R.string.no_moves_label, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
            Dialog v = new Keypad(this, tiles, puzzleview);
            v.show();
        }
    }

    protected int[] getUsedTiles(int x, int y) {
        return usedTiles[x][y];
    }

    private void calculateUsedTiles() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                usedTiles[i][j] = calculateUsedTiles(i, j);
            }
        }
    }


    private int[] calculateUsedTiles(int x, int y) {
        int c[] = new int[9];
        // horizontal
        for (int i = 0; i < 9; i++) {
            if (i == y)
                continue;
            int t = getTile(x, i);
            if (t != 0)
                c[t - 1] = t;
        }
        // vertical
        for (int i = 0; i < 9; i++) {
            if (i == x)
                continue;
            int t = getTile(i, y);
            if (t != 0)
                c[t - 1] = t;
        }

        // same cell block
        int startx = (x / 3) * 3;
        int starty = (y / 3) * 3;


        for (int i = startx; i < startx + 3; i++) {
            for (int j = starty; j < starty + 3; j++) {
                if (i == x || j == y)
                    continue;
                int t = getTile(i, j);
                if (t != 0)
                    c[t - 1] = t;
            }
        }
        // compress
        int nused = 0;
        for (int t : c) {
            if (t != 0)
                nused++;
        }
        int c1[] = new int[nused];
        nused = 0;
        for (int t : c) {
            if (t != 0)
                c1[nused++] = t;
        }
        return c1;
    }
}
