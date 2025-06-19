package ma.rcar.rsu.service;



import ma.rcar.rsu.dto.fef.DetailsFileRequestFefDto;
import ma.rcar.rsu.dto.fef.FileRequestFefDto;
import ma.rcar.rsu.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Service
public class FileReaderParserServiceFef {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderParserServiceFef.class);




    public List<FileRequestFefDto> checkExistenceInbound() {
        List<FileRequestFefDto> out = new ArrayList<>();
        File mesrsiDir = new File("/opt/echange-cnra-af-pp/fef/inbound");

        if (!mesrsiDir.exists() || !mesrsiDir.isDirectory()) {
            System.err.println("FEF directory does not exist.");
            return out;
        }
        File[] files = mesrsiDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv") && file.getName().contains("FEF_CDG_BENEFICIAIRES")) {
                    System.out.println(file.getName());
                    logger.info("Parsing file: " + file.getName());
                    try (FileInputStream fileInputStream = new FileInputStream(file);
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        FileRequestFefDto fileRequest = readAndParseLocal(file, bufferedReader);
                        logger.info("OK Parsing file: " + file.getName());
                        out.add(fileRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return out;
    }


    public FileRequestFefDto readAndParseLocal(File file, BufferedReader br) throws IOException {
        String line;

        FileRequestFefDto fileReq = new FileRequestFefDto(file.getName(), new Date(), "FEF", "BENEFICIAIRES", file.getName().replace("FEF_CDG_BENEFICIAIRES_", ""), null, new ArrayList<>());
        int index = 0;

        while ((line = br.readLine()) != null) {
            if (index == 0) {
                index++;
                continue;
            }
            System.out.println(line);

            if (line == null || line.isEmpty()) {
                continue;
            }

            DetailsFileRequestFefDto det = new DetailsFileRequestFefDto();
            String[] values = line.split(";");
            det.setIsMissingFields(false);


            if (values.length >= 1) {
                det.setCin(values[0]);
            }
            if (values.length >= 2) {
                det.setDatePec(values[1]);
            }
            if (values.length >= 3) {
                det.setDateExpiration(values[2]);
            }
            if (values.length != 3) {
                det.setMotif("Format de ligne incorrect");
                det.setIsMissingFields(true);
            }

            det.setRang(index + 1);
            fileReq.getDetails().add(det);
            index++;
        }

        return fileReq;
    }



    public void extractOutboundLocal(FileRequestFefDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        File outboundFolder = new File("/opt/echange-cnra-af-pp/fef/outbound");

        if (!outboundFolder.exists()) {
            outboundFolder.mkdir();
        }

        String fileOutboundName = fileRequest.getFileName().replace(".csv", "") + "_retour_" + Utils.dateToStringAAAAMMJJhhmm(new Date()) + ".csv";
        File outboundFile = new File(outboundFolder, fileOutboundName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outboundFile))) {
            List<DetailsFileRequestFefDto> ligneRej = fileRequest.getLignesRejet();
            List<DetailsFileRequestFefDto> allLignes = fileRequest.getDetails();

            for (DetailsFileRequestFefDto ligne : ligneRej) {
                int rangInt = ligne.getRang();
                String rangStr = String.valueOf(rangInt);
                writer.write("01");
                writer.write(";");
                writer.write(rangStr);
                writer.write(";");
                writer.write(ligne.getCodeMotif());
                writer.write(";");
                writer.write(ligne.getMotif());
                writer.write(";");
                writer.write(ligne.buildLine());
                writer.newLine();
            }

            writer.write("02");
            writer.write(";");
            writer.write(String.valueOf(allLignes.size()));
            writer.write(";");
            writer.write(String.valueOf(ligneRej.size()));
            writer.write(";");
            writer.write(String.valueOf(allLignes.size() - ligneRej.size()));
            writer.write(";");
            writer.newLine();
            System.out.println("File written successfully.");
            fileRequest.setOutboundFileName(fileOutboundName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void controleFileRequest(FileRequestFefDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        for (DetailsFileRequestFefDto ligne : fileRequest.getDetails()) {
            ligne.setEtat("OK");
            ligne.setCodeMotif("00");
            boolean isMissingFieldsInside = ligne.getIsMissingFields();
            if(isMissingFieldsInside) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("80");
                ligne.setMotif("Format de ligne incorrect");
                continue;
            }
            if (StringUtils.isBlank(ligne.getCin())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("10");
                ligne.setMotif("CIN non valide");
                continue;
            }
            if (StringUtils.isBlank(ligne.getDatePec())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("20");
                ligne.setMotif("Date PEC non valide");
                continue;
            }
            if (StringUtils.isBlank(ligne.getDateExpiration())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("30");
                ligne.setMotif("Date Expiration non valide");
            }
        }

        for (DetailsFileRequestFefDto ligne : fileRequest.getDetails()) {
            System.err.println(ligne.getEtat());
        }
    }





    public void extractAndAddOutboundLocal(FileRequestFefDto fileRequest) {

        try {
            String inboundUrl = "/opt/echange-cnra-af-pp/fef/inbound";
            String execUrl = "/opt/echange-cnra-af-pp/fef/exec";

            File inboundFolder = new File(inboundUrl);
            File execFolder = new File(execUrl);


            if (!inboundFolder.exists()) {
                logger.error("Inbound folder does not exist: " + inboundUrl);
                return;
            }

            if (!execFolder.exists()) {
                logger.error("Exec folder does not exist: " + execUrl);
                return;
            }

            String fileName = fileRequest.getFileName();
            File sourceFile = new File(inboundFolder, fileName);
            File destinationFile = new File(execFolder, fileName);

            extractOutboundLocal(fileRequest);
            if (sourceFile.exists()) {
                if (sourceFile.renameTo(destinationFile)) {
                    logger.info("Moving file to exec folder: " + fileName);
                } else {
                    logger.error("Failed to move the file to exec folder.");
                }
            } else {
                logger.error("File not found in inbound folder: " + fileName);
            }
        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage());
        }
    }
}
