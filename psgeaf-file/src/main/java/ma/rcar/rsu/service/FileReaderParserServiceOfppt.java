package ma.rcar.rsu.service;


import com.jcraft.jsch.*;

import ma.rcar.rsu.dto.ofppt.DetailsFileRequestOfpptDto;
import ma.rcar.rsu.dto.ofppt.FileRequestOfpptDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;




/**
 * @author BAKHALED Ibrahim.
 *
 */

@Service
public class FileReaderParserServiceOfppt {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderParserServiceOfppt.class);


    public List<FileRequestOfpptDto> checkExistenceInbound() {
        List<FileRequestOfpptDto> out = new ArrayList<>();
        File mesrsiDir = new File("/opt/echange-cnra-af-pp/ofppt/inbound");

        if (!mesrsiDir.exists() || !mesrsiDir.isDirectory()) {
            System.err.println("OFPPT directory does not exist.");
            return out;
        }
        File[] files = mesrsiDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".csv") && file.getName().contains("OFPPT_CDG")) {
                    System.out.println(file.getName());
                    logger.info("Parsing file: " + file.getName());
                    try (FileInputStream fileInputStream = new FileInputStream(file);
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        FileRequestOfpptDto fileRequest = readAndParseLocal(file, bufferedReader);
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

    public FileRequestOfpptDto readAndParseLocal(File file, BufferedReader br) throws IOException {
        String line;

        FileRequestOfpptDto fileReq = new FileRequestOfpptDto(file.getName(), new Date(), "OFPPT", "INSCRIPTION",
                file.getName().replace("OFPPT_CDG_", ""), null,new ArrayList<>());
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

                DetailsFileRequestOfpptDto det = new DetailsFileRequestOfpptDto();
                String[] values = line.split(";");
                det.setIsMissingFields(false);
                if(values.length >= 1) {
                    det.setNumLot(values[0]);
                }
                if(values.length >= 2) {
                    det.setCef(values[1]);
                }
                if(values.length >= 3 && !Objects.equals(values[2], "NULL")) {
                    det.setCnie(values[2]);
                }
                if(values.length >= 4) {
                    det.setNumMassar(values[3]);
                }
                if(values.length >= 5) {
                    det.setDateNaissance(values[4]);
                }
                if(values.length >= 6) {
                    det.setNom(values[5]);
                }
                if(values.length >= 7) {
                    det.setPrenom(values[6]);
                }
                if(values.length >= 8) {
                    det.setAnneeScolaire(values[7]);
                }
                if(values.length >= 9){
                    det.setBoursier(values[8]);
                }
                if(values.length >= 10 && !Objects.equals(values[9], "NULL")) {
                    det.setMontant(values[9]);
                }
                if(values.length != 10) {
                    det.setMotif("Format de ligne incorrect");
                    det.setIsMissingFields(true);
                }
                det.setRang(index + 1);
                fileReq.getDetails().add(det);
                index++;
            }

            return fileReq;
        }


    public void extractOutboundFile(FileRequestOfpptDto fileRequest) throws SftpException {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        File outboundFolder = new File("/opt/echange-cnra-af-pp/ofppt/outbound");


        if (!outboundFolder.exists()) {
            outboundFolder.mkdir();
        }

        String fileOutboundName = fileRequest.getFileName().replace(".csv", "") + "_retour_"
                + ma.rcar.rsu.utils.Utils.dateToStringAAAAMMJJhhmm(new Date()) + ".csv";
        File outboundFile = new File(outboundFolder, fileOutboundName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outboundFile))) {
            List<DetailsFileRequestOfpptDto> ligneRej = fileRequest.getLignesRejet();
            List<DetailsFileRequestOfpptDto> allLignes = fileRequest.getDetails();
            for (DetailsFileRequestOfpptDto ligne : ligneRej) {
                writer.write("01");
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
            writer.write(String.valueOf(allLignes.size() - ligneRej.size() ));
            writer.write(";");
            writer.write(ma.rcar.rsu.utils.Utils.dateToStringAAAAMMJJhhmm(new Date()));
            writer.write(";");
            writer.newLine();
            System.out.println("File written successfully.");
            fileRequest.setOutboundFileName(fileOutboundName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void controleFileRequest(FileRequestOfpptDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        for (DetailsFileRequestOfpptDto ligne : fileRequest.getDetails()) {
            ligne.setEtat("OK");
            ligne.setCodeMotif("00");
            boolean isMissingFieldsInside = ligne.getIsMissingFields();
            if(isMissingFieldsInside) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("80");
                ligne.setMotif("Format de ligne incorrect");
                continue;
            }
            if (StringUtils.isBlank(ligne.getCef())) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("10");
                ligne.setMotif("CEF non valide");
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
                ligne.setMotif("Année de formation non valide");
                continue;
            }

            if (StringUtils.isBlank(ligne.getBoursier())
                    || (!ligne.getBoursier().equals("0") &&
                    !ligne.getBoursier().equals("1") &&
                    !ligne.getBoursier().equals("2") &&
                    !ligne.getBoursier().equals("3"))) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("50");
                ligne.setMotif("Donnée Stagiaire Boursier non valide");
                continue;
            }
            if (ligne.getMontant() != null && StringUtils.isBlank(ligne.getMontant()) ) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("60");
                ligne.setMotif("Montant bource non valide");
            }

        }

        for (DetailsFileRequestOfpptDto ligne : fileRequest.getDetails()) {
            System.err.println(ligne.getEtat());
        }
    }

    public void extractAndAddOutbound(FileRequestOfpptDto fileRequest) {
        try {
            String inboundUrl = "/opt/echange-cnra-af-pp/ofppt/inbound";
            String execUrl = "/opt/echange-cnra-af-pp/ofppt/exec";

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

            extractOutboundFile(fileRequest);
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
