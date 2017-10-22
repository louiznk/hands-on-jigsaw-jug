package org.znk.handson.jigsaw.api;

import java.util.List;
import java.util.Optional;

public interface CharactersApi {

    /**
     * Find the Character by is id
     *
     * Data provided by Marvel. © 2014 Marvel
     * https://developer.marvel.com
     * @param id of the character
     * @return the character if the id exist
     */
    Optional<MarvelCharacter> find(int id);

    /**
     * Find all the Characters
     * Data provided by Marvel. © 2014 Marvel
     * https://developer.marvel.com
     * @return All the characters
     */
    List<MarvelCharacter> findAll();


    /**
     * Find all the Characters containing name ignoring case
     * @param name the name (or part of the name) for searching
     * @return All the characters that contains the name
     */
    List<MarvelCharacter> findByNameContaining(String name);
}
