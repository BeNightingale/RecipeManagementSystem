package recipes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    @Query("""
                SELECT r FROM Recipe r
                WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))
                ORDER BY r.date DESC
            """
    )
    List<Recipe> findRecipesByNameLikeIgnoreCaseOrderByDateDesc(String name);

    @Query("""
                SELECT r FROM Recipe r
                WHERE UPPER(r.category) = UPPER(:category)
                ORDER BY r.date DESC
            """
    )
    List<Recipe> findRecipesByCategoryIgnoreCaseOrderByDateDesc(@Param("category") String category);
}