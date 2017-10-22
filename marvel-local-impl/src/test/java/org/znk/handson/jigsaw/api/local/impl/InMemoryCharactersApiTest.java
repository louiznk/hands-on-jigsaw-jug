package org.znk.handson.jigsaw.api.local.impl;

import org.junit.Before;
import org.junit.Test;
import org.znk.handson.jigsaw.api.CharactersApi;
import org.znk.handson.jigsaw.api.MarvelCharacter;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class InMemoryCharactersApiTest {
    CharactersApi api;

    @Before
    public void setUp() throws Exception {
        api = new InMemoryCharactersApi();
    }


    @Test
    public void shouldFindCaptainAmericaById() throws Exception {
        Optional<MarvelCharacter> captainAmerica = api.find(1009220);
        assertThat("I should find a character with the id 1009220", captainAmerica.isPresent(), is(true));
        assertThat("It should be Captain America", captainAmerica.get().name, is("Captain America"));
    }

    @Test
    public void shouldFindCaptainAmericaByNameContaining() throws Exception {
        List<MarvelCharacter> characters = api.findByNameContaining("captain a");
        assertThat("I should find one character with the name containing \"captain a\"", characters.size(),is (1));
        assertThat("It should have the id 1009220", characters.get(0).id, is(1009220));
    }

    @Test
    public void shouldFindAllCharacters() throws Exception {
        List<MarvelCharacter> characters = api.findAll();
        assertThat("I should find 178 characters", characters.size(), is(178));

    }

}