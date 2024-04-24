import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class EncryptionFrame extends JFrame {

    JButton newKey;
    JButton getKey;
    JButton encrypt;
    JButton decrypt;
    JButton closing;
    JButton loadKey;
    JPanel buttonPanel;
    JLabel titleLabel;
    JLabel outputLabel;

    private ArrayList<Character> asciiList;
    private ArrayList<Character> encryptionKey;
    private char character;
    private char[] letters;

    String desktopPath = System.getProperty("user.home") + File.separator +"Desktop";
    File keyfile = new File(desktopPath + "keyfile.txt");

    public EncryptionFrame() {
        asciiList = new ArrayList<>();
        encryptionKey = new ArrayList<>();
        character = ' ';

        createAsciiTable();

        this.setTitle("Encryption Program");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
        this.setSize(1600, 900);
        this.setMinimumSize(new Dimension(1600,900));

        titleLabel = new JLabel("File encryption program");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        outputLabel = new JLabel("Choose the operation");
        outputLabel.setFont(new Font("Serif", Font.BOLD, 15));
        outputLabel.setForeground(Color.GREEN);
        outputLabel.setHorizontalAlignment(SwingConstants.CENTER);

        newKey = new JButton("Create New Key");
        newKey.setBackground(new Color(0,255,0));
        newKey.setForeground(new Color(0,0,102));
        newKey.setFocusable(false);
        newKey.addActionListener((e -> {

            if(!encryptionKey.isEmpty()){
                encryptionKey.clear();
            }
            encryptionKey = new ArrayList<>(asciiList);
            Collections.shuffle(encryptionKey);

            createKey();

        }));

        getKey = new JButton("Display Key");
        getKey.setBackground(new Color(0,255,0));
        getKey.setForeground(new Color(0,0,102));
        getKey.setFocusable(false);
        getKey.addActionListener((e) -> {

            if(encryptionKey.isEmpty()){
                outputLabel.setText("Key has yet to be created");
            }
            else{
                outputLabel.setText(encryptionKey.toString());
            }

        });

        loadKey = new JButton("Load Key");
        loadKey.setBackground(new Color(0,255,0));
        loadKey.setForeground(new Color(0,0,102));
        loadKey.setFocusable(false);

        loadKey.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            File newKeyFile;
            StringBuilder builder = new StringBuilder();
            String ls = System.lineSeparator();
                int response = fileChooser.showOpenDialog(null);
                if(response == JFileChooser.APPROVE_OPTION){

                    loadKey(fileChooser, builder, ls);

                }
                else{

                    outputLabel.setText("Key file has not been chosen");

                }
        });

        encrypt = new JButton("Encrypt file");
        encrypt.setBackground(new Color(0,255,0));
        encrypt.setForeground(new Color(0,0,102));
        encrypt.setFocusable(false);
        encrypt.addActionListener((e) -> {

            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);
            StringBuilder fileContent = new StringBuilder();

            if(response == JFileChooser.APPROVE_OPTION) {

                encryptFile(fileChooser, fileContent);

            }
            else{

                outputLabel.setText("File to encrypt has not been chosen");

            }
        });

        decrypt = new JButton("Decrypt file");
        decrypt.setBackground(new Color(0,255,0));
        decrypt.setForeground(new Color(0,0,102));
        decrypt.setFocusable(false);
        decrypt.addActionListener((e) -> {

            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);
            StringBuilder fileContent = new StringBuilder();

            if(response == JFileChooser.APPROVE_OPTION) {

                decryptFile(fileChooser, fileContent);

            }
            else{

                outputLabel.setText("You have not chosen file to decrypt");

            }
        });

        closing = new JButton("Closing");
        closing.setBackground(new Color(0,255,0));
        closing.setForeground(new Color(0,0,102));
        closing.setFocusable(false);
        closing.addActionListener((e) -> System.exit(0));

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(1600, 200));
        buttonPanel.setLayout(new GridLayout(1,6));

        buttonPanel.add(newKey);
        buttonPanel.add(getKey);
        buttonPanel.add(loadKey);
        buttonPanel.add(encrypt);
        buttonPanel.add(decrypt);
        buttonPanel.add(closing);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(outputLabel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void decryptFile(JFileChooser fileChooser, StringBuilder fileContent) {
        File fileToDecrypt = new File(fileChooser.getSelectedFile().getAbsolutePath());
        try {
            Scanner scanner = new Scanner(fileToDecrypt);
            while (scanner.hasNextLine()) {
                fileContent.append(scanner.nextLine()).append(System.lineSeparator());
            }
            letters = fileContent.toString().toCharArray();

            for(int i = 0; i < letters.length; i++){
                for(int j = 0; j < asciiList.size(); j++){
                    if(letters[i] == encryptionKey.get(j)){
                        letters[i] = asciiList.get(j);
                        break;
                    }
                }
            }
            scanner.close();
            fileToDecrypt.createNewFile();
            FileWriter fileWriter = new FileWriter("decrypt.txt");
            for(char c : letters){
                fileWriter.write(c);
            }
            fileWriter.close();
            outputLabel.setText("Decrypted file has been saved to Desktop");


        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void encryptFile(JFileChooser fileChooser, StringBuilder fileContent) {
        File fileToEncrypt = new File(fileChooser.getSelectedFile().getAbsolutePath());
        try {
            Scanner scanner = new Scanner(fileToEncrypt);
            while(scanner.hasNextLine()){
                fileContent.append(scanner.nextLine()).append(System.lineSeparator());
            }
            letters = fileContent.toString().toCharArray();

            for(int i = 0; i < letters.length; i++){
                for(int j = 0; j < asciiList.size(); j++){
                    if(letters[i] == asciiList.get(j)){
                        letters[i] = encryptionKey.get(j);
                        break;
                    }
                }
            }

            scanner.close();
            fileToEncrypt.createNewFile();
            FileWriter fileWriter = new FileWriter("encrypt.txt");
            for(char c : letters){
                fileWriter.write(c);
            }
            fileWriter.close();
            outputLabel.setText("Encrypted file has been saved to Desktop");
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadKey(JFileChooser fileChooser, StringBuilder builder, String ls) {
        File newKeyFile;
        newKeyFile  = new File(fileChooser.getSelectedFile().getAbsolutePath());
        if(!encryptionKey.isEmpty()) {
            encryptionKey.clear();
        }
        try {
            FileReader fr = new FileReader(newKeyFile);
            BufferedReader br = new BufferedReader(fr);
            String line1;
            while((line1 = br.readLine()) != null){
                builder.append(line1);
                builder.append(ls);
            }
            br.close();
            fr.close();
            for(char c : builder.toString().toCharArray()){
                encryptionKey.add(c);
            }
            outputLabel.setText("Key has been loaded");
            for(char c : encryptionKey){
                System.out.print(c);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void createKey() {
        try {
            keyfile.createNewFile();
            FileWriter writer = new FileWriter("keyfile.txt");
            for(char c: encryptionKey){
                writer.write(c);
            }
            writer.close();
            outputLabel.setText("Key has been saved to Desktop");
        } catch (IOException ex) {
            throw new RuntimeException();
        }
    }

    private void createAsciiTable() {
        for(int i = 32; i<127; i++){
            asciiList.add(character);
            character++;
        }
    }
}
