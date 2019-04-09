package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class StudentAssessment2 extends Fragment
{
	private Activity _activity;
	
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.student_assessment_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
		_activity = getActivity();
	
		final Bundle args = getArguments();
		
		final GridView gridview = view.findViewById(R.id.gridview_skills);
	
		ImageView profilePicture = view.findViewById(R.id.image);
		Picasso.get().load(new File(Utils.getChildPersonnalPicturePath(getContext(), args.getInt("Id")))).into(profilePicture);
		
		TextView textWelcome = view.findViewById(R.id.welcome);
	
        textWelcome.setText("Bienvenue " + args.getString("Prénom"));
	
		final List<Skill> skillList = new SkillDAO(getContext()).getAllSkills();
	
		final EditText editText = view.findViewById(R.id.skill);
		editText.addTextChangedListener(new TextWatcher()
		{
			private Timer timer = new Timer();
			private final long DELAY = 500;
		
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
		
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
		
			@Override
			public void afterTextChanged(final Editable editable)
			{
				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						_activity.runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								skillList.clear();
								for (Skill s : new SkillDAO(_activity).getAllSkills()) {
                                    if (s.getCode().toUpperCase().startsWith(editable.toString().toUpperCase())) {
										skillList.add(s);
									}
								}
							
								GridViewAdapter adapt = new GridViewAdapter(getActivity(), skillList);
								gridview.setAdapter(adapt);
								adapt.notifyDataSetChanged();
							}
						});
					
					}
				}, DELAY);
			}
		});
	
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				Bundle bundle = new Bundle();
				bundle.putString("Skill", ((Skill) parent.getItemAtPosition(position)).getCode());
				bundle.putString("SkillName", ((Skill) parent.getItemAtPosition(position)).getName());
				bundle.putInt("ChildId", args.getInt("Id"));
				bundle.putString("Prénom", args.getString("Prénom"));
				Utils.replaceFragments(StudentAssessment3.class, getActivity(), bundle, true);
			}
		});
		
        ImageView imageView = view.findViewById(R.id.backButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
				getActivity().getSupportFragmentManager().popBackStack();
            }
        });
	}
	
	private class GridViewAdapter extends BaseAdapter
	{
		private List<Skill> _skillList;
		private Activity _activity;
		
		GridViewAdapter(Activity activity, List<Skill> skillList)
		{
			super();
			_activity = activity;
			_skillList = skillList;
		}
		
		@Override
		public int getCount()
		{
			return _skillList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _skillList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private ImageView _skillImage;
			private TextView _skillCode;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_skills_item, null);
				ViewHolder holder = new ViewHolder();
				holder._skillImage = convertView.findViewById(R.id.imageView);
				holder._skillCode = convertView.findViewById(R.id.txt_skillCode);
				
				//holder._skillImage.setImageResource(R.mipmap.ic_launcher_round);
				holder._skillCode.setText(_skillList.get(position).getCode());
			}
			
			return convertView;
		}
	}
}
