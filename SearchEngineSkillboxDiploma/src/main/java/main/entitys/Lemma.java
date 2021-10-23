package main.entitys;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
//@Table(name = "_lemma", indexes = @Index(columnList = "lemma"))
@Table(name = "_lemma")
public class Lemma implements Comparable<Lemma> {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lemma", columnDefinition = "varchar(255) default ''", nullable = false)
    private String lemma;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    @ManyToOne
    @JoinColumn(name="site_id", nullable=true)
    //@JoinColumn(name="site_id", nullable=false)
    private Site site;

    public Lemma(String lemma, Site site) {
        this(lemma);
        this.frequency = 1;
        this.site = site;
    }

    public Lemma(String lemma) {
        this.lemma = lemma;
        this.frequency = 1;
    }



    @Override
    public String toString() {
        return "Lemma{" + "id=" + id + ", lemma='" + lemma + ", frequency=" + frequency + '}';
    }

    @Override
    public int compareTo(Lemma l) {
        int i = frequency - l.getFrequency();
        if (i == 0)
            return lemma.compareTo(l.getLemma());
        return frequency - l.getFrequency();
    }


}
