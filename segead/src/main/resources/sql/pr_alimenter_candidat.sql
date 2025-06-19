CREATE OR REPLACE PROCEDURE ESGEAF.pr_alimenter_candidat (
    p_mois_annee   IN     VARCHAR2,
    p_message         OUT VARCHAR2)
AS
BEGIN
            
            /******************************************************************************************************/
            /* o	Pour les nouveaux dossiers (pas de droit et dossier ouvert)                   *****************/
            /***    	FORFAITAIRE : demander la situation professionnelle pour les demandeurs ;******************/
            /***    	ENFANCE :                                                                ******************/
            /***        •	Demander la situation professionnelle pour les demandeurs ;          ******************/
            /***        •	Demander la situation professionnelle pour les conjoints ;           ******************/ 
            /***        •	Demander scolarité et bourse  et SP pour les enfants ;                       ******************/
            /***        •	Demander handicap pour les enfants handicapé                         ******************/
            /***        •	Demander FEF pour le demandeur de type femme ayant la situation familiale divorcés  ***/
            /******************************************************************************************************/

            --------------------------------------	FORFAITAIRE---------------------------------
    p_message := 'V' ;
    
    INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE  )
                   
        SELECT hibernate_sequence.NEXTVAL,
               m.IDCS,
               m.CIN,
               m.DATE_NAISSANCE,
               m.GENRE,
               1,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               p_mois_annee,
               null,
               NULL,
               1,
                'DEMANDEUR'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_DEMANDEUR m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '1'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
        
         --------------------------------------	ENFANCE---------------------------------
         --------------------------	CAS DEMANDEUR-----------------
        INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE)
           SELECT hibernate_sequence.NEXTVAL,
               m.IDCS,
               m.CIN,
               m.DATE_NAISSANCE,
               m.GENRE,
               1,
               NULL,
               NULL,
               NULL,
               NULL,
               CASE WHEN (m.GENRE = 'F' AND m.ETAT_MATRIMONIAL = 'DIV') THEN 1
                    ELSE NULL END ,
               p_mois_annee,
               null,
               NULL,
               1,
               'DEMANDEUR'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_DEMANDEUR m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '2'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
        --------------------------	CAS CONJOINT-----------------
        INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE)
        SELECT hibernate_sequence.NEXTVAL,
               c.IDCS,
               c.CIN,
               c.DATE_NAISSANCE,
               NULL,
               1,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               p_mois_annee,
               null,
               NULL,
               1,
               'CONJOINT'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_CONJOINT c
         WHERE     d.ID = c.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '2'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
        --------------------------	CAS ENFANT-----------------
        INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE )
        SELECT hibernate_sequence.NEXTVAL,
               e.IDCS,
               e.CIN,
               e.DATE_NAISSANCE,
               NULL,
               1,
               NULL,
               1,
               1,
               decode (e.EST_HANDICAPE, 1, 1, null),
               NULL,
               p_mois_annee,
               CASE WHEN (e.TYPE_IDENTIFIANT_SCOLARITE = 'Massar') THEN e.IDENTIFIANT_SCOLARITE
                    ELSE NULL END ,
               NULL,
               1,
                'ENFANT'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_ENFANT e
         WHERE     d.ID = e.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '2'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
            
            
            /******************************************************************************************************/
            /* o	Pour les anciens  dossiers                                                   ******************/
            /***    	FORFAITAIRE :                                                            ******************/
            /**************  •  Demander la situation professionnelle pour les demandeurs ;      ******************/
            /**************  •	Demander le RSU pour les demandeurs ;                            ******************/
            /***    	ENFANCE :                                                                ******************/
            /***        •	Demander la situation professionnelle pour les demandeurs ;          ******************/
            /***        •	Demander la RSU pour les demandeurs ;                                ******************/ 
            /***        •	Demander la situation professionnelle pour les conjoints ;           ******************/ 
            /***        •	Demander scolarité et bourse et SP pour les enfants ;                      ******************/
            /***        •	Demander handicap pour les enfants handicapé                         ******************/
            /***        •	Demander FEF pour le demandeur de type femme ayant la situation familiale divorcés  ***/
            /******************************************************************************************************/
            
             --------------------------------------	FORFAITAIRE---------------------------------
    INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE)
                   
        SELECT hibernate_sequence.NEXTVAL,
               m.IDCS,
               m.CIN,
               m.DATE_NAISSANCE,
               m.GENRE,
               1,
               1,
               NULL,
               NULL,
               NULL,
               NULL,
               p_mois_annee,
               null,
               NULL,
               1,
               'DEMANDEUR'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_DEMANDEUR m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION  = '1'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') != p_mois_annee;
               
        --------------------------------------	ENFANCE---------------------------------
         --------------------------	CAS DEMANDEUR-----------------
        INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE  )
           SELECT hibernate_sequence.NEXTVAL,
               m.IDCS,
               m.CIN,
               m.DATE_NAISSANCE,
               m.GENRE,
               1,
               1,
               NULL,
               NULL,
               NULL,
               CASE WHEN (m.GENRE = 'F' AND m.ETAT_MATRIMONIAL = 'DIV') THEN 1
                    ELSE NULL END ,
               p_mois_annee,
               null,
               NULL,
               1,
               'DEMANDEUR'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_DEMANDEUR m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '2'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
        --------------------------	CAS CONJOINT-----------------
        INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE)
        SELECT hibernate_sequence.NEXTVAL,
               c.IDCS,
               c.CIN,
               c.DATE_NAISSANCE,
               'F',
               1,
               NULL,
               NULL,
               NULL,
               NULL,
               NULL,
               p_mois_annee,
               null,
               NULL,
               1,
               'CONJOINT'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_CONJOINT c
         WHERE     d.ID = c.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '2'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;
        --------------------------	CAS ENFANT-----------------
        INSERT INTO candidat (ID,
                          IDCS,
                          CIN,
                          DATE_NAISSANCE,
                          GENRE,
                          REQUETE_SP,
                          REQUETE_RSU,
                          REQUETE_SC,
                          REQUETE_BR,
                          REQUETE_HA,
                          REQUETE_FEF,
                          MOIS_ANNEE,
                          MASSAR,
                          CEF,
                          ACTIVE,
                          TYPE)
        SELECT hibernate_sequence.NEXTVAL,
               e.IDCS,
               e.CIN,
               e.DATE_NAISSANCE,
               NULL,
               1,
               NULL,
               1,
               1,
               decode (e.EST_HANDICAPE, 1, 1, null),
               NULL,
               p_mois_annee,
                CASE WHEN (e.TYPE_IDENTIFIANT_SCOLARITE = 'Massar') THEN e.IDENTIFIANT_SCOLARITE
                    ELSE NULL END ,
               NULL,
               1,
               'ENFANT'
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_ENFANT e
         WHERE     d.ID = e.DOSSIER_ASD_ID
               AND d.TYPE_PRESTATION = '2'
               AND TO_CHAR (d.DATE_CREATION, 'mmyyyy') = p_mois_annee;    
               
            
               
EXCEPTION
    WHEN OTHERS
    THEN
        --DBMS_OUTPUT.Put_Line ('alimentation condidat non validée : ' || substr (SQLERRM, 1, 100));
        p_message := 'E';
        ROLLBACK;

COMMIT;      
        
END;
/
