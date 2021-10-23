package main.entitys;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
//@Table(name = "_index", indexes = @javax.persistence.Index(columnList = "page_id, lemma_id"))
@Table(name = "_index")
public class Index {

    @Id
    @Column(name ="id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="page_id", nullable=false)
    private Page page;

    @ManyToOne
    @JoinColumn(name="lemma_id", nullable=false)
    private Lemma lemma;

    @Column(name ="rank_", nullable = false)
    private float rank;

    public Index(Page page, Lemma lemma, float rank) {
        this.page = page;
        this.lemma = lemma;
        this.rank = rank;
    }

    public String toString() {
        return "Index{" + "id=" + id +
                ", page=" + page +
                ", lemma=" + lemma +
                ", rank=" + rank +
                '}';
    }
}
