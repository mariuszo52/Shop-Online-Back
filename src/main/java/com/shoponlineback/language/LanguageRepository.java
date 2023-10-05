package com.shoponlineback.language;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long>{
    Optional<Language> findLanguageByName(String name);
}
