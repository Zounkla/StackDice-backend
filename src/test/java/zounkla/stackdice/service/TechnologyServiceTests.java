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
import zounkla.stackdice.model.dto.Stack;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.repository.TechnologyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @Test
    public void drawTechnologyShouldReturnNullBackendWhenProjectTypeIsDesktop() {
        // GIVEN
        Technology mockFrontDesktop = new Technology("JavaFX", Category.DESKTOP, Compatibility.DESKTOP, "Desc");
        Technology mockDbLocal = new Technology("SQLite", Category.DATABASE, Compatibility.LOCAL, "Desc");
        Technology mockChallenge = new Technology("Le Minimaliste", Category.CHALLENGE, Compatibility.ANY, "Desc");

        when(this.technologyRepository.findRandomByCategoryAndCompatibility(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(mockFrontDesktop))
                .thenReturn(Optional.of(mockDbLocal))
                .thenReturn(Optional.of(mockChallenge));

        // WHEN
        Stack result = technologyService.drawTechnology();

        // THEN
        if ("DESKTOP".equals(result.getProjectType())) {
            assertNull(result.getBackend(), "Un projet DESKTOP ne doit pas posseder de backend");
            assertNotNull(result.getFrontend());
            assertNotNull(result.getDatabase());
        } else {
            assertNotNull(result.getBackend(), "Un projet WEB/MOBILE doit posseder un backend");
        }
    }

    @Test
    public void deleteTechnologyShouldDeleteSuccessfullyWhenIdExists() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(technologyRepository.deleteTechnologyByIdReturningCount(id)).thenReturn(1L);

        // WHEN & THEN
        assertDoesNotThrow(() -> technologyService.deleteTechnology(id));
        verify(technologyRepository, times(1)).deleteTechnologyByIdReturningCount(id);
    }

    @Test
    public void deleteTechnologyShouldThrowExceptionWhenIdDoesNotExist() {
        // GIVEN
        UUID id = UUID.randomUUID();
        when(technologyRepository.deleteTechnologyByIdReturningCount(id)).thenReturn(0L);

        // WHEN & THEN
        TechnologyNotFoundException exception = assertThrows(
                TechnologyNotFoundException.class,
                () -> technologyService.deleteTechnology(id)
        );
        assertEquals("Technology with id: " + id + " not found", exception.getMessage());
        verify(technologyRepository, times(1)).deleteTechnologyByIdReturningCount(id);
    }

    @Test
    public void createTechnologyShouldSaveSuccessfully() {
        // GIVEN
        Technology newTech = new Technology();
        newTech.setName("Spring Boot");
        newTech.setCategory(Category.BACK);

        // WHEN
        technologyService.createTechnology(newTech);

        // THEN
        verify(technologyRepository, times(1)).save(newTech);
    }

    @Test
    public void updateTechnologyShouldUpdateAndReturnSuccessfullyWhenIdExists() throws TechnologyNotFoundException {
        // GIVEN
        UUID uuid = UUID.randomUUID();

        Technology existingTech = new Technology();
        existingTech.setId(uuid);
        existingTech.setName("Old Name");
        existingTech.setDescription("Old Desc");
        existingTech.setCategory(Category.FRONT);
        existingTech.setCompatibility(Compatibility.WEB);

        Technology updateData = new Technology();
        updateData.setName("New Name");
        updateData.setDescription("New Desc");
        updateData.setCategory(Category.BACK);
        updateData.setCompatibility(Compatibility.ANY);

        when(technologyRepository.findById(uuid)).thenReturn(Optional.of(existingTech));
        when(technologyRepository.save(any(Technology.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Technology result = technologyService.updateTechnology(uuid, updateData);

        // THEN
        assertNotNull(result);
        assertEquals(uuid, result.getId());
        assertEquals("New Name", result.getName());
        assertEquals("New Desc", result.getDescription());
        assertEquals(Category.BACK, result.getCategory());
        assertEquals(Compatibility.ANY, result.getCompatibility());

        verify(technologyRepository, times(1)).findById(uuid);
        verify(technologyRepository, times(1)).save(existingTech);
    }

    @Test
    public void updateTechnologyShouldThrowExceptionWhenIdDoesNotExist() {
        // GIVEN
        UUID uuid = UUID.randomUUID();
        Technology updateData = new Technology();

        when(technologyRepository.findById(uuid)).thenReturn(Optional.empty());

        // WHEN & THEN
        TechnologyNotFoundException exception = assertThrows(
                TechnologyNotFoundException.class,
                () -> technologyService.updateTechnology(uuid, updateData)
        );
        assertEquals("Technology with id: " + uuid + " not found", exception.getMessage());

        verify(technologyRepository, never()).save(any(Technology.class));
    }

    @Test
    public void drawTechnologyShouldReturnDeterministicStackLoopingUntilAllBranchesCovered() {
        // GIVEN
        Technology frontWeb = new Technology("React", Category.FRONT, Compatibility.WEB, "Desc");
        Technology backWeb = new Technology("Node", Category.BACK, Compatibility.WEB, "Desc");
        Technology dbLocal = new Technology("Postgres", Category.DATABASE, Compatibility.LOCAL, "Desc");
        Technology frontMobile = new Technology("Flutter", Category.MOBILE, Compatibility.MOBILE, "Desc");
        Technology frontDesktop = new Technology("Electron", Category.DESKTOP, Compatibility.DESKTOP, "Desc");
        Technology challenge = new Technology("No Libraries", Category.CHALLENGE, Compatibility.ANY, "Desc");

        when(technologyRepository.findRandomByCategoryAndCompatibility(Category.FRONT.name(), Compatibility.WEB.name()))
                .thenReturn(Optional.of(frontWeb));
        when(technologyRepository.findRandomByCategoryAndCompatibility(Category.BACK.name(), Compatibility.WEB.name()))
                .thenReturn(Optional.of(backWeb));
        when(technologyRepository.findRandomByCategoryAndCompatibility(Category.DATABASE.name(), Compatibility.LOCAL.name()))
                .thenReturn(Optional.of(dbLocal));
        when(technologyRepository.findRandomByCategoryAndCompatibility(Category.MOBILE.name(), Compatibility.MOBILE.name()))
                .thenReturn(Optional.of(frontMobile));
        when(technologyRepository.findRandomByCategoryAndCompatibility(Category.DESKTOP.name(), Compatibility.DESKTOP.name()))
                .thenReturn(Optional.of(frontDesktop));
        when(technologyRepository.findRandomByCategoryAndCompatibility(Category.CHALLENGE.name(), Compatibility.ANY.name()))
                .thenReturn(Optional.of(challenge));

        boolean webCovered = false;
        boolean mobileCovered = false;
        boolean desktopCovered = false;

        // WHEN & THEN
        for (int i = 0; i < 100; i++) {
            Stack result = technologyService.drawTechnology();
            assertNotNull(result);
            assertEquals(challenge, result.getChallenge());

            switch (result.getProjectType()) {
                case "WEB":
                    assertEquals(frontWeb, result.getFrontend());
                    assertEquals(backWeb, result.getBackend());
                    assertEquals(dbLocal, result.getDatabase());
                    webCovered = true;
                    break;
                case "MOBILE":
                    assertEquals(frontMobile, result.getFrontend());
                    assertEquals(backWeb, result.getBackend());
                    assertEquals(dbLocal, result.getDatabase());
                    mobileCovered = true;
                    break;
                case "DESKTOP":
                    assertEquals(frontDesktop, result.getFrontend());
                    assertNull(result.getBackend(), "Un projet DESKTOP ne doit pas posséder de backend");
                    assertEquals(dbLocal, result.getDatabase());
                    desktopCovered = true;
                    break;
                default:
                    fail("Type de projet inconnu généré : " + result.getProjectType());
            }

            if (webCovered && mobileCovered && desktopCovered) {
                break;
            }
        }

        assertTrue(webCovered, "La branche WEB n'a pas pu être testée");
        assertTrue(mobileCovered, "La branche MOBILE n'a pas pu être testée");
        assertTrue(desktopCovered, "La branche DESKTOP n'a pas pu être testée");
    }



}
