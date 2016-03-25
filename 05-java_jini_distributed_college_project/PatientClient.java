package cs.ds.jiniimpl.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cs.ds.domain.Address;
import cs.ds.domain.Patient;
import cs.ds.domain.Patients;
import cs.ds.domain.Treatment;
import cs.ds.service.interfaces.PatientService;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.DiscoveryEvent;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;

public class PatientClient implements DiscoveryListener{
    protected PatientService patientService;

    public static void main(String[] args) {
        new PatientClient();

          // Keep it running all the time, rather than exiting in 1 minute.
        Object keepAlive = new Object();

        synchronized (keepAlive) {
            try {
                keepAlive.wait();
            } catch(InterruptedException e) {}
        }
    }

    public PatientClient() {
        System.setSecurityManager(new RMISecurityManager());

        LookupDiscovery discover = null;

        try {
            discover = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }

        discover.addDiscoveryListener(this);
    }

    @Override
    public void discovered(DiscoveryEvent discoveryEvent) {
        ServiceRegistrar[] registrars = discoveryEvent.getRegistrars();
        Class[] classes = new Class[]{PatientService.class};

        ServiceTemplate template = new ServiceTemplate(null, classes, null);

        for (int n = 0; n < registrars.length; n++) {
            System.out.println("Lookup service found");
            ServiceRegistrar registrar = registrars[n];

            try {
                patientService = (PatientService) registrar.lookup(template);
            } catch (RemoteException e) {
                System.out.println("PatientClient: PatientService null");
                continue;
            }



            // Use patientService object we got from server
            try {

                // Lets get a file
                byte[] fileData = patientService.downloadFile("op.jpg");



                // Give new file name
                String newFileName = "op_" + System.currentTimeMillis() + ".jpg";
                File file = new File(newFileName);
                BufferedOutputStream output = new
                        BufferedOutputStream(new FileOutputStream(file.getName()));

                System.out.println("Getting file: (with changed name after adding timestamp ) : " +
                        file.getName());

                output.write(fileData, 0, fileData.length);
                output.flush();
                output.close();

            } catch(RemoteException e) {
                e.printStackTrace();
                continue;
            } catch(Exception fe) {
                System.out.println("File not found/Input exception: " + fe.toString());
            }

            try {
                System.out.println("Welcome to the Medical Records System.");
                System.out.println("");
                clientMenu(patientService);
            } catch(Exception ex) {
                System.out.println("Exception in creating client menu: " + ex);
                //continue;
            }

            // Should I exit now or not, Have to look at this later
            System.exit(0);
        }
    }

    @Override
    public void discarded(DiscoveryEvent discoveryEvent) {

    }

    public void clientMenu(PatientService patientService) {
        Scanner in = new Scanner(System.in);
        int menuChoice = 0;

        Patients patientList = new Patients();
        patientList.setPatientsList(patientService.readPatients());

        Long patientID;

        while(menuChoice != 5) {
            try {
                clientMenuText();
                menuChoice = in.nextInt();
                switch (menuChoice) {
                    case 1:
                        patientService.addPatient(patientList, clientAddPatient());
                        break;
                    case 2:
                        System.out.println("Give id of patient to search");
                        patientID = in.nextLong();
                        System.out.println("You choose '" + patientID + "'");
                        searchAndPrintPatient(patientID);
                        break;
                    case 3:
                        patientService.removePatient(patientList, clientAddPatient());  //use after add, sample patient
                        break;
                    case 4:
                        patientService.printPatients();
                        break;
                    case 5:
                        System.out.println("Exiting the application.");
                        break;
                    default:
                        System.out.println("Invalid menu option chosen.");
                }
                System.out.println("");
            }catch (InputMismatchException e) {
                System.out.println("Sorry!! You had to enter a number. Try again. Exiting.. ");
                break;
            }
        }
    }

