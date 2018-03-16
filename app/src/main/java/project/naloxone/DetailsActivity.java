package project.naloxone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    HashMap<String, List<String>> categories;
    List<String> naloxone_list;
    ExpandableListView exp_list;
    CategoriesAdapter adapter;

    ArrayList<Naloxone> list;
    /*
    private String Name;
    private String Description;
    private String Category;
    private String Hours;
    private String Location;
    private String PC;
    private String Phone;
    private String Email;
    private String Website;
    private String X;
    private String Y;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.activity_details);
        list = (ArrayList<Naloxone>) getIntent().getSerializableExtra("locations");

        categories = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            List<String> free = new ArrayList<String>();
            free.add("Description: " + list.get(i).getDescription());
            free.add("Category: " + list.get(i).getCategory());
            free.add("Hours: " + list.get(i).getHours());
            free.add("Location: " + list.get(i).getLocation());
            free.add("Phone: " + list.get(i).getPhone());
            free.add("Email: " + list.get(i).getEmail());
            free.add("Website: " + list.get(i).getWebsite());
            categories.put(list.get(i).getName(), free);
        }

        //ExpandableList :: activity_details
        exp_list = findViewById(R.id.expandableList);

        naloxone_list = new ArrayList<String>(categories.keySet());

        adapter = new CategoriesAdapter(this, categories, naloxone_list);
        exp_list.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_directions:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class CategoriesAdapter extends BaseExpandableListAdapter {
        boolean animatedParent[] = new boolean[list.size()];
        boolean animatedChild[] = new boolean[list.size()];

        private Context ctx;
        private HashMap<String, List<String>> naloxone_category;
        private List<String> naloxone_list;

        public CategoriesAdapter(Context ctx, HashMap<String, List<String>> naloxone_category, List<String> naloxone_list)
        {
            this.ctx = ctx;
            this.naloxone_category = naloxone_category;
            this.naloxone_list = naloxone_list;
        }


        @Override
        public int getGroupCount() {
            return naloxone_list.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return naloxone_category.get(naloxone_list.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return naloxone_list.get(i);
        }

        @Override
        public Object getChild(int parent, int child) {
            return naloxone_category.get(naloxone_list.get(parent)).get(child);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int parent, int child) {
            return child;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {
            String group_title = (String) getGroup(parent);
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.parent_layout,parentView, false);
            }
            TextView parent_textview = convertView.findViewById(R.id.parent_txt);
            parent_textview.setTypeface(null, Typeface.BOLD);
            parent_textview.setText(group_title);
            parent_textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);

            if(animatedParent[parent] == false)
            {
                Animation animation = AnimationUtils.loadAnimation(DetailsActivity.this, R.anim.trans_push_up_in);
                convertView.startAnimation(animation);
                animatedParent[parent] = true;
            }

            return convertView;
        }

        @Override
        public View getChildView(int parent, int child, boolean lastChild, View convertView, ViewGroup parentView) {
            String child_title = (String) getChild(parent,child);
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_layout, parentView,false);
            }
            TextView child_textview = convertView.findViewById(R.id.child_text);
            child_textview.setText(child_title);
            //child_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f);
            child_textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);

//            if(animatedChild[parent] == false)
//            {
//
//                animatedChild[parent] = true;
//            }

            Animation animation = AnimationUtils.loadAnimation(DetailsActivity.this, R.anim.trans_left_in);
            convertView.startAnimation(animation);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
