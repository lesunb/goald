package goald.evaluation.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepoSpec {

	public Map<String, Object> repoSpec = new HashMap<>();
	
	public Map<String, Object> getRepoSpec() {
		return repoSpec;
	}

	public void put(String key, Object value){
		this.repoSpec.put(key, value);
	}
	
	public Integer getInteger(String key){
		return (Integer) this.repoSpec.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> t, String key){
		Object obj = this.repoSpec.get(key);
		if(obj != null && t.isAssignableFrom(obj.getClass())){
			return (T) this.repoSpec.get(key);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getStrList(String key){
		List<String> obj  = null;
		try {
			obj = (List<String>) this.repoSpec.get(key);
		}catch(Exception e){}
		return obj;
	}
}
