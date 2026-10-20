package zounkla.stackdice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.repository.TechnologyRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologyServiceTests {

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private TechnologyService technologyService;

    private Technology technology;

    @BeforeEach
    public void setup() {
        technology = new Technology();
        technology.setId(UUID.randomUUID());
        technology.setName("technology");
        technology.setDescription("technology description");
    }


    @Test
    public void getTechnologyShouldReturnExceptionWhenNotFound() {
        //GIVEN
        UUID uuid = UUID.randomUUID();
        when(this.technologyRepository.findById(uuid)).thenReturn(Optional.empty());

        //WHEN THEN
        assertThrows(TechnologyNotFoundException.class, () -> technologyService.getTechnology(uuid));
    }

    @Test
    public void getTechnologyShouldReturnTechnologyWhenFound() throws TechnologyNotFoundException {
        //GIVEN
        UUID uuid = UUID.randomUUID();
        when(this.technologyRepository.findById(uuid)).thenReturn(Optional.ofNullable(technology));

        //WHEN
        Technology technologyFound = technologyService.getTechnology(uuid);

        //THEN
        assertNotNull(technology);
        assertEquals(technology.getName(), technologyFound.getName());
        assertEquals(technology.getDescription(), technologyFound.getDescription());
    }

}
