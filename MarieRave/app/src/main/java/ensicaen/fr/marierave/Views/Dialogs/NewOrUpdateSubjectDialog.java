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
import android.widget.EditText;

import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationSkills;

public class NewOrUpdateSubjectDialog extends DialogFragment implements android.view.View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_or_update_subject, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			EditText edit_subjectName = view.findViewById(R.id.edit_subjectName);
			edit_subjectName.setText(bundle.getString("subjectName"));
		}
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				
				EditText editSubjectName = getDialog().findViewById(R.id.edit_subjectName);
				
				SubjectDAO subjectDAO = new SubjectDAO(getContext());
				Subject newSubject = new Subject(editSubjectName.getText().toString());
				
				if (getArguments() != null && subjectDAO.subjectExists(getArguments().getString("subjectName"))) {
					subjectDAO.updateSubject(getArguments().getString("subjectName"), newSubject);
				}
				else {
					subjectDAO.addSubject(newSubject);
				}
				
				((AdministrationSkills) getTargetFragment()).reloadSkillListView();
				((AdministrationSkills) getTargetFragment()).reloadSubjectListView();
				
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