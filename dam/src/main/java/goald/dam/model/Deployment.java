package goald.dam.model;

import java.util.ArrayList;
import java.util.Collection;

public class Deployment {

	private Collection<Bundle> bundles;

	public Collection<Bundle> getBundles() {
		if(bundles == null) {
			this.bundles = new ArrayList<>();
		}
		return bundles;
	}

	public void setBundles(Collection<Bundle> bundles) {
		this.bundles = bundles;
	}

}
