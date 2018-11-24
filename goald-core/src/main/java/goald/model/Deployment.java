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
		if(status == null || bundle == null) {
			throw new IllegalStateException();
		}
		getBundles().add(new BundleStatus(status, bundle) );
	}
	
	public void add(Status status, String identification) {
		if(status == null || identification == null) {
			throw new IllegalStateException();
		}
		getBundles().add(new BundleStatus(status, new Bundle(identification)) );
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bundleStatus == null) ? 0 : bundleStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deployment other = (Deployment) obj;
		if (bundleStatus == null) {
			if (other.bundleStatus != null)
				return false;
		}
		
		//TODO
		return this.toString().equals(
				other.toString());
	}
	
	public Deployment clone() {
		Deployment clone = new Deployment();
		
		for(BundleStatus status: this.bundleStatus) {
			clone.getBundles().add(new BundleStatus(status.getStatus(), status.getBundle()));
		}
		return clone;
	}


}
