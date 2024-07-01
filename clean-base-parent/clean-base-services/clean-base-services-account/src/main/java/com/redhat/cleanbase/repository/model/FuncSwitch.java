package com.redhat.cleanbase.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.type.TrueFalseConverter;

@FieldNameConstants
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "FUNC_SWITCH"
        , uniqueConstraints = {
        @UniqueConstraint(columnNames = {FuncSwitch.Fields.funcName, FuncSwitch.Fields.pathExpression})
}
)
public class FuncSwitch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FUNC_NAME")
    private String funcName;

    @Column(name = "PATH_EXPRESSION")
    private String pathExpression;

    @Convert(converter = TrueFalseConverter.class)
    @Column(name = "ENABLED")
    private Boolean enabled;

}
