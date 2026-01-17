package albums.albumsapicontract.dto;

import jakarta.validation.constraints.NotBlank;

public record BandRequest(
        @NotBlank(message = "Название группы не может быть пустым") String bandName,
        @NotBlank(message = "Страна группы не может быть пустой") String bandCountry
) {}