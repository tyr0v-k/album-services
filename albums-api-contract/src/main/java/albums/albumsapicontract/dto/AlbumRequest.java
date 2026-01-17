package albums.albumsapicontract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AlbumRequest(
        @NotBlank(message = "Название не может быть пустым")
        String title,
        @NotNull(message = "Дата выпуска не может быть пустой")
        LocalDate released,
        @NotBlank(message = "Жанр не может быть пустым")
        String genre,
        @Size(min = 3, max = 19, message = "Catalog number должен содержать от 3 до 19 символов")
        String catNum,
        @NotBlank(message = "ID группы не может быть пустым")
        String bandId
) {}