package be.thefluffypangolin.paysville;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class PlayersChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_choice);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("GAME_PARAMETERS_STRING");
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}