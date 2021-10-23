package main.entitys;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
//@Table(name = "_page", indexes = @Index(columnList = "path"))
@Table(name = "_page")
public class Page {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "path", columnDefinition = "TEXT", nullable = false)
    private String path;

    @Column(name = "code", nullable = false)
    private int code;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="site_id", nullable=true)
      private Site site;

    public Page(String path, int code, String content, Site site) {
        this.path = path;
        this.code = code;
        this.content = content;
        this.site = site;
    }

    @Override
    public String toString() {
        return "Page{" + "path='" + path + ", code=" + code + ", content='" + content + ", site=" + site + '}';
    }
}
