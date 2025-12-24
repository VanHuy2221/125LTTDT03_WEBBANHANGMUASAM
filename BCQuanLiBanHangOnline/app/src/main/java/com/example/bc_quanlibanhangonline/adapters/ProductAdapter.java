package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList != null ? productList : new ArrayList<>();
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Product> newProductList) {
        this.productList = newProductList != null ? newProductList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvBrand;
        private TextView tvPrice;
        private RatingBar rbProductRating;
        private TextView tvRating;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvBrand = itemView.findViewById(R.id.tvBrand);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            rbProductRating = itemView.findViewById(R.id.rb_product_rating);
            tvRating = itemView.findViewById(R.id.tvRating);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onProductClick(productList.get(position));
                    }
                }
            });
        }

        public void bind(Product product) {
            if (product == null) return;
            
            tvProductName.setText(product.getProductName() != null ? product.getProductName() : "");
            tvBrand.setText(product.getBrand() != null ? product.getBrand() : "");
            tvPrice.setText(product.getFormattedPrice() != null ? product.getFormattedPrice() : "");
            rbProductRating.setRating(product.getRating());
            tvRating.setText(String.valueOf(product.getRating()));
            ivProductImage.setImageResource(product.getImageResource());
        }
    }
}