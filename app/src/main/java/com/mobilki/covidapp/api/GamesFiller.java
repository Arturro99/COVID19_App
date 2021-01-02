package com.mobilki.covidapp.api;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobilki.covidapp.api.model.Game;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class GamesFiller {
    static FirebaseAuth firebaseAuth;
    static FirebaseFirestore firebaseFirestore;
    static Game games[] = {
            new Game("Monopoly",
                    "Monopoly",
                    "Monopoly – klasyczna gra polegająca na handlu nieruchomościami. " +
                            "Wymyślona w Stanach Zjednoczonych w okresie Wielkiego Kryzysu przez Elizabeth Magie. " +
                            "Daje graczom szansę obracania wielkimi pieniędzmi i szybkiego wzbogacenia się na podstawie The Landlord's Game. " +
                            "Rozpoczynając od pola START, należy okrążać planszę, kupując i sprzedając nieruchomości, budując domy i hotele." +
                            " Za wejście na nieruchomości innych graczy płaci się czynsz. " +
                            "Sukces zależy od trafnych spekulacji, udanych inwestycji i mądrze przeprowadzonych transakcji. Gra została opatentowana 31 grudnia 1935 roku.",
                    "Monopoly is a board game currently published by Hasbro. " +
                            "In the game, players roll two six-sided dice to move around the game board, buying and trading properties, and developing them with houses and hotels. " +
                            "Players collect rent from their opponents, with the goal being to drive them into bankruptcy. " +
                            "Money can also be gained or lost through Chance and Community Chest cards, and tax squares; players can end up in jail, which they cannot move from until they have met one of several conditions. " +
                            "The game has numerous house rules, and hundreds of different editions exist, as well as many spin-offs and related media. " +
                            "Monopoly has become a part of international popular culture, having been licensed locally in more than 103 countries and printed in more than 37 languages.",
                    6, 2, 99, 8, "Strategia i taktyka, planszowe", "Strategy and tactics, board game", "60-180",
                    "https://www.amazon.com/Hasbro-Gaming-C1009-Monopoly-Classic/dp/B01MU9K3XU",
                    "https://images-na.ssl-images-amazon.com/images/I/81qy%2BMXuxDL._AC_SL1392_.jpg"),
            new Game("Chińczyk",
                    "Ludo",
                    "Jest to popularna gra planszowa, przeznaczona dla dwóch, trzech lub czterech osób. " +
                            "To doskonała gra towarzyska, idealna na niedzielne popołudnia czy deszczowe dni. " +
                            "Uwielbiają ją zarówno dzieci jak i dorośli. Polega na tym, aby jak najszybciej przejść czterema pionkami przez wszystkie pola rozmieszczone dookoła planszy. " +
                            "Ma być to przebycie drogi z pozycji początkowej (tzw. „baza”) do pozycji końcowej (tzw. „domek”) - szybciej niż pozostali gracze. " +
                            "Ten zawodnik, który jako pierwszy umieści swoje pionki w „domku”, ten wygrywa.",
                    "The game of Ludo originates in India as far back as 3300 BC. Easy to learn, this classic game is fun for children and adults alike. " +
                            "Ludo’s popularity has gone on to spawn variations such as Parcheesi and Sorry. With centuries of tradition behind Ludo, you will surely become a fan as well!" +
                            "2 to 4 players begin by placing their respective pieces in their bases. " +
                            "Each takes turns throwing the die, and the player with the highest roll plays first. " +
                            "The players to the left follow in turn going clockwise. On each player’s turn, the player rolls the die to determine a move. " +
                            "The goal of the game is to move all four of the player’s pieces clockwise once around the board, up the home column, and into the home triangle.",
                    4, 2, 99, 4, "Strategia, planszowe", "Strategy, board game", "30-90",
                    "https://www.amazon.com/MY-Traditional-Games-Ludo-Game/dp/B00N5TQ3CC",
                    "https://images-na.ssl-images-amazon.com/images/I/81gb3h91pML._AC_SL1500_.jpg"),
            new Game("Scrabble",
                    "Scrabble",
                    "Scrabble to gra słowna, w którą grać może od dwóch do 4 osób. Każda z nich na wstępie rozgrywki losuje 7 literek zapisanych na płytkach, które nie są widoczne dla innych graczy. " +
                            "Trzeba z nich układać wyrazy, ale biorąc pod uwagę słowa widniejące już na planszy. Jeden wyraz powstaje w oparciu o już istniejący. " +
                            "Do każdej litery przypisana jest określona ilość punktów, a niektóre pola na planszy są dodatkowo premiowane. " +
                            "Nie chodzi tutaj o to, aby ułożyć najdłuższy, możliwy wyraz, ale o to, aby miał on najwięcej punktów. " +
                            "Jeżeli w jednym ruchu gracz wykorzysta wszystkie, jakie posiada płytki z literkami, otrzymuje za to dodatkowe punkty. " +
                            "Scrabble to niełatwa gra, wymagająca logicznego myślenia i szerokiego zasobu słownictwa.",
                    "Scrabble is a word game in which two to four players score points by placing tiles, each bearing a single letter, onto a game board divided into a 15×15 grid of squares. " +
                            "The tiles must form words that, in crossword fashion, read left to right in rows or downward in columns, and be included in a standard dictionary or lexicon.",
                    4, 2, 99, 7, "Strategia, słownictwo, planszowe", "Strategy, vocabulary, board game", "25-50",
                    "https://www.amazon.com/Hasbro-Gaming-A8166-Scrabble-Game/dp/B00IL5XY9K",
                    "https://images-na.ssl-images-amazon.com/images/I/81OjLGNO5VL._AC_SL1500_.jpg"),
            new Game("Warcaby",
                    "Checkers",
                    "To bardzo znana i lubiana gra planszowa na całym świecie. Stanowi ona pewną pochodną od szachów, ale jest od nich zdecydowanie łatwiejsza. " +
                            "Warcaby zostały rozpowszechnione w Europie już w XI wieku. Istnieje wiele odmian tejże gry, aczkolwiek za dyscyplinę sportową uważane są warcaby polskie, określane również jako warcaby międzynarodowe. " +
                            "Z innych odmian wymienić można jeszcze takie jak warcaby czeskie, angielskie, klasyczne, dwuliniowe, kanadyjskie, rosyjskie, wybijanka i wiele innych. " +
                            "Gra przeznaczona jest dla wszystkich, bez względu na wiek. Uczy logicznego, strategicznego myślenia. Warcaby polegają na zbiciu wszystkich pionków przeciwnika. " +
                            "Nie zawsze jest to tak proste, jak wydaje się na pierwszy rzut oka. Pionki mogą poruszać się wyłącznie według ustalonych reguł. Istnieje komputerowa odmiana gry w warcaby, jest to Chinook, umożliwiający rozegranie partii warcabów z komputerem, choć pokonanie go jest niemożliwe.",
                    "Checkers is a  game for two players. It is played on an 8x8 checkered board, with a dark square in each player's lower left corner." +
                            "Pieces move only on dark squares which are numbered. Numbers are used to record the moves, for example, if Red moves from square 9 to square 13, then it is recorded as: 9-13" +
                            "Each player controls its own army of pieces (men)." +
                            "The goal in the checkers game is either to capture all of the opponent's pieces or to blockade them. If neither player can accomplish the above, the game is a draw.",
                    2, 2, 99, 5, "Strategia i taktyka, planszowe", "Strategy and tactics, board game", "30-120",
                    "https://www.amazon.com/Popular-Game-Draughts-Checkers/dp/B00IN6O2KW",
                    "https://images-na.ssl-images-amazon.com/images/I/915266VRjuL._AC_SL1500_.jpg"),
            new Game("Dobble",
                    "Dobble",
                    "Spójrz na pierwszą kartę, którą trzymasz w ręku oraz na tę, która leży na środku stołu. Znajdź na nich wspólny symbol, nazwij go i szybko pozbądź się swojej karty. " +
                            "Teraz następna!",
                    "Dobble is a game in which players have to find symbols in common between two cards. It was the UK’s best-selling game in 2018 and 2019." +
                            "The game uses a deck of 55 cards, each printed with eight different symbols. Any two cards always share one, and only one, matching symbol. The object of the game is to be the first to announce the drawing in common between two given cards.",
                    8, 2, 99, 5, "Pamięciowe, zręcznościowe, karciane", "Memory, physical skill, card game", "15",
                    "https://www.amazon.com/Asmodee-ASMDOBB01EN-Dobble-Card-Game/dp/B0031QBHMA",
                    "https://images-na.ssl-images-amazon.com/images/I/71bdqWTRM1L._AC_SL1500_.jpg"),
            new Game("Jungle Speed, złap totem!",
                    "Jungle Speed",
                    "Szalona gra towarzyska w zwariowanej odsłonie Nowe wydanie gry, w której liczy się zręczność, spostrzegawczość i szybkość! " +
                            "Gry, gdzie każdy stara się jak najszybciej pozbyć swoich kart - aby to zrobić, musi w odpowiednim momencie złapać drewniany, przepięknie zdobiony totem. " +
                            "Jeśli gracz złapie go w złym momencie, albo w ogóle tego nie zrobi - będzie musiał wziąć karty od swoich przeciwników!  Dlaczego ta gra jest taka rewelacyjna?! " +
                            "Pozwala siąść zupełnie obcym ludziom przy stole i razem świetnie się bawić! Nie wymaga żmudnego tłumaczenia zasad ani ich zapamiętywania " +
                            "Jest idealna na imprezę, pod namiot i rodzinne spotkanie, gdzie trudno znaleźć wspólny mianownik dla osób o różnych upodobaniach i w różnym wieku! Pozwala grać nieograniczonej liczbie osób!",
                    "In Jungle Speed, each player tries to grab the Totem first when the symbol on their card matches somebody else’s.\n" +
                            "Jungle Speed is an explosive game, easy to transport thanks to its travel bag, for quick on-the-go games!",
                    10, 2, 99, 7, "Imprezowe, karciane", "Party game, card game", "10-15",
                    "https://www.amazon.com/Asmodee-01JSUSASM-Jungle-Speed/dp/B005PXGTV6",
                    "https://images-na.ssl-images-amazon.com/images/I/71cfxGtJagL._AC_SL1200_.jpg"),
            new Game("Osadnicy z Catanu",
                    "Settlers of Catan",
                    "Gracze są osadnikami na niedawno odkrytej wyspie Catan. Każdy z nich przewodzi świeżo założonej kolonii i rozbudowuje ją stawiając na dostępnych obszarach nowe drogi i miasta. " +
                            "Każda kolonia zbiera dostępne dobra naturalne, które są niezbędne do rozbudowy osiedli.\n" +
                            "Gracz musi rozważnie stawiać nowe osiedla i drogi, aby zapewnić sobie dostateczny, ale zrównoważony dopływ zasobów, a jeśli ma ich nadmiar - prowadzić handel z innymi graczami " +
                            "sprzedając im owce, drewno, cegły, zboże lub żelazo a pozyskując od nich te zasoby, których ciągle mu brakuje.\n" +
                            "Pierwszy z graczy, który uzyska dziesięć punktów z wybudowanych przez siebie dróg, osiedli i specjalnych kart - wygrywa.",
                    "With 20 million copies sold since it was first published by Kosmos in 1995, Catan is the biggest best-seller among modern board games. " +
                            "Catan is widely regarded as the game that put board games back in the spotlight.\n" +
                            "In Catan, players develop a colony.\n" +
                            "The most interesting thing about Catan is the permanent interaction between players:\n" +
                            "- negotiation to exchange resources\n" +
                            "- competition to get the best spots first\n" +
                            "- thief management\n" +
                            "Catan is both a tactical and strategical game, with a strong \"social\" component.",
                    4, 3, 99, 10, "Strategia, ekonomia", "Strategy, economy", "90-120",
                    "https://www.amazon.com/Catan-Studios-cantan2017/dp/B00U26V4VQ",
                    "https://images-na.ssl-images-amazon.com/images/I/81%2Bokm4IpfL._AC_SL1500_.jpg"),
            new Game("Jenga",
                    "Jenga",
                    "Popularna gra Jenga powraca!\n" +
                            "- Ułóż wieżę, aż do samego nieba!\n" +
                            "- Wyciągaj klocki jedną ręką i układaj je na szczycie wieży.\n" +
                            "- Osoba, która przewróci wieżę, przegrywa.\n" +
                            "Gra zręcznościowa dla dzieci powyżej 6 roku życia.",
                    "Pull out a block, place it on top, but don't let the tower fall; This fun, challenging game is a great game for families and kids 6 and up.\n" +
                            "Available to play for 1 on more players - no friends around, no problem. Play Jenga solo. " +
                            "Practice stacking skills, building the tower and trying not to let it come tumbling down\n" +
                            "Great party game - liven up a party by bringing out the Jenga game. This classic block stacking game is simple, easy to learn, and makes a great birthday or holiday gift for adults and kids",
                    99, 2, 99, 6, "Zręcznościowe", "Physical skill", "5-15",
                    "https://www.amazon.com/Jenga-A2120EU4-Classic-Game/dp/B00ABA0ZOA",
                    "https://images-na.ssl-images-amazon.com/images/I/81OAWwX3djL._AC_SL1500_.jpg")
    };

    public static void fillDataBase() {
        Log.d(TAG, "fillDataBase: DDD");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference;
        Map<String, Object> game;
        for (Game value : games) {
            documentReference = firebaseFirestore.collection("games").document(value.getTitleEn());
            game = new HashMap<>();
            game.put("titlePl", value.getTitlePl());
            game.put("descriptionPl", value.getDescriptionPl());
            game.put("descriptionEn", value.getDescriptionEn());
            game.put("playersMin", value.getPlayersMin());
            game.put("playersMax", value.getPlayersMax());
            game.put("ageMin", value.getAgeMin());
            game.put("ageMax", value.getAgeMax());
            game.put("genrePl", value.getGenrePl());
            game.put("genreEn", value.getGenreEn());
            game.put("time", value.getTime());
            game.put("link", value.getLink());
            game.put("imgLink", value.getImgLink());
            documentReference.set(game);
        }
    }
}
