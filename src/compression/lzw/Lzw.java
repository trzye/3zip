package compression.lzw;

import compression.CompressionState;
import compression.lzw.dictionary.Dictionary;
import myutils.*;
import myutils.StringReader;

import java.io.*;
import java.util.Scanner;

/**
 * Created by shafe_000 on 2015-01-10.
 */
public class Lzw implements CompressionState {

    @Override
    public File getCompressedFrom(File in) {
        String input = IOUtils.getStringFromFile(in);
        myutils.StringReader stringReader = new StringReader(input);

        //Wypełnij słownik alfabetem źródła informacji.
        Dictionary dictionary = new Dictionary();
        //        c := pierwszy symbol wejściowy
        String c = stringReader.nextCharAsString();
        //Dopóki są dane na wejściu:
        while(stringReader.hasNextChar()) {
            //Wczytaj znak s.
            String s = stringReader.nextCharAsString();
            //        Jeżeli ciąg c + s znajduje się w słowniku, przedłuż ciąg c, tj. c := c + s
            if(dictionary.searchWord(c + s)){
                c = c+s;
            }
            //Jeśli ciągu c + s nie ma w słowniku, wówczas:
            else{
                //wypisz kod dla c (c znajduje się w słowniku)
                System.out.print(dictionary.getWordsCode(c) + " ");
                //dodaj ciąg c + s do słownika
                dictionary.addPosition(c + s);
                //przypisz c := s.
                c = s;
            }
        }
        //        Na końcu wypisz na wyjście kod związany c
        System.out.print(dictionary.getWordsCode(c));
        return null;
    }

    @Override
    public File getUncompressedFrom(File in) {
        try {
            Scanner scanner = new Scanner(in);

            //Wypełnij słownik alfabetem źródła informacji.
            Dictionary dictionary = new Dictionary();
            //        pk := pierwszy kod skompresowanych danych
            String pk = ((Integer)scanner.nextInt()).toString();
            //Wypisz na wyjście ciąg związany z kodem pk, tj. słownik[pk]
            System.out.print(dictionary.getCodesWord(pk));
            //Dopóki są jeszcze jakieś słowa kodu:
            while(scanner.hasNextInt()){
                //Wczytaj kod k
                String k = ((Integer)scanner.nextInt()).toString();
                //pc := słownik[pk] – ciąg skojarzony z poprzednim kodem
                String pc = dictionary.getCodesWord(pk);
                //Jeśli słowo k jest w słowniku, dodaj do słownika ciąg (pc + pierwszy symbol ciągu słownik[k]), a na wyjście wypisz cały ciąg słownik[k].
                if(dictionary.searchCode(k)){
                    dictionary.addPosition(pc + dictionary.getCodesWord(k).charAt(0));
                    System.out.print(dictionary.getCodesWord(k));
                }
                //        W przeciwnym razie (przypadek scscs) dodaj do słownika ciąg (pc + pierwszy symbol pc) i tenże ciąg wypisz na wyjście.
                else{
                    dictionary.addPosition(pc + pc.charAt(0));
                    System.out.print(pc + pc.charAt(0));
                }
                //pk := k
                pk = k;
            }
        }
        catch (FileNotFoundException e){
            System.err.print(e);
        }
        return null;
    }

    @Override
    public boolean searchAt(File in, String keywoard){
        return false;
    }
}
