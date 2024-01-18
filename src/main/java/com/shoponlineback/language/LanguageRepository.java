package com.shoponlineback.language;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long>{
    boolean existsByName(String name);
    Optional<Language> findLanguageByName(String name);
    List<Language> findAllByNameIn(List<String> names);
    @Query("SELECT p.languages FROM Product p WHERE p.id = :productId")
    Set<Language> findLanguagesByProductId(@Param("productId") Long productId);

}
