package ensicaen.fr.marierave.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import ensicaen.fr.marierave.Views.Dialogs.EditEvaluationAndCommentDialog;
import ensicaen.fr.marierave.Views.Dialogs.EditPersonnalPictureDialog;

public class PersonnalProfile extends Fragment
{
	private Integer _childId;
	
	private ListView skillsListview;
	
	private Subject _currentFilter;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.personnal_profile, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		_childId = getArguments().getInt("childId");
		
		Child child = new ChildDAO(getContext()).getChild(_childId);
		
		reloadPicture();
		
		ImageView editPictureButton = view.findViewById(R.id.imageView3);
		editPictureButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("childId", _childId);
				
				EditPersonnalPictureDialog dialog = new EditPersonnalPictureDialog();
				dialog.setArguments(bundle);
				dialog.setTargetFragment(getParentFragment(), 0);
				dialog.show(getActivity().getSupportFragmentManager(), "editPicture");
			}
		});
		
		TextView txtName = view.findViewById(R.id.txtName);
		txtName.setText(child.getName());
		
		TextView txtSurname = view.findViewById(R.id.txtSurname);
		txtSurname.setText(child.getFirstname());
		
		skillsListview = view.findViewById(R.id.listSkills);
		reloadSkillListView();
		skillsListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				if (parent.getAdapter().getItemViewType(position) == 0) {
					
					String code = ((TextView) view.findViewById(R.id.txtCode)).getText().toString();
					String result = ((TextView) view.findViewById(R.id.txtResult)).getText().toString();
					String skill = ((TextView) view.findViewById(R.id.txtSkill)).getText().toString();
					
					Toast.makeText(getContext(), "Code : " + code + "\n" + "Compétence : " + result + "\n" + "Evaluation : " + skill, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
		ListviewTopicsAdapter topicsAdapter = new ListviewTopicsAdapter(getActivity(), subjectList);
		
		ListView topicListview = view.findViewById(R.id.listTopics);
		topicListview.setAdapter(topicsAdapter);
		topicsAdapter.notifyDataSetChanged();
		
		topicListview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				_currentFilter = (Subject) parent.getAdapter().getItem(position);
				reloadSkillListView();
			}
		});
		
		ImageButton btnResults = view.findViewById(R.id.btnResults);
		btnResults.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(PersonnalProfileResults.class, getActivity(), null, true);
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

		ImageButton btnPDF = view.findViewById(R.id.btnPDF);
		btnPDF.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putInt("childId", _childId);
				
				Utils.replaceFragments(PrintPDF.class, getActivity(), bundle, true);
			}
		});
	}
	
	public void reloadPicture()
	{
		ImageView personnalPicture = getView().findViewById(R.id.imgPersonnalPicture);
		
		Bitmap img = Utils.getChildPersonnalPicture(getContext(), _childId);
		if (img != null) {
			personnalPicture.setImageBitmap(img);
		}
	}
	
	public void reloadSkillListView()
	{
		ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(this);
		List<Subject> subjectList = new ArrayList<>();
		
		if (_currentFilter != null) {
			subjectList.add(_currentFilter);
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
					return o1.getName().compareToIgnoreCase(o2.getName());
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
	
	public void scrollTo(int position)
	{
		skillsListview.setSelection(position);
	}
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private ArrayList<Object> _skillsAndHeaders = new ArrayList<>();
		
		private PersonnalProfile _fragment;
		
		ListviewSkillAdapter(PersonnalProfile fragment)
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
			private TextView _result;
			private ImageButton _btnEdit;
		}
		
		private class HeaderHolder
		{
			private TextView _header;
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
						tempView1 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_item, null);
						
						holder1 = new ViewHolder();
						holder1._code = tempView1.findViewById(R.id.txtCode);
						holder1._result = tempView1.findViewById(R.id.txtResult);
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
					switch (new SkillMarkDAO(getContext()).getSkillMark(_childId, item.getCode())) {
						case "A":
							holder1._result.setBackgroundColor(Color.parseColor("#088A08"));
							break;
						
						case "B":
							holder1._result.setBackgroundColor(Color.parseColor("#00FF40"));
							break;
						
						case "C":
							holder1._result.setBackgroundColor(Color.parseColor("#FFD500"));
							break;
						
						case "D":
							holder1._result.setBackgroundColor(Color.parseColor("#FF0000"));
							break;
						
						default:
							holder1._result.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cell_shape_noside, null));
							break;
					}
					
					holder1._btnEdit.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Bundle bundle = new Bundle();
							bundle.putInt("ChildId", _childId);
							bundle.putString("Skill", item.getCode());
							bundle.putInt("SkillPosition", position);
							
							EditEvaluationAndCommentDialog dialog = new EditEvaluationAndCommentDialog();
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
						holder2._header = tempView2.findViewById(R.id.txtBigHeader);
						
						tempView2.setTag(holder2);
					}
					else {
						holder2 = (HeaderHolder) tempView2.getTag();
					}
					
					Subject subject = (Subject) _skillsAndHeaders.get(position);
					holder2._header.setText(subject.getName());
					
					return tempView2;
				
				case 2:
					HeaderHolder holder3;
					
					View tempView3 = convertView;
					if (tempView3 == null) {
						tempView3 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_skillheader_item, null);
						
						holder3 = new HeaderHolder();
						holder3._header = tempView3.findViewById(R.id.txtLittleHeader);
						
						tempView3.setTag(holder3);
					}
					else {
						holder3 = (HeaderHolder) tempView3.getTag();
					}
					
					Skillheader skillheader = (Skillheader) _skillsAndHeaders.get(position);
					holder3._header.setText(skillheader.getName());
					
					return tempView3;
				
				default:
					break;
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
			
			holder._txtTopic.setText(_topicList.get(position).getName());
			
			return convertView;
		}
	}
}

