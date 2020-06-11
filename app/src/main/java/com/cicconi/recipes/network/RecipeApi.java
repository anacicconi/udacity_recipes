package com.cicconi.recipes.network;

import com.cicconi.recipes.model.RecipeResponse;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;

public interface RecipeApi {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<List<RecipeResponse>> getRecipes();
}
