package com.example.user.mycontactmanager;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 27-03-2016.
 */
public class FileOperations {
    String message1 = "there";
    public boolean createPath(File path) throws IOException {
        Log.i("create path :", "True");
        File directory = new File(path, "Contact");
        File file = new File(path, "Contact.txt");

        if (!directory.exists()) {
            // Creates a dir. referenced by this file
            directory.mkdir();
        }
        if (!file.exists()) {
            // Creates a file
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileIsEmpty(file)) {
            Log.i("Is file empty ? :", "True");

            String lineWrite = "Srabonti|Chak|469-1234|email4@.com|2016-3-27|";
            System.out.println(lineWrite);
            saveToDisk(path, lineWrite);
            lineWrite = "Sayanti|Cha|469-4567|email4@.com|2016-3-28|";
            saveToDisk(path, lineWrite);

        }
        return true;
    }

    //function checks if the disk is empty
    private boolean fileIsEmpty(File file) throws IOException {
        Boolean flag = true;
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            String delimited[] = line.split("\\|");
            if (delimited.length >= 1) {
                flag = false;
            }
        }
        reader.close();
        fileReader.close();
        return flag;

    }

    public boolean saveToDisk(File path, String line) throws IOException {
        File file = new File(path, "Contact.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        writer.write(line);
        writer.newLine();

        fileWriter.flush();
        writer.flush();
        writer.close();
        fileWriter.close();
        return true;
    }

    public List<Person> readFile(File path) throws IOException {
        //Toast.makeText(FileOperations.this, message1, Toast.LENGTH_LONG).show();

        File file = new File(path, "Contact.txt");
        List<Person> personList = new ArrayList<Person>();
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;

        while ((line = reader.readLine()) != null) {
            //Log.i("data: ", line);
            String [] delimited = line.split("\\|");

            Person p1 = new Person(delimited[0], delimited[1], delimited[2], delimited[3], delimited[4]);
            personList.add(p1);

        }
        reader.close();
        fileReader.close();
        return personList;
    }

    public boolean writeList(File file, List<Person> listToWrite) throws IOException {

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        fileWriter = new FileWriter(file);
        bufferedWriter = new BufferedWriter(fileWriter);
        for (Person p : listToWrite) {
            bufferedWriter.write(p.getFirstName() + "|" + p.getLastName() + "|" + p.getPhoneNo() + "|" + p.getEmail() + "|" + p.getDate() + "|");
            bufferedWriter.newLine();
        }

        fileWriter.flush();
        bufferedWriter.flush();
        bufferedWriter.close();
        fileWriter.close();
        Log.i("Whole list written : ", "success");
        return true;
    }

    public boolean deleteRecord(File path, String oldFName, String oldLName, String oldPNo, String oldEmail, String oldDate) throws IOException {
        File file = new File(path, "Contact.txt");
        boolean isDeleted = false;
        List<Person> list = new ArrayList<Person>();
        list = readFile(path);
        file.delete();
        file.createNewFile();
        for(Person p: list){
            if(p.getFirstName().equalsIgnoreCase(oldFName) && p.getLastName().equalsIgnoreCase(oldLName) && p.getPhoneNo().equalsIgnoreCase(oldPNo) && p.getEmail().equalsIgnoreCase(oldEmail) && p.getDate().equalsIgnoreCase(oldDate))
            {
                list.remove(p);
                isDeleted = true;
                break;
            }
        }
        if(isDeleted){
            boolean iswritten = writeList(file, list);
            isDeleted = iswritten;
        }
    return isDeleted;
    }
}