package com.danko.filebackend.repository;

import com.danko.filebackend.model.Composer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComposerRepository extends JpaRepository<Composer, Long> {
    Composer findComposerByName(String name);
}
