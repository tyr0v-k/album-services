package albums.albumsapicontract.endpoints;

import albums.albumsapicontract.dto.BandRequest;
import albums.albumsapicontract.dto.BandResponse;
import albums.albumsapicontract.dto.StatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "bands", description = "API для работы с группами")
@RequestMapping("/api/bands")
public interface BandApi {

    @Operation(summary = "Получить всех групп")
    @ApiResponse(responseCode = "200", description = "Список групп")
    @GetMapping
    CollectionModel<EntityModel<BandResponse>> getAllBands();

    @Operation(summary = "Получить группу по ID")
    @ApiResponse(responseCode = "200", description = "Группа найдена")
    @ApiResponse(responseCode = "404", description = "Групп не найдена", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @GetMapping("/{id}")
    EntityModel<BandResponse> getBandById(@PathVariable String id);

    @Operation(summary = "Создать новую группу")
    @ApiResponse(responseCode = "201", description = "Группа успешно создана")
    @ApiResponse(responseCode = "400", description = "Невалидный запрос", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<BandResponse>> createBand(@Valid @RequestBody BandRequest request);

    @Operation(summary = "Обновить группу")
    @ApiResponse(responseCode = "200", description = "Группа обновлена")
    @ApiResponse(responseCode = "404", description = "Группа не найдена", content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    @PutMapping("/{id}")
    EntityModel<BandResponse> updateBand(@PathVariable String id, @Valid @RequestBody BandRequest request);

    @Operation(summary = "Удалить группу")
    @ApiResponse(responseCode = "204", description = "Группа удалена")
    @ApiResponse(responseCode = "404", description = "Группа не найдена")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBand(@PathVariable String id);
}