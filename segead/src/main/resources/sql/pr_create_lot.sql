CREATE OR REPLACE PROCEDURE ESGEAF.pr_create_lot (
    p_message         OUT VARCHAR2)
IS 
n         NUMBER := 0;
num_lot   NUMBER := 0;
id        NUMBER := 99999999;  
BEGIN
    
    p_message := 'V' ;
    select count(1) into n from paiement p where p.LOT_PAIEMENT_ID is null;
    select max(l.NUMERO_LOT)  into num_lot from lot_paiement l;
    n:= TRUNC (n/1000);
    
    FOR i IN 1..n LOOP
    -- Instructions à exécuter à chaque itération
     DBMS_OUTPUT.PUT_LINE('Valeur de i : ' || i);
     id := HIBERNATE_SEQUENCE.NEXTVAL ;
     INSERT INTO lot_paiement (   ID                  ,
                                  DATE_ECHEANCE       ,
                                  NUMERO_LOT          ,
                                  REFERENCE_CNSS      ,
                                  REFERENCE_ECHEANCE  ,
                                  STATUT              ,
                                  TYPE_PRESTATAION    )
                                  values (id, null, num_lot+ i, null, null, null, null);
                                  
      update    paiement  p set p.LOT_PAIEMENT_ID = i where  p.LOT_PAIEMENT_ID is null and rownum <= 1000;                   
     
  END LOOP;
    
EXCEPTION
    WHEN OTHERS
    THEN
        DBMS_OUTPUT.Put_Line ('pr_create_lot validé : ' || SQLERRM);
        p_message :=
            'Erreur : lot n est pas crée ' || SUBSTR (SQLERRM, 1000);
        ROLLBACK;


        COMMIT;
END;
/
