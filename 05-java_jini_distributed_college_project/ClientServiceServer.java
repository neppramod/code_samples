package cs.ds.jiniimpl.clientservice;

import cs.ds.dao.interfaces.PatientDAO;
import cs.ds.service.PatientServiceImpl;
import cs.ds.service.interfaces.PatientService;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.*;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;
import net.jini.lease.LeaseRenewalManager;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
/**
 * @author Pramod Nepal
 * 2nd JINI Service. It is used as a record fetch server, that looks up the 
 * data from another JINI Server. The discovered data is again registered to 
 * the registrar so that client can lookup the service through PatientService
 */
public class ClientServiceServer implements DiscoveryListener, LeaseListener {
    protected LeaseRenewalManager leaseManager = new LeaseRenewalManager();
    protected ServiceID serviceID = null;
    protected PatientService patientService = null;
    protected PatientDAO patientDAO = null;

    public static void main(String[] args) {
        ClientServiceServer s = new ClientServiceServer();

        // Act also as client
        try {
            Thread.sleep(100000L);
        } catch(InterruptedException e) {}

        // This is for service

        Object keepAlive = new Object();

        // Keep this server running
        synchronized (keepAlive) {
            try {
                keepAlive.wait();
            } catch(InterruptedException e) {}
        }

    }

    public ClientServiceServer() {
        System.setSecurityManager(new RMISecurityManager());
        LookupDiscovery discover = null;

        try {
            discover = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
        } catch (Exception e) {
            System.out.println("ClientServiceServer: Discovery failed " + e.toString());
            System.exit(1);
        }

        discover.addDiscoveryListener(this);
    }

    @Override
    public void discovered(DiscoveryEvent discoveryEvent) {
        ServiceRegistrar[] registrars = discoveryEvent.getRegistrars();
        Class[] classes = new Class[] {PatientDAO.class};
        ServiceTemplate template = new ServiceTemplate(null, classes, null);

        for (int n = 0; n < registrars.length; n++) {
            System.out.println("ClientServiceServer: Lookup service found");
            ServiceRegistrar registrar = registrars[n];

            try {
                patientDAO = (PatientDAO) registrar.lookup(template);
            } catch (RemoteException e) {
                System.out.println("ClientServiceServer: PatientDAO null");
                continue;
            }
          

            if (patientDAO != null) {
                System.out.println("Found patientDAO. Good to go !!");
                break;
            }
        }

        System.out.println("Found patientDAO now registering the patient service");

        // Use patientDAO object we got from server
        if (patientDAO != null) {
            patientService = new PatientServiceImpl(patientDAO);           
        }

        for (int n = 0; n < registrars.length; n++) {
            ServiceRegistrar registrar = registrars[n];
            // After we get the patientDAO we can register patientService
            ServiceItem item = new ServiceItem(serviceID, patientService, null);
            ServiceRegistration registration = null;

            try {
                registration = registrar.register(item, Lease.FOREVER);
            } catch(RemoteException e) {
                System.out.println("ClientServiceServer: PatientService registration exception: " + e.toString());
                continue;
            }

            System.out.println("ClientServiceServer: PatientService registered with id " + registration.getServiceID());

            // set lease renewal in place
            leaseManager.renewUntil(registration.getLease(), Lease.FOREVER, this);
        }
    }

    @Override
    public void discarded(DiscoveryEvent discoveryEvent) {

    }

    @Override
    public void notify(LeaseRenewalEvent leaseRenewalEvent) {
        System.out.println("ClientServiceServer: Patient Service: Lease expired " + leaseRenewalEvent.toString());
    }
}
