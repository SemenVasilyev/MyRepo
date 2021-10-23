package main.entitys;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "_field")
public class Field {

    @Id
    @Column(name ="id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", columnDefinition = "varchar(255) default ''", nullable = false)
    private String name;

    @Column(name = "selector", columnDefinition = "varchar(255) default ''", nullable = false)
    private String selector;

    @Column(name = "weight", nullable = false)
    private float weight;

    public Field(String name, String selector, float weight) {
        this.name = name;
        this.selector = selector;
        this.weight = weight;


    }

    public boolean isInDB() {
        return false;
    }
}
