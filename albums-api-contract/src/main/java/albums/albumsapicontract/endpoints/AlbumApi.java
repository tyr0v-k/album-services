package albums.albumsapicontract.endpoints;

import albums.albumsapicontract.dto.AlbumRequest;
import albums.albumsapicontract.dto.AlbumResponse;
import albums.albumsapicontract.dto.StatusResponse;
import albums.albumsapicontract.dto.UpdateAlbumRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "albums", description = "API для работы с альбомами")
@RequestMapping("/api/albums")
public interface AlbumApi {

    @Operation(summary = "Получить альбом по ID")
    @ApiResponse(responseCode = "200", description = "Альбом найден")
    @ApiResponse(responseCode = "404", description = "Альбом не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<AlbumResponse> getAlbumById(@PathVariable("id") String id);

    @Operation(summary = "Получить список всех альбомов с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Список альбомов")
    @GetMapping
    PagedModel<EntityModel<AlbumResponse>> getAllAlbums(
            @Parameter(description = "Фильтр по ID группы") @RequestParam(required = false) String bandId,
            @Parameter(description = "Фильтр по жанру") @RequestParam(required = false) String genre,
            @Parameter(description = "Номер страницы (0..N)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Создать новый альбом")
    @ApiResponse(responseCode = "201", description = "Альбом успешно создан")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Альбом с таким catalog number уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<AlbumResponse>> createAlbum(@Valid @RequestBody AlbumRequest request);

    @Operation(summary = "Обновить альбом по ID")
    @ApiResponse(responseCode = "200", description = "Альбом успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Альбом не найден", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @ApiResponse(responseCode = "409", description = "Альбом с таким catalog number уже существует", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<AlbumResponse> updateAlbum(@PathVariable String id, @Valid @RequestBody UpdateAlbumRequest request);

    @Operation(summary = "Удалить альбом по ID")
    @ApiResponse(responseCode = "204", description = "Альбом успешно удален")
    @ApiResponse(responseCode = "404", description = "Альбом не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAlbum(@PathVariable String id);
}