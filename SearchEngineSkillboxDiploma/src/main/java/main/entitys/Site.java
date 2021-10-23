package main.entitys;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.StatusSite;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "_site")
public class Site {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status", columnDefinition="ENUM('INDEXING', 'INDEXED', 'FAILED')")
    @Enumerated(EnumType.STRING)
    private StatusSite statusSite;

    @Column(name = "status_time")
    private Date statusTime;

    @Column(name = "last_error", columnDefinition ="TEXT")
    private String lastError;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

       public Site(StatusSite statusSite, Date statusTime, String lastError, String url, String name) {
        this.statusSite = statusSite;
        this.statusTime = statusTime;
        this.lastError = lastError;
        this.url = url;
        this.name = name;
    }

    public Site(String url) {
        this.url = url;
    }
}

