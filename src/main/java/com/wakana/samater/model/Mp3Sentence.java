package com.wakana.samater.model;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "mp3_sentence" , uniqueConstraints = {
    @UniqueConstraint(columnNames = "sentence", name = "unique_sentence"),
  
}
)

public class Mp3Sentence {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String sentence;

}
