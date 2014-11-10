package cs4962.teach;

import android.app.Activity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;


public class TeachActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "RPrtSoeaQpcWTiGrxMTVoVjZjEy3iywJ0uKFpLMt", "S95Kcx53yRPntvHUkd9CQTaZOg0dGI6KVdu5hXMZ");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        setContentView(R.layout.activity_teach);
    }
}
