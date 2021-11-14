package com.example.android_b19.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_b19.R;
import com.example.android_b19.model.Category;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        final private TextView tvTitle;
        final private ImageView ivEdit;
        final private ImageView ivDelete;

        CategoryViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCategoryTitle);
            ivEdit = itemView.findViewById(R.id.ivEditCategory);
            ivDelete = itemView.findViewById(R.id.ivDeleteCategory);
        }

    }

    private Context context;
    private LayoutInflater inflater;
    private ClickHandler clickHandler;
    private List<Category> categoryList;
    private boolean isHiddenManage = true;

    public CategoryAdapter(Context context, ClickHandler clickHandler){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clickHandler = clickHandler;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public void reverseHiddenManage(){
        isHiddenManage = !isHiddenManage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.tvTitle.setText(categoryList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedActivity.class);
                intent.putExtra(FeedActivity.CATEGORY_ID_KEY, categoryList.get(holder.getAdapterPosition()).getId().toString());
                context.startActivity(intent);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.editCategory(categoryList.get(holder.getAdapterPosition()).getId(), categoryList.get(holder.getAdapterPosition()).getName());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.deleteCategory(categoryList.get(holder.getAdapterPosition()).getId());
            }
        });

        if(isHiddenManage){
            holder.ivEdit.setVisibility(View.INVISIBLE);
            holder.ivDelete.setVisibility(View.INVISIBLE);
            holder.itemView.setEnabled(true);
        } else {
            holder.ivEdit.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }

    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    interface ClickHandler{
        void editCategory(UUID categoryId, String name);
        void deleteCategory(UUID categoryId);
    }

}
