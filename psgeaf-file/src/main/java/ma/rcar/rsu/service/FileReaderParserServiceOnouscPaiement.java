package ma.rcar.rsu.service;


import ma.rcar.rsu.dto.onousc.paiement.DetailsFileRequestPaiementDto;
import ma.rcar.rsu.dto.onousc.paiement.FileRequestPaiementDto;
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
public class FileReaderParserServiceOnouscPaiement {


    private static final Logger logger = LoggerFactory.getLogger(FileReaderParserServiceOnouscPaiement.class);


    public List<FileRequestPaiementDto> checkExistenceInbound() {
        List<FileRequestPaiementDto> out = new ArrayList<>();
        File mesrsiDir = new File("/opt/echange-cnra-af-pp/onousc/inbound");

        if (!mesrsiDir.exists() || !mesrsiDir.isDirectory()) {
            System.err.println("ONOUSC directory does not exist.");
            return out;
        }

        File[] files = mesrsiDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt") && file.getName().contains("ONOUSC_CDG_paiement")) {
                    System.out.println(file.getName());
                    logger.info("Parsing file: " + file.getName());


                    try (FileInputStream fileInputStream = new FileInputStream(file);
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        FileRequestPaiementDto fileRequest = readAndParseLocal(file, bufferedReader);
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


    public FileRequestPaiementDto readAndParseLocal(File file, BufferedReader br) throws IOException {
        String line;

        FileRequestPaiementDto fileReq = new FileRequestPaiementDto(file.getName(), new Date(), "ONOUSC", "PAIEMENT", file.getName().replace("ONOUSC_CDG_paiement_", ""), null, new ArrayList<>());
        int index = 0;

        while ((line = br.readLine()) != null) {

            System.out.println(line);

            if (line == null || line.isEmpty()) {
                continue;
            }

            DetailsFileRequestPaiementDto det = new DetailsFileRequestPaiementDto();
            String[] values = line.split("\\|");
            det.setIsMissingFields(false);


            if (values.length >= 1) {
                det.setNumMassar(values[0]);
            }
            if (values.length >= 2) {
                det.setCnie(values[1]);
            }
            if (values.length >= 3) {
                det.setDateNaissance(values[2]);
            }
            if (values.length >= 4) {
                det.setNom(values[3]);
            }
            if (values.length >= 5) {
                det.setPrenom(values[4]);
            }
            if (values.length >= 6) {
                det.setAnneeScolaire(values[5]);
            }
            if(values.length >= 7) {
                det.setTypeBource(values[6]);
            }
            if(values.length >= 8) {
                det.setMontant(values[7]);
            }

            if(values.length >= 9) {
                det.setDateEffet(values[8]);
            }
            if(values.length >= 10){
                det.setDateFin(values[9]);
            }

            if (values.length != 10) {
                det.setMotif("Format de ligne incorrect");
                det.setIsMissingFields(true);
            }

            det.setRang(index + 1);
            fileReq.getDetails().add(det);
            index++;
        }

        return fileReq;
    }



    public void extractOutboundLocal(FileRequestPaiementDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        File outboundFolder = new File("/opt/echange-cnra-af-pp/onousc/outbound");

        if (!outboundFolder.exists()) {
            outboundFolder.mkdir();
        }

        String fileOutboundName = fileRequest.getFileName().replace(".txt", "") + "_retour_" + Utils.dateToStringAAAAMMJJhhmm(new Date()) + ".txt";
        File outboundFile = new File(outboundFolder, fileOutboundName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outboundFile))) {
            List<DetailsFileRequestPaiementDto> ligneRej = fileRequest.getLignesRejet();
            List<DetailsFileRequestPaiementDto> allLignes = fileRequest.getDetails();

            for (DetailsFileRequestPaiementDto ligne : ligneRej) {
                writer.write("01");
                writer.write("|");
                writer.write(ligne.getCodeMotif());
                writer.write("|");
                writer.write(ligne.getMotif());
                writer.write("|");
                writer.write(ligne.buildLine());
                writer.newLine();
            }

            writer.write("02");
            writer.write("|");
            writer.write(String.valueOf(allLignes.size()));
            writer.write("|");
            writer.write(String.valueOf(ligneRej.size()));
            writer.write("|");
            writer.write(String.valueOf(allLignes.size() - ligneRej.size()));
            writer.write("|");
            writer.newLine();
            System.out.println("File written successfully.");
            fileRequest.setOutboundFileName(fileOutboundName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void controleFileRequest(FileRequestPaiementDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        for (DetailsFileRequestPaiementDto ligne : fileRequest.getDetails()) {
            ligne.setEtat("OK");
            ligne.setCodeMotif("00");
            boolean isMissingFieldsInside = ligne.getIsMissingFields();
            if(isMissingFieldsInside) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("80");
                ligne.setMotif("Format de ligne incorrect");
                continue;
            }
            if (StringUtils.isBlank(ligne.getNumMassar())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("10");
                ligne.setMotif("Num Massar non valide");
                continue;
            }

            if (StringUtils.isBlank(ligne.getDateNaissance())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("20");
                ligne.setMotif("Date naissance non valide");
                continue;
            }

            Date parsedBirth = ma.rcar.rsu.utils.Utils.stringToDateyyyyMMdd(ligne.getDateNaissance());
            if (parsedBirth == null) {
                System.err.println(ligne.getDateNaissance());
                ligne.setEtat("KO");
                ligne.setCodeMotif("20");
                ligne.setMotif("Date naissance non valide");
                continue;
            }

            if (StringUtils.isBlank(ligne.getAnneeScolaire()) || ligne.getAnneeScolaire().length() != 8) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("30");
                ligne.setMotif("Année scolaire non valide");
                continue;
            }

            if (StringUtils.isBlank(ligne.getTypeBource())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("60");
                ligne.setMotif("Type de bourse invalide");
                continue;
            }
            if (StringUtils.isBlank(ligne.getMontant())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("70");
                ligne.setMotif("Montant invalide");
                continue;
            }

        }

        for (DetailsFileRequestPaiementDto ligne : fileRequest.getDetails()) {
            System.err.println(ligne.getEtat());
        }
    }





    public void extractAndAddOutboundLocal(FileRequestPaiementDto fileRequest) {

        try {
            String inboundUrl = "/opt/echange-cnra-af-pp/onousc/inbound";
            String execUrl = "/opt/echange-cnra-af-pp/onousc/exec";

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
