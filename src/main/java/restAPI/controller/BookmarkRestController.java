package restAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restAPI.model.Bookmark;
import restAPI.repository.AccountRepositiry;
import restAPI.repository.BookmarkRepository;

import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/{userId}/bookmarks")
public class BookmarkRestController {

    private final BookmarkRepository bookmarkRepository;
    private final AccountRepositiry  accountRepositiry;

    @Autowired
    BookmarkRestController(AccountRepositiry accountRepositiry, BookmarkRepository bookmarkRepository){
        this.accountRepositiry = accountRepositiry;
        this.bookmarkRepository = bookmarkRepository;
    }

    @RequestMapping(method = POST)
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input){
        this.validateUser(userId);
        return this.accountRepositiry.findByUsername(userId).map(
                account -> {
                    Bookmark result = bookmarkRepository.save(new Bookmark(account, input.uri, input.descroption));
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                            .buildAndExpand(result.getId()).toUri());
                    return new ResponseEntity(null, httpHeaders, HttpStatus.CREATED);
                }
        ).get();
    }

    @RequestMapping(value = "/{bookmarkId}", method = GET)
    Bookmark reedBookmark(@PathVariable String userId, @PathVariable Long bookmarkId){
        this.validateUser(userId);
        return this.bookmarkRepository.findOne(bookmarkId);
    }

    @RequestMapping(method = GET)
    Collection<Bookmark> reafBookmarks(@PathVariable String userId){
        this.validateUser(userId);
        return this.bookmarkRepository.findByAccountUsername(userId);
    }

    private void validateUser(String userId) {
        this.accountRepositiry.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
    }

}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException{
    UserNotFoundException(String userId){
        super("could not find user '" + userId + "'.");
    }
}
