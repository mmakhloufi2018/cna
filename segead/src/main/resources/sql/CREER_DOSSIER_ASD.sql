CREATE OR REPLACE PROCEDURE ESGEAF.CREER_DOSSIER_ASD
AS
    ID_DOSSIER_ASD   NUMBER;
BEGIN
    DECLARE
        CURSOR c_source IS
              SELECT DISTINCT DD.*,
                              D.DATE_DEMANDE,
                              D.IMMATRICULATION,
                              D.REFERENCE_CNSS,
                              D.TYPE_PRESTATION,
                              D.ID     ID_DEMANDE                --DOSSIER_ASD
                FROM DEMANDE d JOIN d_demandeur dd ON DD.DEMANDE_ID = d.id
               WHERE D.STATUT = 'RECEPTIONNE'
            ORDER BY d.TYPE_PRESTATION ASC, d.DATE_DEMANDE;
    BEGIN
        -- Boucle sur les résultats de la requête SELECT

        FOR r_source IN c_source
        LOOP
            SELECT hibernate_sequence.NEXTVAL INTO ID_DOSSIER_ASD FROM DUAL;

            --DBMS_OUTPUT.Put_Line ('ID_DEMANDE :' || r_source.ID_DEMANDE);
            IF r_source.TYPE_PRESTATION = '3'
            THEN
                INSERT INTO NAISSANCE (ID,
                                       dossier,
                                       MEMBRE,
                                       STATUS,
                                       DATE_EFFET)
                    SELECT hibernate_sequence.NEXTVAL,
                           (SELECT MAX (id)
                              FROM DOSSIER_ASD
                             WHERE IMMATRICULATION = r_source.IMMATRICULATION),
                           ( select id  from M_ENFANT m where m.IDCS = de.idcs  ),
                           '0',
                           r_source.DATE_DEMANDE
                      FROM D_enfant de
                      where de.DEMANDEUR_ID =  r_source.id  ;
            ELSE
                INSERT INTO DOSSIER_ASD (CODE_MENAGE_RSU,
                                         DATE_CREATION,
                                         ID,
                                         IMMATRICULATION,
                                         REFERENCE,
                                         TYPE_PRESTATION)
                     VALUES (r_source.CODE_MENAGE_RSU,
                             r_source.DATE_DEMANDE,
                             ID_DOSSIER_ASD,
                             r_source.IMMATRICULATION,
                             r_source.REFERENCE_CNSS,
                             r_source.TYPE_PRESTATION);

                --DBMS_OUTPUT.Put_Line ('INSERT DOSSIER ID  :' || ID_DOSSIER_ASD);

                INSERT INTO M_DEMANDEUR (ID,
                                         ADRESSE,
                                         CIN,
                                         CODE_MENAGE_RSU,
                                         DATE_DEBUT,
                                         DATE_FIN,
                                         DATE_NAISSANCE,
                                         EST_CHEF_MENAGE_RSU,
                                         ETAT_MATRIMONIAL,
                                         GENRE,
                                         IDCS,
                                         NOM_AR,
                                         NOM_FR,
                                         PRENOM_AR,
                                         PRENOM_FR,
                                         RESIDE_AU_MAROC,
                                         SCORE_RSU,
                                         DOSSIER_ASD_ID)
                     VALUES (ID_DOSSIER_ASD,
                             r_source.ADRESSE,
                             r_source.CIN,
                             r_source.CODE_MENAGE_RSU,
                             r_source.DATE_DEMANDE,
                             NULL,
                             r_source.DATE_NAISSANCE,
                             r_source.EST_CHEF_MENAGE_RSU,
                             r_source.ETAT_MATRIMONIAL,
                             r_source.GENRE,
                             r_source.IDCS,
                             r_source.NOM_AR,
                             r_source.NOM_FR,
                             r_source.PRENOM_AR,
                             r_source.PRENOM_FR,
                             r_source.RESIDE_AU_MAROC,
                             r_source.SCORE_RSU,
                             ID_DOSSIER_ASD);

                -- DBMS_OUTPUT.Put_Line ('INSERT M_DEMANDEUR ID  :' || ID_DOSSIER_ASD);
                UPDATE DOSSIER_ASD do
                   SET DO.MEMBRE_DEMANDEUR_ID = ID_DOSSIER_ASD
                 WHERE DO.ID = ID_DOSSIER_ASD;

                INSERT INTO M_CONJOINT (ID,
                                        CIN,
                                        CODE_MENAGE_RSU,
                                        DATE_DEBUT,
                                        DATE_FIN,
                                        DATE_NAISSANCE,
                                        IDCS,
                                        LIEN_PARENTE,
                                        NOM_AR,
                                        NOM_FR,
                                        PRENOM_AR,
                                        PRENOM_FR,
                                        RESIDE_AU_MAROC,
                                        SCORE_RSU,
                                        DOSSIER_ASD_ID)
                    SELECT hibernate_sequence.NEXTVAL,
                           CIN,
                           CODE_MENAGE_RSU,
                           r_source.DATE_DEMANDE,
                           NULL,
                           DATE_NAISSANCE,
                           IDCS,
                           LIEN_PARENTE,
                           NOM_AR,
                           NOM_FR,
                           PRENOM_AR,
                           PRENOM_FR,
                           RESIDE_AU_MAROC,
                           SCORE_RSU,
                           ID_DOSSIER_ASD
                      FROM D_CONJOINT dc
                     WHERE dc.DEMANDEUR_ID = r_source.id;

                --   DBMS_OUTPUT.Put_Line ('INSERT M_CONJOINT ID  :' || r_source.id);

                INSERT INTO ESGEAF.M_ENFANT (ID,
                                             CIN,
                                             CIN_MERE,
                                             CIN_PERE,
                                             CODE_MENAGE_RSU,
                                             DATE_DEBUT,
                                             DATE_FIN,
                                             DATE_NAISSANCE,
                                             EST_HANDICAPE,
                                             EST_ORPHELIN_PERE,
                                             IDCS,
                                             IDCS_MERE,
                                             IDCS_PERE,
                                             IDENTIFIANT_SCOLARITE,
                                             LIEN_PARENTE,
                                             NOM_AR,
                                             NOM_FR,
                                             PRENOM_AR,
                                             PRENOM_FR,
                                             RESIDE_AU_MAROC,
                                             SCOLARISE,
                                             SCORE_RSU,
                                             TYPE_IDENTIFIANT_SCOLARITE,
                                             TYPE_SCOLARITE,
                                             DOSSIER_ASD_ID)
                    SELECT hibernate_sequence.NEXTVAL,
                           CIN,
                           CIN_MERE,
                           CIN_PERE,
                           CODE_MENAGE_RSU,
                           r_source.DATE_DEMANDE,
                           NULL,
                           DATE_NAISSANCE,
                           EST_HANDICAPE,
                           EST_ORPHELIN_PERE,
                           IDCS,
                           IDCS_MERE,
                           IDCS_PERE,
                           IDENTIFIANT_SCOLARITE,
                           LIEN_PARENTE,
                           NOM_AR,
                           NOM_FR,
                           PRENOM_AR,
                           PRENOM_FR,
                           RESIDE_AU_MAROC,
                           SCOLARISE,
                           SCORE_RSU,
                           TYPE_IDENTIFIANT_SCOLARITE,
                           TYPE_SCOLARITE,
                           ID_DOSSIER_ASD
                      FROM D_ENFANT de
                     WHERE de.DEMANDEUR_ID = r_source.id;

                INSERT INTO M_AUTRE_MEMBRE (ID,
                                            CIN,
                                            CODE_MENAGE_RSU,
                                            DATE_DEBUT,
                                            DATE_FIN,
                                            DATE_NAISSANCE,
                                            IDCS,
                                            LIEN_PARENTE,
                                            NOM_AR,
                                            NOM_FR,
                                            PRENOM_AR,
                                            PRENOM_FR,
                                            RESIDE_AU_MAROC,
                                            SCORE_RSU,
                                            DOSSIER_ASD_ID)
                    SELECT hibernate_sequence.NEXTVAL,
                           CIN,
                           CODE_MENAGE_RSU,
                           r_source.DATE_DEMANDE,
                           NULL,
                           DATE_NAISSANCE,
                           IDCS,
                           LIEN_PARENTE,
                           NOM_AR,
                           NOM_FR,
                           PRENOM_AR,
                           PRENOM_FR,
                           RESIDE_AU_MAROC,
                           SCORE_RSU,
                           ID_DOSSIER_ASD
                      FROM D_AUTRE_MEMBRE da
                     WHERE da.DEMANDEUR_ID = r_source.id;
            END IF;

            UPDATE DEMANDE de
               SET DE.STATUT = 'TRAITE'
             WHERE de.id = r_source.ID_DEMANDE;

            --DBMS_OUTPUT.Put_Line (' update DEMANDE  :' || r_source.ID_DEMANDE);
            COMMIT;
        END LOOP;
    END;
END;
/