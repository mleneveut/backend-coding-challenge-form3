package tech.form3.challenge.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import tech.form3.challenge.util.StringJsonAttributesType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

/**
 * Data Access Object representing a Payment.
 * Linked to the database's table "payment".
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"paymentId", "type", "version", "organisationId", "attributes"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@TypeDefs({@TypeDef(name = "StringJsonObject", typeClass = StringJsonAttributesType.class)})
public class Payment {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID paymentId;

    @NotNull
    private String type;

    @NotNull
    private Integer version;

    @NotNull
    private UUID organisationId;

    @NotNull
    //Store the attributes as a JSON datatype
    @Type(type = "StringJsonObject")
    protected Map<String, Object> attributes;
}
