package ensicaen.fr.marierave.Views.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.AdministrationSkills;

public class NewOrUpdateSkillDialog extends DialogFragment implements android.view.View.OnClickListener
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_new_or_update_skill, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		Spinner spinnerSkillheaders = view.findViewById(R.id.spinner_skillheaders);
		List<Skillheader> skillheaders = new SkillheaderDAO(getContext()).getAllSkillheaders();
		ArrayAdapter<Skillheader> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, skillheaders);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSkillheaders.setAdapter(adapter);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			EditText edit_skillCode = view.findViewById(R.id.edit_skillCode);
			edit_skillCode.setText(bundle.getString("skillCode"));
			
			EditText edit_skillName = view.findViewById(R.id.edit_skillName);
			edit_skillName.setText(bundle.getString("skillName"));
			
			spinnerSkillheaders.setSelection(adapter.getPosition(new Skillheader(bundle.getString("skillHeaderName"))));
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
				EditText editSkillCode = getDialog().findViewById(R.id.edit_skillCode);
				EditText editSkillName = getDialog().findViewById(R.id.edit_skillName);
				Spinner spinnerSkillheaders = getDialog().findViewById(R.id.spinner_skillheaders);
				
				SkillDAO skillDAO = new SkillDAO(getContext());
				Skill newSkill = new Skill(editSkillCode.getText().toString(), editSkillName.getText().toString(), ((Skillheader) spinnerSkillheaders.getSelectedItem())
						.getName());
				
				if (getArguments() != null && skillDAO.skillExists(getArguments().getString("skillCode"))) {
					skillDAO.updateSkill(getArguments().getString("skillCode"), newSkill);
				}
				else {
					skillDAO.addSkill(newSkill);
				}
				
				((AdministrationSkills) getTargetFragment()).reloadSkillListView();
				
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