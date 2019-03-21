package ensicaen.fr.marierave.Views;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

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

public class PersonnalProfile extends Fragment
{
	private Integer _childId;
	
	private ListView skillsListview;
	
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
		
		TextView txtName = view.findViewById(R.id.txtName);
		txtName.setText(child.getName());
		
		TextView txtSurname = view.findViewById(R.id.txtSurname);
		txtSurname.setText(child.getFirstname());
		
		skillsListview = view.findViewById(R.id.listSkills);
		reloadSkillListView(null);
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
				reloadSkillListView((Subject) parent.getAdapter().getItem(position));
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
				generatePDFResults();
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
		}
		
		for (Subject subject : subjectList) {
			skillsAdapter.addBigSectionHeaderItem(subject.getName());
			
			for (Skillheader skillheader : new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName())) {
				skillsAdapter.addLittleSectionHeaderItem(skillheader.getName());
				
				for (Skill skill : new SkillDAO(getContext()).getSkillsInHeader(skillheader.getName())) {
					skillsAdapter.addItem(skill);
				}
			}
		}
		
		skillsListview.setAdapter(skillsAdapter);
		skillsAdapter.notifyDataSetChanged();
	}
	
	public void generatePDFResults()
	{
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
			return;
		}
		
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Log.d("myapp", "ko");
		}

		File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "myPdfFile.pdf");
		
		if (pdfFile.exists()) {
			pdfFile.delete();
			pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), "myPdfFile.pdf");
		}
		
		try {
			Document document = new Document();
			
			PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			document.open();
			
			List<Bitmap> screenList = generateBitmapsFromView(getActivity().getWindow().findViewById(R.id.PersonnalProfileViewLayout));
			
			for (int i = 0; i < screenList.size(); i++) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				screenList.get(i).compress(Bitmap.CompressFormat.PNG, 60, stream);
				byte[] byteArray = stream.toByteArray();
				document.newPage();
				addImage(document, byteArray);
			}
			
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(pdfFile);
		intent.setDataAndType(uri, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent pdfViewer = Intent.createChooser(intent, "Open File");
        try {
            startActivity(pdfViewer);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(getContext(), "Aucune application permettant d'afficher un PDF n'a été détectée." +
                    " Le fichier a été placé dans votre dossier de téléchargements", Toast.LENGTH_SHORT).show();
        }
	}
	
	public List<Bitmap> generateBitmapsFromView(View view)
	{
		view.setDrawingCacheEnabled(true);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int verticalHght = size.x;
		
		ListAdapter adapter = skillsListview.getAdapter();
		int itemscount = adapter.getCount();
		int allitemsheight = 0;
		int tx = (int) skillsListview.getX();
		int ty = (int) skillsListview.getY();
		
		for (int i = 0; i < itemscount; i++) {
			
			View childView = adapter.getView(i, null, skillsListview);
			childView.measure(View.MeasureSpec.makeMeasureSpec(skillsListview.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec
					.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			
			childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
			childView.setDrawingCacheEnabled(true);
			childView.buildDrawingCache();
			allitemsheight += childView.getMeasuredHeight();
		}
		
		skillsListview.measure(View.MeasureSpec.makeMeasureSpec(this.skillsListview.getWidth() + 500/*horizontalWdth*/, View.MeasureSpec.EXACTLY), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		
		Bitmap bm1 = view.getDrawingCache();
		Bitmap bm2 = Bitmap.createBitmap(skillsListview.getMeasuredWidth(), allitemsheight + ty, Bitmap.Config.ARGB_8888);
		
		Bitmap bm = overlay(bm2, bm1);
		
		Canvas canvas = new Canvas(bm);
		
		canvas.translate(tx, ty);
		
		for (int i = 0; i < adapter.getCount(); i++) {
			View childView = adapter.getView(i, null, this.skillsListview);
			
			childView.measure(View.MeasureSpec.makeMeasureSpec(this.skillsListview.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec
					.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			
			childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
			childView.setDrawingCacheEnabled(true);
			childView.buildDrawingCache();
			childView.getDrawingCache();
			childView.draw(canvas);
			canvas.translate(0, childView.getMeasuredHeight());
		}
		
		return splitBitmaps(bm, (int) Math.ceil((double) bm.getHeight() / verticalHght));
	}
	
	public List<Bitmap> splitBitmaps(Bitmap originalBm, int nbBitmaps)
	{
		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		int verticalHght = size.x;
		
		List<Bitmap> list = new ArrayList<>();
		
		int offsetStart = 0;
		int height;
		for (int i = 0; i < nbBitmaps; i++) {
			
			height = Math.min(originalBm.getHeight() - offsetStart, verticalHght);
			
			Bitmap bm1 = Bitmap.createBitmap(originalBm, 0, offsetStart, originalBm.getWidth(), height);
			
			offsetStart += height;
			list.add(bm1);
		}
		
		return list;
	}
	
	public Bitmap overlay(Bitmap bmp1, Bitmap bmp2)
	{
		Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(bmp1, new Matrix(), null);
		canvas.drawBitmap(bmp2, 0, 0, null);
		return bmOverlay;
	}
	
	private void addImage(Document document, byte[] byteArray)
	{
		try {
            Image image = Image.getInstance(byteArray);
            image.scalePercent(25f);
			document.add(image);

        } catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class ListviewSkillAdapter extends BaseAdapter
	{
		private static final int TYPE_ITEM = 0;
		private static final int TYPE_BIG_SEPARATOR = 1;
		private static final int TYPE_LITTLE_SEPARATOR = 2;
		
		private ArrayList<Object> _skillsAndHeaders = new ArrayList<>();
		private TreeSet<Integer> _bigHeaders = new TreeSet<>();
		private TreeSet<Integer> _littleHeaders = new TreeSet<>();
		
		private FragmentActivity _activity;
		private PersonnalProfile _fragment;
		
		ListviewSkillAdapter(PersonnalProfile fragment)
		{
			super();
			_fragment = fragment;
			_activity = fragment.getActivity();
		}
		
		void addItem(final Skill item)
		{
			_skillsAndHeaders.add(item);
			notifyDataSetChanged();
		}
		
		void addBigSectionHeaderItem(final String item)
		{
			_skillsAndHeaders.add(item);
			_bigHeaders.add(_skillsAndHeaders.size() - 1);
			notifyDataSetChanged();
		}
		
		void addLittleSectionHeaderItem(final String item)
		{
			_skillsAndHeaders.add(item);
			_littleHeaders.add(_skillsAndHeaders.size() - 1);
			notifyDataSetChanged();
		}
		
		@Override
		public int getItemViewType(int position)
		{
			if (_bigHeaders.contains(position)) {
				return TYPE_BIG_SEPARATOR;
			}
			
			if (_littleHeaders.contains(position)) {
				return TYPE_LITTLE_SEPARATOR;
			}
			
			return TYPE_ITEM;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 3;
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
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater mInflater = _activity.getLayoutInflater();
			
			switch (getItemViewType(position)) {
				case TYPE_ITEM:
					ViewHolder holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.listview_skill_item, null);
					
					holder._code = convertView.findViewById(R.id.txtCode);
					holder._result = convertView.findViewById(R.id.txtResult);
					holder._name = convertView.findViewById(R.id.txtSkill);
					holder._btnEdit = convertView.findViewById(R.id.btnEdit);
					
					final Skill item = (Skill) _skillsAndHeaders.get(position);
					holder._code.setText(item.getCode());
					holder._name.setText(item.getName());
                    switch (new SkillMarkDAO(getContext()).getSkillMark(_childId, item.getCode())) {
                        case "A":
                            holder._result.setBackgroundColor(Color.parseColor("#088A08"));
                            break;

                        case "B":
                            holder._result.setBackgroundColor(Color.parseColor("#00FF40"));
                            break;

                        case "C":
                            holder._result.setBackgroundColor(Color.parseColor("#FFD500"));
                            break;

                        case "D":
                            holder._result.setBackgroundColor(Color.parseColor("#FF0000"));
                            break;

                        default:
                            break;
                    }

					holder._btnEdit.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							Bundle bundle = new Bundle();
							bundle.putInt("ChildId", _childId);
							bundle.putString("Skill", item.getCode());
							
							EditEvaluationAndCommentDialog dialog = new EditEvaluationAndCommentDialog();
							dialog.setArguments(bundle);
							dialog.setTargetFragment(_fragment, 0);
							dialog.show(_activity.getSupportFragmentManager(), "editEvaluation");
						}
					});
					break;
					
				case TYPE_BIG_SEPARATOR:
					HeaderHolder holderBigHeader = new HeaderHolder();
					convertView = mInflater.inflate(R.layout.listview_skill_subject_item, null);
					holderBigHeader._header = convertView.findViewById(R.id.txtBigHeader);
					holderBigHeader._header.setText((String) _skillsAndHeaders.get(position));
					break;
				
				case TYPE_LITTLE_SEPARATOR:
					HeaderHolder holderHeader = new HeaderHolder();
					convertView = mInflater.inflate(R.layout.listview_skill_skillheader_item, null);
					holderHeader._header = convertView.findViewById(R.id.txtLittleHeader);
					holderHeader._header.setText((String) _skillsAndHeaders.get(position));
					convertView.setTag(holderHeader);
					break;
				
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
			if (convertView == null) {
				ViewHolder holder = new ViewHolder();
				convertView =  _activity.getLayoutInflater().inflate(R.layout.listview_topic_item, null);
				holder._txtTopic = convertView.findViewById(R.id.txtTopic);
				holder._txtTopic.setText(_topicList.get(position).getName());
			}
			
			return convertView;
		}
	}
}

