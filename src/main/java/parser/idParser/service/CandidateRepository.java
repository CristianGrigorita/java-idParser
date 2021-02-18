package parser.idParser.service;

import org.springframework.data.repository.CrudRepository;
import parser.idParser.model.Candidate;

import java.util.UUID;

public interface CandidateRepository extends CrudRepository<Candidate, UUID> {
}