    public void searchAndPrintPatient(Long id) {

        Patient patient = null;

        try {
            patient = patientService.findPatientById(id);
        }catch (RemoteException re) {
            System.out.println("Remote exception: " + re);
        }

        if (patient != null) {
            System.out.println("Information of patient with id : " + patient.getId());
            System.out.println("Name: " + patient.getName());
            System.out.println("Dob: " + patient.getDob());

            Address addr = patient.getAddress();

            System.out.println("Address: " + addr.getStreet() + ", " + addr.getAppartmentNo()
                    + ", " + addr.getCity() + ", " + addr.getZipCode() +
                    ", " + addr.getZipCode() + ", " + addr.getState() + ", " +
                    addr.getCountry());

            addr = patient.getDobPlace();
            System.out.println("Address (of birth): " + addr.getStreet() + ", " + addr.getAppartmentNo()
                    + ", " + addr.getCity() + ", " + addr.getZipCode() +
                    ", " + addr.getZipCode() + ", " + addr.getState() + ", " +
                    addr.getCountry());

            System.out.println("SSN: " + patient.getSsn());
            System.out.println("Phone: " + patient.getPhone());
            System.out.println("Father's name: " + patient.getFathersName());
            System.out.println("Mother's name: " + patient.getMothersName());


            System.out.println("Treatment history");
            for(Treatment t: patient.getTreatmentHistory()) {
                System.out.println("Treatment Id: " + t.getId());
                System.out.println("Name of doctor: " + t.getNameofDoctor());
                System.out.println("Name of hospital: " + t.getNameOfHospital());

                addr = t.getAddressOfHospital();

                System.out.println("Address (of hospital): " + addr.getStreet() + ", " + addr.getAppartmentNo()
                        + ", " + addr.getCity() + ", " + addr.getZipCode() +
                        ", " + addr.getZipCode() + ", " + addr.getState() + ", " +
                        addr.getCountry());

                System.out.println("Date of treatment: " + t.getDateOfTreatment());
                System.out.println("Symptoms: " + t.getSymptoms());
                System.out.println("Description: " + t.getDescriptionOfTreatment());

                System.out.print("Reports: ");
                for(String file : t.getTreatmentReports()) {
                    System.out.print(file + ", ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Did not find the patient with that id");
        }
    }

    public void clientMenuText() {
        System.out.println("Please choose an option below:");
        System.out.println("");
        System.out.println("1 - Add a patient");
        System.out.println("2 - Search for a patient");
        System.out.println("3 - Remove a patient");
        System.out.println("4 - List all patients");
        System.out.println("5 - Exit the application");
    }

    // Local test data created to bypass data creation step
    public Patient clientAddPatient() {
        Address hospitalAddress = new Address();
        hospitalAddress.setId(1L);
        hospitalAddress.setAppartmentNo("Central Hospital Apartment");
        hospitalAddress.setCity("Johnson City");
        hospitalAddress.setCountry("US");
        hospitalAddress.setState("TN");
        hospitalAddress.setStreet("Seminole Drv");
        hospitalAddress.setZipCode("33434");

        Address birthAddress = new Address();
        birthAddress.setId(2L);
        birthAddress.setAppartmentNo("Outer hospital");
        birthAddress.setCity("Bristol");
        birthAddress.setCountry("Canada");
        birthAddress.setState("CN");
        birthAddress.setStreet("Long Drv");
        birthAddress.setZipCode("23434");

        Address currentAddress = new Address();
        currentAddress.setId(3L);
        currentAddress.setAppartmentNo("234");
        currentAddress.setCity("Far city");
        currentAddress.setCountry("Moon");
        currentAddress.setState("Creator");
        currentAddress.setStreet("No street defined");
        currentAddress.setZipCode("334234");

        // Treatment1
        Treatment patientTreatment = new Treatment();
        patientTreatment.setId(1L);
        patientTreatment.setTitle("Severe Heart disease");
        patientTreatment.setNameofDoctor("Dr. Superman");
        patientTreatment.setNameOfHospital("Finest medical hospital");
        patientTreatment.setAddressOfHospital(hospitalAddress);
        patientTreatment.setSymptoms("Heart attack");
        patientTreatment.setDescriptionOfTreatment("Did a surgery");

        List<String> treatmentFiles = new ArrayList<String>();
        treatmentFiles.add("op.jpg");
        treatmentFiles.add("soa.jpg");
        treatmentFiles.add("nar.jpg");
        patientTreatment.setTreatmentReports(treatmentFiles);

        // Treatment2
        Treatment patient1Treatment = new Treatment();
        patient1Treatment.setId(1L);
        patient1Treatment.setTitle("Severe Lungs disease");
        patient1Treatment.setNameofDoctor("Dr. Octopos");
        patient1Treatment.setNameOfHospital("Finest lungs hospital");
        patient1Treatment.setAddressOfHospital(hospitalAddress);
        patient1Treatment.setSymptoms("Lungs attack");
        patient1Treatment.setDescriptionOfTreatment("Lungs surgery");

        List<String> treatment1Files = new ArrayList<String>();
        treatment1Files.add("op1.jpg");
        treatment1Files.add("soa1.jpg");
        treatment1Files.add("nar1.jpg");
        patient1Treatment.setTreatmentReports(treatment1Files);


        Patient patient1 = new Patient();
        patient1.setId(5L);
        patient1.setName("Harry Potter");
        patient1.setAddress(currentAddress);
        patient1.setDobPlace(birthAddress);

        try {
            patient1.setDob(new SimpleDateFormat("M-d-yyyy").parse("05-21-1923"));
            patientTreatment.setDateOfTreatment(new SimpleDateFormat("M-d-yyyy").parse("05-17-1945"));
            patient1Treatment.setDateOfTreatment(new SimpleDateFormat("M-d-yyyy").parse("02-15-1935"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Treatment> treatmentList = new ArrayList<Treatment>();
        treatmentList.add(patientTreatment);
        treatmentList.add(patient1Treatment);

        patient1.setTreatmentHistory(treatmentList);

        //now return the patient
        return patient1;
    }
}
