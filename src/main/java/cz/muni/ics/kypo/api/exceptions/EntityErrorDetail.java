package cz.muni.ics.kypo.api.exceptions;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityErrorDetail {
    @ApiModelProperty(value = "Class of the entity.", example = "IDMGroup")
    private String entity;
    @ApiModelProperty(value = "Identifier of the entity.", example = "id")
    private String identifier;
    @ApiModelProperty(value = "Value of the identifier.", example = "1")
    private Object identifierValue;
    @ApiModelProperty(value = "Detailed message of the exception", example = "Group with same name already exists.")
    private String reason;

    public EntityErrorDetail() {
    }

    public EntityErrorDetail(@NotBlank String reason) {
        this.reason = reason;
    }

    public EntityErrorDetail(@NotNull Class<?> entityClass,
                             @NotBlank String reason) {
        this(reason);
        this.entity = entityClass.getSimpleName();
    }

    public EntityErrorDetail(@NotNull Class<?> entityClass,
                             @NotBlank String identifier,
                             @NotNull Class<?> identifierClass,
                             @NotNull Object identifierValue,
                             @NotBlank String reason) {
        this(entityClass, reason);
        this.identifier = identifier;
        this.identifierValue = identifierClass.cast(identifierValue);
    }

    public EntityErrorDetail(@NotNull Class<?> entityClass,
                             @NotBlank String identifier,
                             @NotNull Class<?> identifierClass,
                             @NotNull Object identifierValue) {
        this.entity = entityClass.getSimpleName();
        this.identifier = identifier;
        this.identifierValue = identifierClass.cast(identifierValue);
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(@NotBlank String entity) {
        this.entity = entity;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotBlank String identifier) {
        this.identifier = identifier;
    }

    public Object getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(@NotNull Object identifierValue) {
        this.identifierValue = identifierValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(@NotBlank String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityErrorDetail)) return false;
        EntityErrorDetail that = (EntityErrorDetail) o;
        return Objects.equals(getEntity(), that.getEntity()) &&
                Objects.equals(getIdentifier(), that.getIdentifier()) &&
                Objects.equals(getIdentifierValue(), that.getIdentifierValue()) &&
                Objects.equals(getReason(), that.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity(), getIdentifier(), getIdentifierValue(), getReason());
    }

    @Override
    public String toString() {
        return "EntityErrorDetail{" +
                "entity='" + entity + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierValue=" + identifierValue +
                ", reason='" + reason + '\'' +
                '}';
    }
}
