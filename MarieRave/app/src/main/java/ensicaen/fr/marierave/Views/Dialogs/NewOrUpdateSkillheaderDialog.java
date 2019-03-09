package ensicaen.fr.marierave.Views.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationSkills;

public class NewOrUpdateSkillheaderDialog extends DialogFragment implements android.view.View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_or_update_skillheader, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		Spinner spinnerSkillheaders = view.findViewById(R.id.spinner_subjects);
		ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new SubjectDAO(getContext()).getAllSubjects());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSkillheaders.setAdapter(adapter);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			
			EditText edit_skillheaderName = view.findViewById(R.id.edit_skillheaderName);
			edit_skillheaderName.setText(bundle.getString("skillheaderName"));
			
			spinnerSkillheaders.setSelection(adapter.getPosition(new Subject(bundle.getString("subjectName"))));
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
				EditText edit_skillheaderName = getDialog().findViewById(R.id.edit_skillheaderName);
				Spinner spinnerSkillheaders = getDialog().findViewById(R.id.spinner_subjects);
				
				SkillheaderDAO skillheaderDAO = new SkillheaderDAO(getContext());
				Skillheader newSkillheader = new Skillheader(edit_skillheaderName.getText().toString(), ((Subject) spinnerSkillheaders.getSelectedItem()).getName());
				
				if (getArguments() != null && skillheaderDAO.skillheaderExists(getArguments().getString("skillheaderName"))) {
					skillheaderDAO.updateSkillheader(getArguments().getString("skillheaderName"), newSkillheader);
				}
				else {
					skillheaderDAO.addSkillheader(newSkillheader);
				}
				
				((AdministrationSkills) getTargetFragment()).reloadSkillListView(null);
				
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