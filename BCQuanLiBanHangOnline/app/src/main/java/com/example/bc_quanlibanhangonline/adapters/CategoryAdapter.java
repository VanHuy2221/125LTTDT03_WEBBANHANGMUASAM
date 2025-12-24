package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCategoryIcon;
        private TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCategoryClick(categoryList.get(position));
                    }
                }
            });
        }

        public void bind(Category category) {
            tvCategoryName.setText(category.getCategoryName());
            ivCategoryIcon.setImageResource(category.getIconResource());
            
            // Set màu icon dựa trên category
            int tintColor = getCategoryTintColor(category.getCategoryId());
            ivCategoryIcon.setColorFilter(tintColor);
        }

        private int getCategoryTintColor(int categoryId) {
            switch (categoryId) {
                case 1: return context.getResources().getColor(R.color.blue_500);
                case 2: return context.getResources().getColor(R.color.green_500);
                case 3: return context.getResources().getColor(R.color.orange_500);
                case 4: return context.getResources().getColor(R.color.purple_500);
                default: return context.getResources().getColor(R.color.blue_500);
            }
        }
    }
}