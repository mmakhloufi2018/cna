CREATE OR REPLACE PROCEDURE ESGEAF.pr_alimenter_data_cap_forf (
    p_mois_annee   IN     VARCHAR2,
    p_message         OUT VARCHAR2)
IS
    v_id_last_lot_rcar   NUMBER := 0;
    v_id_last_lot_cmr    NUMBER := 0;
    v_id_last_lot_cnss   NUMBER := 0;
    v_id_last_lot_tgr    NUMBER := 0;
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

    ----------------------------------------FORFITAIRE-----------------------------------
    ----------Cas de demandeur
    INSERT INTO DATA_CAP_FOR_DEM (ID,
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
               AND d.TYPE_PRESTATION IN ('FORFAITAIRE', 'Forfaitaire')
               AND d.DATE_CLOTURE IS NULL
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
               
    -------Cas des autres membres
     INSERT INTO ESGEAF.DATA_CAP_FOR_MEM (  ID                   ,
                                          AGE                  ,
                                          BENEFICIE_AF         ,
                                          CODE_RSU             ,
                                          DOSSIER              ,
                                          LIEN_AVEC_DEMANDEUR  ,
                                          MEMBRE               ,
                                          MOIS_ANNEE           ,
                                          PENSIONNE            ,
                                          RESIDE_MAROC         ,
                                          SALARIE              ,
                                          CIN                  )
        SELECT hibernate_sequence.NEXTVAL,
               TRUNC(MONTHS_BETWEEN(SYSDATE, m.DATE_NAISSANCE) / 12) age,
               NULL         beneficie_af,
               d.CODE_MENAGE_RSU,
               d.ID,
               m.LIEN_PARENTE,
               m.id,
               p_mois_annee     mois_annee,
               '0'        pensionne,
               m.RESIDE_AU_MAROC,
               '0'         salarie,
               m.CIN
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_AUTRE_MEMBRE m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION IN ('FORFAITAIRE', 'Forfaitaire')
               AND d.DATE_CLOTURE IS NULL
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
    
    IF (v_id_last_lot_rcar IS NOT NULL)
    THEN
        UPDATE DATA_CAP_FOR_DEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_RCAR b
                     WHERE b.ID_LOT = v_id_last_lot_rcar AND a.cin = b.cin);
                     
        UPDATE DATA_CAP_FOR_MEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_RCAR b
                     WHERE b.ID_LOT = v_id_last_lot_rcar AND a.cin = b.cin);             
    END IF;
    
    IF (v_id_last_lot_cmr IS NOT NULL)
    THEN
        UPDATE DATA_CAP_FOR_DEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CMR b
                     WHERE b.ID_LOT = v_id_last_lot_cmr AND a.cin = b.cin);
        
        UPDATE DATA_CAP_FOR_MEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CMR b
                     WHERE b.ID_LOT = v_id_last_lot_cmr AND a.cin = b.cin);             
    END IF;
    
    IF (v_id_last_lot_cnss IS NOT NULL)
    THEN
        UPDATE DATA_CAP_FOR_DEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CNSS b
                     WHERE b.ID_LOT = v_id_last_lot_cnss AND a.cin = b.cin);
        
        UPDATE DATA_CAP_FOR_MEM a
           SET a.PENSIONNE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_CNSS b
                     WHERE b.ID_LOT = v_id_last_lot_cnss AND a.cin = b.cin);
    END IF;    
        
    IF (v_id_last_lot_tgr IS NOT NULL)
    THEN
        UPDATE DATA_CAP_FOR_DEM a
           SET a.SALARIE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_TGR b
                     WHERE b.ID_LOT = v_id_last_lot_tgr AND a.cin = b.cin);
       
        UPDATE DATA_CAP_FOR_MEM a
           SET a.SALARIE = '1'
         WHERE EXISTS
                   (SELECT 1
                      FROM PSGEAF.DETAILS_LOT_SITUATION_TGR b
                     WHERE b.ID_LOT = v_id_last_lot_tgr AND a.cin = b.cin);             
    END IF;      
------------------------------------- FIN FORFAITAIRE-------------------------------------   
EXCEPTION
    WHEN OTHERS
    THEN
        --DBMS_OUTPUT.Put_Line ('alimentation condidat non validée : ' || substr (SQLERRM, 1, 100));
        p_message := 'E';
        ROLLBACK;

        COMMIT;
END;
/
