package qs.pesquisaalfa.domain;

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
 * A Artigo.
 */
@Entity
@Table(name = "artigo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Artigo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "data_apresentacao", nullable = false)
    private LocalDate dataApresentacao;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "artigo_alunos",
               joinColumns = @JoinColumn(name="artigos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="alunos_id", referencedColumnName="ID"))
    private Set<Aluno> alunos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Artigo titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataApresentacao() {
        return dataApresentacao;
    }

    public Artigo dataApresentacao(LocalDate dataApresentacao) {
        this.dataApresentacao = dataApresentacao;
        return this;
    }

    public void setDataApresentacao(LocalDate dataApresentacao) {
        this.dataApresentacao = dataApresentacao;
    }

    public Set<Aluno> getAlunos() {
        return alunos;
    }

    public Artigo alunos(Set<Aluno> alunos) {
        this.alunos = alunos;
        return this;
    }

    public Artigo addAlunos(Aluno aluno) {
        alunos.add(aluno);
        aluno.getArtigos().add(this);
        return this;
    }

    public Artigo removeAlunos(Aluno aluno) {
        alunos.remove(aluno);
        aluno.getArtigos().remove(this);
        return this;
    }

    public void setAlunos(Set<Aluno> alunos) {
        this.alunos = alunos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artigo artigo = (Artigo) o;
        if(artigo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, artigo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Artigo{" +
            "id=" + id +
            ", titulo='" + titulo + "'" +
            ", dataApresentacao='" + dataApresentacao + "'" +
            '}';
    }
}
