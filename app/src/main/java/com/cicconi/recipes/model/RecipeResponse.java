package com.cicconi.recipes.model;

import com.cicconi.recipes.database.Recipe;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponse implements Parcelable
{
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("ingredients")
    @Expose
    public List<IngredientResponse> ingredients = null;
    @SerializedName("steps")
    @Expose
    public List<StepResponse> steps = null;
    @SerializedName("servings")
    @Expose
    public int servings;
    @SerializedName("image")
    @Expose
    public String image;

    public final static Parcelable.Creator<RecipeResponse> CREATOR = new Creator<RecipeResponse>() {
        @SuppressWarnings({ "unchecked" })
        public RecipeResponse createFromParcel(Parcel in) {
            return new RecipeResponse(in);
        }

        public RecipeResponse[] newArray(int size) {
            return (new RecipeResponse[size]);
        }
    };

    protected RecipeResponse(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.ingredients, (IngredientResponse.class.getClassLoader()));
        in.readList(this.steps, (StepResponse.class.getClassLoader()));
        this.servings = ((int) in.readValue((int.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Recipe toRecipe() {
        return new Recipe(this.id, this.name, this.servings, this.image);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<IngredientResponse> getIngredients() {
        return ingredients;
    }

    public List<StepResponse> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public RecipeResponse() {}

    /**
     *
     * @param image
     * @param servings
     * @param name
     * @param ingredients
     * @param id
     * @param steps
     */
    public RecipeResponse(int id, String name, List<IngredientResponse> ingredients, List<StepResponse> steps, int servings, String image) {
        super();
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeList(ingredients);
        dest.writeList(steps);
        dest.writeValue(servings);
        dest.writeValue(image);
    }

    public int describeContents() {
        return 0;
    }
}
