package com.example.lungpanda.deliveryfast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.lungpanda.deliveryfast.R;
import com.example.lungpanda.deliveryfast.model.Store.ProductAddOn;

import java.util.List;

/**
 * Created by LungPanda on 11/7/2017.
 */

public class AddonProductAdapter extends RecyclerView.Adapter {
    public interface OnItemClickListener {
        void onRadioButtonClicked(ProductAddOn productAddOn);

        void onCheckBoxButtonCicked(ProductAddOn productAddOn);
    }

    private final static int RADIO_ROW_TYPE = 0;
    private final static int CHECKBOX_ROW_TYPE = 1;
    private List<ProductAddOn> mProductAddOnList;
    private final OnItemClickListener mListener;
    private final int mViewType;

    public AddonProductAdapter(List<ProductAddOn> mProductAddOnList, OnItemClickListener listener, int mViewType) {
        this.mProductAddOnList = mProductAddOnList;
        this.mListener = listener;
        this.mViewType = mViewType;
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewType == 1) {
            return RADIO_ROW_TYPE;
        } else {
            return CHECKBOX_ROW_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == RADIO_ROW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addon_radiobuton_list_content, parent, false);
            return new RadioViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addon_checkbox_list_content, parent, false);
            return new CheckBoxViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RadioViewHolder) {
            ((RadioViewHolder) holder).bind(mProductAddOnList.get(position), mListener);
        } else {
            ((CheckBoxViewHolder) holder).bind(mProductAddOnList.get(position), mListener);
        }
    }

    @Override
    public int getItemCount() {
        return mProductAddOnList.size();
    }

    public static class RadioViewHolder extends RecyclerView.ViewHolder {
        final TextView addonProductName, addonProductPrice;
        final RadioButton chosenAddon;

        public RadioViewHolder(View view) {
            super(view);
            addonProductName = (TextView) view.findViewById(R.id.tvAddonProductName);
            addonProductPrice = (TextView) view.findViewById(R.id.tvAddonProductPrice);
            chosenAddon = (RadioButton) view.findViewById(R.id.rbChosenAddonProduct);
        }

        public void bind(final ProductAddOn productAddOn, final OnItemClickListener listener) {
            addonProductName.setText(productAddOn.getName());
            addonProductPrice.setText(productAddOn.getPrice() + " đ");
            chosenAddon.setChecked(productAddOn.isChecked());
            chosenAddon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRadioButtonClicked(productAddOn);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRadioButtonClicked(productAddOn);
                }
            });
        }

    }

    public static class CheckBoxViewHolder extends RecyclerView.ViewHolder {
        final TextView addonProductName, addonProductPrice;
        final CheckBox chosenAddon;

        public CheckBoxViewHolder(View view) {
            super(view);
            addonProductName = (TextView) view.findViewById(R.id.tvAddonProductName);
            addonProductPrice = (TextView) view.findViewById(R.id.tvAddonProductPrice);
            chosenAddon = (CheckBox) view.findViewById(R.id.cbChosenAddonProduct);
        }

        public void bind(final ProductAddOn productAddOn, final OnItemClickListener listener) {
            addonProductName.setText(productAddOn.getName());
            addonProductPrice.setText(productAddOn.getPrice() + " đ");
            chosenAddon.setChecked(productAddOn.isChecked());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCheckBoxButtonCicked(productAddOn);
                }
            });
            chosenAddon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCheckBoxButtonCicked(productAddOn);
                }
            });
        }

    }


}
