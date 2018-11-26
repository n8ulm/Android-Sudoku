package edu.lhup.com.sudokuch3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Sudoku extends AppCompatActivity implements View.OnClickListener {

    public static final  String DEBUG_TAG = "edu.lhup.comp225";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.id.layout.activity_sudoku);
        //Bind about button to callback
        Button aboutButton = (Button) findViewById(R.id.about_button) ;
        aboutButton.setOnClickListener(this);

        Button newGameButton = (Button) findViewById(R.id.new_game_button) ;
        newGameButton.setOnClickListener(this);
    }
    //Callback Method
    @Override
    public void onClick(View view){

        Log.i(DEBUG_TAG, "In Sudo Callback!");

        switch (view.getId()){
            case R.id.about_button:
                Log.i(DEBUG_TAG, "About button pressed...");
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                break;
            default:
                Log.i(DEBUG_TAG, "Call back default!");
                break;
            case R.id.new_game_button:
                openNewGameDialog();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent = new Intent(this,Prefs.class);
                startActivity(intent);
                return true;
        }
        return false;
    }
    private void openNewGameDialog(){
        new AlertDialog.Builder(this).setTitle(R.string.new_game_title)
                .setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGame(which);
                    }
                }).show();
    }
    private void startGame(int diff){
        Log.i(DEBUG_TAG,"Clicked at new game at level" + diff);
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, diff);
        startActivity(intent);

    }

    @Override
    protected void onResume(){
        super.onResume();
        Music.play(this, R.raw.main);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Music.stop(this);
    }
}
