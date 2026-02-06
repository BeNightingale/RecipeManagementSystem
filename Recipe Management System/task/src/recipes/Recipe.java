package recipes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Recipe {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "category", nullable = false)
    private String category;

    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "adding_recipe")
    private LocalDateTime date;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @NonNull
    @NotEmpty
    @ElementCollection
    @Column(name = "ingredients", nullable = false)
    private List<String> ingredients = new ArrayList<>();

    @NonNull
    @NotEmpty
    @ElementCollection
    @Column(name = "directions", nullable = false)
    private List<String> directions = new ArrayList<>();

    @Setter
    @JsonIgnore
    @ManyToOne()
    private User user;


    void updateRecipeEntity(Recipe recipeDataToUpdate) {
        this.name = recipeDataToUpdate.name;
        this.category = recipeDataToUpdate.category;
        this.date = LocalDateTime.now();
        this.description = recipeDataToUpdate.description;
        this.ingredients = new ArrayList<>(recipeDataToUpdate.ingredients);
        this.directions = new ArrayList<>(recipeDataToUpdate.directions);
    }
}