package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BancaAvaliacao.
 */
@Entity
@Table(name = "banca_avaliacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BancaAvaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "data_hora_apresentacao", nullable = false)
    private ZonedDateTime dataHoraApresentacao;

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Proposta proposta;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "banca_avaliacao_professores",
               joinColumns = @JoinColumn(name="banca_avaliacaos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="professores_id", referencedColumnName="ID"))
    private Set<Professor> professores = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDataHoraApresentacao() {
        return dataHoraApresentacao;
    }

    public BancaAvaliacao dataHoraApresentacao(ZonedDateTime dataHoraApresentacao) {
        this.dataHoraApresentacao = dataHoraApresentacao;
        return this;
    }

    public void setDataHoraApresentacao(ZonedDateTime dataHoraApresentacao) {
        this.dataHoraApresentacao = dataHoraApresentacao;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public BancaAvaliacao proposta(Proposta proposta) {
        this.proposta = proposta;
        return this;
    }

    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }

    public Set<Professor> getProfessores() {
        return professores;
    }

    public BancaAvaliacao professores(Set<Professor> professors) {
        this.professores = professors;
        return this;
    }

    public BancaAvaliacao addProfessores(Professor professor) {
        professores.add(professor);
        professor.getBancasAvaliacaos().add(this);
        return this;
    }

    public BancaAvaliacao removeProfessores(Professor professor) {
        professores.remove(professor);
        professor.getBancasAvaliacaos().remove(this);
        return this;
    }

    public void setProfessores(Set<Professor> professors) {
        this.professores = professors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BancaAvaliacao bancaAvaliacao = (BancaAvaliacao) o;
        if(bancaAvaliacao.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bancaAvaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BancaAvaliacao{" +
            "id=" + id +
            ", dataHoraApresentacao='" + dataHoraApresentacao + "'" +
            '}';
    }
}
