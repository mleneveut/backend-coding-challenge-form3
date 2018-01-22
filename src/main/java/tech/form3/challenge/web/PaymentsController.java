package tech.form3.challenge.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.form3.challenge.dto.PaymentDto;
import tech.form3.challenge.exceptions.ResourceNotFoundException;
import tech.form3.challenge.exceptions.UnprocessableEntityException;
import tech.form3.challenge.mapper.PaymentMapper;
import tech.form3.challenge.service.PaymentService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * REST Controller handling payments.
 */
@RestController
@RequestMapping(
        value = "/v1/payments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Api(
        value = "Payments API",
        description = "API to handle the payments",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@ExposesResourceFor(PaymentDto.class)
public class PaymentsController {

    private final PaymentService resourceService;

    @ApiOperation(value = "Get all the payments")
    @GetMapping
    public ResponseEntity<Resources<PaymentDto>> getAll() {
        List<PaymentDto> dtos = resourceService.findAll()
                .stream()
                .map(e -> PaymentMapper.instance().toPaymentDto(e))
                .collect(Collectors.toList());

        //Add HATEOAS self link
        final Resources<PaymentDto> resources = new Resources<>(dtos);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));

        return ResponseEntity.ok(resources);
    }

    @ApiOperation(value = "Get a payment by its id")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(
            @ApiParam(value = "The payment's id to retrieve", required = true)
            @PathVariable UUID id) {
        return ofNullable(resourceService.getById(id))
                //map the DAO to the DTO to return to the client
                .map(e -> PaymentMapper.instance().toPaymentDto(e))
                //everything is ok, send a 200 response
                .map(ResponseEntity::ok)
                //if any method returns null, an error is thrown
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The payment %s can not be found", id.toString())));
    }

    @ApiOperation(value = "Create an payment")
    @PostMapping
    public ResponseEntity<PaymentDto> create(
            @ApiParam(value = "The payment's data to create", required = true)
            @Valid @RequestBody PaymentDto dto) {

        return ofNullable(PaymentMapper.instance().toPayment(dto))
                .map(resourceService::create)
                .map(savedDao -> PaymentMapper.instance().toPaymentDto(savedDao))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UnprocessableEntityException(String.format("The payment can not be saved : ", dto)));
    }

    @ApiOperation(value = "Update an payment")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(
            @ApiParam(value = "The identifier of the payment to update", required = true)
            @PathVariable UUID id,
            @ApiParam(value = "The payment's data to update", required = true)
            @Valid @RequestBody PaymentDto dto) {

        //set the id to update in the DTO
        dto.setPaymentId(id);

        return ofNullable(resourceService.edit(dto))
                .map(savedDao -> PaymentMapper.instance().toPaymentDto(savedDao))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("The payment %s can not be found", id.toString())));
    }

    @ApiOperation(value = "Delete a payment by id")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(
            @ApiParam(value = "The payment's id to delete", required = true)
            @PathVariable UUID id) {
        resourceService.delete(id);
        return ResponseEntity.ok().build();
    }

}