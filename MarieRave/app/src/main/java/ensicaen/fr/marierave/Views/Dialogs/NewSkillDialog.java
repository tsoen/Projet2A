package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
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

public class NewSkillDialog extends Dialog implements android.view.View.OnClickListener
{
	
	public NewSkillDialog(Activity a)
	{
		super(a);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_new_skill);
		
		Spinner spinnerSkillheaders = findViewById(R.id.spinner_skillheaders);
		List<Skillheader> skillheaders = new SkillheaderDAO(getContext()).getAllSkillheaders();
		ArrayAdapter<Skillheader> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, skillheaders);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSkillheaders.setAdapter(adapter);
		
		Button btnValidate = findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				EditText editSkillCode = findViewById(R.id.edit_skillCode);
				EditText editSkillName = findViewById(R.id.edit_skillName);
				Spinner spinnerSkillheaders = findViewById(R.id.spinner_skillheaders);
				
				SkillDAO skillDAO = new SkillDAO(getContext());
				skillDAO.addSkill(new Skill(editSkillCode.getText().toString(), editSkillName.getText().toString(), ((Skillheader) spinnerSkillheaders.getSelectedItem())
						.getName()));
				
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