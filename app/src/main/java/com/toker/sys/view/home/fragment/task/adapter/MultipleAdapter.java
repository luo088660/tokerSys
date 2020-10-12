/*
package com.toker.sys.view.home.fragment.task.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.toker.sys.R;

public class MultipleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MultipleAdapter";
    private Context mContext;
    private LayoutInflater mInflater;
    private TopCategoryBean mCategoryBean;

    public MultipleAdapter(Context context, TopCategoryBean categoryBean) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mCategoryBean = categoryBean;
    }

    public void setCategoryBean(TopCategoryBean categoryBean) {
        mCategoryBean = categoryBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_multiple_normal, parent, false);
        return new NormalItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NormalItemViewHolder viewHolder = (NormalItemViewHolder) holder;
        RecyclerView rvCategory = viewHolder.mRvCategory;
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(mContext, 3));
        List<TopCategoryBean.MaleBean> male = mCategoryBean.getMale();
        List<TopCategoryBean.MaleBean> female = mCategoryBean.getFemale();
        CategoryAdapter adapter = new CategoryAdapter(male);
        if (position == 0) {
            viewHolder.mTextView.setText("男生");
            adapter.setCategoryBeans(male);
        } else if (position == 1) {
            viewHolder.mTextView.setText("女生");
            adapter.setCategoryBeans(female);
        } else if (position == 2) {
            viewHolder.mTextView.setText("耽美");
            adapter.setCategoryBeans(mCategoryBean.getPicture());
        } else if (position == 3) {
            viewHolder.mTextView.setText("出版");
            adapter.setCategoryBeans(mCategoryBean.getPress());
        }
        rvCategory.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<TopCategoryBean.MaleBean> mCategoryBeans;

        public CategoryAdapter(List<TopCategoryBean.MaleBean> maleBeans) {
            mCategoryBeans = maleBeans;
        }

        public void setCategoryBeans(List<TopCategoryBean.MaleBean> categoryBeans) {
            mCategoryBeans = categoryBeans;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_category, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            final TopCategoryBean.MaleBean maleBean = mCategoryBeans.get(position);
            viewHolder.mTvBookCount.setText(maleBean.getBookCount() + "本");
            viewHolder.mTvCategoryName.setText(maleBean.getName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mCategoryBeans.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView mTvCategoryName;
            TextView mTvBookCount;
            CardView llItem;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mTvCategoryName = itemView.findViewById(R.id.tvCategoryName);
                mTvBookCount = itemView.findViewById(R.id.tvBookCount);
                llItem = itemView.findViewById(R.id.llItem);
            }
        }
    }

    private class NormalItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        RecyclerView mRvCategory;

        public NormalItemViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tvName);
            mRvCategory = itemView.findViewById(R.id.rvCategory);
        }
    }


}*/
