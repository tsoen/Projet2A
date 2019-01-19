package ensicaen.fr.marierave.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.R;

public class StudentAssessment3 extends Fragment
{
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.student_assessment_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
	
		final Bundle args = getArguments();
	
		TextView textWelcome = view.findViewById(R.id.welcome2);
		textWelcome.setText("Bienvenue " + args.getString("Prénom"));
	
		TextView txtSkill = view.findViewById(R.id.skill);
		txtSkill.setText(args.getString("Skill"));
	
		TextView txt_skillName = view.findViewById(R.id.txt_skillName);
		txt_skillName.setText(args.getString("SkillName"));
	
		String mark = new SkillMarkDAO(getContext()).getSkillMark(args.getInt("ChildId"), args.getString("Skill"));
		RadioButton rdb_mark = null;
		switch (mark) {
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
				String mark = "";
			
				switch (checkedId) {
					case R.id.darkGreen:
						mark = "A";
						break;
					case R.id.green:
						mark = "B";
						break;
					case R.id.yellow:
						mark = "C";
						break;
					case R.id.red:
						mark = "D";
						break;
					default:
						break;
				}
			
				new SkillMarkDAO(getContext()).addSkillMark(args.getInt("ChildId"), args.getString("Skill"), mark);
			}
		});
        
        ImageView imageView = view.findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
				getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
