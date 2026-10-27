package zounkla.stackdice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zounkla.stackdice.exception.CategoryNotFoundException;
import zounkla.stackdice.exception.CompatibilityNotFoundException;
import zounkla.stackdice.exception.TechnologyNotFoundException;
import zounkla.stackdice.mapper.TechnologyMapper;
import zounkla.stackdice.model.dto.Stack;
import zounkla.stackdice.model.dto.TechnologyIn;
import zounkla.stackdice.model.dto.TechnologyOut;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.service.TechnologyService;

import java.util.UUID;

@RestController
@RequestMapping("/technology")
@Tag(name = "Technologies", description = "Gestion du catalogue de technologies et tirages de stacks")
public class TechnologyController {

    private final TechnologyMapper technologyMapper;
    private final TechnologyService technologyService;

    public TechnologyController(TechnologyMapper technologyMapper, TechnologyService technologyService) {
        this.technologyMapper = technologyMapper;
        this.technologyService = technologyService;
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les technologies", description = "Retourne une page de technologies filtrable par catégorie et compatibilité.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "Paramètres de filtrage invalides", content = @Content)
    })
    public ResponseEntity<Page<TechnologyOut>> getAllTechnologies(
            @Parameter(description = "Filtrer par catégorie (ex: DATABASE, FRONTEND)", example = "DATABASE")
            @RequestParam(required = false) String category,
            @Parameter(description = "Filtrer par compatibilité (ex: DESKTOP, WEB)", example = "WEB")
            @RequestParam(required = false) String compatibility,
            @Parameter(description = "Options de pagination (page, size, sort)")
            Pageable pageable
    ) throws CategoryNotFoundException, CompatibilityNotFoundException {
        Page<Technology> technologies = technologyService.getTechnologies(category, compatibility, pageable);
        return ResponseEntity.ok(technologies.map(technologyMapper::technologyToTechnologyOut));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Récupérer une technologie par son UUID", description = "Fournit les détails d'une technologie spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technologie trouvée"),
            @ApiResponse(responseCode = "404", description = "Technologie introuvable avec cet UUID", content = @Content)
    })
    public ResponseEntity<TechnologyOut> getTechnology(
            @Parameter(description = "UUID unique de la technologie", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID uuid
    ) throws TechnologyNotFoundException {
        Technology technology = technologyService.getTechnology(uuid);
        return ResponseEntity.ok(technologyMapper.technologyToTechnologyOut(technology));
    }

    @PostMapping
    @Operation(summary = "Créer une technologie", description = "Crée la technologie spécifiée et la retourne.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Technologie créée"),
            @ApiResponse(responseCode = "400", description = "Paramètres obligatoires non renseignés", content = @Content)
    })
    public ResponseEntity<TechnologyOut> createTechnology(
            @RequestBody @Valid TechnologyIn technologyIn
    ) {
        Technology technology = technologyMapper.technologyInToTechnology(technologyIn);
        technologyService.createTechnology(technology);
        return new ResponseEntity<>(technologyMapper.technologyToTechnologyOut(technology), HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Modifier une technologie", description = "Modifie la technologie spécifiée et la retourne.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technologie mise à jour"),
            @ApiResponse(responseCode = "400", description = "Paramètres obligatoires non renseignés", content = @Content),
            @ApiResponse(responseCode = "404", description = "Technologie introuvable", content = @Content)
    })
    public ResponseEntity<TechnologyOut> updateTechnology(
            @PathVariable UUID uuid,
            @RequestBody @Valid TechnologyIn technologyIn
    ) throws TechnologyNotFoundException {
        Technology technology = technologyMapper.technologyInToTechnology(technologyIn);
        technology = technologyService.updateTechnology(uuid, technology);
        return ResponseEntity.ok(technologyMapper.technologyToTechnologyOut(technology));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Supprimer une technologie", description = "Supprime définitivement une technologie du catalogue via son UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Technologie supprimée avec succès (Aucun contenu renvoyé)"),
            @ApiResponse(responseCode = "404", description = "Échec de la suppression : l'UUID fourni n'existe pas", content = @Content)
    })
    public ResponseEntity<Void> deleteTechnology(
            @Parameter(description = "UUID de la technologie à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID uuid
    ) throws TechnologyNotFoundException {
        technologyService.deleteTechnology(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/draw")
    @Operation(summary = "Tirer une stack technique au sort", description = "Génère de manière aléatoire une combinaison cohérente de technologies (Stack).")
    @ApiResponse(responseCode = "200", description = "Stack générée avec succès")
    public ResponseEntity<Stack> drawTechnology() {
        Stack stack = technologyService.drawTechnology();
        return ResponseEntity.ok(stack);
    }
}
