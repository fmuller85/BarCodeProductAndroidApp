package fr.miage.barcodeproduct.createproduct.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import fr.miage.barcodeproduct.R;

public class AttachedFilesListAdapter extends RecyclerView.Adapter<AttachedFilesListAdapter.MyViewHolder> {
    private static OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(View view, int pos);

        void onItemLongClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.list_item_files, parent, false);
        return new MyViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String path = filesPath.get(position);
        ImageView imageView = holder.ivFile;

        Glide.with(mContext)
                .load(path)
                .apply(requestOptions)
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return (filesPath.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView ivFile;
        ImageView deleteFile;

        MyViewHolder(View itemView) {
            super(itemView);
            deleteFile = itemView.findViewById(R.id.iv_delete_file);
            ivFile = itemView.findViewById(R.id.iv_file);
            deleteFile.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mOnItemClickListener.onItemClicked(view, position);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mOnItemClickListener.onItemLongClick(view, position);
            }
            return true;
        }
    }

    private List<String> filesPath;
    private Context mContext;
    private RequestOptions requestOptions;

    public AttachedFilesListAdapter(Context context, List<String> filesPath) {
        mContext = context;
        this.filesPath = filesPath;
        this.requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.ic_file)
                .centerCrop();
    }
}

