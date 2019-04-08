package ensicaen.fr.marierave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.io.File;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;

public class Utils
{
	public static Integer teacherLoggedInId = 0;
	public static String teacherLoggedInLogin = "";
	public static String tempFileUri = "";
	
	/**
	 * Switchs the fragment displayed in the application
	 * Can be called from inside a FragmentClass
	 *
	 * @param fragmentClass Fragment classroom to display
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
	
	public static String getChildPersonnalPicturePath(Context context, Integer childId)
	{
		
		Child child = new ChildDAO(context).getChild(childId);
		
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "ANEC" + File.separator + child
				.getFirstname() + "_" + child.getName() + "_" + child.getId() + ".jpg";
	}
	
	public static Bitmap getChildPersonnalPicture(Context context, Integer childId)
	{
		return BitmapFactory.decodeFile(getChildPersonnalPicturePath(context, childId));
	}
}
