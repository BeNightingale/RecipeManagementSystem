package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

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
}