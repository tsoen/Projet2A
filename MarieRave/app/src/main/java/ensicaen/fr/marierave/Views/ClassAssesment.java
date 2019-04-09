package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.Dialogs.MarkMultipleChildsDialog;

public class ClassAssesment extends Fragment
{
	private ListView skillsListview;
	
	private String _classroomName;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.class_assesment, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		_classroomName = getArguments().getString("classroomName");
		
		Classroom classroom = new ClassroomDAO(getContext()).getClassroom(_classroomName);
		
		TextView txtClassroomName = view.findViewById(R.id.classname);
		txtClassroomName.setText(classroom.getName());
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		ImageView btnLogOff = view.findViewById(R.id.imageView2);
		btnLogOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setMessage("Etes-vous sûr de vouloir vous déconnecter ?");
				builder.setCancelable(true);
				builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						Utils.replaceFragments(ConnectionFragment.class, getActivity(), null, false);
						
						dialog.cancel();
					}
				});
				builder.setNegativeButton("Non", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
				
				builder.create().show();
			}
		});
		
		skillsListview = view.findViewById(R.id.skills);
		reloadSkillListView(null);
		
		
		List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
		ListviewTopicsAdapter topicsAdapter = new ListviewTopicsAdapter(getActivity(), subjectList);
		
		ListView topicListview = view.findViewById(R.id.jumpToSubject);
		topicListview.setAdapter(topicsAdapter);
		
		topicListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				reloadSkillListView((Subject) parent.getAdapter().getItem(position));
			}
		});
	}
	
	public void reloadSkillListView(Subject filterSubject)
	{
		ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(this);
		List<Subject> subjectList = new ArrayList<>();
		
		if (filterSubject != null) {
			subjectList.add(filterSubject);
		}
		else {
			subjectList = new SubjectDAO(getContext()).getAllSubjects();
			
			Collections.sort(subjectList, new Comparator<Subject>()
			{
				@Override
				public int compare(Subject o1, Subject o2)
				{
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});
		}
		
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
				
				List<Skill> skillList = new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName());
				Collections.sort(skillList, new Comparator<Skill>()
				{
					@Override
					public int compare(Skill o1, Skill o2)
					{
						String o1StringPart = o1.getCode().replaceAll("\\d", "");
						String o2StringPart = o2.getCode().replaceAll("\\d", "");
						
						if (o1StringPart.equalsIgnoreCase(o2StringPart)) {
							return extractInt(o1.getCode()) - extractInt(o2.getCode());
						}
						
						return o1.getCode().compareTo(o2.getCode());
					}
					
					int extractInt(String s)
					{
						String num = s.replaceAll("\\D", "");
						return num.isEmpty() ? 0 : Integer.parseInt(num);
					}
				});
				
				for (Skill skill : skillList) {
					skillsAdapter.addItem(skill);
				}
			}
		}
		
		skillsListview.setAdapter(skillsAdapter);
		skillsAdapter.notifyDataSetChanged();
	}
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private List<Object> _skillsAndHeaders = new ArrayList<>();
		
		private ClassAssesment _fragment;
		
		ListviewSkillAdapter(ClassAssesment fragment)
		{
			super();
			_fragment = fragment;
		}
		
		void addItem(Object item)
		{
			_skillsAndHeaders.add(item);
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount()
		{
			return _skillsAndHeaders.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _skillsAndHeaders.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public int getItemViewType(int position)
		{
			if (_skillsAndHeaders.get(position) instanceof Skill) {
				return 0;
			}
			else if (_skillsAndHeaders.get(position) instanceof Subject) {
				return 1;
			}
			
			return 2;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 3;
		}
		
		private class ViewHolder
		{
			private TextView _code;
			private TextView _name;
			private ImageButton _btnEdit;
		}
		
		private class HeaderHolder
		{
			private TextView _name;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			int viewType = getItemViewType(position);
			
			switch (viewType) {
				case 0:
					ViewHolder holder1;
					
					View tempView1 = convertView;
					if (tempView1 == null) {
						tempView1 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_assesment_item, null);
						
						holder1 = new ViewHolder();
						holder1._code = tempView1.findViewById(R.id.txtCode);
						holder1._name = tempView1.findViewById(R.id.txtSkill);
						holder1._btnEdit = tempView1.findViewById(R.id.btnEdit);
						
						tempView1.setTag(holder1);
					}
					else {
						holder1 = (ViewHolder) tempView1.getTag();
					}
					
					final Skill item = (Skill) _skillsAndHeaders.get(position);
					holder1._code.setText(item.getCode());
					holder1._name.setText(item.getName());
					
					holder1._btnEdit.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Bundle bundle = new Bundle();
							bundle.putString("Skill", item.getCode());
							bundle.putString("ClassroomName", _classroomName);
							
							MarkMultipleChildsDialog dialog = new MarkMultipleChildsDialog();
							dialog.setArguments(bundle);
							dialog.setTargetFragment(_fragment, 0);
							dialog.show(getActivity().getSupportFragmentManager(), "editEvaluation");
						}
					});
					
					return tempView1;
				
				case 1:
					HeaderHolder holder2;
					
					View tempView2 = convertView;
					if (tempView2 == null) {
						tempView2 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_subject_item, null);
						
						holder2 = new HeaderHolder();
						holder2._name = tempView2.findViewById(R.id.txtBigHeader);
						
						tempView2.setTag(holder2);
					}
					else {
						holder2 = (HeaderHolder) tempView2.getTag();
					}
					
					Subject subject = (Subject) _skillsAndHeaders.get(position);
					holder2._name.setText(subject.getName());
					
					return tempView2;
				
				case 2:
					HeaderHolder holder3;
					
					View tempView3 = convertView;
					if (tempView3 == null) {
						tempView3 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_skillheader_item, null);
						
						holder3 = new HeaderHolder();
						holder3._name = tempView3.findViewById(R.id.txtLittleHeader);
						
						tempView3.setTag(holder3);
					}
					else {
						holder3 = (HeaderHolder) tempView3.getTag();
					}
					
					Skillheader skillheader = (Skillheader) _skillsAndHeaders.get(position);
					holder3._name.setText(skillheader.getName());
					
					return tempView3;
					
				default:
					break;
			}
			
			return convertView;
		}
	}
	
	private class ListviewTopicsAdapter extends BaseAdapter
	{
		private List<Subject> _subjectList;
		private Activity _activity;
		
		ListviewTopicsAdapter(Activity activity, List<Subject> subjectList)
		{
			super();
			_activity = activity;
			_subjectList = subjectList;
		}
		
		@Override
		public int getCount()
		{
			return _subjectList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _subjectList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private TextView _txtTopic;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_topic_item, null);
				
				holder = new ViewHolder();
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder._txtTopic.setText(_subjectList.get(position).getName());
			
			return convertView;
		}
	}
}
