package ensicaen.fr.marierave.Views.Dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ensicaen.fr.marierave.Controllers.SkillCommentDAO;
import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.PersonnalProfile;

public class EditEvaluationAndCommentDialog extends DialogFragment
{
	private String newMark;
	
	public EditEvaluationAndCommentDialog() { }
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.dialog_edit_evaluation_and_comment, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		final Bundle args = getArguments();
		
		final TextView textView = view.findViewById(R.id.textView17);
		textView.setText(new SkillCommentDAO(getContext()).getSkillcomment(args.getInt("ChildId"), args.getString("Skill")));
		
		newMark = new SkillMarkDAO(getContext()).getSkillMark(args.getInt("ChildId"), args.getString("Skill"));
		RadioButton rdb_mark = null;
		switch (newMark) {
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
						newMark = "A";
						break;
					case R.id.green:
						newMark = "B";
						break;
					case R.id.yellow:
						newMark = "C";
						break;
					case R.id.red:
						newMark = "D";
						break;
					default:
						break;
				}
			}
		});
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				SkillMarkDAO skillMarkDAO = new SkillMarkDAO(getContext());
				SkillCommentDAO skillCommentDAO = new SkillCommentDAO(getContext());
				
				if (skillMarkDAO.skillMarkExists(args.getInt("ChildId"), args.getString("Skill"))) {
					skillMarkDAO.updateSkillMark(args.getInt("ChildId"), args.getString("Skill"), newMark);
				}
				else {
					skillMarkDAO.addSkillMark(args.getInt("ChildId"), args.getString("Skill"), newMark);
				}
				
				if (skillCommentDAO.skillCommentExists(args.getInt("ChildId"), args.getString("Skill"))) {
					skillCommentDAO.updateSkillcomment(args.getInt("ChildId"), args.getString("Skill"), textView.getText().toString());
				}
				else {
					skillCommentDAO.addSkillComment(args.getInt("ChildId"), args.getString("Skill"), textView.getText().toString());
				}
				
				((PersonnalProfile) getTargetFragment()).reloadSkillListView();
				dismiss();
			}
		});
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		
		return view;
	}
}