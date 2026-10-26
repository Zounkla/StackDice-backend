package zounkla.stackdice.model.dto;

import java.util.UUID;

public record TechnologyOut(UUID id, String name, String description, String category, String compatibility) {
}
