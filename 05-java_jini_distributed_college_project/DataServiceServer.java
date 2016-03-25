package cs.ds.jiniimpl.dataservice;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.List;

import cs.ds.dao.PatientDAOImpl;
import cs.ds.dao.interfaces.PatientDAO;
import cs.ds.domain.Patient;
import cs.ds.domain.Patients;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.DiscoveryEvent;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceRegistration;
import net.jini.core.lease.Lease;
import net.jini.core.lookup.ServiceID ;
import net.jini.lease.LeaseListener;
import net.jini.lease.LeaseRenewalEvent;
import net.jini.lease.LeaseRenewalManager;

/**
 * @author Pramod Nepal
 * 1st JINI Service. It is used as a data service server, that fetches the
 * data requested from another JNNI Server. The data fetched from
 * PatentDAOImpl is registered as a ServiceItem to the ServiceRegistrar
 */
public class DataServiceServer implements  DiscoveryListener, LeaseListener{
    protected LeaseRenewalManager leaseManager = new LeaseRenewalManager();
    protected ServiceID serviceID = null;
    protected PatientDAO patientDAO = null;


    public static void main(String[] args) {

        DataServiceServer s = new DataServiceServer();

        Object keepAlive = new Object();

        synchronized (keepAlive) {
            try {
                keepAlive.wait();
            } catch(InterruptedException e) {}
        }
    }

    public DataServiceServer() {
        patientDAO = new PatientDAOImpl();

        System.setSecurityManager(new RMISecurityManager());

        LookupDiscovery discover = null;
        try {
            discover = new LookupDiscovery(LookupDiscovery.ALL_GROUPS);
        } catch(Exception e) {
            System.out.println("DataServiceServer: Discovery failed " + e.toString());
            System.exit(1);
        }

        discover.addDiscoveryListener(this);
    }



    @Override
    public void discovered(DiscoveryEvent discoveryEvent) {
        ServiceRegistrar[] registrars = discoveryEvent.getRegistrars();

        for (int n = 0; n < registrars.length; n++) {
            ServiceRegistrar registrar = registrars[n];

            ServiceItem item = new ServiceItem(serviceID, patientDAO, null);
            ServiceRegistration reg = null;

            try {
                registrar.register(item, Lease.FOREVER);
            } catch(RemoteException e) {
                System.out.println("DataServiceServer: Register exception " + e.toString());
                continue;
            }

            System.out.println("DataServiceServer: Service registered with id " + reg.getServiceID());

            // Set lease renewal in place
            leaseManager.renewUntil(reg.getLease(), Lease.FOREVER, this);
        }
    }

    @Override
    public void discarded(DiscoveryEvent discoveryEvent) {

    }

    @Override
    public void notify(LeaseRenewalEvent leaseRenewalEvent) {
        System.out.println("DataServiceServer: Lease expired " + leaseRenewalEvent.toString());
    }
}
