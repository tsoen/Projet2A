package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;

public class MarkMultipleChildsDialog extends DialogFragment implements android.view.View.OnClickListener
{
	private ListView _childListview;
	
	private String _skillCode;
	private String _classroomName;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.dialog_mark_multiple_childs, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setCancelable(false);
		
		_skillCode = getArguments().getString("Skill");
		_classroomName = getArguments().getString("ClassroomName");
		
		Button btnValidate = view.findViewById(R.id.button16);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.button15);
		btnCancel.setOnClickListener(this);
		
		_childListview = view.findViewById(R.id.listChildsClassMarks);
		reloadChildListview();
		
		return view;
	}
	
	public void reloadChildListview()
	{
		List<Child> childList = new ChildDAO(getContext()).getAllChildsInClassroom(_classroomName);
		ListviewChildsAdapter childsAdapter = new ListviewChildsAdapter(getActivity(), childList);
		
		_childListview.setAdapter(childsAdapter);
		childsAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				
				dismiss();
				break;
			
			case R.id.btn_cancel:
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}
	
	private class ListviewChildsAdapter extends BaseAdapter
	{
		private List<Child> _childList;
		private Activity _activity;
		
		ListviewChildsAdapter(Activity activity, List<Child> childList)
		{
			super();
			_activity = activity;
			_childList = childList;
		}
		
		@Override
		public int getCount()
		{
			return _childList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return _childList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			private TextView _txtName;
			private TextView _txtSurname;
			private TextView _txtMark;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				ViewHolder holder = new ViewHolder();
				convertView = _activity.getLayoutInflater().inflate(R.layout.listview_skill_assesment_dialog_item, null);
				
				holder._txtName = convertView.findViewById(R.id.textView39);
				holder._txtSurname = convertView.findViewById(R.id.textView40);
				holder._txtMark = convertView.findViewById(R.id.textView41);
				
				holder._txtName.setText(_childList.get(position).getName());
				holder._txtSurname.setText(_childList.get(position).getFirstname());
				
				holder._txtMark.setText(new SkillMarkDAO(getContext()).getSkillMark(_childList.get(position).getId(), _skillCode));
			}
			
			return convertView;
		}
	}
}