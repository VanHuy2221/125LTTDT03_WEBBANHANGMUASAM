package com.example.bc_quanlibanhangonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.CartItem;
import com.example.bc_quanlibanhangonline.models.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    public interface OnCartChangeListener {
        void onCartChanged();
    }
    private OnCartChangeListener listener;
    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper db;

    public CartAdapter(Context context, List<CartItem> cartItems, DatabaseHelper db
                        , OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.db = db;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        Product product = db.getProductById(item.getProductId());
        if (product == null) return;

        holder.txtName.setText(product.getProductName());
        holder.txtPrice.setText(product.getFormattedPrice());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // ➕ TĂNG SỐ LƯỢNG
        holder.btnPlus.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            db.updateCartQuantity(item.getCartId(), newQty);
            item.setQuantity(newQty);
            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });

        // ➖ GIẢM SỐ LƯỢNG
        holder.btnMinus.setOnClickListener(v -> {
            int newQty = item.getQuantity() - 1;
            if (newQty <= 0) return;

            db.updateCartQuantity(item.getCartId(), newQty);
            item.setQuantity(newQty);
            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });

        // ❌ XÓA SẢN PHẨM
        holder.btnRemove.setOnClickListener(v -> {
            db.removeFromCart(item.getCartId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice, txtQuantity;
        TextView btnMinus, btnPlus, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);

            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
