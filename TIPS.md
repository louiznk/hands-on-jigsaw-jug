# TIPS

## Fichier module-info.java

Ce fichier permet de décrire un module, il doit être présent à la racine des sources du module (ici dans src/main/java).
En "standard" l'arborescence des projets avec plusieurs modules diffère de celle que l'on retrouve classiquement :

[Exemple d'Oracle](http://openjdk.java.net/projects/jigsaw/quick-start) pour les modules org.astro et com.greetings
```
    src/org.astro/module-info.java
    src/org.astro/org/astro/World.java
    src/com.greetings/com/greetings/Main.java
    src/com.greetings/module-info.java
```

Mais il reste possible d'avoir une arborescence plus classique (ce qui sera fait ici) :
```
org.astro
    src/java/module-info.java
    src/java/org/astro/World.java
com.greetings
    src/java/com/greetings/Main.java
    src/java/module-info.java
```


Si votre module se nomme "nom.de.mon.module" l'on retrouve ce nom dans la déclaration du module 
```
module nom.de.mon.module {
    // instructions 
}
```

Un module peut contenir des instructions pour :
 * Exposer les éléments publics d'un package : **exports**
```
exports nom.du.package
```
 * Permettre d'utiliser le module via de la reflexivité : **opens** 
```
opens nom.du.package
```
 * Permettre à un module d'utiliser un autre module : **required** 
```
required nom.du.module
```
 * Préciser que l'on fournit l'implementation d'une interface (pour pouvoir l'utiliser avec le ServiceLoader) : **provides** _ **with** _ 
```
provides une.interface
              with une.implementation
```
 * Pouvoir utiliser (grace au ServiceLoader) les implémentations d'une interface qui ont été définies via *provides _ with _* : **uses**
```
uses une.interface
```
En résumé

```
module nom.de.mon.module {
    exports nom.du.package.aa; // rend accessible les objets publique nom.du.package.aa du package
    exports nom.du.package.bb; // rend accessible les objets publique nom.du.package.bb du package
    
    opens nom.du.package.aa; // ouvre à la reflexivité le package nom.du.package.bb 
    
    required nom.dun.autre.module; // permet d'utiliser les objets publique du module nom.dun.autre.module
    required nom.dun.second.module; // permet d'utiliser les objets publique du module nom.dun.second.module
    
    provides nom.du.package.aa.InterfaceFoo with nom.du.package.cc.FooImpl; // fournit l'implementation nom.du.package.cc.FooImpl de nom.du.package.aa.InterfaceFoo pour le ServiceLoader
    
    uses nom.dun.autre.module.InterfaceBar; // Permettra grace au ServiceLoader de récupérer les implémentations "provided" de l'interface nom.dun.autre.module.InterfaceBar présentent au runtime dans le module-path
}

```


### Pour aller plus loin 

La gestion des dépendances peut être affinée :
 * **requires transitive** *ZZZ* : permet de spécifier dans le module la dépendances *ZZZ* sera récupéré par transitivité pour les utilisateurs de ce module
 * **requires static** *mysql.jdbc*: permet de gérer les dépendances optionnel au runtime mais nécessaire à la compilation (ex : une appli qui peut utiliser une base oracle ou mysql ou … on mets un requires static sur les drivers spécifiques)

La porté de ce qui est exposé peut être limiter à des modules spécifiques :
 * **exports** *process* **to** *dev.random* : expose les méthodes publique du package *process* (avec une portée limité au module *dev.random*)

Le module peut être completement ouvert 
 * **open** *module nom.de.mon.module* **{}**

