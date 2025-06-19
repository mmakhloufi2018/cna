package ma.rcar.rsu.service;





import ma.rcar.rsu.dto.mesrsi.DetailsFileRequestMesrsiDto;
import ma.rcar.rsu.dto.mesrsi.FileRequestMesrsiDto;
import ma.rcar.rsu.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.io.File;
import java.io.IOException;



/**
 * @author BAKHALED Ibrahim.
 *
 */



@Service
public class FileReaderParserServiceMesrsi {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderParserServiceMesrsi.class);

    public List<FileRequestMesrsiDto> checkExistenceInbound() {
        List<FileRequestMesrsiDto> out = new ArrayList<>();
        File mesrsiDir = new File("/opt/echange-cnra-af-pp/mesrsi/inbound");

        if (!mesrsiDir.exists() || !mesrsiDir.isDirectory()) {
            System.err.println("MESRSI directory does not exist.");
            return out;
        }

        File[] files = mesrsiDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt") && file.getName().contains("MESRSI_CDG_inscription")) {
                    System.out.println(file.getName());
                    logger.info("Parsing file: " + file.getName());


                    try (FileInputStream fileInputStream = new FileInputStream(file);
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        FileRequestMesrsiDto fileRequest = readAndParseLocal(file, bufferedReader);
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

public FileRequestMesrsiDto readAndParseLocal(File file, BufferedReader br) throws IOException {
    String line;

    FileRequestMesrsiDto fileReq = new FileRequestMesrsiDto(file.getName(), new Date(), "MESRSI", "INSCRIPTION", file.getName().replace("MESRSI_CDG_inscription_", ""), null, new ArrayList<>());
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

        DetailsFileRequestMesrsiDto det = new DetailsFileRequestMesrsiDto();
        String[] values = line.split("\t");
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

        if (values.length != 6) {
            det.setMotif("Format de ligne incorrect");
            det.setIsMissingFields(true);
        }

        det.setRang(index + 1);
        fileReq.getDetails().add(det);
        index++;
    }

    return fileReq;
}

    public void extractOutboundLocal(FileRequestMesrsiDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        File outboundFolder = new File("/opt/echange-cnra-af-pp/mesrsi/outbound");

        if (!outboundFolder.exists()) {
            outboundFolder.mkdir();
        }

        String fileOutboundName = fileRequest.getFileName().replace(".txt", "") + "_retour_" + Utils.dateToStringAAAAMMJJhhmm(new Date()) + ".txt";
        File outboundFile = new File(outboundFolder, fileOutboundName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outboundFile))) {
            List<DetailsFileRequestMesrsiDto> ligneRej = fileRequest.getLignesRejet();
            List<DetailsFileRequestMesrsiDto> allLignes = fileRequest.getDetails();

            for (DetailsFileRequestMesrsiDto ligne : ligneRej) {
                writer.write("01");
                writer.write("\t");
                writer.write(ligne.getCodeMotif());
                writer.write("\t");
                writer.write(ligne.getMotif());
                writer.write("\t");
                writer.write(ligne.buildLine());
                writer.newLine();
            }

            writer.write("02");
            writer.write("\t");
            writer.write(String.valueOf(allLignes.size()));
            writer.write("\t");
            writer.write(String.valueOf(ligneRej.size()));
            writer.write("\t");
            writer.write(String.valueOf(allLignes.size() - ligneRej.size()));
            writer.write("\t");
            writer.newLine();
            System.out.println("File written successfully.");
            fileRequest.setOutboundFileName(fileOutboundName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void controleFileRequest(FileRequestMesrsiDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        for (DetailsFileRequestMesrsiDto ligne : fileRequest.getDetails()) {
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




        }

        for (DetailsFileRequestMesrsiDto ligne : fileRequest.getDetails()) {
            System.err.println(ligne.getEtat());
        }
    }




    public void extractAndAddOutboundLocal(FileRequestMesrsiDto fileRequest) {

        try {
            String inboundUrl = "/opt/echange-cnra-af-pp/mesrsi/inbound";
            String execUrl = "/opt/echange-cnra-af-pp/mesrsi/exec";

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
