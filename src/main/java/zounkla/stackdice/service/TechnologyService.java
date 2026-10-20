package zounkla.stackdice.service;

import org.springframework.stereotype.Service;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.repository.TechnologyRepository;

import java.util.UUID;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public Technology getTechnology(UUID uuid) throws TechnologyNotFoundException {
        return technologyRepository.findById(uuid).orElseThrow(
                () -> new TechnologyNotFoundException("Technology with id: " + uuid + " not found")
        );
    }
}
