package ensicaen.fr.marierave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.application_layout_fragment_container);
	
		if (findViewById(R.id.fragment_container) != null) {
			
			if (savedInstanceState == null) {
				Utils.replaceFragments(HelloWorld1.class, this, null, false);
			}
		}
    }
}
