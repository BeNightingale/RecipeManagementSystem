package recipes;

import com.google.gson.Gson;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RecipeService {
    private RecipeRepository recipeRepository;


//    Identifier addRecipe(String recipe) {
//        if (StringUtils.isNotBlank(recipe)) {
//            log.error("1");
//            throw new IllegalArgumentException("No recipe");
//        }
//        final Gson gson = new Gson();
//        final Recipe recipeEntity = gson.fromJson(recipe, Recipe.class);
//        if (recipeEntity == null) {
//            log.error("2");
//            throw new IllegalArgumentException("Couldn't create recipe entity");
//        }
//        if (StringUtils.isNotBlank(recipeEntity.getName()) || StringUtils.isNotBlank(recipeEntity.getDescription())) {
//            log.error("3");
//            throw new IllegalArgumentException("Invalid recipe field. Recipe: %s.".formatted(recipeEntity));
//        }
//        if (CollectionUtils.isEmpty(recipeEntity.getIngredients()) || CollectionUtils.isEmpty(recipeEntity.getDirections())) {
//            log.error("4");
//            throw new IllegalArgumentException("No full instructions!");
//        }
//        final Recipe savedRecipe = recipeRepository.save(recipeEntity);
//        return new Identifier(savedRecipe.getId());
//
//    }

    Identifier addRecipe(Recipe recipe) {
     //   final Gson gson = new Gson();
    //    final Recipe recipeEntity = gson.fromJson(recipe, Recipe.class);
        final Recipe savedRecipe = recipeRepository.save(recipe);
        return new Identifier(savedRecipe.getId());
    }

    List<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    Recipe getRecipe(Integer id) {
        return recipeRepository.findById(id).orElse(null);
    }

    boolean deleteRecipe(int id) {
        final Recipe recipe = recipeRepository.findById(id).orElse(null);
        if (recipe == null) {
            log.info("Recipe with id {} not found in database", id);
            return false;
        }
        log.info("Deleting recipe: {}.", recipe);
        recipeRepository.deleteById(id);
        return true;
    }
}