package com.tingeso.acopioservice.entities;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "acopio")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AcopioEntity{
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String fecha;
    private String proveedor;
    private String turno;
    private double klsLeche;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AcopioEntity that = (AcopioEntity) o;
        return getID() != null && Objects.equals(getID(), that.getID());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}