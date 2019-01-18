package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.max;

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
	
	public static double compareStrings(String str1, String str2)
	{
		
		// pairs de 2 caractères qui composent la 1ère chaîne //
		ArrayList<String> pairs1 = wordLetterPairs(str1.toUpperCase());
		// pairs de 2 caractères qui composent la 2ème chaîne //
		ArrayList<String> pairs2 = wordLetterPairs(str2.toUpperCase());
		
		int similarity = 0;
		int totalSize = pairs1.size() + pairs2.size();
		
		for (String pair1 : pairs1) {
			
			for (String pair2 : pairs2) {
				if (pair1.equals(pair2)) {
					similarity++;
					pairs2.remove(pair2);
					break;
				}
			}
		}
		
		return (2.0 * similarity) / totalSize;
	}
	
	private static ArrayList<String> wordLetterPairs(String str)
	{
		ArrayList<String> allPairs = new ArrayList<>();
		
		// sépare les mots de la chaîne et les place dans un tableau //
		String[] words = str.split("\\s");
		
		// parcours des mots de la chaîne //
		for (String word : words) {
			// découpe le mot en chaînes de 2 caractères //
			String[] pairsInWord = letterPairs(word);
			
			allPairs.addAll(Arrays.asList(pairsInWord));
		}
		
		return allPairs;
	}
	
	private static String[] letterPairs(String str)
	{
		int numPairs = max(0, str.length() - 1);
		
		String[] pairs = new String[numPairs];
		for (int i = 0; i < numPairs; i++) {
			pairs[i] = str.substring(i, i + 2);
		}
		
		return pairs;
	}
}
