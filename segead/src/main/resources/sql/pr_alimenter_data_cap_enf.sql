CREATE OR REPLACE PROCEDURE ESGEAF.pr_alimenter_data_cap_enf (
    p_mois_annee   IN     VARCHAR2,
    p_message         OUT VARCHAR2)
IS
    v_id_last_lot_rcar   NUMBER := 0;
    v_id_last_lot_cmr    NUMBER := 0;
    v_id_last_lot_cnss   NUMBER := 0;
    v_id_last_lot_tgr    NUMBER := 0;
    
    v_id_last_FILE_R_OFPPT  NUMBER := 0;
    v_id_last_FILE_R_MESRSI  NUMBER := 0;
    v_id_last_FILE_R_MHAI    NUMBER := 0;
    v_id_last_FILE_R_ONOUSC   NUMBER := 0;
    v_id_last_FILE_R_ONOUSC_P    NUMBER := 0;
    

BEGIN
    p_message := 'V';


    SELECT ID
      INTO v_id_last_lot_rcar
      FROM (  SELECT *
                FROM psgeaf.LOT_SITUATION_RCAR l
            ORDER BY l.DATE_CREATION DESC)
     WHERE ROWNUM = 1;

    SELECT ID
      INTO v_id_last_lot_cmr
      FROM (  SELECT *
                FROM psgeaf.LOT_SITUATION_CMR l
            ORDER BY l.DATE_CREATION DESC)
     WHERE ROWNUM = 1;

    SELECT ID
      INTO v_id_last_lot_cnss
      FROM (  SELECT *
                FROM psgeaf.LOT_SITUATION_cnss l
            ORDER BY l.DATE_CREATION DESC)
     WHERE ROWNUM = 1;

    SELECT ID
      INTO v_id_last_lot_tgr
      FROM (  SELECT *
                FROM psgeaf.LOT_SITUATION_TGR l
            ORDER BY l.DATE_CREATION DESC)
     WHERE ROWNUM = 1;

    ----------------------------------------ENFANCE-----------------------------------
    ----------Cas de demandeur
    INSERT INTO DATA_CAP_ENF_DEM (ID,
                                  CIN,
                                  BENEFICIE_AF,
                                  CLOTURE_DEMANDE,
                                  CODE_RSU,
                                  EST_CHEF_MENAGE,
                                  MOIS_ANNEE,
                                  PENSIONNE,
                                  RESIDE_MAROC,
                                  SALARIE,
                                  SCORE_RSU,
                                  MEMBRE,
                                  DOSSIER)
        SELECT hibernate_sequence.NEXTVAL,
               m.CIN,
               NULL         beneficie_af,
               NULL         cloture_demande,
               d.CODE_MENAGE_RSU,
               m.EST_CHEF_MENAGE_RSU,
               p_mois_annee     mois_annee,
               '0'        pensionne,
               m.RESIDE_AU_MAROC,
               '0'         salarie,
               m.SCORE_RSU,
               m.ID,
               d.ID
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_DEMANDEUR m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION IN ('ENFANCE', 'Enfance')
               AND d.DATE_CLOTURE IS NULL
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
               
               
----------Cas de conj
    INSERT INTO DATA_CAP_ENF_CONJ (ID,
                                  CIN,
                                  BENEFICIE_AF,
                                  CODE_RSU,
                                  MOIS_ANNEE,
                                  PENSIONNE,
                                  RESIDE_MAROC,
                                  SALARIE,
                                  MEMBRE,
                                  DOSSIER)
        SELECT hibernate_sequence.NEXTVAL,
               m.CIN,
               NULL         beneficie_af,
               d.CODE_MENAGE_RSU,
               p_mois_annee     mois_annee,
               '0'        pensionne,
               m.RESIDE_AU_MAROC,
               '0'         salarie,
               m.ID,
               d.ID
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_CONJOINT m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION IN ('ENFANCE', 'Enfance')
               AND d.DATE_CLOTURE IS NULL
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;

