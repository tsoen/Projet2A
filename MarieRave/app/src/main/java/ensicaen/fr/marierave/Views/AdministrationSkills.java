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
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.administration_skills, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(getActivity());
		
		List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
		
		for (Subject subject : subjectList) {
			skillsAdapter.addItem(subject);
			
			for (Skillheader skillheader : new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName())) {
				skillsAdapter.addItem(skillheader);
				
				for (Skill skill : new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName())) {
					skillsAdapter.addItem(skill);
				}
			}
		}
		
		final ListView skillsListview = view.findViewById(R.id.listCompetences);
		skillsListview.setAdapter(skillsAdapter);
		skillsAdapter.notifyDataSetChanged();
		
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
				NewSkillDialog dialog = new NewSkillDialog(getActivity());
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface arg0)
					{
						ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(getActivity());
						
						List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
						
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
					}
				});
				
				dialog.show();
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
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private List<Object> _skillsAndHeaders = new ArrayList<>();

		
		private Activity _activity;
		
		ListviewSkillAdapter(Activity activity)
		{
			super();
			_activity = activity;
		}
		
		void addItem(final Object item)
		{
			_skillsAndHeaders.add(item);
			notifyDataSetChanged();
		}

		void removeItem(int position) {
			_skillsAndHeaders.remove(position);
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
		}
		
		private class HeaderHolder
		{
			private TextView _header;
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			LayoutInflater mInflater = _activity.getLayoutInflater();

			Object objectAt = _skillsAndHeaders.get(position);

			if(objectAt instanceof Skill){
				ViewHolder holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.listview_skill_admin_item, null);

				holder._code = convertView.findViewById(R.id.txtCode);
				holder._name = convertView.findViewById(R.id.txtSkill);

				final Skill skill = (Skill) objectAt;
				holder._code.setText(skill.getCode());
				holder._name.setText(skill.getName());

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

										removeItem(position);
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
											new SkillDAO(_activity).deleteAllSkillsInHeader(skillheader.getName());
										}

										new SkillheaderDAO(_activity).deleteAllSkillheadersInSubject(subject.getName());

										removeItem(position);
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
				convertView.setTag(holderHeader);
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
