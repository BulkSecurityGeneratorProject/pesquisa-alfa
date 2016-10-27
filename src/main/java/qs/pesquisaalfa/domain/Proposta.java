package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Proposta.
 */
@Entity
@Table(name = "proposta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Proposta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "proposta_aceita", nullable = false)
    private Boolean propostaAceita;

    @NotNull
    @Column(name = "tema", nullable = false)
    private String tema;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isPropostaAceita() {
        return propostaAceita;
    }

    public Proposta propostaAceita(Boolean propostaAceita) {
        this.propostaAceita = propostaAceita;
        return this;
    }

    public void setPropostaAceita(Boolean propostaAceita) {
        this.propostaAceita = propostaAceita;
    }

    public String getTema() {
        return tema;
    }

    public Proposta tema(String tema) {
        this.tema = tema;
        return this;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Proposta proposta = (Proposta) o;
        if(proposta.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, proposta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Proposta{" +
            "id=" + id +
            ", propostaAceita='" + propostaAceita + "'" +
            ", tema='" + tema + "'" +
            '}';
    }
}
