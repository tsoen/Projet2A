package ensicaen.fr.marierave.Views.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.R;

public class NewClassroomDialog extends Dialog implements android.view.View.OnClickListener {
	
	public NewClassroomDialog(Activity a) {
		super(a);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		setContentView(R.layout.dialog_new_classroom);
		
		Button btnValidate = findViewById(R.id.btn_validate);
		btnValidate.setOnClickListener(this);
		
		Button btnCancel = findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_validate:
				EditText edtClassroomName = findViewById(R.id.edit_classroomName);
				
				ClassroomDAO classroomDAO = new ClassroomDAO(getContext());
				
				try {
					classroomDAO.addClassroom(new Classroom(edtClassroomName.getText().toString()));
				} catch (SQLiteConstraintException e){
					Toast.makeText(getContext(), "Une classe avec ce nom existe déjà !", Toast.LENGTH_SHORT).show();
				}
				
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
