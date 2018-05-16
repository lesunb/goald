

import goald.beliefs.model.Bundle;
import goald.beliefs.model.IRepository;
import goalp.repository.HashMapRepository;

public class RepositoryBuilder {

	private IRepository repository;

	private RepositoryBuilder() {
		this.repository = new HashMapRepository();
	}

	public static RepositoryBuilder create() {
		return new RepositoryBuilder();
	}

	public IRepository build() {
		IRepository built = this.repository;
		this.repository = null;
		return built;
	}

	public RepositoryBuilder add(Bundle bundle) {
		this.repository.add(bundle);
		return this;
	}
}
