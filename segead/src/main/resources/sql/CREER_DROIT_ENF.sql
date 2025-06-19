CREATE OR REPLACE PROCEDURE ESGEAF.CREER_DROIT_ENF (  p_mois_annee   IN     VARCHAR2 , p_score in number)
AS
   seq       NUMBER;
   montant_2024   NUMBER;
   montant_2025   NUMBER;
   montant_2026   NUMBER;
   montant_dossier NUMBER;
   montant_dossier_2024   NUMBER;
   montant_dossier_2025   NUMBER;
   montant_dossier_2026   NUMBER;
   montant_enfant NUMBER;
   numero_dossier NUMBER;
   count_constat_conj NUMBER;
   
BEGIN
   -- Déclaration du curseur
    numero_dossier := 0;
    montant_dossier := 0;
	count_constat_conj := 0;
   DECLARE
      CURSOR c_source
      IS
      select ROW_NUMBER()  OVER (PARTITION BY DOSSIER ORDER BY  CASE
           WHEN (age <= 20  or  (EST_HANDICAPE = '1' and ORPHELIN_PERE = '1')) and data_cap_dem = '11000'
            AND data_cap_conj = '1000'
            AND data_cap_enf  = '1000' THEN to_number(age || ORPHELIN_PERE || SCOLARISE|| EST_HANDICAPE )
           ELSE -to_number(age || ORPHELIN_PERE || SCOLARISE|| EST_HANDICAPE ) 
         END DESC) AS rang_enfant  , a.* from (
        select distinct DC_ENF.DOSSIER ,nvl(DC_ENF.EST_HANDICAPE  ,'0') EST_HANDICAPE ,  nvl(DC_ENF.age,0)  age, ENF.DOSSIER_ASD_ID, D.DATE_CREATION,DC_DEM.SCORE_RSU score,
         enf.id ID_ENFANT,nvl(DC_ENF.ORPHELIN_PERE,'0') ORPHELIN_PERE, nvl(DC_ENF_S.MONTANT_BOURSE,0)  MONTANT_BOURSE , DC_ENF_S.PARTENAIRE PART_ENF_S,
        NVL (DC_DEM.EST_CHEF_MENAGE, '1')  || NVL (DC_DEM.RESIDE_MAROC, '1')  || NVL (DC_DEM.SALARIE, '0') || NVL (DC_DEM.PENSIONNE, '0') || NVL (DC_DEM.BENEFICIE_AF, '0') data_cap_dem,DC_DEM.PARTENAIRE PART_DEM,
		NVL (DC_DEM.EST_CHEF_MENAGE, '1') DEM_CHEF_M  , NVL (DC_DEM.RESIDE_MAROC, '1') DEM_RES, NVL (DC_DEM.SALARIE, '0') DEM_SAL, NVL (DC_DEM.PENSIONNE, '0') DEM_PEN, NVL (DC_DEM.BENEFICIE_AF, '0') DEM_BEN,
        NVL (DC_CONJ.RESIDE_MAROC, '1') || NVL (DC_CONJ.SALARIE, '0') || NVL (DC_CONJ.PENSIONNE, '0')|| NVL (DC_CONJ.BENEFICIE_AF, '0') data_cap_conj,DC_CONJ.PARTENAIRE PART_MEM,
		NVL (DC_CONJ.RESIDE_MAROC, '1') MEM_RES, NVL (DC_CONJ.SALARIE, '0') MEM_SAL, NVL (DC_CONJ.PENSIONNE, '0')MEM_PEN, NVL (DC_CONJ.BENEFICIE_AF, '0') MEM_AF,
        NVL (DC_ENF.RESIDE_MAROC, '1') || NVL (DC_ENF.SALARIE, '0') || NVL (DC_ENF.PENSIONNE, '0')|| NVL (DC_ENF.BENEFICIE_AF, '0') data_cap_enf,
		NVL (DC_ENF.RESIDE_MAROC, '1')ENF_RES , NVL (DC_ENF.SALARIE, '0') ENF_SAL, NVL (DC_ENF.PENSIONNE, '0')ENF_PEN, NVL (DC_ENF.BENEFICIE_AF, '0') ENF_BEN,DC_ENF.PARTENAIRE PART_ENF,
        NVL(DC_ENF_S.SCOLARISE,'1') SCOLARISE, mc.CIN,DC_CONJ.ID DC_CONJ_ID,
        DC_CONJ.CODE_RSU CODE_RSU_CONJ , DC_DEM.CODE_RSU CODE_RSU_DEM,DC_ENF.CODE_RSU CODE_RSU_ENF , DC_DEM.MEMBRE ID_DEMENDAUR
        from  DATA_CAP_ENF_DEM DC_DEM join 
        DOSSIER_ASD D on DC_DEM.dossier = d.id
        join M_enfant enf on ENF.DOSSIER_ASD_ID = D.ID
        left join droit dr on DR.DOSSIER_ASD_ID = d.id
        left join M_conjoint mc on mc.DOSSIER_ASD_ID = d.id and mc.IDCS = enf.IDCS_MERE
        left join DATA_CAP_ENF_CONJ DC_CONJ on mc.id = DC_CONJ.MEMBRE    and DC_CONJ.MOIS_ANNEE =p_mois_annee
        join DATA_CAP_ENF_ENF DC_ENF on  DC_ENF.MEMBRE = enf.id     and DC_ENF.MOIS_ANNEE = p_mois_annee  --DC_ENF.DOSSIER = D.ID  And
        left join DATA_CAP_ENF_ENF_SCOL DC_ENF_S on  DC_ENF_S.MEMBRE = enf.id  and DC_ENF_S.MOIS_ANNEE = p_mois_annee --DC_ENF_S.DOSSIER = D.ID  And
        where DR.ID is null
        and DC_DEM.MOIS_ANNEE =p_mois_annee
        and D.DATE_CLOTURE is null) a ;
   BEGIN
      -- Boucle sur les résultats de la requête SELECT
     
      FOR r_source IN c_source
      LOOP
         -- Insertion dans la table de destination
         montant_2024   := 0;
         montant_2025   := 0;
         montant_2026   := 0;
        
         IF     r_source.data_cap_dem = '11000'
            AND r_source.data_cap_conj = '1000'
            AND r_source.score < p_score
            AND r_source.data_cap_enf  = '1000'
           -- AND r_source.CODE_RSU_CONJ = r_source.CODE_RSU_DEM
           -- AND r_source.CODE_RSU_ENF = r_source.CODE_RSU_DEM
         THEN
            IF
                r_source.age > 21 and nvl(r_source.est_handicape,'0') <> '1'
            THEN
                montant_2024   := 0;
                montant_2025   := 0;
                montant_2026   := 0;
            ELSE
                IF  r_source.rang_enfant < 4 THEN
                    IF r_source.age < 6 THEN
                        IF r_source.est_handicape = '1'  THEN
                            IF r_source.ORPHELIN_PERE = '1' THEN
                                montant_2024 := 450;
                                montant_2025 := 475;
                                montant_2026 := 500;
                            ELSE
                                montant_2024 := 300;
                                montant_2025 := 350;
                                montant_2026 := 400;
                            END IF;
                        ELSE
                            IF r_source.ORPHELIN_PERE = '1' THEN
                                montant_2024 := 350;
                                montant_2025 := 375;
                                montant_2026 := 400;
                            ELSE
                                montant_2024 := 200;
                                montant_2025 := 250;
                                montant_2026 := 300;
                            END IF;
                        END IF;
                        
                     END IF;
                    IF r_source.age >= 6 AND r_source.age < 21 THEN
                            IF r_source.est_handicape = '1' AND r_source.ORPHELIN_PERE = '1'THEN
                                montant_2024 := 450;
                                montant_2025 := 475;
                                montant_2026 := 500;
                            END IF;
                           
                            IF r_source.est_handicape = '1' AND r_source.ORPHELIN_PERE = '0' THEN
                                montant_2024 := 300;
                                montant_2025 := 350;
                                montant_2026 := 400;
                            END IF;
                            
                            
                            --NON HANDICAPE
                            IF r_source.est_handicape = '0' AND r_source.ORPHELIN_PERE = '1' AND r_source.SCOLARISE = '1'THEN
                                montant_2024 := 350;
                                montant_2025 := 375;
                                montant_2026 := 400;
                            END IF;
                            IF r_source.est_handicape = '0' AND r_source.ORPHELIN_PERE = '0' AND r_source.SCOLARISE = '1'THEN
                                montant_2024 := 200;
                                montant_2025 := 250;
                                montant_2026 := 300;
                            END IF;
                            IF r_source.est_handicape = '0' AND r_source.SCOLARISE = '0'  THEN
                                montant_2024 := 150;
                                montant_2025 := 175;
                                montant_2026 := 200;
                            END IF;
                                                
                        END IF;
                    IF r_source.age >= 21 AND r_source.est_handicape = '1' AND r_source.ORPHELIN_PERE = '1' THEN
                            montant_2024 := 450;
                            montant_2025 := 475;
                            montant_2026 := 500;
                        END IF;
                END IF;
                
                IF  r_source.rang_enfant > 3 AND r_source.rang_enfant < 7 THEN
                    IF r_source.age < 6 AND r_source.est_handicape = '1' THEN
                        montant_2024 := 136;
                        montant_2025 := 136;
                        montant_2026 := 136;
                    END IF;
                    IF r_source.age < 6 AND r_source.est_handicape <> '1' THEN
                        montant_2024 := 36;
                        montant_2025 := 36;
                        montant_2026 := 36;
                    END IF;
                    
                    IF r_source.age >= 6 AND r_source.age < 21 THEN
                        IF r_source.est_handicape = '1' THEN--AND r_source.SCOLARISE = '1'
                            montant_2024 := 136;
                            montant_2025 := 136;
                            montant_2026 := 136;
                        END IF;
                      --  IF r_source.est_handicape = '1' AND r_source.SCOLARISE = '0'THEN
                      --      montant_2024 := 124;
                      --      montant_2025 := 124;
                      --      montant_2026 := 124;
                      -- END IF;
                        IF r_source.est_handicape = '0' AND r_source.SCOLARISE = '1'THEN
                            montant_2024 := 36;
                            montant_2025 := 36;
                            montant_2026 := 36;
                        END IF;
                        IF r_source.est_handicape = '0' AND r_source.SCOLARISE = '0'THEN
                            montant_2024 := 24;
                            montant_2025 := 24;
                            montant_2026 := 24;
                        END IF;
                    END IF;
                    
                    IF r_source.age >= 21 AND r_source.est_handicape = '1' AND r_source.ORPHELIN_PERE = '1' THEN
                        montant_2024 := 136;
                        montant_2025 := 136;
                        montant_2026 := 136;
                    END IF;
                END IF;
                
                IF  r_source.rang_enfant > 6 THEN
                    IF (r_source.est_handicape = '1' AND r_source.age < 21) OR (r_source.est_handicape = '1' AND r_source.ORPHELIN_PERE = '1') THEN
                        montant_2024 := 100;
                        montant_2025 := 100;
                        montant_2026 := 100;
                    ELSE
                        montant_2024 := 0;
                        montant_2025 := 0;
                        montant_2026 := 0;
                    END IF;
                END IF;
            END IF;
         ELSE
            montant_2024 := 0;
            montant_2025 := 0;
            montant_2026 := 0;
         END IF;
         IF r_source.MONTANT_BOURSE > 0 THEN
            --payer = max (droit mensuel calculé – X/12 ; 0). 
            montant_2024 := GREATEST(montant_2024 - (r_source.MONTANT_BOURSE / 12),0);
            montant_2025 := GREATEST(montant_2025 - (r_source.MONTANT_BOURSE / 12),0);
            montant_2026 := GREATEST(montant_2026 - (r_source.MONTANT_BOURSE / 12),0);
         END IF;

        IF numero_dossier <> r_source.DOSSIER_ASD_ID THEN
            --MAJ montant pour le dossier precedant
            IF montant_dossier > 0  and numero_dossier > 0 THEN
                IF montant_dossier < 500 THEN
                    montant_dossier := 500;
                END IF;

                UPDATE DROIT SET MONTANT = montant_dossier
                WHERE DOSSIER_ASD_ID = numero_dossier;
            END IF;
            --NOUUVEAU DOSSIER
           montant_dossier_2024 := 0;
           montant_dossier_2025 := 0;
           montant_dossier_2026 := 0;
           montant_dossier := 0;
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
                      r_source.DOSSIER_ASD_ID,
                      seq,
                      0);
              numero_dossier := r_source.DOSSIER_ASD_ID;
			  
			  Insert into CONSTAT (ID, DROIT_ID, TYPE_MEMBRE, ID_MEMBRE, CONSTAT) Values
				(hibernate_sequence.NEXTVAL, seq, 'DEMENDEUR', r_source.ID_DEMENDAUR,
				'RES:'   ||r_source.DEM_RES        ||':'||r_source.PART_DEM||';'|| 
				'SAL:'   ||r_source.DEM_SAL		   ||':'||r_source.PART_DEM||';'|| 
				'RET:'   ||r_source.DEM_PEN        ||':'||r_source.PART_DEM||';'|| 
				'AF:'    ||r_source.DEM_BEN        ||':'||r_source.PART_DEM||';'|| 
				'CHEF_M:'||r_source.DEM_CHEF_M     ||':'||r_source.PART_DEM||';'|| 
				'RSU:'   ||r_source.score          ||':'||r_source.PART_ENF||';' 
				);

        
        END IF;
         
         INSERT INTO DETAIL_DROIT (DROIT_ID,
                                   ID,
                                   MONTANT,
                                   OFFSET,
                                   RUBRIQUE,
                                   MEMBRE
                                   )
              VALUES (seq,
                      hibernate_sequence.NEXTVAL,
                      montant_2024,
                      'DEM:' || r_source.data_cap_dem || ' - CONJ CIN:' ||r_source.CIN || ' ' || r_source.data_cap_conj || '- ENF:' || r_source.data_cap_enf || '- RANG:' || r_source.rang_enfant || '- HANDICAPE:' || r_source.est_handicape
                        || '- AGE:' ||r_source.age    || '- SCOLARISE:' ||r_source.SCOLARISE || '-ORPHELIN_PERE:'||r_source.ORPHELIN_PERE               || '- SCORE:' ||                        r_source.score,                
                      'ENFANCE' , r_source.ID_ENFANT );
					  
			Insert into CONSTAT (ID, DROIT_ID, TYPE_MEMBRE, ID_MEMBRE, CONSTAT) Values
			(hibernate_sequence.NEXTVAL, seq, 'ENFANT', r_source.ID_ENFANT,
			'RES:'   ||r_source.ENF_RES        ||':'||r_source.PART_ENF||';'||
			'SAL:'   ||r_source.ENF_SAL		   ||':'||r_source.PART_ENF||';'|| 
			'RET:'   ||r_source.ENF_PEN        ||':'||r_source.PART_ENF||';'|| 
			'AF:'    ||r_source.ENF_BEN        ||':'||r_source.PART_ENF||';'|| 
			'RANG:'  ||r_source.rang_enfant    ||':'||r_source.PART_ENF_S||';'|| 
			'HAN:'   ||r_source.est_handicape  ||':'||r_source.PART_ENF_S||';'|| 
			'AGE:'   ||r_source.age            ||':'||r_source.PART_ENF||';'|| 	  
			'SCO:'   ||r_source.SCOLARISE      ||':'||r_source.PART_ENF_S||';'||
			'BRS:'   ||r_source.MONTANT_BOURSE ||':'||r_source.PART_ENF_S||';'|| 			
			'ORPH_P:'||r_source.ORPHELIN_PERE  ||':'||r_source.PART_ENF||';'	
			);
			
			
			
			
				
			
			
			IF r_source.DC_CONJ_ID > 0 THEN
				select count(1) into count_constat_conj from CONSTAT c where DROIT_ID = seq and ID_MEMBRE = r_source.DC_CONJ_ID;
				IF count_constat_conj = 0 THEN
					Insert into CONSTAT (ID, DROIT_ID, TYPE_MEMBRE, ID_MEMBRE, CONSTAT) Values
					(hibernate_sequence.NEXTVAL, seq, 'CONJOINT', r_source.DC_CONJ_ID,
					'RES:'   ||r_source.MEM_RES        ||':'||r_source.PART_MEM||';' ||
					'SAL:'   ||r_source.MEM_SAL		   ||':'||r_source.PART_MEM||';' ||
					'RET:'   ||r_source.MEM_PEN        ||':'||r_source.PART_MEM||';' ||
					'AF:'    ||r_source.MEM_AF         ||':'||r_source.PART_MEM||';'
					);
				END IF;
			END IF;
			 
COMMIT;
		  
                  
           montant_dossier := montant_dossier + montant_2024;
         COMMIT;
         
      END LOOP;
      
       IF montant_dossier > 0  and numero_dossier > 0 THEN
                IF montant_dossier < 500 THEN
                    montant_dossier := 500;
                END IF;
                UPDATE DROIT SET MONTANT = montant_dossier   WHERE DOSSIER_ASD_ID = numero_dossier;
                commit;
            END IF;
            
   END;
   EXCEPTION
    WHEN OTHERS
    THEN
        ROLLBACK;
        Insert into ESGEAF.EXCEPTION    (ID, DATE_EXCEPTION, PROCEDURE, DOSSIER, ERREUR)  Values    (hibernate_sequence.nextval, sysdate, 'CREER_DROIT_ENF', to_char(numero_dossier), ''); 
        COMMIT;
END;
/