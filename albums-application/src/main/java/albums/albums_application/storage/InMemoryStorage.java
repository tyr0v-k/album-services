package albums.albums_application.storage;

import albums.albumsapicontract.dto.AlbumResponse;
import albums.albumsapicontract.dto.BandResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStorage {
    public final Map<String, BandResponse> bands = new ConcurrentHashMap<>();
    public final Map<String, AlbumResponse> albums = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        BandResponse band1 = new BandResponse(UUID.randomUUID().toString(), "Гражданская Оборона", "Россия");
        BandResponse band2 = new BandResponse(UUID.randomUUID().toString(), "Black Magick SS", "Австралия");
        bands.put(band1.getId(), band1);
        bands.put(band2.getId(), band2);

        AlbumResponse album1 = new AlbumResponse(UUID.randomUUID().toString(), "Война", LocalDate.of(2001, 1, 1), "Punk Rock", "HCD-028", band1, LocalDateTime.now());
        albums.put(album1.getId(), album1);

        AlbumResponse album2 = new AlbumResponse(UUID.randomUUID().toString(), "Игра В Бисер Перед Свиньями", LocalDate.of(1999, 1, 1), "Punk Rock", "HCD-003", band1, LocalDateTime.now());
        albums.put(album2.getId(), album2);

        AlbumResponse album3 = new AlbumResponse(UUID.randomUUID().toString(), "Rainbow Nights", LocalDate.of(2020, 4, 30), "Psychedelic Rock", "CPR-020", band1, LocalDateTime.now());
        albums.put(album3.getId(), album3);
    }
}