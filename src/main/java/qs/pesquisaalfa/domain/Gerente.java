package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Gerente.
 */
@Entity
@Table(name = "gerente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Gerente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Usuario nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getNome() {
        return nome;
    }

    public Gerente nome(Usuario usuario) {
        this.nome = usuario;
        return this;
    }

    public void setNome(Usuario usuario) {
        this.nome = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Gerente gerente = (Gerente) o;
        if(gerente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, gerente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Gerente{" +
            "id=" + id +
            '}';
    }
}
