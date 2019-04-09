package ensicaen.fr.marierave.Views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ensicaen.fr.marierave.Controllers.ChildDAO;
import ensicaen.fr.marierave.Controllers.SkillCommentDAO;
import ensicaen.fr.marierave.Controllers.SkillDAO;
import ensicaen.fr.marierave.Controllers.SkillMarkDAO;
import ensicaen.fr.marierave.Controllers.SkillheaderDAO;
import ensicaen.fr.marierave.Controllers.SubjectDAO;
import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Skill;
import ensicaen.fr.marierave.Model.Skillheader;
import ensicaen.fr.marierave.Model.Subject;
import ensicaen.fr.marierave.R;

public class PrintPDF extends Fragment
{
	private PrintPDF _frag = this;
	private BackgroundTask _task;
	private Integer _childId;
	private ListView skillsListview;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.print_pdf, container, false);
		
		final ViewGroup layout = view.findViewById(R.id.PrintPDFViewLayout);
		ViewTreeObserver vto = layout.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{
			@Override
			public void onGlobalLayout()
			{
				layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				
				_task = new BackgroundTask(_frag);
				_task.execute();
			}
		});
		
		return view;
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		_childId = getArguments().getInt("childId");
		
		Child child = new ChildDAO(getContext()).getChild(_childId);
		
		TextView txtName = view.findViewById(R.id.textView43);
		txtName.setText(child.getFirstname() + " " + child.getName());
		
		TextView txtSurname = view.findViewById(R.id.textView45);
		txtSurname.setText(child.getClassroom());
		
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
	}
	
	private static class BackgroundTask extends AsyncTask<Void, Integer, Void>
	{
		private ProgressDialog dialog;
		
		PrintPDF _pdf;
		
		public BackgroundTask(PrintPDF pdf)
		{
			_pdf = pdf;
			dialog = new ProgressDialog(pdf.getContext());
		}
		
		public void setProgress(Integer value)
		{
			publishProgress(value);
		}
		
		@Override
		protected void onPreExecute()
		{
			dialog.setMessage("Génération du PDF en cours... 0%");
			dialog.show();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			dialog.setMessage("Génération du PDF en cours... " + values[0] + "%");
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			_pdf.getActivity().getSupportFragmentManager().popBackStack();
		}
		
		@Override
		protected Void doInBackground(Void... params)
		{
			_pdf.generatePDFResults();
			
			return null;
		}
	}
	
	public void reloadSkillListView()
	{
		ListviewSkillAdapter skillsAdapter = new ListviewSkillAdapter(this);
		List<Subject> subjectList = new SubjectDAO(getContext()).getAllSubjects();
		Collections.sort(subjectList, new Comparator<Subject>()
		{
			@Override
			public int compare(Subject o1, Subject o2)
			{
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		
		for (Subject subject : subjectList) {
			skillsAdapter.addItem(subject);
			
			List<Skillheader> skillheaderList = new SkillheaderDAO(getContext()).getSkillheadersInSubject(subject.getName());
			Collections.sort(skillheaderList, new Comparator<Skillheader>()
			{
				@Override
				public int compare(Skillheader o1, Skillheader o2)
				{
					String o1StringPart = o1.getName().replaceAll("\\d", "");
					String o2StringPart = o2.getName().replaceAll("\\d", "");
					
					if (o1StringPart.equalsIgnoreCase(o2StringPart)) {
						return extractInt(o1.getName()) - extractInt(o2.getName());
					}
					
					return o1.getName().compareTo(o2.getName());
				}
				
				int extractInt(String s)
				{
					String num = s.replaceAll("\\D", "");
					return num.isEmpty() ? 0 : Integer.parseInt(num);
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
	
	public void generatePDFResults()
	{
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
			
			List<Bitmap> screenList = generateBitmapsFromView(getActivity().getWindow().findViewById(R.id.PrintPDFViewLayout));
			
			for (int i = 0; i < screenList.size(); i++) {
				_task.setProgress((i + 1) * 100 / (screenList.size()));
				
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
			
			Toast.makeText(getContext(), "Aucune application permettant d'afficher un PDF n'a été détectée." + " Le fichier a été placé dans votre dossier de " +
					"téléchargements", Toast.LENGTH_SHORT)
					.show();
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
		
		verticalHght += verticalHght * 0.5;
		
		return splitBitmaps(bm, (int) Math.ceil((double) bm.getHeight() / verticalHght));
	}
	
	public List<Bitmap> splitBitmaps(Bitmap originalBm, int nbBitmaps)
	{
		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		int verticalHght = size.x;
		
		verticalHght += verticalHght * 0.5;
		
		List<Bitmap> list = new ArrayList<>();
		
		int offsetStart = 0;
		int height;
		for (int i = 0; i < nbBitmaps; i++) {
			
			height = Math.min(originalBm.getHeight() - offsetStart, verticalHght);
			
			Bitmap bm1 = Bitmap.createBitmap(originalBm, 0, offsetStart, originalBm.getWidth(), height);
			
			offsetStart += height - 30;
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
		private ArrayList<Object> _skillsAndHeaders = new ArrayList<>();
		
		private PrintPDF _fragment;
		
		ListviewSkillAdapter(PrintPDF fragment)
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
			private TextView _comment;
		}
		
		private class HeaderHolder
		{
			private TextView _header;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			int viewType = getItemViewType(position);
			
			switch (viewType) {
				case 0:
					ViewHolder holder1;
					
					View tempView1 = convertView;
					if (tempView1 == null) {
						tempView1 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_pdf_item, null);
						
						holder1 = new ViewHolder();
						holder1._code = tempView1.findViewById(R.id.txtCode);
						holder1._result = tempView1.findViewById(R.id.txtResult);
						holder1._name = tempView1.findViewById(R.id.txtSkill);
						holder1._comment = tempView1.findViewById(R.id.textView46);
						
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
					
					holder1._comment.setText(new SkillCommentDAO(getContext()).getSkillcomment(_childId, item.getCode()));
					
					return tempView1;
				
				case 1:
					HeaderHolder holder2;
					
					View tempView2 = convertView;
					if (tempView2 == null) {
						tempView2 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_subject_pdf_item, null);
						
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
						tempView3 = _fragment.getLayoutInflater().inflate(R.layout.listview_skill_skillheader_pdf_item, null);
						
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
}
