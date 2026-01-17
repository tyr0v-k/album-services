package albums.albums_application.assemblers;

import albums.albums_application.controllers.AlbumController;
import albums.albums_application.controllers.BandController;
import albums.albumsapicontract.dto.BandResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BandModelAssembler implements RepresentationModelAssembler<BandResponse, EntityModel<BandResponse>> {

    @Override
    public EntityModel<BandResponse> toModel(BandResponse author) {
        return EntityModel.of(author,
                linkTo(methodOn(BandController.class).getBandById(author.getId())).withSelfRel(),
                linkTo(methodOn(AlbumController.class).getAllAlbums(author.getId(), null, 0, 10)).withRel("albums"),
                linkTo(methodOn(BandController.class).getAllBands()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<BandResponse>> toCollectionModel(Iterable<? extends BandResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(BandController.class).getAllBands()).withSelfRel());
    }
}