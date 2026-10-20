package zounkla.stackdice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.mapper.TechnologyMapper;
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

    // TODO getAll

    @GetMapping("/{uuid}")
    public ResponseEntity<TechnologyOut> getTechnology(@PathVariable UUID uuid) throws TechnologyNotFoundException {
        Technology technology = technologyService.getTechnology(uuid);
        return new ResponseEntity<>(
                technologyMapper.technologyToTechnologyOut(technology),
                HttpStatus.OK);
    }

    // TODO create

    // Todo update

    // TODO delete

    // TODO drawStack
}
