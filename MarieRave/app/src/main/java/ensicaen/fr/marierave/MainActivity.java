package ensicaen.fr.marierave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.ClassroomDAO;
import ensicaen.fr.marierave.Controllers.DAOBase;
import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Controllers.TeacherDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.Model.Teacher;
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
		classroomDAO.addClassroom(new Classroom("Ecole"));
		classroomDAO.addClassroom(new Classroom("CP"));
		classroomDAO.addClassroom(new Classroom("CE1"));
		
		ChildDAO childDAO = new ChildDAO(this);
		childDAO.addChild(new Child("Soen", "Timothee", "CP"));
		childDAO.addChild(new Child("Rey", "Raphael", "CE1"));
		childDAO.addChild(new Child("Morretton", "Julie", "CP"));
		
		TeacherDAO teacherDAO = new TeacherDAO(this);
		teacherDAO.addTeacher(new Teacher("Lhote", "Loick"));
		teacherDAO.addTeacher(new Teacher("Simon", "Loick"));
		teacherDAO.addTeacher(new Teacher("admin", "admin"));
		
		SubjectDAO subjectDAO = new SubjectDAO(this);
		subjectDAO.addSubject(new Subject("FRANCAIS"));
		subjectDAO.addSubject(new Subject("MATHS"));
		subjectDAO.addSubject(new Subject("SPORT"));
		
		SkillheaderDAO skillheaderDAO = new SkillheaderDAO(this);
		skillheaderDAO.addSkillheader(new Skillheader("Lire et écrire", "FRANCAIS"));
		skillheaderDAO.addSkillheader(new Skillheader("Numération", "MATHS"));
		skillheaderDAO.addSkillheader(new Skillheader("Courir", "SPORT"));
		
		SkillDAO skillDAO = new SkillDAO(this);
		skillDAO.addSkill(new Skill("FRL1", "Lire des mots", "Lire et écrire"));
		skillDAO.addSkill(new Skill("FRE1", "Recopier un texte court", "Lire et écrire"));
		skillDAO.addSkill(new Skill("FRL10", "Reconnaître les pronoms personnels", "Lire et écrire"));
		skillDAO.addSkill(new Skill("MAN5", "Additionner", "Numération"));
		
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState == null) {
				Utils.replaceFragments(ConnectionFragment.class, this, null, false);
			}
		}
	}
	
	@Override
	public void onBackPressed() { }
}
