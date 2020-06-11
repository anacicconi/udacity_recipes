package com.cicconi.recipes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cicconi.recipes.R;
import com.cicconi.recipes.database.Recipe;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private RecipeClickListener mClickListener;

    private List<Recipe> mRecipeData  = new ArrayList<>();

    private Context mContext;

    public RecipeAdapter(RecipeClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setRecipeData(List<Recipe> recipeData) {
        mRecipeData = recipeData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        String name = mRecipeData.get(position).name;
        int servings = mRecipeData.get(position).servings;

        if(!name.isEmpty()) {
            holder.mName.setText(name);
        } else {
            holder.mName.setText(R.string.unknown);
        }

        holder.mServings.setText(String.valueOf(servings));
    }

    @Override
    public int getItemCount() {
        return mRecipeData.size();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mName;
        final TextView mServings;

        RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mName = itemView.findViewById(R.id.tv_name);
            this.mServings = itemView.findViewById(R.id.tv_servings_value);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickListener.onRecipeItemClick(mRecipeData.get(clickedPosition));
        }
    }

    public interface RecipeClickListener {
        void onRecipeItemClick(Recipe recipe);
    }
}
