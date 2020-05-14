package employees;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Data
@Table(name = "employees")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "mother"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;



    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "forename", column = @Column(name = "employee_forename")),
            @AttributeOverride(name = "surename", column = @Column(name = "employee_surename"))
    })
    public Name name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "forename", column = @Column(name = "mother_forename")),
            @AttributeOverride(name = "surename", column = @Column(name = "mother_surename"))
    })
    public Name mother;
}
