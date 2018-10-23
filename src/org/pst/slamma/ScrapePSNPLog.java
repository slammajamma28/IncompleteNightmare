package org.pst.slamma;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;
import org.omg.PortableInterceptor.ACTIVE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrapePSNPLog {

    private DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy");
    private DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy h:mm:ss a");
//    private LocalDate TEST_DATE = LocalDate.of(2018, 10, 23);

    public List<Trophy> trophiesFromLog(String psn) {
        List<Trophy> all_trophies = new ArrayList<>();
        int page = 1;
        return trophiesFromLog(psn, all_trophies, page);
    }

    public List<Trophy> trophiesFromLog(String psn, List<Trophy> all_trophies, int page) {
        Element table;
        Elements all_rows;

        Element e_current_log_num;
        Element e_current_log_date;
        Element e_current_log_time;
        String trophy_date_noformat;
        String trophy_time_noformat;
        String trophy_date;
        String trophy_time;
        LocalDateTime current_trophy_date;
        int current_log_num;
        String url;
        if (page == 1) {
            url = "https://psnprofiles.com/" + psn + "/log";
        }
        else {
            url = "https://psnprofiles.com/" + psn + "/log?page=" + page;
        }
        // Begin jaunt stuff
        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(url);

            table = userAgent.doc.findFirst("<div class='box'>");
            all_rows = table.findEvery("<tr>");

            for (Element row : all_rows) {
                // Find date
                e_current_log_date = row.findFirst("<span class='typo-top-date'>");
                e_current_log_time = row.findFirst("<span class='typo-bottom-date'>");

                // Parse date
                trophy_date_noformat = e_current_log_date.findFirst("<nobr>").getChildText().trim();
                trophy_time = e_current_log_time.findFirst("<nobr>").getChildText().trim();
                trophy_date = trophy_date_noformat.replaceAll("st","").replaceAll("nd","").replaceAll("rd", "").replaceAll("th","").concat(" " + trophy_time);
                current_trophy_date = LocalDateTime.parse(trophy_date, DATE_TIME_FORMAT);

                LocalDateTime ACTIVE_TIME = LocalDateTime.of(2018, 10, 23, 2, 30, 0);
                LocalDateTime INACTIVE_TIME;

                if (psn.equals("leptonic")) {
                    INACTIVE_TIME = LocalDateTime.of(2018, 10, 23, 16, 28, 0);
                } else if (psn.equals("MikeKeese")) {
                    INACTIVE_TIME = LocalDateTime.of(2018, 10, 23, 3, 31, 0);
                } else if (psn.equals("Dolken_swe")) {
                    INACTIVE_TIME = LocalDateTime.of(2018, 10, 23, 16, 48, 0);
                } else{
                    INACTIVE_TIME = LocalDateTime.of(2018, 10, 25, 2, 30, 0);
                }
                if (current_trophy_date.isAfter(INACTIVE_TIME)) {
//                       System.out.println("Haven't reached it yet...");
                } else if (current_trophy_date.isAfter(ACTIVE_TIME)) {
                    all_trophies.add(getTrophyInfo(row,psn));
                } else if (current_trophy_date.isBefore(ACTIVE_TIME)) {
                    return all_trophies;
                }
            }
            trophiesFromLog(psn, all_trophies, ++page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return all_trophies;
    }

    private Trophy getTrophyInfo(Element row, String psn) {
        Trophy trophy = new Trophy();
        try {
            Elements info_things = row.findEach("<td>");

            // Log number
            Element log = info_things.getElement(4);
//            String lognum = log.innerText().replaceAll("#","").replaceAll(",","").trim();
            String lognum = log.getTextContent().replaceAll("#","").replaceAll(",","").trim();
            trophy.setLog_num(Integer.parseInt(lognum));

            // Time stamp
            Element datestamp = info_things.getElement(5);
            String trophy_date = datestamp.findFirst("<span class='typo-top-date'>").findFirst("<nobr>").getChildText().trim();
            String trophy_time = datestamp.findFirst("<span class='typo-bottom-date'>").findFirst("<nobr>").getChildText().trim();
//            System.out.println("Trophy date = " + trophy_date);
//            System.out.println("Trophy time = " + trophy_time);
            String date_stamp = trophy_date.replaceAll("st","").replaceAll("nd","").replaceAll("rd", "").replaceAll("th","").concat(" " + trophy_time);
            LocalDateTime trophy_stamp = LocalDateTime.parse(date_stamp, DATE_TIME_FORMAT);
            trophy.setTimestamp(trophy_stamp);

            // Owners
            Element owners = info_things.getElement(7);
            String number_owned = owners.findFirst("<span class='typo-top'>").getChildText().replaceAll(",", "").trim();
//            System.out.println("Number of owners = " + number_owned);
            trophy.setOwners(Integer.parseInt(number_owned));

            // Rarity
            Element rarity = info_things.getElement(8);
            String rarity_percent = rarity.findFirst("<span class='typo-top'>").getChildText();
//            System.out.println("Rarity = " + rarity_percent + " (" + rarity_type + ")");
            trophy.setRarity_percentage(Double.parseDouble(rarity_percent.replaceAll("%", "")));

        } catch (Exception e) {
            e.printStackTrace();
        }
//        printTrophy(trophy);
        return trophy;
    }

//    private void writeTrophyToFile(Trophy trophy, String user) {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd");
//        String filename = ".\\overall\\" + user + ".csv";
//        new File(".\\overall\\").mkdirs();
//        File newFile = new File(filename);
//        try {
//            if (!newFile.exists()) {
//                newFile.createNewFile();
//            }
//            FileWriter fw = new FileWriter(filename,true);
//            fw.write(trophy.getGame_id() + "|" + trophy.getTrophy_title() + "|" +
//                        trophy.getTrophy_description() + "|" + trophy.getLog_num() + "|" +
//                        trophy.getTimestamp().format(DATE_FORMAT) + "|" + trophy.getRarity_percentage());
//            fw.write("\n");
//            fw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main (String args[]){
        ScrapePSNPLog test = new ScrapePSNPLog();
//        String psn = "slammajamma28";
//        String team = "Dabaholics Anonymous";
        String psn;
        // Slamma,slammajamma28,true
        List<Trophy> trophies = new ArrayList<>();
        String line;
        try {
            FileReader fr = new FileReader("participant_list");
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                if (line.substring(0, 1).equals("+")) {
                    // Do nothing
                } else {
                    List<String> id = Arrays.asList(line.split(","));
                    psn = id.get(1);
                    System.out.println("Pulling log info for " + psn);
                    trophies = test.trophiesFromLog(psn);
//                    trophies = test.trophiesFromLog(psn);
                    System.out.println("Printing to file for " + psn);
                    for (Trophy trophy : trophies) {
//                        test.writeTrophyToFile(trophy, psn);
//                        test.printTrophy(trophy);
                    }

                    System.out.println("\n****************************\n");

                    trophies = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printTrophy(Trophy trophy) {
        System.out.println("Log number: " + Integer.toString(trophy.getLog_num()));
        System.out.println("Trophy timestamp: " + trophy.getTimestamp().toString());
        System.out.println("Trophy rarity: " + Double.toString(trophy.getRarity_percentage()));
        System.out.println("\n *************************** \n ");
    }
}