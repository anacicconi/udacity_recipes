package com.cicconi.recipes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cicconi.recipes.R;
import com.cicconi.recipes.database.Ingredient;
import com.cicconi.recipes.database.Step;
import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private List<Ingredient> mIngredientData  = new ArrayList<>();

    private Context mContext;

    public void setIngredientData(List<Ingredient> ingredientData) {
        mIngredientData = ingredientData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapterViewHolder holder, int position) {
        String ingredient = mIngredientData.get(position).ingredient;
        double quantity = mIngredientData.get(position).quantity;
        String measure = mIngredientData.get(position).measure;

        if(!ingredient.isEmpty()) {
            holder.mIngredient.setText(ingredient);
            holder.mQuantity.setText(String.valueOf(quantity));

            if(!measure.isEmpty()) {
                holder.mMeasure.setText(measure);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mIngredientData==null?0:mIngredientData.size();
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mIngredient;
        final TextView mQuantity;
        final TextView mMeasure;

        IngredientAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mIngredient = itemView.findViewById(R.id.tv_ingredient_name);
            this.mQuantity = itemView.findViewById(R.id.tv_ingredient_quantity);
            this.mMeasure = itemView.findViewById(R.id.tv_ingredient_measure);
        }
    }
}
