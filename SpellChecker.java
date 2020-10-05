
/** @author MBaumgarten
 * this class reads a file supplied by the user and spell-checks each word against a dictionary
 * the user is offered to replace the word with one of the recommendations, leave the word as is, or type it in manually
 * once the user makes a selection, the program will write all words to a new file that is spell checked

 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SpellChecker {

    public static ArrayList<String> getSuggestionsForWord(String word) {
	return new ArrayList<>();
    }

    public static void main(String[] args) {
	// instance of WordRecommender class
	WordRecommender myRecom = new WordRecommender("engDictionary.txt");
	// ask user for a file to check
	Scanner FI = new Scanner(System.in);
	System.out.println("Please enter the name of a file that you would like to spell check!");
	String FileName = FI.nextLine();
	File user_test = new File(FileName);
	// generate new file to be spell-checked
	String outputFileName = FileName.substring(0, FileName.lastIndexOf('.')) + "_chk"
		+ FileName.substring(FileName.lastIndexOf('.'));
	File checked_output = new File(outputFileName);
	if (!checked_output.exists()) {
	    try {
		checked_output.createNewFile();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	try {
	    // reads user's file
	    Scanner user_scan = new Scanner(user_test);
	    while (user_scan.hasNext()) {
		String user_word = user_scan.next();
		// ignore a/i
		ArrayList<String> ignoredWords = new ArrayList<String>();
		ignoredWords.add("a");
		ignoredWords.add("i");
		if (ignoredWords.contains(user_word)) {
		    try {
			FileWriter fw_ignore = new FileWriter(checked_output, true);
			PrintWriter pw_ignore = new PrintWriter(fw_ignore);
			pw_ignore.print(user_word + " ");
			pw_ignore.flush();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		// handles misspelled word
		if (!myRecom.isWordInDict(user_word) && !ignoredWords.contains(user_word)) {
		    String misspelledWord = user_word;
		    System.out.println(misspelledWord);
		    boolean finishedCheckingWords = false;
		    while (!finishedCheckingWords) {
			// handles situation of having suggestions
			if ((myRecom.getWordSuggestions(misspelledWord, 3, .75, 3).size() > 0)) {
			    myRecom.prettyPrint(myRecom.getWordSuggestions(misspelledWord, 3, .75, 3));
			    System.out.println(
				    " Please enter 'r' to replace a word, 'a' to leave as is, or 't' to type a word manually. ");
			    Scanner in = new Scanner(System.in);
			    String choice = in.next();
			    // handles replacement option
			    if (choice.equals("r")) {
				try {
				    finishedCheckingWords = true;
				    String replaced_word = null;
				    boolean finished_replacing = false;
				    while (!finished_replacing) {
					System.out.println(
						"Please select the number of the word you would like to use as a replacement.");
					Scanner in4 = new Scanner(System.in);
					int replaced_number = in4.nextInt();
					if (replaced_number == 1) {
					    replaced_word = myRecom.getWordSuggestions(misspelledWord, 3, .75, 3)
						    .get(0);
					    finished_replacing = true;
					}
					if (replaced_number == 2) {
					    replaced_word = myRecom.getWordSuggestions(misspelledWord, 3, .75, 3)
						    .get(1);
					    finished_replacing = true;
					}
					if (replaced_number == 3) {
					    replaced_word = myRecom.getWordSuggestions(misspelledWord, 3, .75, 3)
						    .get(2);
					    finished_replacing = true;
					}

					if (replaced_number != 1 && replaced_number != 2 && replaced_number != 3) {
					    System.out.println("Error. Please enter a valid selection.");
					}
				    }
				    // writes the selected replacement word to the output file
				    FileWriter fw0 = new FileWriter(checked_output, true);
				    PrintWriter pw0 = new PrintWriter(fw0);
				    pw0.print(replaced_word + " ");
				    pw0.flush();

				} catch (IOException e) {
				    e.printStackTrace();
				}

			    }
			    // handles leaving the word as is option by writing the word to the output file
			    else if (choice.equals("a")) {
				try {
				    finishedCheckingWords = true;
				    FileWriter fw1 = new FileWriter(checked_output, true);
				    PrintWriter pw1 = new PrintWriter(fw1);
				    pw1.print(misspelledWord + " ");
				    pw1.flush();
				} catch (IOException e) {
				    e.printStackTrace();
				}
				// handles the option of manual typing by the writing the manually typed word to
				// the output file
			    } else if (choice.equals("t")) {
				try {
				    finishedCheckingWords = true;
				    FileWriter fw2 = new FileWriter(checked_output, true);
				    PrintWriter pw2 = new PrintWriter(fw2);
				    Scanner in6 = new Scanner(System.in);
				    System.out.println("Please type the word");
				    String manWrittenWord = in6.nextLine();
				    pw2.print(manWrittenWord + " ");
				    pw2.flush();
				} catch (IOException e) {
				    e.printStackTrace();
				}
			    } else {
				System.out.println("Error. That is not an option!");
			    }

			}
			// handles the case of not having any suggestions
			if ((myRecom.getWordSuggestions(misspelledWord, 3, .75, 3).size() == 0)) {
			    System.out.println(
				    "There are 0 suggestions. Please enter 'a' to leave the word as is, or 't' to type a word manually. ");
			    Scanner user_in = new Scanner(System.in);
			    String choice2 = user_in.next();
			    // leaves word as is and writes it to the output file
			    if (choice2.equals("a")) {
				try {
				    finishedCheckingWords = true;
				    FileWriter fw_choice2 = new FileWriter(checked_output, true);
				    PrintWriter pw_choice2 = new PrintWriter(fw_choice2);
				    pw_choice2.print(misspelledWord + " ");
				    pw_choice2.flush();
				} catch (IOException e) {
				    e.printStackTrace();
				}
				// takes manually typed word and adds it to the output file
			    } else if (choice2.equals("t")) {
				try {
				    finishedCheckingWords = true;
				    FileWriter fw_choice3 = new FileWriter(checked_output, true);
				    PrintWriter pw_choice3 = new PrintWriter(fw_choice3);
				    Scanner in7 = new Scanner(System.in);
				    System.out.println("Please type the word");
				    String manWrittenWord = in7.nextLine();
				    pw_choice3.print(manWrittenWord + " ");
				    pw_choice3.flush();
				} catch (IOException e) {
				    e.printStackTrace();
				}
				// handles invalid input error
			    } else {
				System.out.println("Error. That is not an option!");
			    }

			}
		    }
		}
		// writes the correctly spelled words to the output file
		else {
		    if (myRecom.isWordInDict(user_word)) {
			try {
			    FileWriter fw3 = new FileWriter(checked_output, true);
			    PrintWriter pw3 = new PrintWriter(fw3);
			    pw3.print(user_word + " ");
			    pw3.flush();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	    System.out.println("Your work here is done. Please check out your checked file " + checked_output
		    + ". Have a great day!");

	}

	catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }
}
