package twitterApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.LMClassifier;
import com.aliasi.classify.NaiveBayesClassifier;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.tokenizer.RegExTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;
import com.aliasi.util.Files;

public class FileTools {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		// divideIntoFIles("a");

		System.exit(0);
	}

	public static String divideIntoFIles(String fileName) throws IOException {
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		int i = 0;
		String line = new String("");
		while (sc.hasNextLine()) {
			line = line + sc.nextLine();
			if (line.endsWith("1")) {
				System.out.println("That's positive!!!");
				System.out.println(line);
				System.out.println("----------------------\n");
				Integer a = new Integer(line.substring(line.length() - 1));
				line = line.substring(0, line.length() - 2);
				String filename = new String(String.valueOf(i));
				PrintWriter writer = new PrintWriter("data\\pos\\" + filename + "amazon.txt", "UTF-8");
				writer.println(line);
				writer.close();
				line = "";
			} else if (line.endsWith("0")) {
				System.out.println("Thats' negative!!!");
				System.out.println(line);
				System.out.println("----------------------\n");

				// jest negatywny
				Integer a = new Integer(line.substring(line.length() - 1));
				line = line.substring(0, line.length() - 2);
				String filename = new String(String.valueOf(i));
				PrintWriter writer = new PrintWriter("data\\neg\\" + filename + "amazon.txt", "UTF-8");
				writer.println(line);
				writer.close();
				line = "";
			}

			i++;
		}
		sc.close();
		return "\\data";

	}

	public static String train(String fileName) throws IOException, ClassNotFoundException {
		File trainDir;
		String[] categories;
		LMClassifier<?, ?> classi;
		trainDir = new File("C:\\Users\\Bartek\\Documents\\PROGRAMY\\Java\\Workspace\\TwitteAnalyseApp\\data");
		categories = trainDir.list();
		System.out.println(categories);
		int nGram = 7; // the nGram level, any value between 7 and 12 works
		classi = NaiveBayesClassifier.createNGramProcess(categories, nGram);

//		String regex = "[a-zA-Z]+|[0-9]+|\\S";
//		TokenizerFactory tf = new RegExTokenizerFactory(regex);
//		char[] cs = "abc de 123. ".toCharArray();
//		Tokenizer tokenizer = tf.tokenizer(cs, 0, cs.length);

		classi = DynamicLMClassifier.createNGramProcess(categories, nGram);
		for (int i = 0; i < categories.length; ++i) {
			String category = categories[i];
			Classification classification = new Classification(category);
			File file = new File(trainDir, categories[i]);
			File[] trainFiles = file.listFiles();
			for (int j = 0; j < trainFiles.length; ++j) {
				System.out.println(trainFiles.length - j + " left");
				File trainFile = trainFiles[j];
				String review = Files.readFromFile(trainFile, "ISO-8859-1");
				Classified<String> classified = new Classified<String>(review, classification);
				((ObjectHandler<Classified<String>>) classi).handle(classified);
			}
		}
		String className = new String("userClassifier.txt");
		AbstractExternalizable.compileTo((Compilable) classi, new File(className));
		return className;
	}
}