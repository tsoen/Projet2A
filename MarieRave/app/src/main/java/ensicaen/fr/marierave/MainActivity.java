package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ensicaen.fr.marierave.Controllers.DAOBase;
import ensicaen.fr.marierave.Views.ConnectionFragment;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.application_layout_fragment_container);
		
		new DAOBase(this).clearDatabase();
		
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState == null) {
				Utils.replaceFragments(ConnectionFragment.class, this, null, false);
			}
		}
	}
}
