/*
Mascon Dump
Source Host:           localhost
Source Server Version: 3.23.53-max-nt
Source Database:       ciapl
Date:                  2003-11-19 15:57:19
*/

#----------------------------
# Table structure for CATEGORY
#----------------------------
drop table if exists CATEGORY;
create table CATEGORY (
   ID_INTERNAL int(11) not null auto_increment,
   CODE varchar(20) not null,
   LONG_NAME varchar(50) not null,
   SHORT_NAME varchar(20) not null,
   primary key (ID_INTERNAL),
   unique U1 (CODE))
   type=InnoDB comment="InnoDB free: 211968 kB";

#----------------------------
# Records for table CATEGORY
#----------------------------



insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (2, 'AS1', 'PRIM.ASSISTENTE', 'PRIMEIRO ASSISTENTE') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (3, 'AS2', 'SEG.ASSISTENTE', 'SEGUNDO ASSISTENTE') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (4, 'ASC', 'ASSIST.CONVIDADO', 'ASSISTENTE CONVIDADO') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (5, 'ASE', 'ASSIST.ESTAGIARIO', 'ASSISTENTE ESTAGIARI') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (6, 'ASG', 'ASSIST.ESTAGIARIO', 'ASSISTENTE ESTAGIARI') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (7, 'AST', 'ASSISTENTE', 'ASSISTENTE') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (8, 'ASV', 'ASSIST.EVENTUAL', 'ASSISTENTE EVENTUAL') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (9, 'EAE', 'EQU.ASSIST.EVENTUAL', 'EQUIPARADO A ASSISTE') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (10, 'EAS', 'EQU.ASSISTENTE', 'EQUIPARADO A ASSISTE') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (11, 'EPA', 'EQU.PROF.AUXILIAR', 'EQUIPARADO A PROFESS') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (12, 'EPC', 'EQU.PROF.CATEDR.', 'EQUIPARADO A PROFESS') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (13, 'EPE', 'EQU.PROF.EXTRAORD.', 'EQUIPARADO A PROFESS') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (14, 'ESP', 'ESPECIALISTA', 'ESPECIALISTA') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (15, 'LEI', 'LEITOR', 'LEITOR') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (16, 'MNL', 'MONITOR-ECD C/LIC', 'MONITOR-E.C.D. COM L') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (17, 'MNT', 'MONITOR-ECD S/LIC', 'MONITOR-E.C.D. SEM L') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (18, 'PAG', 'PROF.AGREGADO', 'PROFESSOR AGREGADO') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (19, 'PAS', 'PROF.ASSOCIADO', 'PROFESSOR ASSOCIADO') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (20, 'PAX', 'PROF.AUXILIAR', 'PROFESSOR AUXILIAR') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (21, 'PCA', 'PROF.CATEDRATICO', 'PROFESSOR CATEDRATIC') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (22, 'PCC', 'PROF.CAT.CONVIDADO', 'PROFESSOR CATEDRATIC') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (23, 'PCV', 'PROF.CAT.VISITANTE', 'PROFESSOR CATEDRATIC') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (24, 'PEF', 'PROF.EDUC.FISICA', 'PROFESSOR DE EDUCACA') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (25, 'PEX', 'PROF.EXTRAORDINARIO', 'PROFESSOR EXTRAORDIN') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (26, 'PSC', 'PROF.ASSOC.CONVIDADO', 'PROFESSOR ASSOCIADO') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (27, 'PSV', 'PROF.ASSOC.VISITANTE', 'PROFESSOR ASSOCIADO') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (28, 'PXC', 'PROF.AUX.CONVIDADO', 'PROFESSOR AUXILIAR C') ;
insert  into CATEGORY ( ID_INTERNAL,CODE,LONG_NAME,SHORT_NAME) values (29, 'PXV', 'PROF.AUX.VISITANTE', 'PROFESSOR AUXILIAR V') ;

