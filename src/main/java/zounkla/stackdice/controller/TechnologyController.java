package zounkla.stackdice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zounkla.stackdice.exception.CategoryNotFoundException;
import zounkla.stackdice.exception.CompatibilityNotFoundException;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.mapper.TechnologyMapper;
import zounkla.stackdice.model.dto.Stack;
import zounkla.stackdice.model.dto.TechnologyOut;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.service.TechnologyService;

import java.util.UUID;

@RestController
@RequestMapping("/technology")
public class TechnologyController {

    private final TechnologyMapper technologyMapper;
    private final TechnologyService technologyService;

    public TechnologyController(TechnologyMapper technologyMapper, TechnologyService technologyService) {
        this.technologyMapper = technologyMapper;
        this.technologyService = technologyService;
    }

    @GetMapping
    public ResponseEntity<Page<TechnologyOut>> getAllTechnologies(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String compatibility,
            Pageable pageable
    ) throws CategoryNotFoundException, CompatibilityNotFoundException {
        Page<Technology> technologies = technologyService.getTechnologies(category, compatibility, pageable);
        return ResponseEntity.ok(technologies.map(technologyMapper::technologyToTechnologyOut));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TechnologyOut> getTechnology(@PathVariable UUID uuid) throws TechnologyNotFoundException {
        Technology technology = technologyService.getTechnology(uuid);
        return ResponseEntity.ok(
                technologyMapper.technologyToTechnologyOut(technology)
        );
    }

    // TODO create

    // Todo update

    // TODO delete

    @GetMapping("/draw")
    public ResponseEntity<Stack> drawTechnology() {
        Stack stack = technologyService.drawTechnology();
        return ResponseEntity.ok(
                stack
        );
    }
}
