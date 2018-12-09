package ensicaen.fr.marierave;

import android.app.Activity;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
	
	private ArrayList<Model> productList;
	private ArrayList<String> subjectList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Model item1, item2, item3;
		item1 = new Model("FRL1", "Lire des mots", "A");
		item2 = new Model("FRE1", "Recopier un texte court", "D");
		item3 = new Model("FRL10", "Reconnaître les pronoms personnels","C");
		
		ListView lview = findViewById(R.id.listview);
		ListviewCompetenceAdapter adapter = new ListviewCompetenceAdapter(this);
		
		adapter.addItem(item1);
		adapter.addItem(item2);
		adapter.addItem(item3);
		
		lview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		subjectList = new ArrayList<>();
		ListView v = findViewById(R.id.listSubjects);
		subjectList.add("Français");
		subjectList.add("Maths");
		subjectList.add("Histoire");
		ListviewTopicsAdapter a = new ListviewTopicsAdapter(this, subjectList);
		v.setAdapter(a);
		
		a.notifyDataSetChanged();
		
		lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				
				
				
				String sno = ((TextView)view.findViewById(R.id.sNo)).getText().toString();
				String product = ((TextView)view.findViewById(R.id.product)).getText().toString();
				String price = ((TextView)view.findViewById(R.id.price)).getText().toString();
				
				Toast.makeText(getApplicationContext(), "Code : " + sno +"\n"
						+"Compétence : " + product +"\n"
						+"Evaluation : " + price, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public class ListviewCompetenceAdapter extends BaseAdapter
	{
		private static final int TYPE_ITEM = 0;
		private static final int TYPE_SEPARATOR = 1;
		
		private ArrayList<Model> mData = new ArrayList<>();
		private TreeSet<Integer> sectionHeader = new TreeSet<>();
		private Activity activity;
		
		public ListviewCompetenceAdapter(Activity activity) {
			super();
			this.activity = activity;
		}
		
		public void addItem(final Model item) {
			mData.add(item);
			notifyDataSetChanged();
		}
		
		public void addSectionHeaderItem(final Model item) {
			mData.add(item);
			sectionHeader.add(mData.size() - 1);
			notifyDataSetChanged();
		}
		
		@Override
		public int getItemViewType(int position) {
			return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
		}
		
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public int getCount() {
			return mData.size();
		}
		
		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private class ViewHolder {
			TextView mSNo;
			TextView mProduct;
			TextView mPrice;
		}
		
		private class HeaderHolder {
			TextView header;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater mInflater = activity.getLayoutInflater();
			int rowType = getItemViewType(position);
			
			if (convertView == null) {
				switch (rowType) {
					case TYPE_ITEM:
						ViewHolder holder = new ViewHolder();
						convertView = mInflater.inflate(R.layout.listview_row, null);
						
						holder.mSNo = convertView.findViewById(R.id.sNo);
						holder.mProduct = convertView.findViewById(R.id.product);
						holder.mPrice = convertView.findViewById(R.id.price);
						
						Model item = mData.get(position);
						holder.mSNo.setText(item.getsNo());
						holder.mProduct.setText(item.getProduct());
						holder.mPrice.setText(item.getPrice());
						
						break;
					case TYPE_SEPARATOR:
						HeaderHolder holderHeader = new HeaderHolder();
						convertView = mInflater.inflate(R.layout.competence_big_header, null);
						holderHeader.header = convertView.findViewById(R.id.textView4);
						
						break;
				}
				
				
				//convertView.setTag(holder);
			} else {
				//holder = (ViewHolder) convertView.getTag();
			}
			
			
			
			return convertView;
		}
	}
	
	public class ListviewTopicsAdapter extends BaseAdapter
	{
		private ArrayList<String> productList;
		private Activity activity;
		
		public ListviewTopicsAdapter(Activity activity, ArrayList<String> productList) {
			super();
			this.activity = activity;
			this.productList = productList;
		}
		
		@Override
		public int getCount() {
			return productList.size();
		}
		
		@Override
		public Object getItem(int position) {
			return productList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		private class ViewHolder {
			TextView txtTopic;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			LayoutInflater inflater = activity.getLayoutInflater();
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listview_topic_row, null);
				holder = new ViewHolder();
				holder.txtTopic = convertView.findViewById(R.id.txtTopic);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			String item = productList.get(position);
			holder.txtTopic.setText(item);
			
			return convertView;
		}
	}
}
