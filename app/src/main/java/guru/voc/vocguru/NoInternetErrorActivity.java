package guru.voc.vocguru;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Activity to show NO-INTERNET connection error.
 *
 * @author alexey.alexeenka 20 July 2018
 */
public class NoInternetErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);


        Button buttonOne = findViewById(R.id.refreshButton);
        buttonOne.setOnClickListener(v -> {
            this.finish();
        });
    }
}
