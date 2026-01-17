package albums.albumsapicontract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAlbumRequest(
        @NotBlank(message = "Название не может быть пустым")
        String title,
        @Size(min = 3, max = 19, message = "Catalog number должен содержать от 3 до 19 символов")
        String catNum
) {}