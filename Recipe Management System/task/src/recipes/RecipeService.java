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

    Identifier addRecipe(Recipe recipe) {
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