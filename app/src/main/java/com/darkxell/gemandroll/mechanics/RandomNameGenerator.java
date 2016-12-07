package com.darkxell.gemandroll.mechanics;

import java.util.Random;

/**
 * Created by Darkxell on 07/12/2016.
 */

public class RandomNameGenerator {

    private static final String[] NAMES = {
            "Ouwiw", "Sehi", "Sig", "Heechou", "Oufug", "Azej", "Ekepa", "Nuz", "Chegee", "Kusee", "Houf", "Fito", "Mog", "Urife", "Pel", "Icekee", "Vuze", "Ivaj", "Edusto", "Douki", "Gere", "Wic", "Amur", "Sup", "Wofu", "Ezew", "Guko", "Huc", "Ruho", "Oukem", "Ohevo", "Epeer", "Inor", "Ileew", "Gor", "Vahu", "Ruhi", "Ecimu", "Mel", "Weechu", "Oumaze", "Tos", "Stife", "Kuj", "Nedou", "Uguce", "Dorou", "Fouwee", "Neej", "Itoudee", "Soka", "Afidee", "Amaj", "Cad", "Ikacho", "Zebo", "Ukidi", "Osehee", "Etaje", "Wouk", "Nout", "Gic", "Houcee", "Astowa", "Feej", "Ceb", "Ogaha", "Von", "Etaf", "Cil", "Oudire", "Udeg", "Cukee", "Sovi", "Chabee", "Astihi", "Icobo", "Bouve", "Ejiwa", "Wup", "Chistou", "Jouhou", "Stukee", "Inouj", "Opob", "Liz", "Azab", "Couchee", "Ovucha", "Omast", "Imib", "Ipist", "Luwe", "Ivigi", "Rich", "Omucou", "Oujeet", "Arouj", "Tipou", "Roufe", "Ocheesou", "Ogudou", "Fest", "Outomo", "Vicu", "Houte", "Imowee", "Behu", "Ajoj", "Ifouzu", "Miz", "Istich", "Keew", "Reep", "Elus", "Chag", "Joudo", "Ijeema", "Ekeek", "Geew", "Ukuc", "Fevu", "Wape", "Cip", "Ipoun", "Ehij", "Ousteeca", "Noum", "Kif", "Enoud", "Oupago", "Owapu", "Ebutee", "Mowe", "Enur", "Ikomee", "Ewoc", "Outustou", "Uwustee", "Oreesti", "Ajosee", "Reenou", "Sog", "Ukop", "Cele", "Louc", "Roj", "Uchizo", "Cojee", "Bup", "Oudees", "Bucha", "Peej", "Steejee", "Icefou", "Aduk", "Awubu", "Hufou", "Alaree", "Oroum", "Stoumo", "Jesa", "Ouvas", "Teeb", "Chouta", "Koh", "Hufu", "Icuve", "Chadou", "Outoul", "Peba", "Oufeeze", "Teel", "Fouj", "Jus", "Doup", "Touwu", "Ouposu", "Ofum", "Uvosou", "Echuchi", "Par", "Homee", "Echas", "Ruh", "Gozo", "Stog", "Zoumo", "Nag", "Ougibe", "Suw", "Noba", "Ouzeeba", "Ijouv", "Zimou", "Abaci", "Gadu", "Uwast", "Ekeh", "Ofehe"
    };

    public static String getRandomName(){
        Random r = new Random();
        int index = r.nextInt(NAMES.length);
        return NAMES[index];
    }
}
