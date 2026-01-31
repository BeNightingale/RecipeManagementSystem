package recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    List<Recipe> findRecipesByNameLikeIgnoreCaseOrderByDateDesc(String name);

    List<Recipe> findRecipesByCategoryIgnoreCaseOrderByDateDesc(String category);
}