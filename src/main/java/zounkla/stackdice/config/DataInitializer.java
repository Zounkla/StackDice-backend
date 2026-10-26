package zounkla.stackdice.config;

import org.jspecify.annotations.NullMarked;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zounkla.stackdice.model.Category;
import zounkla.stackdice.model.Compatibility;
import zounkla.stackdice.model.entity.Technology;
import zounkla.stackdice.repository.TechnologyRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TechnologyRepository techRepo;

    public DataInitializer(TechnologyRepository techRepo) {
        this.techRepo = techRepo;
    }

    @Override
    @NullMarked
    public void run(String... args) {
        // Si la base contient déjà des données, on ne fait rien (évite les doublons)
        if (techRepo.count() > 0) {
            return;
        }

        List<Technology> defaultTechnologies = List.of(
                // INTERFACES WEB (FRONT)
                new Technology("Vue.js 3", Category.FRONT, Compatibility.WEB, "Framework JavaScript progressif et réactif."),
                new Technology("React", Category.FRONT, Compatibility.WEB, "Librairie JavaScript populaire basée sur les composants."),
                new Technology("Angular", Category.FRONT, Compatibility.WEB, "Framework TypeScript complet et robuste de Google."),
                new Technology("SvelteKit", Category.FRONT, Compatibility.WEB, "Framework ultra-rapide sans DOM virtuel."),
                new Technology("HTML5 / CSS3 / JS Vanilla", Category.FRONT, Compatibility.WEB, "Le trio classique, sans aucun framework ni surcouche."),
                new Technology("HTMX + Tailwind CSS", Category.FRONT, Compatibility.WEB, "Approche moderne Low-JS combinée à du CSS utilitaire."),
                new Technology("Thymeleaf", Category.FRONT, Compatibility.WEB, "Moteur de template côté serveur, idéal avec Spring Boot."),
                new Technology("Blade", Category.FRONT, Compatibility.WEB, "Moteur de template côté serveur, natif pour Laravel (PHP)."),

                // LOGIQUE API / SERVEUR (BACK)
                new Technology("Spring Boot (Java)", Category.BACK, Compatibility.WEB, "L'incontournable écosystème Java pour les API robustes."),
                new Technology("NestJS (TypeScript)", Category.BACK, Compatibility.WEB, "Framework Node.js ultra-structuré inspiré d'Angular."),
                new Technology("Express.js (JavaScript)", Category.BACK, Compatibility.WEB, "Le framework Node.js minimaliste, rapide et flexible."),
                new Technology("Symfony (PHP)", Category.BACK, Compatibility.WEB, "Framework PHP professionnel, structuré et hautement évolutif."),
                new Technology("Laravel (PHP)", Category.BACK, Compatibility.WEB, "Framework PHP moderne avec une syntaxe élégante et expressive."),
                new Technology("Django (Python)", Category.BACK, Compatibility.WEB, "Framework Python batteries incluses axé sur la rapidité de dev."),
                new Technology("FastAPI (Python)", Category.BACK, Compatibility.WEB, "Framework Python moderne, ultra-rapide et basé sur les type hints."),
                new Technology("ASP.NET Core (C#)", Category.BACK, Compatibility.WEB, "La solution haute performance multiplateforme de Microsoft."),
                new Technology("Go (Standard Library)", Category.BACK, Compatibility.WEB, "Utilisation pure du langage Go et de ses paquets natifs pour l'API."),
                new Technology("PHP Vanilla", Category.BACK, Compatibility.WEB, "Du bon vieux PHP pur à l'ancienne, sans framework."),

                // APPS MOBILES (MOBILE)
                new Technology("Flutter (Dart)", Category.MOBILE, Compatibility.MOBILE, "Framework cross-platform de Google pour des UI natives et fluides."),
                new Technology("React Native", Category.MOBILE, Compatibility.MOBILE, "Permet de build des apps mobiles natives en utilisant React et JavaScript."),
                new Technology("SwiftUI (iOS)", Category.MOBILE, Compatibility.MOBILE, "Le framework natif et moderne d'Apple pour les applications iOS."),
                new Technology("Jetpack Compose (Android)", Category.MOBILE, Compatibility.MOBILE, "Le kit d'outils moderne de Google pour concevoir des UI Android natives en Kotlin."),
                new Technology("Ionic / Capacitor", Category.MOBILE, Compatibility.MOBILE, "Approche hybride pour transformer votre code web en application mobile."),

                // BASES DE DONNÉES EN LOCAL (DATABASE)
                new Technology("PostgreSQL", Category.DATABASE, Compatibility.LOCAL, "Base de données relationnelle open-source ultra-puissante en conteneur Docker."),
                new Technology("MySQL / MariaDB", Category.DATABASE, Compatibility.LOCAL, "Le grand classique des bases relationnelles en conteneur Docker."),
                new Technology("MongoDB", Category.DATABASE, Compatibility.LOCAL, "Base de données NoSQL orientée documents pour stocker du JSON."),
                new Technology("Redis", Category.DATABASE, Compatibility.LOCAL, "Stockage clé-valeur en mémoire, parfait pour du cache local."),
                new Technology("SQLite", Category.DATABASE, Compatibility.LOCAL, "Base de données SQL embarquée dans un simple fichier local."),

                // LOGICIELS DESKTOP (DESKTOP)
                new Technology("JavaFX (Java)", Category.DESKTOP, Compatibility.DESKTOP, "Plateforme moderne pour créer des applications de bureau riches en Java."),
                new Technology("Swing (Java)", Category.DESKTOP, Compatibility.DESKTOP, "La vieille école de l'interface Java, look rétro garanti."),
                new Technology("Electron (JS/HTML/CSS)", Category.DESKTOP, Compatibility.DESKTOP, "Permet de packager des technologies Web dans une application Desktop."),
                new Technology("Tauri (Rust / Web)", Category.DESKTOP, Compatibility.DESKTOP, "Alternative ultra-légère à Electron propulsée par Rust."),
                new Technology("Qt (C++)", Category.DESKTOP, Compatibility.DESKTOP, "Le framework industriel de référence pour les logiciels C++ natifs."),

                // DEFIS ET CONTRAINTES (CHALLENGE)
                new Technology("Le Minimaliste", Category.CHALLENGE, Compatibility.ANY, "Interdiction d'utiliser plus de 2 librairies tierces."),
                new Technology("Le Puriste (Vanilla CSS)", Category.CHALLENGE, Compatibility.ANY, "Pas de framework CSS (Tailwind/Bootstrap). Tout en CSS pur."),
                new Technology("Le Codeur d'Élite", Category.CHALLENGE, Compatibility.ANY, "Interdiction d'utiliser un ORM (pas d'Hibernate). Requêtes écrites en SQL brut."),
                new Technology("L'Artisan du Temps Réel", Category.CHALLENGE, Compatibility.ANY, "L'application doit intégrer une communication instantanée via des WebSockets."),
                new Technology("L'Inspecteur des Travaux Finis", Category.CHALLENGE, Compatibility.ANY, "Obligation de coder le projet en TDD avec une couverture de tests minimale de 80%."),
                new Technology("L'Architecte Propre", Category.CHALLENGE, Compatibility.ANY, "Découpler totalement le code métier en appliquant l'Architecture Hexagonale."),
                new Technology("Le Vampire", Category.CHALLENGE, Compatibility.ANY, "L'application doit obligatoirement posséder un Mode Sombre futuriste par défaut.")
        );

        // Sauvegarde de tout le catalogue en base via le Repository
        techRepo.saveAll(defaultTechnologies);
        System.out.println(">> Catalogue StackDice initialise avec succes avec l'index unique !");
    }
}
