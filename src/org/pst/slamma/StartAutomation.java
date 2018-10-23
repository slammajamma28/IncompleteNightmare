package org.pst.slamma;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This main class will read in the list of participants and then all the work will continue in other classes.
 */
public class StartAutomation {

    public static void main (String args[]) {
        List<PSTMember> all_members = new ArrayList<>();
        String line;
        try {
            FileReader fr = new FileReader("participant_list");
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                List<String> id = Arrays.asList(line.split(","));
                PSTMember newMember = new PSTMember(id.get(0), id.get(1), Boolean.getBoolean(id.get(2)));
                all_members.add(newMember);
            }
            br.close();
        } catch (IOException e1) {
            System.out.println("File issue.");
            e1.printStackTrace();
        }

        // Sort members alphabetically
        Comparator<PSTMember> alphabeticalSolo = new Comparator<PSTMember>() {
            @Override
            public int compare(PSTMember o1, PSTMember o2) {
                return o1.getPstName().compareToIgnoreCase(o2.getPstName());
            }
        };

        System.out.println("\n ********************* \n");

        System.out.println("Exporting main trophy data to CSV...");

        Collections.sort(all_members, alphabeticalSolo);

        ExportData ed = new ExportData();
        ed.exportIndividuals(all_members);

        System.out.println("\n ********************* \n");
    }

}