package com.ceg.compiler;

import com.ceg.utils.Alerts;
import java.util.List;
import java.util.ListIterator;

public class CodeParser {

    /**
     * Dopisuje nową linię po każdym wywołaniu instrukcji 'cout' lub 'printf'.
     * @param lines Wejściowa lista linii kodu.
     * @return Liczba znalezionych instrukcji wypisania na ekran.
     */
	static public int addNewlineAfterEachCout(List<String> lines){
            int couts = 0;
            try{
                for (ListIterator<String> iterator = lines.listIterator(); iterator.hasNext() ;)
                {
                    String str = iterator.next();
                    if ((str.contains("cout <<") || str.contains("cout<<")) && (!str.contains("<<endl") && !str.contains("<< endl")))
                    {
                        iterator.add("cout<<endl;");
                        couts++;
                    }
                    else if(str.matches(".*printf\\(\\\"(?!.*\\\\n).*")){
                        iterator.add("printf(\"\\n\");");
                        couts++;
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());
                Alerts.parsingCodeErrorAlert();
            }
            return couts;
		
	}

    /**
     * Usuwa wszystie linie w których wystąpiła instrukcja wypisania 'cout' lub 'printf'.
     * @param lineNo Lista numerów linii, których nie należy usuwać.
     * @param code Lista linii kodu.
     */
	static public void deleteOtherCouts(List lineNo, List<String> code){
            try{
                for (ListIterator<String> iterator = code.listIterator(); iterator.hasNext() ;)
                {
                    String str = iterator.next();
                    if ((!lineNo.contains(iterator.nextIndex()-1))  && (str.contains("cout <<") || str.contains("cout<<") || str.contains("printf(")))
                    {
                        iterator.remove();
                    }
                }
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());
            }
        }
	
}
