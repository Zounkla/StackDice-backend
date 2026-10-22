package zounkla.stackdice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zounkla.stackdice.model.entity.Technology;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, UUID> {


    @Query(value = "SELECT * FROM technology WHERE category = :category " +
            "AND (compatibility = :compatibility OR compatibility = 'ANY') " +
            "ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Technology> findRandomByCategoryAndCompatibility(
            @Param("category") String category,
            @Param("compatibility") String compatibility
    );

    @Modifying
    @Query("DELETE FROM Technology t WHERE t.id = :uuid")
    long deleteTechnologyByIdReturningCount(UUID uuid);
}
