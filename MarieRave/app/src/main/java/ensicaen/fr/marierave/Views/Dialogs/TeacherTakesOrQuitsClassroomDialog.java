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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.TeacherClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;
import ensicaen.fr.marierave.Utils;
import ensicaen.fr.marierave.Views.TeacherHome;

public class TeacherTakesOrQuitsClassroomDialog extends DialogFragment implements View.OnClickListener
{
    private GridView _classroomsGridview;
	
	private String _mode;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialog_teacher_takes_or_quits_classroom, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
	
		_mode = getArguments().getString("mode");
        
        Button btnValidate = view.findViewById(R.id.button24);
        btnValidate.setOnClickListener(this);

        Button btnCancel = view.findViewById(R.id.button23);
        btnCancel.setOnClickListener(this);
	
		List<String> classroomNames = new TeacherClassroomDAO(getContext()).getClassroomsWithThisTeacher(Utils.teacherLoggedInId);
	
		List<Classroom> classroomList = new ArrayList<>();
	
		if (_mode.equals("add")) {
			classroomList = new ClassroomDAO(getContext()).getAllClassrooms();
			for (String name : classroomNames) {
				classroomList.remove(new Classroom(name));
			}
		}
		else if (_mode.equals("delete")) {
			for (String name : classroomNames) {
				classroomList.add(new Classroom(name));
			}
		}

        final GridViewAdapter adapter = new GridViewAdapter(getActivity(), classroomList);
        _classroomsGridview = view.findViewById(R.id.gridview_classes);
        _classroomsGridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        _classroomsGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (adapter._selectedPositions == position) {
                    adapter._selectedPositions = -1;
                } else {
                    adapter._selectedPositions = position;
                }

                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button24:
	
				if (((GridViewAdapter) _classroomsGridview.getAdapter())._selectedPositions != -1) {
					Classroom classroom = (Classroom) _classroomsGridview.getAdapter().getItem(((GridViewAdapter) _classroomsGridview.getAdapter())._selectedPositions);
		
					TeacherClassroomDAO teacherClassroomDAO = new TeacherClassroomDAO(getContext());
					
					if (_mode.equals("add")) {
						teacherClassroomDAO.addTeacherToClassroom(classroom.getName(), Utils.teacherLoggedInId);
					}
					else if (_mode.equals("delete")) {
						teacherClassroomDAO.deleteTeacherFromCLassroom(classroom.getName(), Utils.teacherLoggedInId);
					}
		
					((TeacherHome) getTargetFragment()).reloadClassroomListview();
				}
				
                dismiss();
                break;

            case R.id.button23:
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    private class GridViewAdapter extends BaseAdapter {
        private Activity _activity;
        private List<Classroom> _classroomList;
        private Integer _selectedPositions = -1;

        GridViewAdapter(Activity activity, List<Classroom> childList) {
            super();
            _activity = activity;
            _classroomList = childList;
        }

        @Override
        public int getCount() {
            return _classroomList.size();
        }

        @Override
        public Object getItem(int position) {
            return _classroomList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            private TextView _txtClassroomName;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
        	
            if (convertView == null) {
                convertView = _activity.getLayoutInflater().inflate(R.layout.gridview_classroom_class_item, null);
	
				viewHolder = new ViewHolder();
				viewHolder._txtClassroomName = convertView.findViewById(R.id.textView35);
	
				convertView.setTag(viewHolder);
	
			}
			else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
	
			viewHolder._txtClassroomName.setText(_classroomList.get(position).getName());
	
			if (_selectedPositions == position) {
                convertView.setBackgroundColor(Color.GRAY);
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }

            return convertView;
        }
    }
}