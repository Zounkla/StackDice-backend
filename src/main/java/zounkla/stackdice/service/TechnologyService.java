package zounkla.stackdice.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zounkla.stackdice.exception.CategoryNotFoundException;
import zounkla.stackdice.exception.CompatibilityNotFoundException;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.model.Category;
import zounkla.stackdice.model.Compatibility;
import zounkla.stackdice.model.dto.Stack;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.repository.TechnologyRepository;

import java.util.UUID;

@Service
@Transactional
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public Page<Technology> getTechnologies(String category, String compatibility, Pageable pageable) throws CompatibilityNotFoundException, CategoryNotFoundException {
        Technology filter = new Technology();

        if (category != null && !category.trim().isEmpty()) {
            checkCategory(category);
            filter.setCategory(Category.valueOf(category.toUpperCase()));
        }

        if (compatibility != null && !compatibility.trim().isEmpty()) {
            checkCompatibility(compatibility);
            filter.setCompatibility(Compatibility.valueOf(compatibility.toUpperCase()));
        }

        Example<Technology> example = Example.of(filter, ExampleMatcher.matching().withIgnoreNullValues());
        return technologyRepository.findAll(example, pageable);
    }


    public Technology getTechnology(UUID uuid) throws TechnologyNotFoundException {
        return technologyRepository.findById(uuid).orElseThrow(
                () -> new TechnologyNotFoundException("Technology with id: " + uuid + " not found")
        );
    }

    public void createTechnology(Technology technology) {
        technologyRepository.save(technology);
    }

    public Technology  updateTechnology(UUID uuid, Technology technology) throws TechnologyNotFoundException {
        Technology existingTech = technologyRepository.findById(uuid)
                .orElseThrow(() -> new TechnologyNotFoundException("Technology with id: " + uuid + " not found"));

        existingTech.setName(technology.getName());
        existingTech.setCategory(technology.getCategory());
        existingTech.setCompatibility(technology.getCompatibility());
        existingTech.setDescription(technology.getDescription());

        return technologyRepository.save(existingTech);
    }

    public void deleteTechnology(UUID uuid) throws TechnologyNotFoundException {
        long deletedCount = technologyRepository.deleteTechnologyByIdReturningCount(uuid);

        if (deletedCount == 0) {
            throw new TechnologyNotFoundException("Technology with id: " + uuid + " not found");
        }
    }

    public Stack drawTechnology()  {
        String[] projectTypes = {"WEB", "MOBILE", "DESKTOP"};
        String chosenType = projectTypes[(int) (Math.random() * projectTypes.length)];

        Stack stack = new Stack();
        stack.setProjectType(chosenType);

        switch (chosenType) {
            case "WEB":
                stack.setFrontend(technologyRepository.findRandomByCategoryAndCompatibility(Category.FRONT.name(), Compatibility.WEB.name()).orElse(null));
                stack.setBackend(technologyRepository.findRandomByCategoryAndCompatibility(Category.BACK.name(), Compatibility.WEB.name()).orElse(null));
                stack.setDatabase(technologyRepository.findRandomByCategoryAndCompatibility(Category.DATABASE.name(), Compatibility.LOCAL.name()).orElse(null));
                break;

            case "MOBILE":
                stack.setFrontend(technologyRepository.findRandomByCategoryAndCompatibility(Category.MOBILE.name(), Compatibility.MOBILE.name()).orElse(null));
                stack.setBackend(technologyRepository.findRandomByCategoryAndCompatibility(Category.BACK.name(), Compatibility.WEB.name()).orElse(null));
                stack.setDatabase(technologyRepository.findRandomByCategoryAndCompatibility(Category.DATABASE.name(), Compatibility.LOCAL.name()).orElse(null));
                break;

            case "DESKTOP":
                stack.setFrontend(technologyRepository.findRandomByCategoryAndCompatibility(Category.DESKTOP.name(), Compatibility.DESKTOP.name()).orElse(null));
                stack.setBackend(null);
                stack.setDatabase(technologyRepository.findRandomByCategoryAndCompatibility(Category.DATABASE.name(), Compatibility.LOCAL.name()).orElse(null));
                break;
        }
        stack.setChallenge(technologyRepository.findRandomByCategoryAndCompatibility(Category.CHALLENGE.name(), Compatibility.ANY.name()).orElse(null));

        return stack;
    }



    private void checkCategory(String category) throws CategoryNotFoundException {
        try {
            Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotFoundException("La catégorie '" + category + "' n'existe pas.");
        }
    }

    private void checkCompatibility(String compatibility) throws CompatibilityNotFoundException {
        try {
            Compatibility.valueOf(compatibility.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CompatibilityNotFoundException("La compatibilité '" + compatibility + "' n'existe pas.");
        }
    }
}
