package zounkla.stackdice.mapper;

import org.mapstruct.Mapper;
import zounkla.stackdice.model.dto.TechnologyIn;
import zounkla.stackdice.model.dto.TechnologyOut;
import zounkla.stackdice.model.entity.Technology;

@Mapper(componentModel = "spring")
public interface TechnologyMapper {
    TechnologyOut technologyToTechnologyOut(Technology technology);
    Technology technologyInToTechnology(TechnologyIn technologyIn);
}
