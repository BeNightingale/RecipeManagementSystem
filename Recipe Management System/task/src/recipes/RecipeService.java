package recipes;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RecipeService {

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;

    Identifier addRecipe(Recipe recipe, String userEmail) {
        final User currentUser = checkUserExistence(userEmail);
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(currentUser);
        final Recipe savedRecipe = recipeRepository.save(recipe);
        log.info("New recipe saved for user with email {}.", userEmail);
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
            log.info("Category is not empty: {}.", category);
            if (StringUtils.isBlank(category)) {
                throw new ParameterValidationException("Parameter category is blank!");
            }
            final List<Recipe> found = recipeRepository.findRecipesByCategoryIgnoreCaseOrderByDateDesc(category);
            log.info("For category {} found recipes: {}", category, found);
            return found;
        }
        log.info("Name is not empty: {}.", name);
        if (StringUtils.isBlank(name)) {
            log.info("Name ({}) expected to be not empty but is blank!", category);
            throw new ParameterValidationException("Parameter name is blank!");
        }
        final List<Recipe> found = recipeRepository.findRecipesByNameLikeIgnoreCaseOrderByDateDesc(name);
        log.info("For name {} found recipes: {}", name, found);
        return found;
    }

    Recipe getRecipe(Integer id) {
        return recipeRepository.findById(id).orElse(null);
    }

    void deleteRecipe(int id, String userEmail) {
        //checkUserExistence(userEmail);
        final Recipe recipe = findRecipeIfExists(id);
        checkIsUserRecipeAuthor(recipe, userEmail);
        log.info("Deleting recipe: {}.", recipe);
        recipeRepository.deleteById(id);
    }

    void updateRecipe(int id, Recipe recipeDataToUpdate, String userEmail) {
      //  checkUserExistence(userEmail);
        final Recipe recipe = findRecipeIfExists(id);
        checkIsUserRecipeAuthor(recipe, userEmail);
        recipe.updateRecipeEntity(recipeDataToUpdate);
        recipeRepository.save(recipe);
    }

    private Recipe findRecipeIfExists(int id) {
        return recipeRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404), "No recipe with id %d found.".formatted(id)));
    }

    private User checkUserExistence(String passedUserEmail) {
        final Optional<User> currentUser = userRepository.findUserByEmail(passedUserEmail);
        if (currentUser.isEmpty()) {
            log.error("User with email {} not found", passedUserEmail);
            throw new ResponseStatusException(
                    HttpStatusCode.valueOf(401), "User with email %s not found".formatted(passedUserEmail)
            );
        }
        return currentUser.get();
    }

    private void checkIsUserRecipeAuthor(Recipe recipeDataToUpdate, String passedUserEmail) {
        if (!Objects.equals(recipeDataToUpdate.getUser().getEmail(), passedUserEmail)) {
            log.error("Recipe author is not the current user!");
            throw new ResponseStatusException(HttpStatusCode.valueOf(403), "Recipe author is not the current user!");
        }
    }
}