package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Doutorando.
 */
@Entity
@Table(name = "doutorando")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Doutorando implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "terminou_1_o_periodo", nullable = false)
    private Boolean terminou1OPeriodo;

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

    public Boolean isTerminou1OPeriodo() {
        return terminou1OPeriodo;
    }

    public Doutorando terminou1OPeriodo(Boolean terminou1OPeriodo) {
        this.terminou1OPeriodo = terminou1OPeriodo;
        return this;
    }

    public void setTerminou1OPeriodo(Boolean terminou1OPeriodo) {
        this.terminou1OPeriodo = terminou1OPeriodo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Doutorando aluno(Aluno aluno) {
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
        Doutorando doutorando = (Doutorando) o;
        if(doutorando.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, doutorando.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Doutorando{" +
            "id=" + id +
            ", terminou1OPeriodo='" + terminou1OPeriodo + "'" +
            '}';
    }
}
