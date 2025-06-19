CREATE OR REPLACE PROCEDURE ESGEAF.CLOTURER_DROIT (
   p_mois_annee   IN VARCHAR2,
   p_score        IN NUMBER)
AS
   seq       NUMBER;
   montant   NUMBER;
   numero_dossier NUMBER;
BEGIN
   -- Déclaration du curseur

   DECLARE
      CURSOR c_source
      IS
         SELECT DISTINCT
                dr.id droit_id,
                da.id dossier_id,
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
                JOIN droit dr
                   ON DR.DOSSIER_ASD_ID = da.id
                JOIN M_DEMANDEUR dm
                   ON DM.DOSSIER_ASD_ID = da.id
                JOIN DATA_CAP_FOR_DEM dc
                   ON DM.ID = DC.MEMBRE
                LEFT JOIN M_CONJOINT mc
                   ON MC.DOSSIER_ASD_ID = DM.DOSSIER_ASD_ID
                LEFT JOIN DATA_CAP_FOR_MEM dcc
                   ON mc.ID = DCc.MEMBRE
          WHERE     DC_.MOIS_ANNEE = p_mois_annee
                AND dc.MOIS_ANNEE = p_mois_annee
                AND dcc.MOIS_ANNEE = p_mois_annee
                AND DR.DATE_FIN IS  NULL
                AND DR.MONTANT > 0
                AND (      NVL (dc.EST_CHEF_MENAGE, '1')
                        || NVL (dc.RESIDE_MAROC, '1')
                        || NVL (dc.SALARIE, '0')
                        || NVL (dc.PENSIONNE, '0')
                        || NVL (dc.BENEFICIE_AF, '0') <> '11000'
                     OR    NVL (dcc.RESIDE_MAROC, '1')
                        || NVL (dcc.SALARIE, '0')
                        || NVL (dcc.PENSIONNE, '0')
                        || NVL (dcc.BENEFICIE_AF, '0') <> '1000'
                     OR DC.SCORE_RSU < p_score);
   BEGIN
      -- Boucle sur les résultats de la requête SELECT
      FOR r_source IN c_source
      LOOP
         -- Insertion dans la table de destination
        numero_dossier := r_source.dossier_id;
         UPDATE droit d
            SET D.DATE_CREATION_FIN = SYSDATE,
                D.DATE_FIN = TO_DATE ('01' || p_mois_annee, 'ddMMYYYY')
          WHERE d.id = r_source.droit_id;
          
          update DOSSIER_ASD d set D.DATE_CLOTURE  = TO_DATE ('01' || p_mois_annee, 'ddMMYYYY') where D.ID = r_source.dossier_id;

         SELECT hibernate_sequence.NEXTVAL INTO seq FROM DUAL;

         INSERT INTO droit (DATE_CREATION,
                            DATE_CREATION_FIN,
                            DATE_EFFET,
                            DATE_FIN,
                            DOSSIER_ASD_ID,
                            ID,
                            MONTANT)
              VALUES (SYSDATE,
                      SYSDATE,
                      r_source.DATE_CREATION,
                      TO_DATE ('01' || p_mois_annee, 'ddMMYYYY'),
                      r_source.dossier_id,
                      seq,
                      0);

         INSERT INTO DETAIL_DROIT (DROIT_ID,
                                   ID,
                                   MONTANT,
                                   OFFSET,
                                   RUBRIQUE)
              VALUES (
                        seq,
                        hibernate_sequence.NEXTVAL,
                        0,
                           r_source.data_cap_dem
                        || '-'
                        || r_source.data_cap_conj
                        || '-'
                        || r_source.score,
                        'FORFAITAIRE');

         COMMIT;
      END LOOP;
   END;
    EXCEPTION
    WHEN OTHERS
    THEN
        ROLLBACK;
        Insert into ESGEAF.EXCEPTION    (ID, DATE_EXCEPTION, PROCEDURE, DOSSIER, ERREUR)  Values    (hibernate_sequence.nextval, sysdate, 'CLOTURER_DROIT', to_char(numero_dossier), ''); 
        COMMIT;
END;
/
