package com.shoponlineback.screenshot;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenshotRepository extends CrudRepository<Screenshot, Long> {
}
