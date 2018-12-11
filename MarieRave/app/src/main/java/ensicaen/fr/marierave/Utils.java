package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class Utils
{
	/**
	 * Switchs the fragment displayed in the application
	 * Can be called from inside a FragmentClass
	 *
	 * @param fragmentClass Fragment class to display
	 * @param activity      Reference to the activity containing fragments
	 * @param bundle        Variables to pass to the fragment
	 */
	public static void replaceFragments(Class fragmentClass, FragmentActivity activity, @Nullable Bundle bundle, Boolean addToBackStack)
	{
		try {
			Fragment fragment = (Fragment) fragmentClass.newInstance();
			
			if (bundle != null) {
				fragment.setArguments(bundle);
			}
			
			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment);
			
			if (addToBackStack) {
				transaction.addToBackStack(null);
			}
			
			transaction.commit();
		} catch (Exception e) {
			Log.d("myapp", "replaceFragments: " + e.toString());
		}
	}
}
