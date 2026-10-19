package zounkla.stackdice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zounkla.stackdice.model.entity.Technology;

import java.util.UUID;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, UUID> {
}