Le JDK étant lui même modulaire pour connaitre la liste des modules et leurs dépendances :
 * La [javadoc](https://docs.oracle.com/javase/9/docs/api/overview-summary.html) (en htm5 avec un search, la révolution...)
 * Le [JDK Module Summary](http://cr.openjdk.java.net/~mr/jigsaw/jdk9-module-summary.html)
 * De nouvelles options et programmes dans la CLI du JDK 
```
java -p api-marvel/build/libs/api-marvel.jar --describe-module org.znk.handson.jigsaw.api
jar --file=api-marvel/build/libs/api-marvel.jar --describe-module
jdeps --module-path ./api-marvel/build/libs/api-marvel.jar -recursive ./marvel-local-impl/build/libs/marvel-local-impl.jar 
...
```

## [javac](https://docs.oracle.com/javase/9/tools/javac.htm#JSWOR627) et les modules
Quand vous construisez un module il est nécessaire de lui spécifier le chemin des modules **requires** qui ne sont pas fournis par le JDK (ceux packagés avec votre JDK sont par défaut accessibles).
```sh
javac ... --module-path PATH ...
# équivaut à
javac ... -p PATH ...
```
Avec **PATH** qui est la listes des modules (sous formes de JAR, mais peut aussi être des classes et des répertoires; utilise le séparateur du système). Pour retenir : les classes sont dans le classpath, les modules sont dans le modulepath 

Astuces : 
 * dans le cas de jar legacy il est possible de combiner classpath (legacy) et modulepath
 * dans le cas de multimodules avec des dépendances l'on peut utiliser la notion de module-source-path
 * dans le cas de tests unitaire les dépendances et les droits d'accès peuvent être données sans modifier le fichier module-info.java
```sh
# avec junit 4.12
javac ... --add-modules junit --add-reads nom.de.mon.module=junit ...
```
 * et le patch d'un module pour lui ajouter les classes de tests (les packages sont scélés)
```sh
javac ... --patch-module nom.de.mon.module=src/test/java... ...
```
 

## [java](https://docs.oracle.com/javase/9/tools/java.htm#JSWOR624) et les modules
Même idée que pour javac "les classes sont dans le classpath, les modules sont dans le modulepath". Pour lancer la main de la classe "ma.MainClass" du module "nom.de.mon.module" :
```sh
java ... --module-path PATH --module nom.de.mon.module/ma.MainClass ...
# équivaut à
java ... -p PATH -m nom.de.mon.module/ma.MainClass ...
```
Avec **PATH** qui est la listes des modules (sous formes de JAR, mais peut aussi être des classes et des répertoires; utilise le séparateur du système

Pour les tests unitaires il faut la aussi ouvrir à l'execution le (ou les) modules

```sh
# avec junit 4.12 & hamcrest
java ...  --module-path PATH
          --add-modules junit \
          --add-reads nom.de.mon.module=junit \
          --add-reads nom.de.mon.module=hamcrest.core \    
          --add-exports nom.de.mon.module/nom.du.package=junit \
          --add-exports nom.de.mon.module/nom.du.package=hamcrest.core \    
          --patch-module nom.de.mon.module=src/test/java... \
          ...
```
 * **--add-reads** : permet au runtime de rendre accessible au module  *nom.de.mon.module* un module qui n'est pas *required* (ici *junit*) 
 * **--add-exports** : permet au runtime de rendre exportable le package *nom.du.package* du module *nom.de.mon.module* à un autre module qui n'y aurait normalement pas accès (ici *junit*)
 * **--add-opens** : idem que **--add-exports** mais ouverture complète à la reflexivité
 * **--patch-module** : permet de patcher un module en lui ajoutant des classes non présentes initialement (permet d'avoir pour les tests unitaire des noms de package identique à ce qui est testé ce qui n'est pas autorisé par défaut)
 
## Génération de modules "linké" et JVM optimisé

Ces fonctions avancées permettent d’avoir une application packagée avec une JVM allégée ainsi qu’un graphe des dépendances déjà calculé et contrôler pour optimiser le temps de démarrage.

**Attention** cette fonctionnalité est limitée aux modules (on ne peut pas utiliser de jar legacy)

```sh
# generation des jar 
./gradlew jar

# generation de la JVM packager avec nos modules et ces dépendances (sans le serveur http car legacy)
jlink --module-path $JAVA_HOME/jmods:`pwd`/api-marvel/build/libs/api-marvel.jar:`pwd`/marvel-local-impl/build/libs/marvel-local-impl.jar \
--add-modules org.znk.handson.jigsaw.api.local.impl \
--output marveljre --compress=2

# affichage des modules de notre JRE optimisé
./marveljre/bin/java --list-modules
```

La liste des modules de notre JRE "light" est
 * java.base@9.0.1
 * org.znk.handson.jigsaw.api
 * org.znk.handson.jigsaw.api.local.impl

La version @9.0.1 est celle de la JVM "source". Si l'on voulait avoir un numéro de version il faut générer le jar avec l'option *--module-version* (mais cette information n'est malheureusement pas exploitée)