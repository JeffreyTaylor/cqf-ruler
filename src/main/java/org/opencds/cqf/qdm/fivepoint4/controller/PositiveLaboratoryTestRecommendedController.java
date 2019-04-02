package org.opencds.cqf.qdm.fivepoint4.controller;

import org.opencds.cqf.qdm.fivepoint4.exception.ResourceNotFound;
import org.opencds.cqf.qdm.fivepoint4.model.PositiveLaboratoryTestRecommended;
import org.opencds.cqf.qdm.fivepoint4.repository.PositiveLaboratoryTestRecommendedRepository;
import org.opencds.cqf.qdm.fivepoint4.validation.QdmValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class PositiveLaboratoryTestRecommendedController implements Serializable
{
    private final PositiveLaboratoryTestRecommendedRepository repository;

    @Autowired
    public PositiveLaboratoryTestRecommendedController(PositiveLaboratoryTestRecommendedRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/PositiveLaboratoryTestRecommended")
    public List<PositiveLaboratoryTestRecommended> getAll()
    {
        return repository.findAll();
    }

    @GetMapping("/PositiveLaboratoryTestRecommended/{id}")
    public @ResponseBody PositiveLaboratoryTestRecommended getById(@PathVariable(value = "id") String id)
    {
        return repository.findBySystemId(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("Read Failed: PositiveLaboratoryTestRecommended/%s not found", id),
                                new ResourceNotFound()
                        )
                );
    }

    @PostMapping("/PositiveLaboratoryTestRecommended")
    public PositiveLaboratoryTestRecommended create(@RequestBody @Valid PositiveLaboratoryTestRecommended positiveLaboratoryTestRecommended)
    {
        QdmValidator.validateResourceTypeAndName(positiveLaboratoryTestRecommended, positiveLaboratoryTestRecommended);
        return repository.save(positiveLaboratoryTestRecommended);
    }

    @PutMapping("/PositiveLaboratoryTestRecommended/{id}")
    public PositiveLaboratoryTestRecommended update(@PathVariable(value = "id") String id,
                                             @RequestBody @Valid PositiveLaboratoryTestRecommended positiveLaboratoryTestRecommended)
    {
        QdmValidator.validateResourceId(positiveLaboratoryTestRecommended.getId(), id);
        Optional<PositiveLaboratoryTestRecommended> update = repository.findById(id);
        if (update.isPresent())
        {
            PositiveLaboratoryTestRecommended updateResource = update.get();
            QdmValidator.validateResourceTypeAndName(positiveLaboratoryTestRecommended, updateResource);
            updateResource.copy(positiveLaboratoryTestRecommended);
            return repository.save(updateResource);
        }

        QdmValidator.validateResourceTypeAndName(positiveLaboratoryTestRecommended, positiveLaboratoryTestRecommended);
        return repository.save(positiveLaboratoryTestRecommended);
    }

    @DeleteMapping("/PositiveLaboratoryTestRecommended/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id)
    {
        PositiveLaboratoryTestRecommended pep =
                repository.findById(id)
                        .orElseThrow(
                                () -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        String.format("Delete Failed: PositiveLaboratoryTestRecommended/%s not found", id),
                                        new ResourceNotFound()
                                )
                        );

        repository.delete(pep);

        return ResponseEntity.ok().build();
    }
}