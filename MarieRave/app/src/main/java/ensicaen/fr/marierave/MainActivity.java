package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.application_layout_fragment_container);
		
		Log.d("myapp", "1");
		
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState == null) {
				Log.d("myapp", "2");
				Utils.replaceFragments(HomeClassroom.class, this, null, false);
			}
		}
	}
}
