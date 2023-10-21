package net.kravuar.tinkofffootball.domain.model.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final String entityName;
    private final String searchParamName;
    private final Object searchParamValue;
}
