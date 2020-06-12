package com.cicconi.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.cicconi.recipes.database.Step;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StepResponse implements Parcelable
{
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("shortDescription")
    @Expose
    public String shortDescription;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("videoURL")
    @Expose
    public String videoURL;
    @SerializedName("thumbnailURL")
    @Expose
    public String thumbnailURL;

    public final static Parcelable.Creator<StepResponse> CREATOR = new Creator<StepResponse>() {
        @SuppressWarnings({ "unchecked" })
        public StepResponse createFromParcel(Parcel in) {
            return new StepResponse(in);
        }

        public StepResponse[] newArray(int size) {
            return (new StepResponse[size]);
        }
    };

    protected StepResponse(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.shortDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.videoURL = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailURL = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public StepResponse() {}

    /**
     *
     * @param videoURL
     * @param description
     * @param id
     * @param shortDescription
     * @param thumbnailURL
     */
    public StepResponse(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        super();
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(shortDescription);
        dest.writeValue(description);
        dest.writeValue(videoURL);
        dest.writeValue(thumbnailURL);
    }

    public int describeContents() {
        return 0;
    }

    public Step toStep(Long recipeId) {
        return new Step(this.shortDescription, this.description, this.videoURL, this.thumbnailURL, this.id, recipeId);
    }
}
