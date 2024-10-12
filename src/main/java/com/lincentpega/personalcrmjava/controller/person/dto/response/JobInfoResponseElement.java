package com.lincentpega.personalcrmjava.controller.person.dto.response;

public record JobInfoResponseElement(
    String company,
    String position,
    boolean isCurrent
) {}
