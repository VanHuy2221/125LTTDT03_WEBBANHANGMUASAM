package com.example.bc_quanlibanhangonline.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bc_quanlibanhangonline.CreProActivity;
import com.example.bc_quanlibanhangonline.R;
import com.example.bc_quanlibanhangonline.database.DatabaseHelper;
import com.example.bc_quanlibanhangonline.models.Product;

import java.util.List;

public class ProductManagementAdapter extends RecyclerView.Adapter<ProductManagementAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private DatabaseHelper db;

    public ProductManagementAdapter(Context context, List<Product> productList, DatabaseHelper db) {
        this.context = context;
        this.productList = productList;
        this.db = db;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_management, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Bind dữ liệu cơ bản
        holder.txtProductName.setText(product.getProductName());

        // Giá tiền - sử dụng getFormattedPrice() từ model
        holder.txtProductPrice.setText(product.getFormattedPrice());

        // Hình ảnh
        if (product.getImageResource() != 0) {
            holder.imgProduct.setImageResource(product.getImageResource());
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_image);
        }

        // Thương hiệu
        if (holder.txtProductBrand != null) {
            String brand = product.getBrand();
            if (brand != null && !brand.isEmpty() && !brand.equals("null")) {
                holder.txtProductBrand.setText(brand);
            } else {
                holder.txtProductBrand.setText("Không có thương hiệu");
                holder.txtProductBrand.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        }

        // Số lượng - QUAN TRỌNG: Chuyển int thành String
        if (holder.txtProductQuantity != null) {
            int quantity = product.getQuantity();
            holder.txtProductQuantity.setText("Còn lại: " + quantity);

            // Đổi màu nếu số lượng thấp
            if (quantity <= 5) {
                holder.txtProductQuantity.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else if (quantity <= 10) {
                holder.txtProductQuantity.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                holder.txtProductQuantity.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        }

        // Trạng thái - Cần thêm phương thức getCategory() hoặc xử lý categoryId
        if (holder.txtProductStatus != null) {
            String status = product.getStatus();
            if (status != null && !status.isEmpty() && !status.equals("null")) {
                holder.txtProductStatus.setText(status);

                // Set màu sắc theo trạng thái
                switch (status) {
                    case "Đang bán":
                        holder.txtProductStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                        // Đổi background cho view tròn
                        View statusDot = holder.itemView.findViewById(R.id.statusDot);
                        if (statusDot != null) {
                            statusDot.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
                        }
                        break;
                    case "Ngừng bán":
                        holder.txtProductStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                        break;
                    case "Hết hàng":
                        holder.txtProductStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                        break;
                    case "active": // Nếu status là "active" (từ database)
                        holder.txtProductStatus.setText("Đang bán");
                        holder.txtProductStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                        break;
                    default:
                        holder.txtProductStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                }
            } else {
                holder.txtProductStatus.setText("Không xác định");
                holder.txtProductStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        }

        // Danh mục - Cần thêm phương thức getCategoryName() trong Product
        if (holder.txtProductCategory != null) {
            // Tạm thời hiển thị categoryId, bạn có thể thêm phương thức getCategoryName()
            int categoryId = product.getCategoryId();
            String categoryName = getCategoryNameById(categoryId);
            holder.txtProductCategory.setText(categoryName);
        }

        // Sửa sản phẩm
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, CreProActivity.class);
            intent.putExtra("product_id", product.getProductId());
            context.startActivity(intent);
        });

        // Xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa sản phẩm này không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        int res = db.deleteProduct(product.getProductId());
                        if (res != -1) {
                            productList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, productList.size());
                            Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnEdit, btnDelete;
        TextView txtProductName, txtProductPrice, txtProductBrand,
                txtProductQuantity, txtProductStatus, txtProductCategory;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ tất cả các view
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);

            // Các TextView mới từ layout
            txtProductBrand = itemView.findViewById(R.id.txtProductBrand);
            txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
            txtProductStatus = itemView.findViewById(R.id.txtProductStatus);
            txtProductCategory = itemView.findViewById(R.id.txtProductCategory);

            // Các nút
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }

    public void updateData(List<Product> newList){
        this.productList = newList;
        notifyDataSetChanged();
    }

    // Phương thức helper để lấy tên danh mục từ ID
    private String getCategoryNameById(int categoryId) {
        switch (categoryId) {
            case 1: return "Điện thoại";
            case 2: return "Laptop";
            case 3: return "Tablet";
            case 4: return "Phụ kiện điện tử";
            case 5: return "Thời trang nam";
            case 6: return "Thời trang nữ";
            case 7: return "Giày dép";
            case 8: return "Túi xách";
            case 9: return "Đồng hồ";
            case 10: return "Mỹ phẩm";
            case 11: return "Nội thất";
            case 12: return "Đồ gia dụng";
            case 13: return "Thiết bị nhà bếp";
            case 14: return "Sách";
            case 15: return "Thể thao";
            case 16: return "Đồ chơi";
            case 17: return "Mẹ và bé";
            case 18: return "Ô tô - Xe máy";
            case 19: return "Khác";
            default: return "Không xác định";
        }
    }

    // Phương thức để refresh khi có thay đổi
    public void refreshData(List<Product> newProducts) {
        this.productList = newProducts;
        notifyDataSetChanged();
    }
}