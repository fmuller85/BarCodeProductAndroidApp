package fr.miage.barcodeproduct.listProduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import fr.miage.barcodeproduct.R;
import fr.miage.barcodeproduct.model.Product;
import io.realm.RealmResults;

public class ProductListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Product> productList = Collections.emptyList();

    SimpleDateFormat simpleDateFormat;

    public ProductListViewAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = productList.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_product, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvProductName.setText(product.getName());
        viewHolder.tvProductPrice.setText("Price : ".concat(String.valueOf(product.getPrice())));
        if(product.getPurchaseDate() != null){
            viewHolder.tvProductDate.setText("Purchase date : ".concat(simpleDateFormat.format(product.getPurchaseDate())));
        }else{
            viewHolder.tvProductDate.setText("No defined");
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView tvProductName;

        public TextView tvProductPrice;

        public TextView tvProductDate;

        public ViewHolder(View view) {
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvProductPrice = view.findViewById(R.id.tv_product_price);
            tvProductDate = view.findViewById(R.id.tv_product_purchase_date);
        }
    }

    public void updateList(RealmResults<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }
}
