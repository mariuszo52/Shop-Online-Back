package com.shoponlineback.platform;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformRepository extends CrudRepository<Platform, Long> {
    Optional<Platform> findByName(String name);
    List<Platform> findAllByDevice(String device);
}
