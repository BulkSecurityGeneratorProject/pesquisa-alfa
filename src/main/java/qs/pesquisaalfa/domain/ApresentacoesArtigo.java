package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A ApresentacoesArtigo.
 */
@Entity
@Table(name = "apresentacoes_artigo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApresentacoesArtigo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_apresentacao", nullable = false)
    private LocalDate dataApresentacao;

    @NotNull
    @Size(min = 3)
    @Column(name = "local", nullable = false)
    private String local;

    @ManyToOne
    @NotNull
    private Artigo artigo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataApresentacao() {
        return dataApresentacao;
    }

    public ApresentacoesArtigo dataApresentacao(LocalDate dataApresentacao) {
        this.dataApresentacao = dataApresentacao;
        return this;
    }

    public void setDataApresentacao(LocalDate dataApresentacao) {
        this.dataApresentacao = dataApresentacao;
    }

    public String getLocal() {
        return local;
    }

    public ApresentacoesArtigo local(String local) {
        this.local = local;
        return this;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Artigo getArtigo() {
        return artigo;
    }

    public ApresentacoesArtigo artigo(Artigo artigo) {
        this.artigo = artigo;
        return this;
    }

    public void setArtigo(Artigo artigo) {
        this.artigo = artigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApresentacoesArtigo apresentacoesArtigo = (ApresentacoesArtigo) o;
        if(apresentacoesArtigo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, apresentacoesArtigo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ApresentacoesArtigo{" +
            "id=" + id +
            ", dataApresentacao='" + dataApresentacao + "'" +
            ", local='" + local + "'" +
            '}';
    }
}
