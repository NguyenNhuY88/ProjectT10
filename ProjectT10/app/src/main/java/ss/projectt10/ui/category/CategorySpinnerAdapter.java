package ss.projectt10.ui.category;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ss.projectt10.model.Category;

public class CategorySpinnerAdapter extends ArrayAdapter<Category> {
    private Context context;
    private ArrayList<Category> listCategory;

    public CategorySpinnerAdapter(@NonNull Context context, int resource, ArrayList<Category> listCategory) {
        super(context, resource, listCategory);
        this.context = context;
        this.listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Nullable
    @Override
    public Category getItem(int position) {
        return listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(listCategory.get(position).getNameCard());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(listCategory.get(position).getNameCard());
        return label;
    }
}
