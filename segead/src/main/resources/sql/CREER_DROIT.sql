CREATE OR REPLACE PROCEDURE ESGEAF.CREER_DROIT (  p_mois_annee   IN     VARCHAR2 , p_score in number)
AS
   seq       NUMBER;
   montant   NUMBER;
   DOSSIER_ID NUMBER;
BEGIN
   -- Déclaration du curseur
    
   DECLARE
      CURSOR c_source
      IS
         SELECT DISTINCT
                da.id,
                DA.DATE_CREATION,
                   NVL (dc.EST_CHEF_MENAGE, '1')
                || NVL (dc.RESIDE_MAROC, '1')
                || NVL (dc.SALARIE, '0')
                || NVL (dc.PENSIONNE, '0')
                || NVL (dc.BENEFICIE_AF, '0')
                   data_cap_dem,
                   NVL (dcc.RESIDE_MAROC, '1')
                || NVL (dcc.SALARIE, '0')
                || NVL (dcc.PENSIONNE, '0')
                || NVL (dcc.BENEFICIE_AF, '0')
                   data_cap_conj,
                DC.SCORE_RSU score
           FROM DATA_CAP_FOR_DEM dc_
                JOIN DOSSIER_ASD da
                   ON DC_.DOSSIER = DA.id
                LEFT JOIN droit dr
                   ON DR.DOSSIER_ASD_ID = da.id
                JOIN M_DEMANDEUR dm
                   ON DM.DOSSIER_ASD_ID = da.id
                JOIN DATA_CAP_FOR_DEM dc
                   ON DM.ID = DC.MEMBRE
                LEFT JOIN M_CONJOINT mc
                   ON MC.DOSSIER_ASD_ID = DM.DOSSIER_ASD_ID
                LEFT JOIN DATA_CAP_FOR_MEM dcc
                   ON (mc.ID = DCc.MEMBRE and dcc.MOIS_ANNEE = p_mois_annee)
          WHERE DR.DOSSIER_ASD_ID IS NULL AND dc_.MOIS_ANNEE = p_mois_annee and dc.MOIS_ANNEE = p_mois_annee ;
   BEGIN
      -- Boucle sur les résultats de la requête SELECT
      FOR r_source IN c_source
      LOOP
         -- Insertion dans la table de destination
        DOSSIER_ID := r_source.id;

         IF     r_source.data_cap_dem = '11000'
            AND r_source.data_cap_conj = '1000'
            AND r_source.score < p_score
         THEN
            montant := 500;
         ELSE
            montant := 0;
         END IF;

         SELECT hibernate_sequence.NEXTVAL INTO seq FROM DUAL;

         INSERT INTO droit (DATE_CREATION,
                            DATE_CREATION_FIN,
                            DATE_EFFET,
                            DATE_FIN,
                            DOSSIER_ASD_ID,
                            ID,
                            MONTANT)
              VALUES (SYSDATE,
                      NULL,
                      r_source.DATE_CREATION,
                      NULL,
                      r_source.id,
                      seq,
                      montant);

         INSERT INTO DETAIL_DROIT (DROIT_ID,
                                   ID,
                                   MONTANT,
                                   OFFSET,
                                   RUBRIQUE)
              VALUES (seq,
                      hibernate_sequence.NEXTVAL,
                      montant,
                       'DEM:' || r_source.data_cap_dem || ' - CONJ:' || r_source.data_cap_conj  || '- SCORE:' ||   r_source.score,
                      'FORFAITAIRE');

         COMMIT;
      END LOOP;
   END;
    EXCEPTION
    WHEN OTHERS
    THEN
        ROLLBACK;
        Insert into ESGEAF.EXCEPTION    (ID, DATE_EXCEPTION, PROCEDURE, DOSSIER, ERREUR)  Values    (hibernate_sequence.nextval, sysdate, 'CREER_DROIT', to_char(DOSSIER_ID), ''); 
        COMMIT;
END;
/
