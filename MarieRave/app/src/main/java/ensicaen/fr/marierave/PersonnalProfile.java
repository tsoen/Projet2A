package ensicaen.fr.marierave;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

public class PersonnalProfile extends Fragment
{
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.personnal_profile, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull final View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		
		
		Model item1, item2, item3;
		item1 = new Model("FRL1", "Lire des mots", "A");
		
		item2 = new Model("FRE1", "Recopier un texte court", "D");
		
		item3 = new Model("FRL10", "Reconnaître les pronoms personnels", "C");
		
		ListView lview = view.findViewById(R.id.listview);
		ListviewCompetenceAdapter adapter = new ListviewCompetenceAdapter(getActivity());
		
		adapter.addBigSectionHeaderItem("FRANCAIS");
		adapter.addLittleSectionHeaderItem("Lire et écrire");
		adapter.addItem(item1);
		adapter.addItem(item2);
		adapter.addItem(item3);
		adapter.addBigSectionHeaderItem("MATHS");
		adapter.addLittleSectionHeaderItem("Numération");
		adapter.addItem(new Model("MAN5", "Additionner", "B"));
		adapter.addBigSectionHeaderItem("SPORT");
		adapter.addLittleSectionHeaderItem("Courir");
		
		lview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		ArrayList<String> subjectList = new ArrayList<>();
		subjectList.add("Français");
		subjectList.add("Maths");
		subjectList.add("Histoire");
		ListviewTopicsAdapter a = new ListviewTopicsAdapter(getActivity(), subjectList);
		
		ListView v = view.findViewById(R.id.listSubjects);
		v.setAdapter(a);
		
		a.notifyDataSetChanged();
		
		lview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id)
			{
				if (parent.getAdapter().getItemViewType(position) == 0) {
					
					String sno = ((TextView) view.findViewById(R.id.sNo)).getText().toString();
					String product = ((TextView) view.findViewById(R.id.product)).getText().toString();
					String price = ((TextView) view.findViewById(R.id.price)).getText().toString();
					
					Toast.makeText(getContext(), "Code : " + sno + "\n" + "Compétence : " + product + "\n" + "Evaluation : " + price, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		Button btnResults = view.findViewById(R.id.btnBilan);
		btnResults.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.replaceFragments(PersonnalProfileResults.class, getActivity(), null, true);
			}
		});
		
		Button btnBack = view.findViewById(R.id.button2);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}
	
	public class ListviewCompetenceAdapter extends BaseAdapter
	{
		private static final int TYPE_ITEM = 0;
		private static final int TYPE_BIG_SEPARATOR = 1;
		private static final int TYPE_LITTLE_SEPARATOR = 2;
		
		private ArrayList<Object> mData = new ArrayList<>();
		private TreeSet<Integer> bigSectionHeader = new TreeSet<>();
		private TreeSet<Integer> littleSectionHeader = new TreeSet<>();
		
		private Activity activity;
		
		public ListviewCompetenceAdapter(Activity activity)
		{
			super();
			this.activity = activity;
		}
		
		public void addItem(final Model item)
		{
			mData.add(item);
			notifyDataSetChanged();
		}
		
		public void addBigSectionHeaderItem(final String item)
		{
			mData.add(item);
			bigSectionHeader.add(mData.size() - 1);
			notifyDataSetChanged();
		}
		
		public void addLittleSectionHeaderItem(final String item)
		{
			mData.add(item);
			littleSectionHeader.add(mData.size() - 1);
			notifyDataSetChanged();
		}
		
		@Override
		public int getItemViewType(int position)
		{
			if (bigSectionHeader.contains(position)) {
				return TYPE_BIG_SEPARATOR;
			}
			
			if (littleSectionHeader.contains(position)) {
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
			return mData.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return mData.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			TextView mSNo;
			TextView mProduct;
			TextView mPrice;
		}
		
		private class HeaderHolder
		{
			TextView header;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			LayoutInflater mInflater = activity.getLayoutInflater();
			int rowType = getItemViewType(position);
			
			switch (rowType) {
				case TYPE_ITEM:
					ViewHolder holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.listview_skill_row, null);
					
					holder.mSNo = convertView.findViewById(R.id.sNo);
					holder.mProduct = convertView.findViewById(R.id.product);
					holder.mPrice = convertView.findViewById(R.id.price);
					
					Model item = (Model) mData.get(position);
					holder.mSNo.setText(item.getsNo());
					holder.mProduct.setText(item.getProduct());
					holder.mPrice.setText(item.getPrice());
					
					break;
				case TYPE_BIG_SEPARATOR:
					HeaderHolder holderBigHeader = new HeaderHolder();
					convertView = mInflater.inflate(R.layout.skill_big_header, null);
					holderBigHeader.header = convertView.findViewById(R.id.textView4);
					holderBigHeader.header.setText((String) mData.get(position));
					break;
				
				case TYPE_LITTLE_SEPARATOR:
					
					
					HeaderHolder holderHeader = new HeaderHolder();
					convertView = mInflater.inflate(R.layout.skill_little_header, null);
					holderHeader.header = convertView.findViewById(R.id.textView5);
					holderHeader.header.setText((String) mData.get(position));
					convertView.setTag(holderHeader);
					break;
				
				default:
					break;
			}
			
			return convertView;
		}
	}
	
	public class ListviewTopicsAdapter extends BaseAdapter
	{
		private ArrayList<String> productList;
		private Activity activity;
		
		public ListviewTopicsAdapter(Activity activity, ArrayList<String> productList)
		{
			super();
			this.activity = activity;
			this.productList = productList;
		}
		
		@Override
		public int getCount()
		{
			return productList.size();
		}
		
		@Override
		public Object getItem(int position)
		{
			return productList.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		private class ViewHolder
		{
			TextView txtTopic;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			ViewHolder holder;
			LayoutInflater inflater = activity.getLayoutInflater();
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listview_topic_row, null);
				holder = new ViewHolder();
				holder.txtTopic = convertView.findViewById(R.id.txtTopic);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			String item = productList.get(position);
			holder.txtTopic.setText(item);
			
			return convertView;
		}
	}
}

