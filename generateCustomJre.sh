#!/bin/sh
jlink --module-path $JAVA_HOME/jmods:`pwd`/api-marvel/build/libs/api-marvel.jar:`pwd`/marvel-local-impl/build/libs/marvel-local-impl.jar:`pwd`/marvel-cli/build/libs/marvel-cli.jar \
--add-modules org.znk.handson.jigsaw.cli,org.znk.handson.jigsaw.api.local.impl \
--compress=2 \
--launcher marvel=org.znk.handson.jigsaw.cli/org.znk.handson.jigsaw.cli.Application \
--output marveljre