----------Cas de enf
    INSERT INTO DATA_CAP_ENF_ENF (   ID             ,
                                      AGE            ,
                                      CODE_RSU       ,
                                      DOSSIER        ,
                                      EST_HANDICAPE  ,
                                      IDCS_MERE      ,
                                      IDCS_PERE      ,
                                      MEMBRE         ,
                                      MOIS_ANNEE     ,
                                      ORPHELIN_PERE  ,
                                      RESIDE_MAROC   )
        SELECT hibernate_sequence.NEXTVAL,
               TRUNC(MONTHS_BETWEEN(SYSDATE, m.DATE_NAISSANCE) / 12),
                d.CODE_MENAGE_RSU,
                d.id,
                m.EST_HANDICAPE,
                m.IDCS_MERE,
                m.IDCS_PERE,
                m.id,
                p_mois_annee     mois_annee,
                m.EST_ORPHELIN_PERE,
                m.RESIDE_AU_MAROC
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_ENFANT m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION IN ('ENFANCE', 'Enfance')
               AND d.DATE_CLOTURE IS NULL
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
               
 IF (v_id_last_lot_rcar IS NOT NULL)
    THEN
        UPDATE DATA_CAP_ENF_DEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_RCAR b
                     WHERE b.ID_LOT = v_id_last_lot_rcar AND a.cin = b.cin);
 
        UPDATE DATA_CAP_ENF_CONJ a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_RCAR b
                     WHERE b.ID_LOT = v_id_last_lot_rcar AND a.cin = b.cin);                               
    END IF;
    
    IF (v_id_last_lot_cmr IS NOT NULL)
    THEN
        UPDATE DATA_CAP_ENF_DEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CMR b
                     WHERE b.ID_LOT = v_id_last_lot_cmr AND a.cin = b.cin);
                     
        UPDATE DATA_CAP_ENF_CONJ a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CMR b
                     WHERE b.ID_LOT = v_id_last_lot_cmr AND a.cin = b.cin); 
                   
    END IF;
    
    IF (v_id_last_lot_cnss IS NOT NULL)
    THEN
        UPDATE DATA_CAP_ENF_DEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CNSS b
                     WHERE b.ID_LOT = v_id_last_lot_cnss AND a.cin = b.cin);

        UPDATE DATA_CAP_ENF_CONJ a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CNSS b
                     WHERE b.ID_LOT = v_id_last_lot_cnss AND a.cin = b.cin);        
        
    END IF;    
        
    IF (v_id_last_lot_tgr IS NOT NULL)
    THEN
        UPDATE DATA_CAP_ENF_DEM a
           SET a.SALARIE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_TGR b
                     WHERE b.ID_LOT = v_id_last_lot_tgr AND a.cin = b.cin);

        UPDATE DATA_CAP_ENF_CONJ a
           SET a.SALARIE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_TGR b
                     WHERE b.ID_LOT = v_id_last_lot_tgr AND a.cin = b.cin);               
    END IF;

  SELECT ID
      INTO v_id_last_FILE_R_OFPPT
      FROM (  SELECT *
                FROM psgeaf.FILE_REQUEST_OFPPT l
            ORDER BY l.DATE_RECEPTION DESC)
     WHERE ROWNUM = 1;
     
  SELECT ID
      INTO v_id_last_FILE_R_MESRSI
      FROM (  SELECT *
                FROM psgeaf.FILE_REQUEST_MESRSI l
            ORDER BY l.DATE_RECEPTION DESC)
     WHERE ROWNUM = 1;
 SELECT ID
      INTO v_id_last_FILE_R_MHAI
      FROM (  SELECT *
                FROM psgeaf.FILE_REQUEST_MHAI l
            ORDER BY l.DATE_RECEPTION DESC)
     WHERE ROWNUM = 1;

SELECT ID
      INTO v_id_last_FILE_R_ONOUSC
      FROM (  SELECT *
                FROM psgeaf.FILE_REQUEST_ONOUSC_INSCRIP l
            ORDER BY l.DATE_RECEPTION DESC)
     WHERE ROWNUM = 1;

SELECT ID
      INTO v_id_last_FILE_R_ONOUSC_P
      FROM (  SELECT *
                FROM psgeaf.FILE_REQUEST_ONOUSC_PAIEMENT l
            ORDER BY l.DATE_RECEPTION DESC)
     WHERE ROWNUM = 1;

    INSERT INTO ESGEAF.DATA_CAP_ENF_ENF_SCOL (   ID             ,
                                          DATE_EFFET_BOURSE     ,
                                          DATE_EFFET_SCOLARISE  ,
                                          DOSSIER               ,
                                          MEMBRE                ,
                                          MOIS_ANNEE            ,
                                          MONTANT_BOURSE        ,
                                          SCOLARISE             ,
                                          BOURSIER,
                                          CIN,
                                          NUM_MASSAR,
                                          CEF  )
        SELECT hibernate_sequence.NEXTVAL,
                NULL, 
                NULL,
                m.id,
                m.id,
                p_mois_annee     mois_annee,
                NULL,
                NULL,
                NULL,
                m.cin,
                m.IDENTIFIANT_SCOLARITE,
                NULL
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_ENFANT m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION IN ('ENFANCE', 'Enfance')
               AND d.DATE_CLOTURE IS NULL
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
                   
    IF (v_id_last_FILE_R_OFPPT IS NOT NULL)
    THEN
        


        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND  a.CEF IS NOT NULL
                 AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_OFPPT b
                     WHERE b.FILE_REQUEST_OFPPT_ID = v_id_last_FILE_R_OFPPT
                      AND b.ANNEE_SCOLAIRE = '20232024'
                      AND b.SCOLARISE = 1 AND b.CEF = a.CEF);
                      
         UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND a.NUM_MASSAR IS NOT NULL
                  AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_OFPPT b
                     WHERE b.FILE_REQUEST_OFPPT_ID = v_id_last_FILE_R_OFPPT
                      AND b.ANNEE_SCOLAIRE = '20232024'
                      AND b.SCOLARISE = 1 AND  b.NUM_MASSAR = a.NUM_MASSAR);              
                      
       UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND a.CIN IS NOT NULL
          AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_OFPPT b
                     WHERE b.FILE_REQUEST_OFPPT_ID = v_id_last_FILE_R_OFPPT
                     AND b.ANNEE_SCOLAIRE = '20232024'
                      AND b.CNIE = a.CIN);

        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.BOURSIER = 1
         WHERE a.BOURSIER IS NULL AND  a.CEF IS NOT NULL
                 AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_OFPPT b
                     WHERE b.FILE_REQUEST_OFPPT_ID = v_id_last_FILE_R_OFPPT
                     AND b.ANNEE_SCOLAIRE = '20232024'
                      AND b.BOURSIER = 1 AND b.CEF = a.CEF);                      
        
                                
    END IF;    
    
    IF (v_id_last_FILE_R_MESRSI IS NOT NULL)
    THEN
       

        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND a.NUM_MASSAR IS NOT NULL
         AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_MESRSI b
                     WHERE b.FILE_REQUEST_MESRSI_ID = v_id_last_FILE_R_MESRSI
                       AND b.ANNEE_SCOLAIRE = '20232024'
                       AND a.SCOLARISE = 1 AND b.NUM_MASSAR = a.NUM_MASSAR);
                      
        /*
         UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE = 0 AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_MESRSI b
                     WHERE b.FILE_REQUEST_MESRSI_ID = v_id_last_FILE_R_MESRSI
                      AND b.CNIE = a.CIN);
        
        */              

                                
    END IF;    
    
