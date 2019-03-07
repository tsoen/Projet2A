package ensicaen.fr.marierave;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

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

        classroomDAO.addClassroom(new Classroom("Ecole")); //!! DO NOT REMOVE UNTIL ARCHITECTURE CHANGE !!\\

        classroomDAO.addClassroom(new Classroom("CP"));
		classroomDAO.addClassroom(new Classroom("CE1"));

        ChildDAO childDAO = new ChildDAO(this);
		childDAO.addChild(new Child("Soen", "Timothee", "CP"));
		childDAO.addChild(new Child("Rey", "Raphael", "CE1"));
		childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Morretton", "Julie", "CP"));
        childDAO.addChild(new Child("Rey", "Raphael", "CP"));


		TeacherDAO teacherDAO = new TeacherDAO(this);
		teacherDAO.addTeacher(new Teacher("Lhote", "Loick", "lolo", "plolo"));
		teacherDAO.addTeacher(new Teacher("Simon", "Loick", "sisi", "psisi"));
		teacherDAO.addTeacher(new Teacher("admin", "admin", "admin", "admin"));

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


        try {
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = getAssets();
            //  open excel file name as myexcelsheet.xls
            myInput = assetManager.open("myexcelsheet.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno = 0;

            while (rowIter.hasNext()) {
                Log.e("myapp", " row no " + rowno);

                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowno != 0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    String sno = "", date = " ", det = "";

                    while (cellIter.hasNext()) {

                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            sno = myCell.toString();
                        } else if (colno == 1) {
                            date = myCell.toString();
                        } else if (colno == 2) {
                            det = myCell.toString();
                        }
                        colno++;
                        Log.e("myapp", " Index :" + myCell.getColumnIndex() + " -- " + myCell.toString());

                    }
                    Log.e("myapp", sno + " " + date + " " + det);
                }
                rowno++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

	@Override
	public void onBackPressed() { }
}
