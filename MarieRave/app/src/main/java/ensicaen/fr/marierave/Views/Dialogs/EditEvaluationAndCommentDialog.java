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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ensicaen.fr.marierave.Controllers.SkillCommentDAO;
import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.PersonnalProfile;

public class EditEvaluationAndCommentDialog extends DialogFragment implements View.OnClickListener
{
	private String _newMark;
	
	private Integer _childId;
	
	private String _skillCode;
	
	public EditEvaluationAndCommentDialog() { }
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.dialog_edit_evaluation_and_comment, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		final Bundle args = getArguments();
		
		_childId = args.getInt("ChildId");
		_skillCode = args.getString("Skill");
		
		TextView textView = view.findViewById(R.id.textView17);
		textView.setText(new SkillCommentDAO(getContext()).getSkillcomment(_childId, _skillCode));
		
		_newMark = new SkillMarkDAO(getContext()).getSkillMark(_childId, _skillCode);
		RadioButton rdb_mark = null;
		switch (_newMark) {
			case "A":
				rdb_mark = view.findViewById(R.id.darkGreen);
				break;
			case "B":
				rdb_mark = view.findViewById(R.id.green);
				break;
			case "C":
				rdb_mark = view.findViewById(R.id.yellow);
				break;
			case "D":
				rdb_mark = view.findViewById(R.id.red);
				break;
			default:
				break;
		}
		if (rdb_mark != null) {
			rdb_mark.setChecked(true);
		}
		
		RadioGroup radioGroup = view.findViewById(R.id.radio_group_assess);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				switch (checkedId) {
					case R.id.darkGreen:
						_newMark = "A";
						break;
					case R.id.green:
						_newMark = "B";
						break;
					case R.id.yellow:
						_newMark = "C";
						break;
					case R.id.red:
						_newMark = "D";
						break;
					default:
						break;
				}
			}
		});
		
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
				TextView textView = getDialog().findViewById(R.id.textView17);
				
				SkillMarkDAO skillMarkDAO = new SkillMarkDAO(getContext());
				SkillCommentDAO skillCommentDAO = new SkillCommentDAO(getContext());
				
				if (skillMarkDAO.skillMarkExists(_childId, _skillCode)) {
					skillMarkDAO.updateSkillMark(_childId, _skillCode, _newMark);
				}
				else {
					skillMarkDAO.addSkillMark(_childId, _skillCode, _newMark);
				}
				
				if (skillCommentDAO.skillCommentExists(_childId, _skillCode)) {
					skillCommentDAO.updateSkillcomment(_childId, _skillCode, textView.getText().toString());
				}
				else {
					skillCommentDAO.addSkillComment(_childId, _skillCode, textView.getText().toString());
				}
				
				((PersonnalProfile) getTargetFragment()).reloadSkillListView(null);
				dismiss();
				break;
			
			case R.id.btn_cancel:
				dismiss();
				break;
			
			default:
				break;
		}
	}
}