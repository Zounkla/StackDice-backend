package zounkla.stackdice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import zounkla.stackdice.model.Category;
import zounkla.stackdice.model.Compatibility;

import java.util.UUID;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Compatibility compatibility;

    private String description;

    public  Technology(String name, Category category, Compatibility compatibility, String description) {
        this.name = name;
        this.category = category;
        this.compatibility = compatibility;
        this.description = description;
    }
}
