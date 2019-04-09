package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.AdministrationClassroom;

public class AddChildToClassroomDialog extends DialogFragment implements View.OnClickListener
{
	private String _classroomName;
	private String _mode;
	private GridView _childsGridview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.dialog_add_child_to_classroom, container);
		
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		
		_mode = getArguments().getString("mode");
		_classroomName = getArguments().getString("classroomName");
		
		Button btnValidate = view.findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = view.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
		
		List<Child> childList = new ArrayList<>();
		if (_mode.equals("Add")) {
			childList = new ChildDAO(getContext()).getAllChildsNotInClassroom(_classroomName);
		}
		else if (_mode.equals("Delete")) {
			childList = new ChildDAO(getContext()).getAllChildsInClassroom(_classroomName);
		}
		
		final GridViewAdapter adapter = new GridViewAdapter(getActivity(), childList);
		_childsGridview = view.findViewById(R.id.gridview_childsToAdd);
		_childsGridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		_childsGridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				if (!adapter._selectedPositions.contains(position)) {
					adapter._selectedPositions.add(position);
				}
				else {
					adapter._selectedPositions.remove(Integer.valueOf(position));
				}
				
				TextView txtNumberOfSelected = view.findViewById(R.id.textView14);
				txtNumberOfSelected.setText(Integer.toString(adapter._selectedPositions.size()));
				
				adapter.notifyDataSetChanged();
			}
		});
		
		CheckBox cboSelectAll = view.findViewById(R.id.checkBox);
		cboSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked) {
					for (int i = 0; i < _childsGridview.getChildCount(); i++) {
						if (!adapter._selectedPositions.contains(i)) {
							adapter._selectedPositions.add(i);
						}
					}
				}
				else {
					for (int i = 0; i < _childsGridview.getChildCount(); i++) {
						adapter._selectedPositions.remove(Integer.valueOf(i));
					}
				}
				
				TextView txtNumberOfSelected = view.findViewById(R.id.textView14);
				txtNumberOfSelected.setText(Integer.toString(adapter._selectedPositions.size()));
				adapter.notifyDataSetChanged();
			}
		});
		
		return view;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
		params.width = ViewGroup.LayoutParams.MATCH_PARENT;
		getDialog().getWindow().setAttributes(params);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.btn_validate:
				
				ChildDAO childDAO = new ChildDAO(getContext());
				for (int i : ((GridViewAdapter) _childsGridview.getAdapter())._selectedPositions) {
					Child child = (Child) _childsGridview.getAdapter().getItem(i);
					if (_mode.equals("Add")) {
						child.setClassroom(_classroomName);
					}
					else if (_mode.equals("Delete")) {
						child.setClassroom("Ecole");
					}
					
					childDAO.updateChild(child);
				}
				
				((AdministrationClassroom) getTargetFragment()).reloadChildsGridview();
				
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
	
	private class GridViewAdapter extends BaseAdapter
	{
		private Activity _activity;
		private List<Child> _childList;
		private List<Integer> _selectedPositions = new ArrayList<>();
		
		GridViewAdapter(Activity activity, List<Child> childList)
		{
			super();
			_activity = activity;
			_childList = childList;
			
			Collections.sort(_childList, new Comparator<Child>()
			{
				@Override
				public int compare(Child o1, Child o2)
				{
					int value1 = o1.getName().compareToIgnoreCase(o2.getName());
					
					if (value1 == 0) {
						return o1.getFirstname().compareToIgnoreCase(o2.getFirstname());
					}
					
					return value1;
				}
			});
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
			private ImageView _profilePic;
			private TextView _txtName;
			private TextView _txtSurname;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			
			if (convertView == null) {
				convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_guy_item, null);
				
				holder = new ViewHolder();
				holder._profilePic = convertView.findViewById(R.id.imgProfilePicture);
				holder._txtName = convertView.findViewById(R.id.txtName);
				holder._txtSurname = convertView.findViewById(R.id.txtSurname);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if (Utils.getChildPersonnalPicture(getContext(), _childList.get(position).getId()) != null) {
				Picasso.get().load(new File(Utils.getChildPersonnalPicturePath(getContext(), _childList.get(position).getId()))).into(holder._profilePic);
			}
			else {
				Picasso.get().load(R.drawable.garcon_icon).into(holder._profilePic);
			}
			
			holder._txtName.setText(_childList.get(position).getName());
			holder._txtSurname.setText(_childList.get(position).getFirstname());
			
			if (_selectedPositions.contains(position)) {
				convertView.setBackgroundColor(Color.GRAY);
			}
			else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
			}
			
			return convertView;
		}
	}
}