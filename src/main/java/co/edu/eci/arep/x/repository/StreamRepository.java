package co.edu.eci.arep.x.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.eci.arep.x.model.Stream;

public interface StreamRepository extends MongoRepository<Stream, String> {
}