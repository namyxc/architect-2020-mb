package employees;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "forename", column = @Column(name = "employee_forename")),
            @AttributeOverride( name = "surename", column = @Column(name = "employee_surename")),
    })
    private Name nameOfEmployee;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "forename", column = @Column(name = "mother_forename")),
            @AttributeOverride( name = "surename", column = @Column(name = "mother_surename")),
    })
    private Name nameOfMother;
}
