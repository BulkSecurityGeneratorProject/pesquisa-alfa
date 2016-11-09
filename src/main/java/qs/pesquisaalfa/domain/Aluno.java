package qs.pesquisaalfa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Aluno.
 */
@Entity
@Table(name = "aluno")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Aluno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dre")
    private Integer dre;

    @NotNull
    @Column(name = "data_matricula", nullable = false)
    private LocalDate dataMatricula;

    @NotNull
    @Column(name = "eh_candidato", nullable = false)
    private Boolean ehCandidato;

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Usuario usuario;

    @ManyToOne
    private Orientador orientador;

    @ManyToMany(mappedBy = "alunos")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artigo> artigos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDre() {
        return dre;
    }

    public Aluno dre(Integer dre) {
        this.dre = dre;
        return this;
    }

    public void setDre(Integer dre) {
        this.dre = dre;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public Aluno dataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
        return this;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public Boolean isEhCandidato() {
        return ehCandidato;
    }

    public Aluno ehCandidato(Boolean ehCandidato) {
        this.ehCandidato = ehCandidato;
        return this;
    }

    public void setEhCandidato(Boolean ehCandidato) {
        this.ehCandidato = ehCandidato;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Aluno usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Orientador getOrientador() {
        return orientador;
    }

    public Aluno orientador(Orientador orientador) {
        this.orientador = orientador;
        return this;
    }

    public void setOrientador(Orientador orientador) {
        this.orientador = orientador;
    }

    public Set<Artigo> getArtigos() {
        return artigos;
    }

    public Aluno artigos(Set<Artigo> artigos) {
        this.artigos = artigos;
        return this;
    }

    public Aluno addArtigo(Artigo artigo) {
        artigos.add(artigo);
        artigo.getAlunos().add(this);
        return this;
    }

    public Aluno removeArtigo(Artigo artigo) {
        artigos.remove(artigo);
        artigo.getAlunos().remove(this);
        return this;
    }

    public void setArtigos(Set<Artigo> artigos) {
        this.artigos = artigos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Aluno aluno = (Aluno) o;
        if(aluno.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, aluno.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Aluno{" +
            "id=" + id +
            ", dre='" + dre + "'" +
            ", dataMatricula='" + dataMatricula + "'" +
            ", ehCandidato='" + ehCandidato + "'" +
            '}';
    }
}
