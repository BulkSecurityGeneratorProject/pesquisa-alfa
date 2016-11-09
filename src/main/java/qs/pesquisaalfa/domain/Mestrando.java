package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Mestrando.
 */
@Entity
@Table(name = "mestrando")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mestrando implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "terminou_obrigatorias", nullable = false)
    private Boolean terminouObrigatorias;

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Aluno aluno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isTerminouObrigatorias() {
        return terminouObrigatorias;
    }

    public Mestrando terminouObrigatorias(Boolean terminouObrigatorias) {
        this.terminouObrigatorias = terminouObrigatorias;
        return this;
    }

    public void setTerminouObrigatorias(Boolean terminouObrigatorias) {
        this.terminouObrigatorias = terminouObrigatorias;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Mestrando aluno(Aluno aluno) {
        this.aluno = aluno;
        return this;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mestrando mestrando = (Mestrando) o;
        if(mestrando.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mestrando.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mestrando{" +
            "id=" + id +
            ", terminouObrigatorias='" + terminouObrigatorias + "'" +
            '}';
    }
}
