package goald.beliefs.util;

import goald.beliefs.model.Dependency;
import goalp.repository.HashMapRepository.BundleType;

public class DependencyBuilder {

	private Dependency dependency;

	private DependencyBuilder() {
		this.dependency = new Dependency();
	}

	public static DependencyBuilder create() {
		return new DependencyBuilder();
	}

	public Dependency build() {
		Dependency built = this.dependency;
		this.dependency = null;
		return built;
	}

	public DependencyBuilder identifier(String identifier) {
		//this.model.add(bundle);
		return this;
	}

	public DependencyBuilder type(BundleType type) {
		this.dependency.type = type;
		return this;
	}
}
