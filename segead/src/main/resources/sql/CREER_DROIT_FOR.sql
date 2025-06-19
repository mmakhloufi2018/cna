/* Formatted on 07/12/2023 15:07:36 (QP5 v5.326) */
CREATE OR REPLACE PROCEDURE ESGEAF.CREER_DROIT_FOR (
    p_mois_annee   IN VARCHAR2,
    p_score        IN NUMBER)
AS
    seq           NUMBER;
    montant       NUMBER;
    dossier       NUMBER;
    conjoin_ok    NUMBER;
    offset_conj   VARCHAR2 (255);
BEGIN
    -- Déclaration du curseur

    DECLARE
        CURSOR c_source IS
            SELECT DISTINCT
                   da.id,
                   DA.DATE_CREATION,
                   dc.MEMBRE ID_DEMENDAUR,
                      NVL (dc.EST_CHEF_MENAGE, '1')
                   || NVL (dc.RESIDE_MAROC, '1')
                   || NVL (dc.SALARIE, '0')
                   || NVL (dc.PENSIONNE, '0')
                   || NVL (dc.BENEFICIE_AF, '0')
                       data_cap_dem,
                   DC.SCORE_RSU
                       score,
                   dc.PARTENAIRE
                       PART_DEM,
                   NVL (dc.EST_CHEF_MENAGE, '1')
                       DEM_CHEF_M,
                   NVL (dc.RESIDE_MAROC, '1')
                       DEM_RES,
                   NVL (dc.SALARIE, '0')
                       DEM_SAL,
                   NVL (dc.PENSIONNE, '0')
                       DEM_PEN,
                   NVL (dc.BENEFICIE_AF, '0')
                       DEM_BEN
              FROM DATA_CAP_FOR_DEM  dc
                   JOIN DOSSIER_ASD da ON DC.DOSSIER = DA.id
                   LEFT JOIN droit dr ON DR.DOSSIER_ASD_ID = da.id
             WHERE     DR.DOSSIER_ASD_ID IS NULL
                   AND dc.MOIS_ANNEE = p_mois_annee
                   AND dc.MOIS_ANNEE = p_mois_annee;

        CURSOR data_cap_c IS
            SELECT DISTINCT
                      NVL (dcc.RESIDE_MAROC, '1')
                   || NVL (dcc.SALARIE, '0')
                   || NVL (dcc.PENSIONNE, '0')
                   || NVL (dcc.BENEFICIE_AF, '0')
                       data_cap_conj
              FROM DATA_CAP_FOR_MEM dcc
             WHERE dcc.DOSSIER = dossier AND dcc.MOIS_ANNEE = p_mois_annee;
    BEGIN
        -- Boucle sur les résultats de la requête SELECT
        FOR r_source IN c_source
        LOOP
            -- Insertion dans la table de destination
            SELECT hibernate_sequence.NEXTVAL INTO seq FROM DUAL;

            IF r_source.data_cap_dem = '11000' AND r_source.score < p_score
            THEN
                dossier := r_source.id;
                conjoin_ok := 1;
                offset_conj := NULL;

                FOR r_data_cap_c IN data_cap_c
                LOOP
                    IF r_data_cap_c.data_cap_conj <> '1000'
                    THEN
                        conjoin_ok := 0;
                    END IF;

                    offset_conj :=
                        offset_conj || r_data_cap_c.data_cap_conj || ' ';
                END LOOP;

                IF conjoin_ok = 1
                THEN
                    montant := 500;
                ELSE
                    montant := 0;
                END IF;
            ELSE
                montant := 0;
            END IF;



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

            INSERT INTO CONSTAT (ID,
                                 DROIT_ID,
                                 TYPE_MEMBRE,
                                 ID_MEMBRE,
                                 CONSTAT)
                     VALUES (
                         hibernate_sequence.NEXTVAL,
                         seq,
                         'DEMENDEUR',
                         r_source.ID_DEMENDAUR,
                            'RES:'
                         || r_source.DEM_RES
                         || ':'
                         || r_source.PART_DEM
                         || ';'
                         || 'SAL:'
                         || r_source.DEM_SAL
                         || ':'
                         || r_source.PART_DEM
                         || ';'
                         || 'RET:'
                         || r_source.DEM_PEN
                         || ':'
                         || r_source.PART_DEM
                         || ';'
                         || 'AF:'
                         || r_source.DEM_BEN
                         || ':'
                         || r_source.PART_DEM
                         || ';'
                         || 'CHEF_M:'
                         || r_source.DEM_CHEF_M
                         || ':'
                         || r_source.PART_DEM
                         || ';'
                         || 'RSU:'
                         || r_source.score
                         || ':'
                         || r_source.PART_DEM
                         || ';');

            INSERT INTO DETAIL_DROIT (DROIT_ID,
                                      ID,
                                      MONTANT,
                                      OFFSET,
                                      RUBRIQUE)
                     VALUES (
                         seq,
                         hibernate_sequence.NEXTVAL,
                         montant,
                            'DEM:'
                         || r_source.data_cap_dem
                         || ' - CONJ:'
                         || offset_conj
                         || '- SCORE:'
                         || r_source.score,
                         'FORFAITAIRE');

            COMMIT;
        END LOOP;
    END;
    EXCEPTION
    WHEN OTHERS
    THEN
        ROLLBACK;
        Insert into ESGEAF.EXCEPTION    (ID, DATE_EXCEPTION, PROCEDURE, DOSSIER, ERREUR)  Values    (hibernate_sequence.nextval, sysdate, 'CREER_DROIT_FOR', to_char(dossier), ''); 
        COMMIT;
END;
/