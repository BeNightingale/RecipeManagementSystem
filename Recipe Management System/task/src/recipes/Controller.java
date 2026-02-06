package recipes;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("/api")
@AllArgsConstructor
@RestController
@Validated
public class Controller {

    private RecipeService recipeService;
    private RegisterService registerService;

    @PostMapping(value = "/recipe/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Identifier> addRecipe(
            @Valid @RequestBody Recipe recipe,
            Authentication authentication
    ) {
        try {
            final Identifier identifier = recipeService.addRecipe(recipe, authentication.getName());
            return ResponseEntity.ok(identifier);
        } catch (NoObjectInDatabaseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/recipe/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipe(
            @PathVariable Integer id
    ) {
        try {
            final Recipe recipe = recipeService.getRecipe(id);
            return ResponseEntity.ok(recipe);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        try {
            recipeService.deleteRecipe(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            return getResponseEntityWhenError(ex);
        }
    }

    @PutMapping(value = "/recipe/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateRecipe(
            @PathVariable Integer id,
            @Valid @RequestBody Recipe recipe,
            Authentication authentication
    ) {
        try {
            recipeService.updateRecipe(id, recipe, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException ex) {
            return getResponseEntityWhenError(ex);
        }
    }

    @GetMapping("/recipe/search/")
    public ResponseEntity<List<Recipe>> getRecipies(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name
    ) {
        try {
            return ResponseEntity.ok(recipeService.getRecipes(category, name));
        } catch (ParameterValidationException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody User user) {
        if (registerService.registerUser(user.getEmail(), user.getPassword())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    private static @NonNull ResponseEntity<Void> getResponseEntityWhenError(ResponseStatusException ex) {
        final int statusCode = ex.getStatusCode().value();
        return switch (statusCode) {
            case 404 -> ResponseEntity.notFound().build();
            case 403 -> ResponseEntity.status(403).build();
            default -> ResponseEntity.internalServerError().build();
        };
    }
}