package tech.form3.challenge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.util.Map;
import java.util.UUID;

/**
 * Data Transfer Object representing a Payment.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel("Payment attributes")
@Relation(collectionRelation = "data", value = "data")
public class PaymentDto extends ResourceSupport {

    @ApiModelProperty(value = "The resource's id")
    @JsonProperty(value = "id")
    private UUID paymentId;

    @ApiModelProperty(value = "The resource's type", required = true)
    private String type;

    @ApiModelProperty(value = "The resource's version", required = true)
    private Integer version;

    @ApiModelProperty(value = "The resource's orgaznisation id", required = true)
    private UUID organisationId;

    @ApiModelProperty(value = "The resource's attributes in JSON format", required = true)
    protected Map<String, Object> attributes;

}
