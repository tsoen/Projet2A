package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.DAOBase;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.Views.ConnectionFragment;

public class MainActivity extends AppCompatActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.application_layout_fragment_container);
		
		new DAOBase(this).clearDatabase();
		
		ClassroomDAO classroomDAO = new ClassroomDAO(this);
		classroomDAO.addClassroom(new Classroom("CP"));
		classroomDAO.addClassroom(new Classroom("CE1"));
		
		ChildDAO childDAO = new ChildDAO(this);
		childDAO.addChild(new Child("Soen", "Timothee", "CP"));
		childDAO.addChild(new Child("Rey", "Raphael", "CE1"));
		childDAO.addChild(new Child("Morretton", "Julie", "CP"));
		
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState == null) {
				Utils.replaceFragments(ConnectionFragment.class, this, null, false);
			}
		}
	}
}
