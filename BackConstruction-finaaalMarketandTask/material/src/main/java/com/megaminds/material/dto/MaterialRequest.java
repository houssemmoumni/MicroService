package com.megaminds.material.dto;

import com.megaminds.material.entity.Image;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MaterialRequest(Integer id,


                              @NotNull(message = "Material name is required")
                              String name,

                              @NotNull(message = "Material description is required")
                              String description,

                              @Positive(message = "Available quantity should be positive")
                              double availableQuantity,

                              @Positive(message = "Price should be positive")
                              BigDecimal price,

                              @NotNull(message = "Material category is required")
                              Integer categoryId,

                              @NotNull(message = "Material status is required")
                              String status)
{
}
