package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;

public class NewChildDialog extends Dialog implements android.view.View.OnClickListener {
	
	public NewChildDialog(Activity a) {
		super(a);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		setContentView(R.layout.dialog_new_child);
		
		Spinner spinnerClassrooms = findViewById(R.id.spinner_classrooms);
		List<Classroom> classrooms = new ClassroomDAO(getContext()).getAllClassrooms();
		ArrayAdapter<Classroom> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, classrooms);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerClassrooms.setAdapter(adapter);
		
		Button btnValidate = findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_validate:
				EditText edtChildName = findViewById(R.id.edit_childName);
				EditText edtChildSurname = findViewById(R.id.edit_childSurname);
				Spinner spinnerClassrooms = findViewById(R.id.spinner_classrooms);
				
				ChildDAO childDAO = new ChildDAO(getContext());
				
				childDAO.addChild(new Child(edtChildName.getText().toString(), edtChildSurname.getText().toString(), ((Classroom)spinnerClassrooms.getSelectedItem()).getName()));
				
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
}
