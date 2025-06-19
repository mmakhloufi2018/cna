package ma.rcar.rsu.service;


import com.jcraft.jsch.*;

import ma.rcar.rsu.dto.mhai.DetailsFileRequestMhaiDto;
import ma.rcar.rsu.dto.mhai.FileRequestMhaiDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;



/**
 * @author BAKHALED Ibrahim.
 *
 */


@Service
public class FileReaderParserServiceMhai {


    private static final Logger logger = LoggerFactory.getLogger(FileReaderParserServiceMhai.class);
    @Value("${file.sftp.mhai.user}")
    private String userSftp;

    @Value("${file.sftp.mhai.pass}")
    private String passSftp;

    @Value("${file.sftp.mhai.host}")
    private String hostSftp;

    @Value("${file.sftp.mhai.port}")
    private int portSftp;

    @Value("${file.sftp.mhai.inbound}")
    private String inboundUrl;

    @Value("${file.sftp.mhai.outbound}")
    private String outboundUrl;

    @Value("${file.sftp.mhai.extract}")
    private String extractUrl;

    public List<FileRequestMhaiDto> checkExistenceInbound() {
        List<FileRequestMhaiDto> out = new ArrayList<>();
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(userSftp, hostSftp, portSftp);
            session.setPassword(passSftp);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            Vector<ChannelSftp.LsEntry> fileList = channel.ls(inboundUrl);
            for (ChannelSftp.LsEntry entry : fileList) {
                if (!entry.getAttrs().isDir() && entry.getFilename().endsWith(".csv")
                        && entry.getFilename().contains("MHAI_CDG_inscription")) {
                    System.out.println(entry.getFilename());
                    try {
                        logger.info("Parsing file : " + entry.getFilename());
                        FileRequestMhaiDto fileRequest = readAndParseInbound(channel, entry.getFilename());
                        logger.info("OK Parsing file : " + entry.getFilename());
                        out.add(fileRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
        return out;
    }

    public FileRequestMhaiDto readAndParseInbound(ChannelSftp channel, String filename) throws SftpException, IOException {
        try (InputStream is = channel.get(inboundUrl + "/" + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            FileRequestMhaiDto fileReq = new FileRequestMhaiDto(filename, new Date(), "MHAI", "INSCRIPTION",
                    filename.replace("MHAI_CDG_inscription_", ""), null,new ArrayList<>());
            int index = 0;
            br.readLine();
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if (line == null || line.isEmpty()) {
                    continue;
                }

                DetailsFileRequestMhaiDto det = new DetailsFileRequestMhaiDto();
                String[] values = line.split(";");
                det.setIsMissingFields(false);

                if(values.length >=1){
                    det.setIdEtudiant(values[0]);
                }
                if(values.length >= 2){
                    det.setNumMassar(values[1]);
                }
                if (values.length >=3){
                    det.setCnie(values[2]);

                }
                if (values.length >=4){
                    det.setNationalite(values[3]);

                }
                if (values.length >=5){
                    det.setDateNaissance(values[4]);

                }
                if (values.length >=6){
                    det.setNomFr(values[5]);

                }
                if (values.length >=7){
                    det.setPrenomFr(values[6]);

                }
                if (values.length >=8){
                    det.setNomAr(values[7]);

                }
                if (values.length >=9){
                    det.setPrenomAr(values[8]);

                }
                if (values.length >=10){
                    det.setAnneeScolaire(values[9]);

                }
                if (values.length >=11){
                    det.setScolarise(values[10]);

                }
                if (values.length >=12){
                    det.setBoursier(values[11]);

                }
                if (values.length >=13){
                    det.setNiveau(values[12]);

                }
                if (values.length >=14){
                    det.setMontant(values[13]);

                }

                if (values.length != 14) {
                    det.setMotif("Format de ligne incorrect");
                    det.setIsMissingFields(true);
                }
                det.setRang(index);
                fileReq.getDetails().add(det);
                index++;
            }
            return fileReq;
        }
    }

    public void extractOutboundFile(ChannelSftp channel, FileRequestMhaiDto fileRequest) throws SftpException {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }
        String fileOutboundName = fileRequest.getFileName().replace(".csv", "") + "_retour_"
                + ma.rcar.rsu.utils.Utils.dateToStringAAAAMMJJhhmm(new Date()) + ".csv";
        try (OutputStream os = channel.put(outboundUrl + "/" + fileOutboundName, ChannelSftp.OVERWRITE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
            List<DetailsFileRequestMhaiDto> ligneRej = fileRequest.getLignesRejet();
            List<DetailsFileRequestMhaiDto> allLignes = fileRequest.getDetails();
            for (DetailsFileRequestMhaiDto ligne : ligneRej) {
                int rangInt = ligne.getRang() + 2;
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
            writer.write(String.valueOf(allLignes.size() - ligneRej.size() ));
            writer.write(";");
            writer.newLine();
            System.out.println("File written successfully.");
            fileRequest.setOutboundFileName(fileOutboundName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void controleFileRequest(FileRequestMhaiDto fileRequest) {
        if (fileRequest == null || fileRequest.getDetails() == null || fileRequest.getDetails().isEmpty()) {
            return;
        }

        for (DetailsFileRequestMhaiDto ligne : fileRequest.getDetails()) {
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



            if (StringUtils.isBlank(ligne.getAnneeScolaire()) || ligne.getAnneeScolaire().length() != 8) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("20");
                ligne.setMotif("Année scolaire non valide");
                continue;
            }

            if (StringUtils.isBlank(ligne.getScolarise())
                    || (!ligne.getScolarise().equals("0") && !ligne.getScolarise().equals("1"))) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("30");
                ligne.setMotif("Donnée « Elève scolarisé » non valide");
                continue;
            }

            if (StringUtils.isBlank(ligne.getBoursier())
                    || (!ligne.getBoursier().equals("0") && !ligne.getBoursier().equals("1"))) {
                ligne.setEtat("KO");
                ligne.setCodeMotif("40");
                ligne.setMotif("Donnée « Elève Boursier » non valide");
                continue;
            }
        }

        for (DetailsFileRequestMhaiDto ligne : fileRequest.getDetails()) {
            System.err.println(ligne.getEtat());
        }
    }

    public void extractAndAddOutbound(FileRequestMhaiDto fileRequest) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(userSftp, hostSftp, portSftp);
            session.setPassword(passSftp);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            extractOutboundFile(channel,fileRequest);
            channel.rename(inboundUrl + "/" + fileRequest.getFileName(), extractUrl + "/" + fileRequest.getFileName());
            logger.info("moving file to extra folder file : " + fileRequest.getFileName());
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
    }
}
