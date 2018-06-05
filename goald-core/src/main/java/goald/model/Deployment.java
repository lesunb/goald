package goald.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Deployment {

	private Collection<BundleStatus> bundleStatus;

	public Collection<BundleStatus> getBundles() {
		if(bundleStatus == null) {
			this.bundleStatus = new ArrayList<>();
		}
		return bundleStatus;
	}

	public void setBundles(Collection<BundleStatus> bundleStatus) {
		this.bundleStatus = bundleStatus;
	}
	
	public void add(Status status, Bundle bundle) {
		getBundles().add(new BundleStatus(status, bundle) );
	}
	
	public void remove(Bundle bundle) {
		getBundles().removeIf( bundleStatus -> bundleStatus.bundle.identification == bundle.identification);
	}
	
	public Status getStatus(Bundle bundle) {
		for(BundleStatus bstatus: getBundles()) {
			if(bstatus.getBundle().getIdentification() == bundle.identification) {
				return bstatus.getStatus();
			}
		}
		return null;
	}
	
	public Set<Bundle> getAll(Status status) {
		Set<Bundle> result = new HashSet<>();
		for(BundleStatus bstatus: getBundles()) {
			if(bstatus.getStatus() == status) {
				result.add(bstatus.getBundle());
			}
		}
		return result;
	}
	
	public enum Status {
		INSTALLED,
		RESOLVED,
		UNINSTALLED,
		STOPED,
		ACTIVE
	}
	
	
	public class BundleStatus {
		private Status status;
		private Bundle bundle;
		
		public BundleStatus(Status status, Bundle bundle) {
			this.status = status;
			this.bundle = bundle;
		}
		
		public Status getStatus() {
			return this.status;
		}
		
		public Bundle getBundle() {
			return this.bundle;
		}

		@Override
		public String toString() {
			return "BundleStatus [status=" + status + ", bundle=" + bundle + "]";
		}
	}


	@Override
	public String toString() {
		return "Deployment [bundleStatus=" + bundleStatus + "]";
	}

}
