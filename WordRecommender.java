
/**@author M Baumgarten
 * This class uses a dictionary to provide recommendations for a document that needs to be spell-checked
 * The class generates these recommendations by generating lists of words with the following characteristics:   
 * 1.length of the recommended word falls within a normal length range of the word in question
 * 2.the recommended word has a threshold "common percent" similarity with the word in question 
 * 3. the recommended word has a threshold "similarity metric" with the word in question
 * the top choices of words with these characteristics are displayed
 * 
 */

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class WordRecommender {
    private String filename;
    private ArrayList<String> dictionary;

    // constructor
    public WordRecommender(String filename) {
	dictionary = new ArrayList<String>();
	File dF = new File(filename);
	try {
	    Scanner dScanner = new Scanner(dF);
	    while (dScanner.hasNext()) {
		String word = dScanner.next();
		dictionary.add(word);
	    }
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    // method checks if the word in question in the dictionary
    public boolean isWordInDict(String word) {
	return dictionary.contains(word);
    }

    // method calculates the similarity metric between two words
    public double getSimilarityMetric(String word1, String word2) {
	double avgSim = 0;
	StringBuilder wordI = new StringBuilder(word1);
	StringBuilder wordII = new StringBuilder(word2);
	String word1rev = wordI.reverse().toString();
	String word2rev = wordII.reverse().toString();

	int numSimLeft = 0;
	for (int i = 0; i < word1.length(); i++) {
	    char word1_letter = word1.charAt(i);
	    for (int j = 0; j < word2.length(); j++) {
		char word2_letter = word2.charAt(j);
		if (i == j && word1_letter == word2_letter) {
		    numSimLeft++;

		}
	    }
	}
	int numSimRight = 0;
	for (int i = 0; i < word1rev.length(); i++) {
	    char word1_letter_rev = word1rev.charAt(i);
	    for (int j = 0; j < word2rev.length(); j++) {
		char word2_letter_rev = word2rev.charAt(j);
		if (i == j && word1_letter_rev == word2_letter_rev) {
		    numSimRight++;
		}
	    }
	}

	avgSim = (numSimLeft + numSimRight) / 2.0;
	return avgSim;
    }

    // method finds the common percent of two words
    private double findCommonPercent(String word1, String word2) {
	Set<Character> w1 = new HashSet<Character>();
	Set<Character> w2 = new HashSet<Character>();

	for (int i = 0; i < word1.length(); i++) {
	    w1.add(word1.charAt(i));
	}
	for (int j = 0; j < word2.length(); j++) {
	    w2.add(word2.charAt(j));
	}

	Set<Character> commonLetters = new HashSet<Character>(w1);
	Set<Character> all_Letters = new HashSet<Character>(w1);
	all_Letters.addAll(w2);
	commonLetters.retainAll(w2);
	double total_letters = all_Letters.size();
	double commonPercent = commonLetters.size() / total_letters;
	return commonPercent;
    }

    // method loops through the dictionary to come up with a list of top suggestions
    // based on length & common percent
    // them method then sorts the list of suggestions and generates a list of
    // suggestions with the top similarity metrics
    public ArrayList<String> getWordSuggestions(String word, int n, double commonPercent, int topN) {
	ArrayList<String> sug_AL = new ArrayList<String>();

	// loop through dictionary to generate list of suggestions
	for (int i = 0; i < dictionary.size(); i++) {
	    for (int j = 0; j < dictionary.get(i).length(); j++) {
		String diction_word = dictionary.get(i);
		if (word.length() - n <= diction_word.length() && diction_word.length() <= word.length() + n) {
		    double foundPercent = findCommonPercent(word, diction_word);
		    if (foundPercent >= (commonPercent)) {
			if (!word.equals(diction_word) && !sug_AL.contains(diction_word)) {
			    sug_AL.add(diction_word);
			}
		    }
		}
	    }
	}

	// sort the list of words by best similarity metric, store sorted ArrayList in
	// new ArrayList
	ArrayList<String> max_sorted = new ArrayList<String>();

	for (int i = 0; i < sug_AL.size(); i++) {
	    String topWord = sug_AL.get(0);
	    String sugWord_i = sug_AL.get(i);
	    double simMet_i = getSimilarityMetric(word, sugWord_i);
	    double simMet_top = getSimilarityMetric(word, topWord);
	    if (simMet_i > simMet_top) {
		if (!max_sorted.contains(topWord)) {
		    topWord = sugWord_i;
		}
	    }
	    for (int j = i + 1; j < sug_AL.size(); j++) {
		String sugWord_j = sug_AL.get(j);
		double simMet_j = getSimilarityMetric(word, sugWord_j);
		if (simMet_j > getSimilarityMetric(word, topWord)) {
		    if (!max_sorted.contains(topWord)) {
			topWord = sugWord_j;
		    }
		}

	    }

	    max_sorted.add(topWord);
	    sug_AL.remove(topWord);
	}

	// generate arrayList with topN suggestions, if topN is greater than the number
	// of available suggestions, return the whole original arrayList
	ArrayList<String> topWords_FINAL = new ArrayList<String>();
	for (int i = 0; i < topN; i++) {
	    if (topN > max_sorted.size()) {
		return max_sorted;
	    }
	    topWords_FINAL.add(max_sorted.get(i));
	}
	return topWords_FINAL;
    }

    // method takes a word and uses list of words to generate list of words with n
    // common letters
    public ArrayList<String> getWordsWithCommonLetters(String word, ArrayList<String> listOfWords, int n) {
	ArrayList<String> commonsList = new ArrayList<String>();
	ArrayList<ArrayList<Character>> word_lists = new ArrayList<ArrayList<Character>>();

	// generate arrayList of characters for word in question
	ArrayList<Character> chars_of_word = new ArrayList<Character>();
	for (int i = 0; i < word.length(); i++) {
	    if (!chars_of_word.contains(word.charAt(i))) {
		chars_of_word.add(word.charAt(i));
	    }
	}
	// generate arrayList of arrayList of characters for each of the words in the
	// list
	for (int i = 0; i < listOfWords.size(); i++) {
	    ArrayList<Character> letter_list = new ArrayList<Character>();
	    for (int j = 0; j < listOfWords.get(i).length(); j++) {
		if (!letter_list.contains(listOfWords.get(i).charAt(j))) {
		    letter_list.add(listOfWords.get(i).charAt(j));
		}
	    }
	    word_lists.add(letter_list);
	}
	// check if word in question contains any of the same characters as the words in
	// the list
	// if the word shares at least n amount of characters, add the word to an
	// arrayList of words with common letters
	ArrayList<Character> temp_list = new ArrayList<Character>();
	for (int i = 0; i < word_lists.size(); i++) {
	    temp_list = (word_lists.get(i));
	    int letter_count = 0;
	    for (int j = 0; j < chars_of_word.size(); j++) {
		if (temp_list.contains(chars_of_word.get(j))) {
		    letter_count++;
		}
	    }
	    if (letter_count >= n) {
		commonsList.add(listOfWords.get(i));
	    }
	}
	return commonsList;
    }

    // method prints the list recommended words in a neat display
    public String prettyPrint(ArrayList<String> list) {
	StringBuilder prettyBuilder = new StringBuilder();
	int i = 1;
	for (String str : list) {
	    prettyBuilder.append(i + ".");
	    i++;
	    prettyBuilder.append(str);
	    prettyBuilder.append("\n");
	}
	String prettyList = prettyBuilder.toString();
	System.out.println(prettyList);
	return prettyList;

    }

    public static void main(String[] args) {
	WordRecommender myWordRec = new WordRecommender("engDictionary.txt");
	ArrayList<String> sampleWords = new ArrayList<String>();
	sampleWords.add("can");
	sampleWords.add("and");
	sampleWords.add("dandy");
	sampleWords.add("ski");
	sampleWords.add("man");
	sampleWords.add("in");
	sampleWords.add("an");
	sampleWords.add("dandelion");
	sampleWords.add("us");
	sampleWords.add("Iceland");
	sampleWords.add("candyland");
	sampleWords.add("condor");

	System.out.println(myWordRec.getWordsWithCommonLetters("candor", sampleWords, 5));

    }

}
