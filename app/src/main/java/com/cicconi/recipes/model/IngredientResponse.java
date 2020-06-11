package com.cicconi.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.cicconi.recipes.database.Ingredient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IngredientResponse implements Parcelable
{
    @SerializedName("quantity")
    @Expose
    public double quantity;
    @SerializedName("measure")
    @Expose
    public String measure;
    @SerializedName("ingredient")
    @Expose
    public String ingredient;

    public final static Parcelable.Creator<IngredientResponse> CREATOR = new Creator<IngredientResponse>() {
        @SuppressWarnings({ "unchecked" })
        public IngredientResponse createFromParcel(Parcel in) {
            return new IngredientResponse(in);
        }

        public IngredientResponse[] newArray(int size) {
            return (new IngredientResponse[size]);
        }
    }
        ;

    protected IngredientResponse(Parcel in) {
        this.quantity = ((double) in.readValue((int.class.getClassLoader())));
        this.measure = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredient = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public IngredientResponse() {}

    /**
     *
     * @param quantity
     * @param measure
     * @param ingredient
     */
    public IngredientResponse(double quantity, String measure, String ingredient) {
        super();
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(quantity);
        dest.writeValue(measure);
        dest.writeValue(ingredient);
    }

    public int describeContents() {
        return 0;
    }

    public Ingredient toIngredient(Long recipeId) {
        return new Ingredient(this.quantity, this.measure, this.ingredient, recipeId);
    }
}
