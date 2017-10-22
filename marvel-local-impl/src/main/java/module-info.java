module org.znk.handson.jigsaw.api.local.impl {
    requires transitive org.znk.handson.jigsaw.api;

    provides org.znk.handson.jigsaw.api.CharactersApi
            with org.znk.handson.jigsaw.api.local.impl.InMemoryCharactersApi;
}