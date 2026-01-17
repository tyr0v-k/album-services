package albums.albums_application.controllers;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class RootController {
    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(BandController.class).getAllBands()).withRel("bands"),
                linkTo(methodOn(AlbumController.class).getAllAlbums(null, null, 0, 10)).withRel("albums"),
                Link.of("http://localhost:8080/swagger-ui/index.html", "documentation")
        );
        return rootModel;
    }
}