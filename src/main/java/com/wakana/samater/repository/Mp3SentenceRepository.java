package com.wakana.samater.repository;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wakana.samater.model.Mp3Sentence;

public interface Mp3SentenceRepository  extends JpaRepository< Mp3Sentence , Long> {
    Optional<Mp3Sentence> findBySentence(String sentence );
}
