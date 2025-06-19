CREATE OR REPLACE PROCEDURE ESGEAF.pr_alimenter_nv_doss_cloture (
    p_mois_annee   IN     VARCHAR2,
    p_message         OUT VARCHAR2)
AS
BEGIN

    p_message := 'V' ;
    INSERT INTO notification (ID,
                              IDCS,
                              CIN,
                              MOIS_ANNEE,
                              ACTIVE)
        SELECT hibernate_sequence.NEXTVAL,
               m.IDCS,
               m.CIN,
               p_mois_annee,
               0
          FROM ESGEAF.DOSSIER_ASD d, ESGEAF.M_DEMANDEUR m
         WHERE     d.ID = m.DOSSIER_ASD_ID
               AND TO_CHAR (d.DATE_CLOTURE, 'mmyyyy') = p_mois_annee;
EXCEPTION
    WHEN OTHERS
    THEN
        DBMS_OUTPUT.Put_Line ('dossier_cloture validé : ' || SQLERRM);
        p_message :=
            'Erreur : dossiers cloturés non traités ' || SUBSTR (SQLERRM, 1000);
        ROLLBACK;


        COMMIT;
END;
/
