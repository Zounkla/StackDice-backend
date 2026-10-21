package zounkla.stackdice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import zounkla.stackdice.exception.CategoryNotFoundException;
import zounkla.stackdice.exception.CompatibilityNotFoundException;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.model.Category;
import zounkla.stackdice.model.Compatibility;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.repository.TechnologyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologyServiceTests {

    @Mock
    private TechnologyRepository technologyRepository;

    @InjectMocks
    private TechnologyService technologyService;

    private Technology technology;
    private Technology technology2;

    private List<Technology> technologies;

    @BeforeEach
    public void setup() {
        technologies = new ArrayList<>();
        technology = new Technology();
        technology.setId(UUID.randomUUID());
        technology.setName("technology");
        technology.setDescription("technology description");
        technology.setCategory(Category.BACK);
        technologies.add(technology);

        technology2 = new Technology();
        technology2.setId(UUID.randomUUID());
        technology2.setName("technology2");
        technology2.setDescription("technology2 description");
        technology2.setCompatibility(Compatibility.WEB);
        technologies.add(technology2);
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

    @Test
    public void getTechnologiesShouldReturnAll() throws CategoryNotFoundException, CompatibilityNotFoundException {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        List<Technology> allTechs = List.of(technology, technology2);
        Page<Technology> expectedPage = new PageImpl<>(allTechs, pageable, technologies.size());
        when(this.technologyRepository.findAll(ArgumentMatchers.any(), eq(pageable)))
                .thenReturn(expectedPage);
        //WHEN
        Page<Technology> technologyFound = technologyService.getTechnologies(null, null, pageable);

        //THEN
        assertNotNull(technologyFound);
        assertEquals(2, technologyFound.getTotalElements());
    }

    @Test
    public void getTechnologiesShouldReturnOneBecauseOfCategory() throws CategoryNotFoundException, CompatibilityNotFoundException {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        String category = "BACK";
        List<Technology> filteredTechs = List.of(technology);
        Page<Technology> expectedPage = new PageImpl<>(filteredTechs, pageable, filteredTechs.size());

        when(this.technologyRepository.findAll(ArgumentMatchers.any(), eq(pageable)))
                .thenReturn(expectedPage);
        //WHEN
        Page<Technology> technologyFound = technologyService.getTechnologies(category, null, pageable);

        //THEN
        assertNotNull(technologyFound);
        assertEquals(1, technologyFound.getTotalElements());
    }

    @Test
    public void getTechnologiesShouldReturnOneBecauseOfCompatibility() throws CategoryNotFoundException, CompatibilityNotFoundException {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        String compatibility = "WEB";
        List<Technology> filteredTechs = List.of(technology2);
        Page<Technology> expectedPage = new PageImpl<>(filteredTechs, pageable, filteredTechs.size());

        when(this.technologyRepository.findAll(ArgumentMatchers.any(), eq(pageable)))
                .thenReturn(expectedPage);

        //WHEN
        Page<Technology> technologyFound = technologyService.getTechnologies(null, compatibility, pageable);

        //THEN
        assertNotNull(technologyFound);
        assertEquals(1, technologyFound.getTotalElements());
    }

    @Test
    public void getTechnologiesShouldReturnNone() throws CategoryNotFoundException, CompatibilityNotFoundException {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        String compatibility = "WEB";
        String category = "BACK";
        Page<Technology> expectedPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(this.technologyRepository.findAll(ArgumentMatchers.any(), eq(pageable)))
                .thenReturn(expectedPage);
        //WHEN
        Page<Technology> technologyFound = technologyService.getTechnologies(category, compatibility, pageable);

        //THEN
        assertNotNull(technologyFound);
        assertEquals(0, technologyFound.getTotalElements());
    }

    @Test
    public void getTechnologiesShouldReturnCompatibilityNotFound() {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        String compatibility = "AHUUHAOIAOIA";

        //WHEN
        assertThrows(CompatibilityNotFoundException.class, () ->
                technologyService.getTechnologies(null, compatibility, pageable)
        );
    }

    @Test
    public void getTechnologiesShouldReturnCategoryNotFound() {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        String category = "AHUUHAOIAOIA";

        //WHEN
        assertThrows(CategoryNotFoundException.class, () ->
                technologyService.getTechnologies(category, null, pageable)
        );
    }

}
