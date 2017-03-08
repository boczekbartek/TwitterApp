package twitterApp;

import java.io.File;
import java.io.IOException;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

public class SentimentClassifier {
	String[] categories;
	LMClassifier classe;

	public SentimentClassifier() {
		try {
			classe = (LMClassifier) AbstractExternalizable.readObject(new File("predwnlclass.txt"));
			categories = classe.categories();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SentimentClassifier(String path) {
		try {
			classe = (LMClassifier) AbstractExternalizable.readObject(new File(path));
			categories = classe.categories();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	public String classify(String text) {
		ConditionalClassification classification = classe.classify(text);
		return classification.bestCategory();
	}
}