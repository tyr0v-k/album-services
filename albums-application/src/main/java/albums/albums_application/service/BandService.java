package albums.albums_application.service;

import albums.albums_application.storage.InMemoryStorage;
import albums.albumsapicontract.dto.BandRequest;
import albums.albumsapicontract.dto.BandResponse;
import albums.albumsapicontract.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BandService {
    private final InMemoryStorage storage;
    private final AlbumService albumService;

    public BandService(InMemoryStorage storage, @Lazy AlbumService albumService) {
        this.storage = storage;
        this.albumService = albumService;
    }

    public List<BandResponse> findAll() {
        return storage.bands.values().stream().toList();
    }

    public BandResponse findById(String id) {
        return Optional.ofNullable(storage.bands.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Band", id));
    }

    public BandResponse create(BandRequest request) {
        String id = UUID.randomUUID().toString();
        BandResponse band = new BandResponse(id, request.bandName(), request.bandCountry());
        storage.bands.put(id, band);
        return band;
    }

    public BandResponse update(String id, BandRequest request) {
        findById(id);
        BandResponse updatedBand = new BandResponse(id, request.bandName(), request.bandCountry());
        storage.bands.put(id, updatedBand);
        return updatedBand;
    }

    public void delete(String id) {
        findById(id);

        albumService.deleteAlbumsByBandId(id);

        storage.bands.remove(id);
    }
}
