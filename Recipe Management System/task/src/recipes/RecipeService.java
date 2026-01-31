package recipes;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RecipeService {

    private RecipeRepository recipeRepository;

    Identifier addRecipe(Recipe recipe) {
        recipe.setDate(LocalDateTime.now());
        final Recipe savedRecipe = recipeRepository.save(recipe);
        return new Identifier(savedRecipe.getId());
    }

    List<Recipe> getRecipes(String category, String name) {
        boolean isCategoryEmpty = StringUtils.isEmpty(category);
        boolean isNameEmpty = StringUtils.isEmpty(name);
        if (
                (isCategoryEmpty && isNameEmpty) || (!isCategoryEmpty && !isNameEmpty)
        ) {
            throw new ParameterValidationException(
                    "Not valid parameter count: category = %s, name = %s.".formatted(category, name)
            );
        }
        if (!isCategoryEmpty) {
            log.info("kategoria niepusta! {}", category);
            if (StringUtils.isBlank(category)) {
                throw new ParameterValidationException("Parameter category is blank!");
            }
            List<Recipe> found = recipeRepository.findRecipesByCategoryIgnoreCaseOrderByDateDesc(category);
            log.info("Znaleziono dla category {} przepisy: {}", category, found);
            return recipeRepository.findRecipesByCategoryIgnoreCaseOrderByDateDesc(category);
        }
        if (StringUtils.isBlank(name)) {
            throw new ParameterValidationException("Parameter name is blank!");
        }
        return recipeRepository.findRecipesByNameLikeIgnoreCaseOrderByDateDesc(name);
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

    void updateRecipe(int id, Recipe recipeDataToUpdate) {
        final Recipe recipe = recipeRepository
                .findById(id)
                .orElseThrow(() -> new NoObjectInDatabaseException("No recipe with id %d found.".formatted(id)));
        recipe.updateRecipeEntity(recipeDataToUpdate);
        recipeRepository.save(recipe);
    }
}