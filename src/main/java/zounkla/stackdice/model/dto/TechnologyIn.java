package zounkla.stackdice.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import zounkla.stackdice.model.Category;
import zounkla.stackdice.model.Compatibility;

@AllArgsConstructor
@Getter @Setter
public class TechnologyIn {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @NotNull(message = "La catégorie est obligatoire")
    private Category category;

    @NotNull(message = "La compatibilité est obligatoire")
    private Compatibility compatibility;

    private String description;
}
