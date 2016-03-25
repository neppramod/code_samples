Complete project at github.com/neppramod/distributed

 Introduction
 ============
 A distributed medical record system using JINI. Source code compiled in java 8, should run on Apache River Library.
 
 For simplicity, model data is stored statically.
 
 
 1.RMID
 rmid
 
 2. HTTP

cd /home/dell/ds/final_project/src/git_ds_jini/medical_report_system 

java -jar lib/tools.jar -port 8080 -dir host_this/ -verbose

3. JINI Lookup Service

cd /home/dell/ds/final_project/src/git_ds_jini/medical_report_system 

java -Djava.security.policy=all_rmi_lib/jsk-all.policy -jar all_rmi_lib/start.jar all_rmi_lib/start-transient-reggie.config

4. DataServer (1st JINI Service)

cd ~/ds/final_project/src/git_ds_jini/medical_report_system/out/production/medical_report_system/

java -Djava.rmi.server.codebase=http://localhost:8080/data_service-dl.jar -Djava.security.policy=../../../all_rmi_lib/jsk-all.policy cs/ds/jiniimpl/dataservice/DataServiceServer

5. ClientServiceServer (2nd JINI Service)

cd ~/ds/final_project/src/git_ds_jini/medical_report_system/out/production/medical_report_system/

java -Djava.rmi.server.codebase=http://localhost:8080/client_service-dl.jar -Djava.security.policy=../../../all_rmi_lib/jsk-all.policy cs/ds/jiniimpl/clientservice/ClientServiceServer

6. PatientClient (JINI Client)

cd ~/ds/final_project/src/git_ds_jini/medical_report_system/out/production/medical_report_system/

java -Djava.security.policy=../../../all_rmi_lib/jsk-all.policy cs/ds/jiniimpl/client/PatientClient

7. Creating jar (Make the project before running these commands).

a. Create jar for dataservice (only needs DOA interfaces).

cd ~/git_ds_jini/medical_report_system/out/production/medical_report_system
jar cf ~/git_ds_jini/medical_report_system/host_this/data_service-dl.jar cs/ds/dao/interfaces/

b. Create jar for clientservice (needs DAO, Service interfaces)
jar cf ~/git_ds_jini/medical_report_system/host_this/client_service-dl.jar cs/ds/dao/interfaces/ cs/ds/service/interfaces/