IF (v_id_last_FILE_R_MHAI IS NOT NULL)
    THEN
       /* UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL  AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_MHAI b
                     WHERE b.FILE_REQUEST_MHAI_ID = v_id_last_FILE_R_MHAI
                      AND b.ANNEE_SCOLAIRE = '20232024'
                      AND b.CNIE = a.CIN);*/

        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_REQUEST_MHAI b
                     WHERE b.FILE_REQUEST_MHAI_ID = v_id_last_FILE_R_MHAI
                     AND b.ANNEE_SCOLAIRE = '20232024' AND a.SCOLARISE = 1
                      AND b.NUM_MASSAR = a.NUM_MASSAR);

                                
    END IF;   
    
    
    IF (v_id_last_FILE_R_ONOUSC IS NOT NULL)
    THEN

        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_ONOUSC_INSCRIP b
                     WHERE b.FILE_REQUEST_ONOUSC_ID = v_id_last_FILE_R_ONOUSC
                     AND b.ANNEE_SCOLAIRE = '20232024' AND a.SCOLARISE = 1
                      AND b.NUM_MASSAR = a.NUM_MASSAR);
                      
         UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.BOURSIER = 1
         WHERE a.BOURSIER IS NULL AND a.NUM_MASSAR IS NOT NULL
         AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_ONOUSC_INSCRIP b
                     WHERE b.FILE_REQUEST_ONOUSC_ID = v_id_last_FILE_R_ONOUSC
                     AND b.ANNEE_SCOLAIRE = '20232024' AND a.BOURSIER = 1
                      AND b.NUM_MASSAR = a.NUM_MASSAR);              

                                
    END IF;  
    
    
    IF (v_id_last_FILE_R_ONOUSC IS NOT NULL)
    THEN

        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.SCOLARISE = 1
         WHERE a.SCOLARISE IS NULL AND a.NUM_MASSAR IS NOT NULL
             AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_ONOUSC_INSCRIP b
                     WHERE b.FILE_REQUEST_ONOUSC_ID = v_id_last_FILE_R_ONOUSC
                     AND b.ANNEE_SCOLAIRE = '20232024' AND a.SCOLARISE = 1
                      AND b.NUM_MASSAR = a.NUM_MASSAR);
                      
         UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.BOURSIER = 1
         WHERE a.BOURSIER IS NULL AND a.NUM_MASSAR IS NOT NULL
         AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_ONOUSC_INSCRIP b
                     WHERE b.FILE_REQUEST_ONOUSC_ID = v_id_last_FILE_R_ONOUSC
                     AND b.ANNEE_SCOLAIRE = '20232024' AND a.BOURSIER = 1
                      AND b.NUM_MASSAR = a.NUM_MASSAR);              

                                
    END IF;  
    
    
    IF (v_id_last_FILE_R_ONOUSC_P IS NOT NULL)
    THEN

        UPDATE DATA_CAP_ENF_ENF_SCOL a
           SET a.BOURSIER = 1
         WHERE a.BOURSIER IS NULL AND a.NUM_MASSAR IS NOT NULL
         AND EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_FILE_ONOUSC_PAIEMENT b
                     WHERE --b.FILE_REQUEST_ONOUSC_ID = v_id_last_FILE_R_ONOUSC_P AND
                      b.ANNEE_SCOLAIRE = '20232024' AND b.TYPE_BOURCE = 1
                      AND b.NUM_MASSAR = a.NUM_MASSAR);
            

                                
    END IF;  
------------------------------------- FIN ENFANCE-------------------------------------   
EXCEPTION
    WHEN OTHERS
    THEN
        --DBMS_OUTPUT.Put_Line ('alimentation condidat non validée : ' || substr (SQLERRM, 1, 100));
        p_message := 'E';
        ROLLBACK;

        COMMIT;
END;
/
