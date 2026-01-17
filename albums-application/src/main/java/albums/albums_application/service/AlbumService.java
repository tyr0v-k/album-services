package albums.albums_application.service;

import albums.albums_application.config.RabbitMQConfig;
import albums.albums_application.storage.InMemoryStorage;
import albums.albumsapicontract.dto.*;
import albums.albumsapicontract.exception.CatalogNumberAlreadyExistsException;
import albums.albumsapicontract.exception.ResourceNotFoundException;
import events.AlbumCreatedEvent;
import events.AlbumDeletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class AlbumService {

    private final InMemoryStorage storage;
    private final BandService bandService;
    private final RabbitTemplate rabbitTemplate;

    public AlbumService(InMemoryStorage storage, @Lazy BandService bandService, RabbitTemplate rabbitTemplate) {
        this.storage = storage;
        this.bandService = bandService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public AlbumResponse findAlbumById(String id) {
        return Optional.ofNullable(storage.albums.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Album", id));
    }

    public PagedResponse<AlbumResponse> findAllAlbums(String bandId, String genre, int page, int size) {
        Stream<AlbumResponse> albumsStream = storage.albums.values().stream()
                .sorted((b1, b2) -> b1.getId().compareTo(b2.getId()));

        if (bandId != null) {
            albumsStream = albumsStream.filter(album -> album.getBand() != null && album.getBand().getId().equals(bandId));
        }

        if (genre != null) {
            albumsStream = albumsStream.filter(album -> album.getGenre() != null && album.getGenre().equals(genre));
        }

        List<AlbumResponse> allAlbums = albumsStream.toList();

        int totalElements = allAlbums.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<AlbumResponse> pageContent = (fromIndex > toIndex) ? List.of() : allAlbums.subList(fromIndex, toIndex);

        return new PagedResponse<>(pageContent, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public AlbumResponse createAlbum(AlbumRequest request) {
        validateCatNum(request.catNum(), null);

        BandResponse band = bandService.findById(request.bandId());

        var album = new AlbumResponse(
                UUID.randomUUID().toString(),
                request.title(),
                request.released(),
                request.genre(),
                request.catNum(),
                band,
                LocalDateTime.now()
        );
        storage.albums.put(album.getId(), album);

        AlbumCreatedEvent event = new AlbumCreatedEvent(
                album.getId(),
                album.getTitle(),
                band.getBandName() + " from " + band.getBandCountry()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_ALBUM_CREATED, event);

        return album;
    }

    public AlbumResponse updateAlbum(String id, UpdateAlbumRequest request) {
        AlbumResponse existingAlbum = findAlbumById(id);
        validateCatNum(request.catNum(), id);

        var updatedAlbum = new AlbumResponse(
                id,
                request.title(),
                existingAlbum.getReleased(),
                existingAlbum.getGenre(),
                request.catNum(),
                existingAlbum.getBand(),
                existingAlbum.getCreatedAt()
        );
        storage.albums.put(id, updatedAlbum);
        return updatedAlbum;
    }

    public AlbumResponse updateAlbumGenre(String id, String genre) {
        AlbumResponse existingAlbum = findAlbumById(id);

        var updatedAlbum = new AlbumResponse(
                id,
                existingAlbum.getTitle(),
                existingAlbum.getReleased(),
                genre,
                existingAlbum.getCatNum(),
                existingAlbum.getBand(),
                existingAlbum.getCreatedAt()
        );
        storage.albums.put(id, updatedAlbum);
        return updatedAlbum;
    }


    public void deleteAlbum(String id) {
        findAlbumById(id);

        AlbumDeletedEvent event = new AlbumDeletedEvent(
                id
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_ALBUM_DELETED, event);

        storage.albums.remove(id);
    }

    public void deleteAlbumsByBandId(String bandId) {
        List<String> albumIdsToDelete = storage.albums.values().stream()
                .filter(album -> album.getBand() != null && album.getBand().getId().equals(bandId))
                .map(AlbumResponse::getId)
                .toList();

        albumIdsToDelete.forEach(storage.albums::remove);
    }

    private void validateCatNum(String catNum, String currentAlbumId) {
        storage.albums.values().stream()
                .filter(album -> album.getCatNum().equalsIgnoreCase(catNum))
                .filter(album -> !album.getId().equals(currentAlbumId))
                .findAny()
                .ifPresent(album -> {
                    throw new CatalogNumberAlreadyExistsException(catNum);
                });
    }
}
