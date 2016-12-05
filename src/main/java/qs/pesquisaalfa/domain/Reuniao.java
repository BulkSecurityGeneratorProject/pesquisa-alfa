package qs.pesquisaalfa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Reuniao.
 */
@Entity
@Table(name = "reuniao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reuniao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "horario", nullable = false)
    private ZonedDateTime horario;

    @NotNull
    @Size(min = 10)
    @Column(name = "assunto", nullable = false)
    private String assunto;

    @Column(name = "reuniao_aprovada")
    private Boolean reuniaoAprovada;

    @ManyToOne
    @NotNull
    private Aluno aluno;

    @ManyToOne
    @NotNull
    private Orientador orientador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getHorario() {
        return horario;
    }

    public Reuniao horario(ZonedDateTime horario) {
        this.horario = horario;
        return this;
    }

    public void setHorario(ZonedDateTime horario) {
        this.horario = horario;
    }

    public String getAssunto() {
        return assunto;
    }

    public Reuniao assunto(String assunto) {
        this.assunto = assunto;
        return this;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Boolean isReuniaoAprovada() {
        return reuniaoAprovada;
    }

    public Reuniao reuniaoAprovada(Boolean reuniaoAprovada) {
        this.reuniaoAprovada = reuniaoAprovada;
        return this;
    }

    public void setReuniaoAprovada(Boolean reuniaoAprovada) {
        this.reuniaoAprovada = reuniaoAprovada;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Reuniao aluno(Aluno aluno) {
        this.aluno = aluno;
        return this;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
       System.out.println();
         System.out.println(aluno.toString());
       System.out.println();
    }

    public Orientador getOrientador() {
        return orientador;
    }

    public Reuniao orientador(Orientador orientador) {
        this.orientador = orientador;
        return this;
    }

    public void setOrientador(Orientador orientador) {
       this.orientador = orientador;
       if (getAluno().getOrientador().getId() == orientador.getId()){
		System.out.println();
		System.out.println("\u001B[32m" + "REUNIAO OK! "+"\u001B[0m");
		System.out.println();       
	   }
	  else {
		System.out.println();
		System.out.println("\u001B[31m"+"ORIENTADOR "+orientador.getId()+" NAO ORIENTA O ALUNO "+getAluno().getId()+"!"+"\u001B[0m");
		System.out.println();      
	  } 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reuniao reuniao = (Reuniao) o;
        if(reuniao.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reuniao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Reuniao{" +
            "id=" + id +
            ", horario='" + horario + "'" +
            ", assunto='" + assunto + "'" +
            ", reuniaoAprovada='" + reuniaoAprovada + "'" +
            '}';
    }
}
