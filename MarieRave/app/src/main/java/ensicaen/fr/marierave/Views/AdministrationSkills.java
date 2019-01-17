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
import android.widget.BaseAdapter;
import android.widget.Button;
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
import ensicaen.fr.marierave.Views.Dialogs.NewSkillDialog;

public class AdministrationSkills extends Fragment
{
	private ListView skillsListview;
	
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
		
		reloadSkillListView();
		
		List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
		
		ListviewTopicsAdapter topicsAdapter = new ListviewTopicsAdapter(getActivity(), subjectList);
		ListView topicListview = view.findViewById(R.id.listSubjects);
		topicListview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
		
		Button btnNewSkill = view.findViewById(R.id.button4);
		btnNewSkill.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FragmentManager fm = getActivity().getSupportFragmentManager();
				NewSkillDialog dialog = new NewSkillDialog();
				dialog.show(fm, "newSkill");
			}
		});
		
		Button btnBack = view.findViewById(R.id.button1);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}
	
	public void reloadSkillListView()
	{
		ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(this);
		
		for (Subject subject : new SubjectDAO(getContext()).getAllSubjects()) {
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
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private List<Object> _skillsAndHeaders = new ArrayList<>();
		
		private AdministrationSkills fragment;
		
		ListviewSkillAdapter(AdministrationSkills fragment)
		{
			super();
			this.fragment = fragment;
		}
		
		void addItem(final Object item)
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
			private Button _btnEdit;
		}
		
		private class HeaderHolder
		{
			private TextView _header;
			private Button _btnEdit;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			LayoutInflater mInflater = fragment.getLayoutInflater();
			
			final Object objectAt = _skillsAndHeaders.get(position);

			if(objectAt instanceof Skill){
				ViewHolder holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.listview_skill_admin_item, null);

				holder._code = convertView.findViewById(R.id.txtCode);
				holder._name = convertView.findViewById(R.id.txtSkill);

				final Skill skill = (Skill) objectAt;
				holder._code.setText(skill.getCode());
				holder._name.setText(skill.getName());
				
				Button btnEditSkill = convertView.findViewById(R.id.btnEdit);
				btnEditSkill.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Bundle bundle = new Bundle();
						bundle.putString("skillCode", skill.getCode());
						bundle.putString("skillName", skill.getName());
						bundle.putString("skillHeaderName", skill.getSkillheader());
						
						FragmentManager fm = getActivity().getSupportFragmentManager();
						NewSkillDialog dialog = new NewSkillDialog();
						dialog.setArguments(bundle);
						dialog.setTargetFragment(fragment, 0);
						dialog.show(fm, "newSkill");
					}
				});
				
				Button btnDeleteSkill = convertView.findViewById(R.id.button9);
				btnDeleteSkill.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
						builder1.setMessage("Etes-vous sûr de vouloir supprimer la compétence ? Cette action est irréversible");
						builder1.setCancelable(true);

						builder1.setPositiveButton(
								"Oui",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										new SkillDAO(getContext()).deleteSkill(skill.getCode());
										removeItem(skill);
										dialog.cancel();
									}
								});

						builder1.setNegativeButton(
								"Non",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});

						AlertDialog alert11 = builder1.create();
						alert11.show();
					}
				});
			}
			else if(objectAt instanceof Subject){
				HeaderHolder holderBigHeader = new HeaderHolder();
				convertView = mInflater.inflate(R.layout.listview_skill_subject_admin_item, null);
				holderBigHeader._header = convertView.findViewById(R.id.txtBigHeader);
				holderBigHeader._header.setText(((Subject) _skillsAndHeaders.get(position)).getName());

				final Subject subject = (Subject) objectAt;
				Button btnDeleteSubject = convertView.findViewById(R.id.button11);
				btnDeleteSubject.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
						builder1.setMessage("Etes-vous sûr de vouloir supprimer cette matière ? " +
								"Toutes les sections et compétences associées seront supprimées. " +
								"Cette action est irreversible.");
						builder1.setCancelable(true);

						builder1.setPositiveButton(
								"Oui",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

										for (Skillheader skillheader : new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName())) {
											for (Skill skill : new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName())) {
												removeItem(skill);
											}
											new SkillDAO(getContext()).deleteAllSkillsInHeader(skillheader.getName());
											removeItem(skillheader);
										}
										
										new SkillheaderDAO(getContext()).deleteAllSkillheadersInSubject(subject.getName());
										new SubjectDAO(getContext()).deleteSubject(subject.getName());
										removeItem(subject);
										dialog.cancel();
									}
								});

						builder1.setNegativeButton(
								"Non",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});

						AlertDialog alert11 = builder1.create();
						alert11.show();
					}
				});
			}
			else if(objectAt instanceof Skillheader){
				HeaderHolder holderHeader = new HeaderHolder();
				convertView = mInflater.inflate(R.layout.listview_skill_skillheader_admin_item, null);
				holderHeader._header = convertView.findViewById(R.id.txtLittleHeader);
				holderHeader._header.setText(((Skillheader) _skillsAndHeaders.get(position)).getName());
				
				final Skillheader skillheader = (Skillheader) objectAt;
				Button btnDeleteSkillheader = convertView.findViewById(R.id.button11);
				btnDeleteSkillheader.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						
						AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
						builder1.setMessage("Etes-vous sûr de vouloir supprimer cette section ? " + "Toutes les compétences associées seront supprimées. " + "Cette " +
								"action est irreversible.");
						builder1.setCancelable(true);
						
						builder1.setPositiveButton("Oui", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								
								for (Skill skill : new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName())) {
									removeItem(skill);
								}
								new SkillDAO(getContext()).deleteAllSkillsInHeader(skillheader.getName());
								removeItem(skillheader);
								dialog.cancel();
							}
						});
						builder1.setNegativeButton("Non", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int id)
							{
								dialog.cancel();
							}
						});
						AlertDialog alert11 = builder1.create();
						alert11.show();
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
				convertView =  _activity.getLayoutInflater().inflate(R.layout.listview_topic_item, null);
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				holder._txtTopic.setText(_topicList.get(position).getName());
			}
			
			return convertView;
		}
	}
}
