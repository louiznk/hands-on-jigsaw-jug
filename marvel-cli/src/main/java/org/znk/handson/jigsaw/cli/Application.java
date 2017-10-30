package org.znk.handson.jigsaw.cli;

import org.znk.handson.jigsaw.api.CharactersApi;
import org.znk.handson.jigsaw.api.MarvelCharacter;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

public class Application {
    private static CharactersApi charactersApi = ServiceLoader.load(CharactersApi.class).findFirst().get();
    public static void main(String[] args) {
        if(null == args || args.length < 1) {
            System.out.println("Args required : desc {id} | search {containing}");

            System.exit(1);
        }
        final String action = args[0].toLowerCase().trim();
        final String arg = args[1].trim();

        switch (action) {
            case "desc" :
                Optional<MarvelCharacter> character = charactersApi.find(Integer.valueOf(arg));
                character.ifPresentOrElse(c ->
                   System.out.printf("%s\n-------------------------\n%s", c.name, c.description)
                , () ->
                    System.out.printf("Find no characters with the id <%s>\n", arg)
                );
                break;
            case "search" :
                final List<MarvelCharacter> characters = charactersApi.findByNameContaining(arg);
                System.out.printf("Find %d character(s)\n", characters.size());
                characters.forEach(c -> System.out.printf("\t%d - %s\n", c.id, c.name));
                break;
            default:
                System.out.println("Unknow arg");
                System.exit(1);
        }

    }
}
