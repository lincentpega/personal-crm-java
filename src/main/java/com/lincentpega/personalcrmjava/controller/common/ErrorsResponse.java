package com.lincentpega.personalcrmjava.controller.common;

import java.util.List;

public record ErrorsResponse<T>(List<T> errors) {
}
