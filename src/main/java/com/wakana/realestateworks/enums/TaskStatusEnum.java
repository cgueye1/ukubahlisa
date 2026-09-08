package com.wakana.realestateworks.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum TaskStatusEnum {
    TODO,
    IN_PROGRESS,
    BLOCKED,
    DONE
}
