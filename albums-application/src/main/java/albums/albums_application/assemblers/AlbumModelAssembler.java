package albums.albums_application.assemblers;

import albums.albums_application.controllers.AlbumController;
import albums.albums_application.controllers.BandController;
import albums.albumsapicontract.dto.AlbumResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AlbumModelAssembler implements RepresentationModelAssembler<AlbumResponse, EntityModel<AlbumResponse>> {

    @Override
    public EntityModel<AlbumResponse> toModel(AlbumResponse Album) {
        return EntityModel.of(Album,
                linkTo(methodOn(AlbumController.class).getAlbumById(Album.getId())).withSelfRel(),
                linkTo(methodOn(BandController.class).getBandById(Album.getBand().getId())).withRel("band"),
                linkTo(methodOn(AlbumController.class).getAllAlbums(null, null, 0, 10)).withRel("collection")
        );
    }
}