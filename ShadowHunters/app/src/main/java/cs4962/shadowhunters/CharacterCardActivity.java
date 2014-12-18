package cs4962.shadowhunters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class CharacterCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_card);

        Intent i = getIntent();
        Character c = i.getParcelableExtra("character");
        String name = c.getName();

        ImageView characterImage = (ImageView)findViewById(R.id.characterCardImage);
        characterImage.setImageDrawable(getCharacterImage(name));

        Button closeBtn = (Button)findViewById(R.id.characterCardCloseButton);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private Drawable getCharacterImage(String name) {
        if (name.equals("Allie")) {
           return getResources().getDrawable(R.drawable.allie);
        }
        else if (name.equals("Bob")) {
            return getResources().getDrawable(R.drawable.bob);
        }
        else if (name.equals("Charles")) {
            return getResources().getDrawable(R.drawable.charles);
        }
        else if (name.equals("Daniel")) {
            return getResources().getDrawable(R.drawable.daniel);
        }
        else if (name.equals("Emi")) {
            return getResources().getDrawable(R.drawable.emi);
        }
        else if (name.equals("Franklin")) {
            return getResources().getDrawable(R.drawable.franklin);
        }
        else if (name.equals("George")) {
            return getResources().getDrawable(R.drawable.george);
        }
        else if (name.equals("Unknown")) {
            return getResources().getDrawable(R.drawable.unknown);
        }
        else if (name.equals("Vampire")) {
            return getResources().getDrawable(R.drawable.vampire);
        }
        else {
            return getResources().getDrawable(R.drawable.werewolf);
        }
    }
}
