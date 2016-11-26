package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import qs.pesquisaalfa.domain.enumeration.ConceitoPesquisa;

/**
 * A Tese.
 */
@Entity
@Table(name = "tese")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tese implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "conceito_medio_pesquisa", nullable = false)
    private ConceitoPesquisa conceitoMedioPesquisa;

    @OneToOne
    @NotNull
    @JoinColumn(unique = true)
    private Proposta proposta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConceitoPesquisa getConceitoMedioPesquisa() {
        return conceitoMedioPesquisa;
    }

    public Tese conceitoMedioPesquisa(ConceitoPesquisa conceitoMedioPesquisa) {
        this.conceitoMedioPesquisa = conceitoMedioPesquisa;
        return this;
    }

    public void setConceitoMedioPesquisa(ConceitoPesquisa conceitoMedioPesquisa) {
        this.conceitoMedioPesquisa = conceitoMedioPesquisa;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public Tese proposta(Proposta proposta) {
        this.proposta = proposta;
        return this;
    }

    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tese tese = (Tese) o;
        if(tese.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tese.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tese{" +
            "id=" + id +
            ", conceitoMedioPesquisa='" + conceitoMedioPesquisa + "'" +
            '}';
    }
}
