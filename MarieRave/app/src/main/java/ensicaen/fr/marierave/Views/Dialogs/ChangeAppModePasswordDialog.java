package ensicaen.fr.marierave.Views.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.TeacherHome;

public class ChangeAppModePasswordDialog extends DialogFragment implements android.view.View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_change_app_mode, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		Button btnValidate = view.findViewById(R.id.button10);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.button18);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.button10:
				Utils.replaceFragments(TeacherHome.class, getActivity(), null, false);
				dismiss();
				break;
			
			case R.id.button18:
				dismiss();
				break;
			
			default:
				dismiss();
				break;
		}
	}
}