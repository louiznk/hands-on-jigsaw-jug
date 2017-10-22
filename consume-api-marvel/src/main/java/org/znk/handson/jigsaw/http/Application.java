package org.znk.handson.jigsaw.http;


import org.znk.handson.jigsaw.api.CharactersApi;
import org.znk.handson.jigsaw.api.MarvelCharacter;
import spark.Route;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.port;

public class Application {
    private static CharactersApi charactersApi = ServiceLoader.load(CharactersApi.class).findFirst().get();

    public static void main(String[] args) {
        port(8080);

        get("/characters", getRouteOfCharacters());

        get("/characters/:id", getRouteOfCharacterById());

        get("/characters/search/:name", getRouteOfCharactersByName());

    }

    private static Route getRouteOfCharactersByName() {
        return (request, response) ->
        {
            response.type("application/json");
            final List<MarvelCharacter> characters = charactersApi.findByNameContaining(request.params(":name"));
            if (characters.isEmpty()) {
                response.type("application/json");
                response.status(404);
                return "{\"message\":\"Not Found\"}";
            } else
                return marvelCharactersToJson(characters);
        };
    }

    private static Route getRouteOfCharacterById() {
        return (request, response) ->
        {
            Optional<MarvelCharacter> character = charactersApi.find(Integer.valueOf(request.params(":id")));
            if (character.isPresent()) {
                response.type("application/json");
                return marvelCharacterToJson(character.get());
            } else {
                response.type("application/json");
                response.status(404);
                return "{\"message\":\"Not Found\"}";
            }
        };
    }

    private static Route getRouteOfCharacters() {
        return (request, response) ->
        {
            response.type("application/json");
            return marvelCharactersToJson(charactersApi.findAll());
        };
    }


    private static String marvelCharacterToJson(MarvelCharacter character) {
        StringBuffer sb = new StringBuffer()
                .append("{\n")
                .append("  \"id\": ").append(character.id).append(",\n")
                .append("  \"name\": \"").append(character.name).append("\",\n")
                .append("  \"description\": \"").append(character.description).append("\"\n")
                .append("}");
        return sb.toString();
    }

    private static String marvelCharactersToJson(List<MarvelCharacter> characters) {
        return
                "{ \"characters\": [\n".concat(
                        characters.stream().map(c -> marvelCharacterToJson(c))
                                .collect(Collectors.joining(",\n")))
                        .concat("\n] }\n");

    }
}
