package qs.pesquisaalfa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Professor.
 */
@Entity
@Table(name = "professor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Professor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "departamento")
    private String departamento;

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Usuario usuario;

    @ManyToMany(mappedBy = "professores")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BancaAvaliacao> bancasAvaliacaos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartamento() {
        return departamento;
    }

    public Professor departamento(String departamento) {
        this.departamento = departamento;
        return this;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Professor usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<BancaAvaliacao> getBancasAvaliacaos() {
        return bancasAvaliacaos;
    }

    public Professor bancasAvaliacaos(Set<BancaAvaliacao> bancaAvaliacaos) {
        this.bancasAvaliacaos = bancaAvaliacaos;
        return this;
    }

    public Professor addBancasAvaliacao(BancaAvaliacao bancaAvaliacao) {
        bancasAvaliacaos.add(bancaAvaliacao);
        bancaAvaliacao.getProfessores().add(this);
        return this;
    }

    public Professor removeBancasAvaliacao(BancaAvaliacao bancaAvaliacao) {
        bancasAvaliacaos.remove(bancaAvaliacao);
        bancaAvaliacao.getProfessores().remove(this);
        return this;
    }

    public void setBancasAvaliacaos(Set<BancaAvaliacao> bancaAvaliacaos) {
        this.bancasAvaliacaos = bancaAvaliacaos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Professor professor = (Professor) o;
        if(professor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, professor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Professor{" +
            "id=" + id +
            ", departamento='" + departamento + "'" +
            '}';
    }
}
