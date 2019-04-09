package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;

public class PersonnalProfileResults extends Fragment
{
	private Integer _childId;
	
	private ListView _listView;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.personnal_profile_results, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		_childId = getArguments().getInt("childId");
		
		Child child = new ChildDAO(getContext()).getChild(_childId);
		
		TextView txtName = view.findViewById(R.id.txtName);
		txtName.setText(child.getName());
		
		TextView txtSurname = view.findViewById(R.id.txtSurname);
		txtSurname.setText(child.getFirstname());
		
		ImageView personnalPicture = view.findViewById(R.id.imgPersonnalPicture);
		
		if (Utils.getChildPersonnalPicture(getContext(), _childId) != null) {
			Picasso.get().load(new File(Utils.getChildPersonnalPicturePath(getContext(), _childId))).into(personnalPicture);
		}
		else {
			Picasso.get().load(R.drawable.garcon_icon).into(personnalPicture);
		}
		
		_listView = view.findViewById(R.id.listviewResults);
		reloadSkillListView();
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		Button btnResults = view.findViewById(R.id.btnResults);
		btnResults.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}
	
	public void reloadSkillListView()
	{
		SkillListViewAdapter skillsAdapter = new SkillListViewAdapter(getActivity());
		
		List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
		
		Collections.sort(subjectList, new Comparator<Subject>()
		{
			@Override
			public int compare(Subject o1, Subject o2)
			{
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		
		
		for (Subject subject : subjectList) {
			skillsAdapter.addItem(subject);
			
			List<Skillheader> skillheaderList = new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName());
			Collections.sort(skillheaderList, new Comparator<Skillheader>()
			{
				@Override
				public int compare(Skillheader o1, Skillheader o2)
				{
					String o1StringPart = o1.getName().replaceAll("\\d", "");
					String o2StringPart = o2.getName().replaceAll("\\d", "");
					
					if (o1StringPart.equalsIgnoreCase(o2StringPart)) {
						return extractInt(o1.getName()) - extractInt(o2.getName());
					}
					
					return o1.getName().compareTo(o2.getName());
				}
				
				int extractInt(String s)
				{
					String num = s.replaceAll("\\D", "");
					return num.isEmpty() ? 0 : Integer.parseInt(num);
				}
			});
			
			for (Skillheader skillheader : skillheaderList) {
				skillsAdapter.addItem(skillheader);
			}
		}
		
		_listView.setAdapter(skillsAdapter);
		skillsAdapter.notifyDataSetChanged();
	}
	
	public Integer getPercentForSkillHeader(String skillHeaderName)
	{
		
		List<Skill> skillList = new SkillDAO(getContext()).getSkillsInHeader(skillHeaderName);
		
		SkillMarkDAO skillMarkDAO = new SkillMarkDAO(getContext());
		
		int percent = 0;
		
		for (Skill skill : skillList) {
			
			String mark = skillMarkDAO.getSkillMark(_childId, skill.getCode());
			
			if (mark.equals("A") || mark.equals("B")) {
				percent += 1;
			}
		}
		
		int max = skillList.size() == 0 ? 1 : skillList.size();
		
		return percent * 100 / max;
	}
	
	public Integer getPercentForSubject(String subjectName)
	{
		
		List<Skillheader> skillheaderList = new SkillheaderDAO(getContext()).getSkillheadersInSubject(subjectName);
		
		int percent = 0;
		
		for (Skillheader skillheader : skillheaderList) {
			
			percent += getPercentForSkillHeader(skillheader.getName());
		}
		
		int max = skillheaderList.size() == 0 ? 1 : skillheaderList.size();
		
		return percent / max;
	}
	
	private class SkillListViewAdapter extends BaseAdapter
	{
		private List<Object> _headersList = new ArrayList<>();
		
		private Activity _activity;
		
		SkillListViewAdapter(Activity activity)
		{
			super();
			_activity = activity;
		}
		
		void addItem(Object item)
		{
			_headersList.add(item);
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount()
		{
			return _headersList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _headersList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public int getItemViewType(int position)
		{
			if (_headersList.get(position) instanceof Subject) {
				return 0;
			}
			
			return 1;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 2;
		}
		
		private class ViewHolder
		{
			private TextView _txtTopic;
			private ProgressBar _prgsbarSkill;
			private TextView _txtPercent;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_progressbar_item, null);
				
				holder = new ViewHolder();
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				holder._prgsbarSkill = convertView.findViewById(R.id.progressBar);
				holder._txtPercent = convertView.findViewById(R.id.txtPercent);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (getItemViewType(position) == 0) {
				Subject subject = (Subject) _headersList.get(position);
				
				holder._txtTopic.setText(subject.getName());
				holder._txtTopic.setTypeface(Typeface.DEFAULT_BOLD);
				
				Integer percent = getPercentForSubject(subject.getName());
				
				holder._prgsbarSkill.setProgress(percent);
				holder._txtPercent.setText(percent + "%");
			}
			else {
				Skillheader skillheader = (Skillheader) _headersList.get(position);
				
				holder._txtTopic.setText("\t\t" + _headersList.get(position));
				
				Integer percent = getPercentForSkillHeader(skillheader.getName());
				
				holder._prgsbarSkill.setProgress(percent);
				holder._txtPercent.setText(percent + "%");
			}
			
			return convertView;
		}
	}
}
