package albums.albums_application.controllers;

import albums.albums_application.assemblers.AlbumModelAssembler;
import albums.albums_application.service.AlbumService;
import albums.albumsapicontract.dto.AlbumRequest;
import albums.albumsapicontract.dto.AlbumResponse;
import albums.albumsapicontract.dto.PagedResponse;
import albums.albumsapicontract.dto.UpdateAlbumRequest;
import albums.albumsapicontract.endpoints.AlbumApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumController implements AlbumApi {

    private final AlbumService albumService;
    private final AlbumModelAssembler albumModelAssembler;
    private final PagedResourcesAssembler<AlbumResponse> pagedResourcesAssembler;

    public AlbumController(AlbumService albumService, AlbumModelAssembler albumModelAssembler, PagedResourcesAssembler<AlbumResponse> pagedResourcesAssembler) {
        this.albumService = albumService;
        this.albumModelAssembler = albumModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<AlbumResponse> getAlbumById(String id) {
        AlbumResponse album = albumService.findAlbumById(id);
        return albumModelAssembler.toModel(album);
    }

    @Override
    public PagedModel<EntityModel<AlbumResponse>> getAllAlbums(String bandId, String genre, int page, int size) {
        PagedResponse<AlbumResponse> pagedResponse = albumService.findAllAlbums(bandId, genre, page, size);
        Page<AlbumResponse> albumPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );
        return pagedResourcesAssembler.toModel(albumPage, albumModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<AlbumResponse>> createAlbum(AlbumRequest request) {
        AlbumResponse createdAlbum = albumService.createAlbum(request);
        EntityModel<AlbumResponse> entityModel = albumModelAssembler.toModel(createdAlbum);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<AlbumResponse> updateAlbum(String id, UpdateAlbumRequest request) {
        AlbumResponse updatedAlbum = albumService.updateAlbum(id, request);
        return albumModelAssembler.toModel(updatedAlbum);
    }

    @Override
    public void deleteAlbum(String id) {
        albumService.deleteAlbum(id);
    }
}