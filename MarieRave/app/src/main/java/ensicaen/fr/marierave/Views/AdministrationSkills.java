package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Views.Dialogs.ImportFileDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateSkillDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateSkillheaderDialog;
import ensicaen.fr.marierave.Views.Dialogs.NewOrUpdateSubjectDialog;

public class AdministrationSkills extends Fragment
{
	private ListView skillsListview;
	
	private ListView topicListview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_skills, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		skillsListview = view.findViewById(R.id.listCompetences);
		topicListview = view.findViewById(R.id.listSubjects);
		
		reloadSkillListView(null);
		
		reloadSubjectListView();
		
		Button btn_newSubject = view.findViewById(R.id.btn_newSubject);
		btn_newSubject.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrUpdateSubjectDialog dialog = new NewOrUpdateSubjectDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newSubject");
			}
		});
		
		Button btn_newSkillheader = view.findViewById(R.id.btn_newSkillheader);
		btn_newSkillheader.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrUpdateSkillheaderDialog dialog = new NewOrUpdateSkillheaderDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newSkillheader");
			}
		});
		
		Button btnNewSkill = view.findViewById(R.id.button4);
		btnNewSkill.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewOrUpdateSkillDialog dialog = new NewOrUpdateSkillDialog();
				FragmentManager fm = getActivity().getSupportFragmentManager();
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "newSkill");
			}
		});
		
		ImageButton btnBack = view.findViewById(R.id.backButton);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		Button btnImportSkills = view.findViewById(R.id.button25);
		btnImportSkills.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				Bundle bundle = new Bundle();
				bundle.putString("mode", "importSkills");
				
				ImportFileDialog dialog = new ImportFileDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(fm.findFragmentById(R.id.fragment_container), 0);
				dialog.show(fm, "importFile");
			}
		});
		
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
		}
		
		for (Subject subject : subjectList) {
			skillsAdapter.addItem(subject);
			
			for (Skillheader skillheader : new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName())) {
				skillsAdapter.addItem(skillheader);
				
				for (Skill skill : new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName())) {
					skillsAdapter.addItem(skill);
				}
			}
		}
		
		skillsListview.setAdapter(skillsAdapter);
		skillsAdapter.notifyDataSetChanged();
	}
	
	public void reloadSubjectListView()
	{
		ListviewTopicsAdapter topicsAdapter = new ListviewTopicsAdapter(getActivity(), new SubjectDAO(getContext()).getAllSubjects());
		topicListview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
	}
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private List<Object> _skillsAndHeaders = new ArrayList<>();
		
		private AdministrationSkills fragment;
		
		ListviewSkillAdapter(AdministrationSkills fragment)
		{
			super();
			this.fragment = fragment;
		}
		
		void addItem(Object item)
		{
			_skillsAndHeaders.add(item);
			notifyDataSetChanged();
		}
		
		void removeItem(Object obj)
		{
			_skillsAndHeaders.remove(obj);
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
		
		private class ViewHolder
		{
			private TextView _code;
			private TextView _name;
			private ImageButton _btnEdit;
			private ImageButton _btnDelete;
		}
		
		private class HeaderHolder
		{
			private TextView _header;
			private ImageButton _btnEdit;
			private ImageButton _btnDelete;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			LayoutInflater mInflater = fragment.getLayoutInflater();
			
			final Object objectAt = _skillsAndHeaders.get(position);
			
			if (objectAt instanceof Skill) {
				convertView = mInflater.inflate(R.layout.listview_skill_admin_item, null);
				
				ViewHolder holder = new ViewHolder();
				holder._code = convertView.findViewById(R.id.txtCode);
				holder._name = convertView.findViewById(R.id.txtSkill);
				holder._btnEdit = convertView.findViewById(R.id.btnEdit);
				holder._btnDelete = convertView.findViewById(R.id.button9);
				
				final Skill skill = (Skill) objectAt;
				holder._code.setText(skill.getCode());
				holder._name.setText(skill.getName());
				
				holder._btnEdit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("skillCode", skill.getCode());
						bundle.putString("skillName", skill.getName());
						bundle.putString("skillHeaderName", skill.getSkillheader());
						
						NewOrUpdateSkillDialog dialog = new NewOrUpdateSkillDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(getActivity().getSupportFragmentManager(), "newSkill");
					}
				});
				
				holder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer la compétence ? Cette action est irréversible");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new SkillDAO(getContext()).deleteSkill(skill.getCode());
								removeItem(skill);
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
			}
			else if (objectAt instanceof Subject) {
				convertView = mInflater.inflate(R.layout.listview_skill_subject_admin_item, null);
				
				HeaderHolder subjectHolder = new HeaderHolder();
				subjectHolder._header = convertView.findViewById(R.id.txtBigHeader);
				subjectHolder._btnEdit = convertView.findViewById(R.id.btnEdit);
				subjectHolder._btnDelete = convertView.findViewById(R.id.button11);
				
				final Subject subject = (Subject) objectAt;
				subjectHolder._header.setText(subject.getName());
				
				subjectHolder._btnEdit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("subjectName", subject.getName());
						
						NewOrUpdateSubjectDialog dialog = new NewOrUpdateSubjectDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(getActivity().getSupportFragmentManager(), "newSubject");
					}
				});
				
				subjectHolder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer cette matière ? " + "Toutes les sections et compétences associées seront supprimées. " +
								"Cette action est irreversible.");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new SubjectDAO(getContext()).deleteSubject(subject.getName());
								reloadSkillListView(null);
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
			}
			else if (objectAt instanceof Skillheader) {
				convertView = mInflater.inflate(R.layout.listview_skill_skillheader_admin_item, null);
				
				HeaderHolder skillheaderHolder = new HeaderHolder();
				skillheaderHolder._header = convertView.findViewById(R.id.txtLittleHeader);
				skillheaderHolder._btnEdit = convertView.findViewById(R.id.btnEdit);
				skillheaderHolder._btnDelete = convertView.findViewById(R.id.button11);
				
				final Skillheader skillheader = (Skillheader) objectAt;
				skillheaderHolder._header.setText(skillheader.getName());
				
				skillheaderHolder._btnEdit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("skillheaderName", skillheader.getName());
						bundle.putString("subjectName", skillheader.getSubject());
						
						NewOrUpdateSkillheaderDialog dialog = new NewOrUpdateSkillheaderDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(getActivity().getSupportFragmentManager(), "newSkillheader");
					}
				});
				
				skillheaderHolder._btnDelete.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setMessage("Etes-vous sûr de vouloir supprimer cette section ? " + "Toutes les compétences associées seront supprimées. " + "Cette " +
								"action est irreversible.");
						builder.setCancelable(true);
						builder.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								new SkillheaderDAO(getContext()).deleteSkillheader(skillheader.getName());
								reloadSkillListView(null);
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
			}
			
			return convertView;
		}
	}
	
	private class ListviewTopicsAdapter extends BaseAdapter
	{
		private List<Subject> _topicList;
		private Activity _activity;
		
		ListviewTopicsAdapter(Activity activity, List<Subject> productList)
		{
			super();
			_activity = activity;
			_topicList = productList;
		}
		
		@Override
		public int getCount()
		{
			return _topicList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _topicList.get(position);
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
			if (convertView == null) {
				ViewHolder holder = new ViewHolder();
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_topic_item, null);
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				holder._txtTopic.setText(_topicList.get(position).getName());
			}
			
			return convertView;
		}
	}
}
