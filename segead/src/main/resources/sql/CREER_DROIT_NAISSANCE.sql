CREATE OR REPLACE PROCEDURE ESGEAF.CREER_DROIT_NAISSANCE
AS
    rang_enfant   NUMBER;
    seq           NUMBER;
    prime         NUMBER;
    DOSSIER_ID    NUMBER;
BEGIN
    -- Déclaration du curseur
    prime := 0;

    DECLARE
        CURSOR c_source IS
            SELECT n.membre,
                   n.dossier,
                   d.OFFSET,
                   n.id,
                   d.MONTANT,
                   MONTHS_BETWEEN (n.DATE_EFFET, e.DATE_NAISSANCE)
                       agemois
              FROM naissance  n
                   JOIN DETAIL_DROIT d ON d.MEMBRE = n.membre
                   JOIN droit dr ON dr.id = d.DROIT_ID
                   JOIN dossier_asd do ON do.ID = n.dossier
                   JOIN m_enfant e
                       ON e.id = n.membre AND e.DOSSIER_ASD_ID = do.ID
             WHERE     n.status = '0'
                   AND dr.DATE_FIN IS NULL
                   AND do.DATE_CLOTURE IS NULL;
    BEGIN
        FOR r_source IN c_source
        LOOP
            DOSSIER_ID := r_source.dossier;
            rang_enfant := 0;
            prime := 0;

            SELECT rang
              INTO rang_enfant
              FROM (SELECT ROW_NUMBER ()
                               OVER (PARTITION BY DOSSIER_ASD_ID, CIN_MERE
                                     ORDER BY e.date_naissance asc, id ASC)
                               rang,
                           e.*
                      FROM M_enfant e
                     WHERE e.DOSSIER_ASD_ID = r_source.dossier)
             WHERE id = r_source.membre;

            IF r_source.agemois <= 6
            THEN
                IF rang_enfant = 1 AND r_source.MONTANT > 0
                THEN
                    prime := 2000;
                END IF;

                IF rang_enfant = 2 AND r_source.MONTANT > 0
                THEN
                    prime := 1000;
                END IF;
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
                         SYSDATE,
                         SYSDATE,
                         NULL,
                         r_source.DOSSIER,
                         seq,
                         prime);
            Insert into CONSTAT (ID, DROIT_ID, TYPE_MEMBRE, ID_MEMBRE, CONSTAT) Values
					(hibernate_sequence.NEXTVAL, seq, 'ENFANT', r_source.membre,
					'RANG:'   ||rang_enfant        ||':'||';' ||
					'AGE:'   ||r_source.agemois		   ||':;' 
					);
            INSERT INTO DETAIL_DROIT (DROIT_ID,
                                      ID,
                                      MONTANT,
                                      OFFSET,
                                      RUBRIQUE,
                                      MEMBRE)
                 VALUES (seq,
                         hibernate_sequence.NEXTVAL,
                         prime,
                         'Rang ' || rang_enfant || ' ' || r_source.OFFSET,
                         'PRIME_NAISSANCE',
                         r_source.membre);


            UPDATE naissance
               SET status = '1'
             WHERE id = r_source.id;

            COMMIT;
        END LOOP;
    END;
EXCEPTION
    WHEN OTHERS
    THEN
        ROLLBACK;

        INSERT INTO ESGEAF.EXCEPTION (ID,
                                      DATE_EXCEPTION,
                                      PROCEDURE,
                                      DOSSIER,
                                      ERREUR)
             VALUES (hibernate_sequence.NEXTVAL,
                     SYSDATE,
                     'CREER_DROIT_NAISSANCE',
                     TO_CHAR (DOSSIER_ID),
                     '');

        COMMIT;
END;
/