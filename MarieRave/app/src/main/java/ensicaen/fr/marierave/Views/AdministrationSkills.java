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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
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
	
	public void reloadSubjectListView()
	{
		ListviewTopicsAdapter topicsAdapter = new ListviewTopicsAdapter(getActivity(), new SubjectDAO(getContext()).getAllSubjects());
		topicListview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
	}
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private List<Object> _skillsAndHeaders = new ArrayList<>();
		
		private AdministrationSkills _fragment;
		
		ListviewSkillAdapter(AdministrationSkills fragment)
		{
			super();
			_fragment = fragment;
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
			int viewType = getItemViewType(position);
			
			switch (viewType) {
				case 0:
					ViewHolder holder1;
					
					View tempView1 = convertView;
					if (tempView1 == null) {
						tempView1 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_admin_item, null);
						
						holder1 = new ViewHolder();
						holder1._code = tempView1.findViewById(R.id.txtCode);
						holder1._name = tempView1.findViewById(R.id.txtSkill);
						holder1._btnEdit = tempView1.findViewById(R.id.btnEdit);
						holder1._btnDelete = tempView1.findViewById(R.id.button9);
						
						tempView1.setTag(holder1);
					}
					else {
						holder1 = (ViewHolder) tempView1.getTag();
					}
					
					final Skill skill = (Skill) _skillsAndHeaders.get(position);
					holder1._code.setText(skill.getCode());
					holder1._name.setText(skill.getName());
					
					holder1._btnEdit.setOnClickListener(new View.OnClickListener()
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
							dialog.setTargetFragment(_fragment, 0);
							dialog.show(getActivity().getSupportFragmentManager(), "newSkill");
						}
					});
					
					holder1._btnDelete.setOnClickListener(new View.OnClickListener()
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
					
					return tempView1;
				
				case 1:
					HeaderHolder holder2;
					
					View tempView2 = convertView;
					if (tempView2 == null) {
						tempView2 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_subject_admin_item, null);
						
						holder2 = new HeaderHolder();
						holder2._header = tempView2.findViewById(R.id.txtBigHeader);
						holder2._btnEdit = tempView2.findViewById(R.id.btnEdit);
						holder2._btnDelete = tempView2.findViewById(R.id.button11);
						
						tempView2.setTag(holder2);
					}
					else {
						holder2 = (HeaderHolder) tempView2.getTag();
					}
					
					final Subject subject = (Subject) _skillsAndHeaders.get(position);
					holder2._header.setText(subject.getName());
					
					holder2._btnEdit.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Bundle bundle = new Bundle();
							bundle.putString("subjectName", subject.getName());
							
							NewOrUpdateSubjectDialog dialog = new NewOrUpdateSubjectDialog();
							dialog.setArguments(bundle);
							dialog.setTargetFragment(_fragment, 0);
							dialog.show(getActivity().getSupportFragmentManager(), "newSubject");
						}
					});
					
					holder2._btnDelete.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
							builder.setMessage("Etes-vous sûr de vouloir supprimer cette matière ? " + "Toutes les sections et compétences associées seront supprimées. "
									+ "Cette action est irreversible.");
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
					
					return tempView2;
				
				case 2:
					HeaderHolder holder3;
					
					View tempView3 = convertView;
					if (tempView3 == null) {
						tempView3 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_skillheader_admin_item, null);
						
						holder3 = new HeaderHolder();
						holder3._header = tempView3.findViewById(R.id.txtLittleHeader);
						holder3._btnEdit = tempView3.findViewById(R.id.btnEdit);
						holder3._btnDelete = tempView3.findViewById(R.id.button11);
						
						tempView3.setTag(holder3);
					}
					else {
						holder3 = (HeaderHolder) tempView3.getTag();
					}
					
					final Skillheader skillheader = (Skillheader) _skillsAndHeaders.get(position);
					holder3._header.setText(skillheader.getName());
					
					holder3._btnEdit.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Bundle bundle = new Bundle();
							bundle.putString("skillheaderName", skillheader.getName());
							bundle.putString("subjectName", skillheader.getSubject());
							
							NewOrUpdateSkillheaderDialog dialog = new NewOrUpdateSkillheaderDialog();
							dialog.setArguments(bundle);
							dialog.setTargetFragment(_fragment, 0);
							dialog.show(getActivity().getSupportFragmentManager(), "newSkillheader");
						}
					});
					
					holder3._btnDelete.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View view)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
							builder.setMessage("Etes-vous sûr de vouloir supprimer cette section ? " + "Toutes les compétences associées seront supprimées. " + "Cette " + "action est irreversible.");
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
			
			Collections.sort(_subjectList, new Comparator<Subject>()
			{
				@Override
				public int compare(Subject o1, Subject o2)
				{
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});
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
