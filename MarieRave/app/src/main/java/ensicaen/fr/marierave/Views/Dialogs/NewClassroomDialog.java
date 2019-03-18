package ensicaen.fr.marierave.Views.Dialogs;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationHome;

public class NewClassroomDialog extends DialogFragment implements android.view.View.OnClickListener
{
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_classroom, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_validate:
				EditText edtClassroomName = v.findViewById(R.id.edit_classroomName);
				
				ClassroomDAO classroomDAO = new ClassroomDAO(getContext());
				
				try {
					classroomDAO.addClassroom(new Classroom(edtClassroomName.getText().toString()));
				} catch (SQLiteConstraintException e){
					Toast.makeText(getContext(), "Une classe avec ce nom existe déjà !", Toast.LENGTH_SHORT).show();
				}
				
				((AdministrationHome) getTargetFragment()).reloadClassroomListview();
				
				dismiss();
				break;
				
			case R.id.btn_cancel:
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
}
