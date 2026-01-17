package albums.albums_application.controllers;

import albums.albums_application.assemblers.BandModelAssembler;
import albums.albums_application.service.BandService;
import albums.albumsapicontract.dto.BandRequest;
import albums.albumsapicontract.dto.BandResponse;
import albums.albumsapicontract.endpoints.BandApi;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BandController implements BandApi {

    private final BandService bandService;
    private final BandModelAssembler bandModelAssembler;

    public BandController(BandService bandService, BandModelAssembler bandModelAssembler) {
        this.bandService = bandService;
        this.bandModelAssembler = bandModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<BandResponse>> getAllBands() {
        List<BandResponse> bands = bandService.findAll();
        return bandModelAssembler.toCollectionModel(bands);
    }

    @Override
    public EntityModel<BandResponse> getBandById(String id) {
        BandResponse band = bandService.findById(id);
        return bandModelAssembler.toModel(band);
    }

    @Override
    public ResponseEntity<EntityModel<BandResponse>> createBand(BandRequest request) {
        BandResponse createdBand = bandService.create(request);
        EntityModel<BandResponse> entityModel = bandModelAssembler.toModel(createdBand);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<BandResponse> updateBand(String id, BandRequest request) {
        BandResponse updatedBand = bandService.update(id, request);
        return bandModelAssembler.toModel(updatedBand);
    }

    @Override
    public void deleteBand(String id) {
        bandService.delete(id);
    }
}
